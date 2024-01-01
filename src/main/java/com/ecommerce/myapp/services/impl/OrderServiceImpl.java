package com.ecommerce.myapp.services.impl;

import com.ecommerce.myapp.dtos.dashboard.CategoryRevenueDto;
import com.ecommerce.myapp.dtos.dashboard.SalesDto;
import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.model.checkoutGroup.Order;
import com.ecommerce.myapp.model.checkoutGroup.OrderDetail;
import com.ecommerce.myapp.model.checkoutGroup.OrderStatusV2;
import com.ecommerce.myapp.model.client.CartItem;
import com.ecommerce.myapp.model.client.UserAddress;
import com.ecommerce.myapp.model.group.Product;
import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.repositories.CartItemRepository;
import com.ecommerce.myapp.repositories.OrderDetailRepository;
import com.ecommerce.myapp.repositories.OrderRepository;
import com.ecommerce.myapp.repositories.UserAddressRepository;
import com.ecommerce.myapp.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final UserAddressRepository userAddressRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    @Override
    public void createOrder(AppUser appUser, String note,Set<CartItem> cartItems) {
        Set<Product> products = new HashSet<>();
        cartItems.forEach(cartItem -> {
            cartItemRepository.getProduct(cartItem).ifPresent(products::add);
        });
        Order order = new Order();
        order.setAppUser(appUser);
        products.forEach(product -> {
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .productName(product.getProductName())
                    .unitPrice(product.getUnitPrice())
                    .order(order)
                    .build();
           Set<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
           orderDetails.add(orderDetail);
            orderDetailRepository.saveAll(orderDetails);
            order.setTotalPrice(order.getTotalPrice().add(product.getUnitPrice()));
        });
        Optional<UserAddress> userAddress =
                userAddressRepository.findAllByAppUser(appUser)
                        .stream().filter(UserAddress::getIsMainAddress).findFirst();
        if (userAddress.isPresent()) {
            var address = userAddress.get();
            order.setReceiveAddress(address.getFullAddress());
            order.setReceivePhone(address.getPhone());
            order.setStatus(OrderStatusV2.WAITING);
            orderRepository.save(order);
        }
    }

        @Override
        public List<Order> findAllById (List < Long > orderIds) {
            return orderRepository.findAllById(orderIds);
        }

        @Override
        public Order findById (Long orderId){
            return orderRepository.findById(orderId).orElseThrow(() ->
                    new ResourceNotFoundException(STR."Order with ID \{orderId} can't found"));
        }

        @Override
        public List<Order> findAllByStatus (OrderStatusV2 statusV2){
            return orderRepository.findAllByStatus(statusV2);
        }

        @Override
        public List<Order> getOrderHistoryByUser (AppUser appUser, OrderStatusV2 status){
            if (status == null) {
                return orderRepository.findAllByAppUser(appUser);
            } else {
                return orderRepository.findAllByAppUserAndStatus(appUser, status);
            }
        }

        @Override
        public void cancelOrder (AppUser user, Long orderId){
            if (orderId == null) {
                throw new RuntimeException("Order ID cannot be null");
            }
            Order order = orderRepository.findByAppUserAndOrderId(user, orderId).orElseThrow(
                    () -> new ResourceNotFoundException(
                            STR."Order with ID \{orderId} of User with ID \{user.getUserId()} can't found !")
            );
            if (!order.getStatus().equals(OrderStatusV2.WAITING)) {
                throw new IllegalStateException(STR."Order with \{orderId} is not in a state that can be cancelled.");
            }
            order.setStatus(OrderStatusV2.CANCEL);
            orderRepository.save(order);
        }

        @Override
        public Page<Order> getAllOrder (String query, Pageable pageable){
            return orderRepository.orderPage(query, pageable);
        }

        @Override
        public void updateStatus (Long orderId, OrderStatusV2 statusV2){
            var order = findById(orderId);
            order.setStatus(statusV2);
            orderRepository.save(order);
        }

        @Override
        public SalesDto getSalesByTime (String dateA, String dateB){
            return new SalesDto(
                    orderRepository.countTotalPriceByTime(dateA, dateB),
                    calculateGrowthRate(dateA, dateA, dateB, dateB));
        }

        @Override
        public Page<Product> bestSellerProducts (Pageable pageable){
            return orderRepository.findAllBestSellerProduct(pageable);
        }

        @Override
        public List<CategoryRevenueDto> categoryRevenueDto () {
            return orderRepository.listCategoryRevenues();
        }

        private BigDecimal calculateGrowthRate (String fromA, String toA, String fromB, String toB){
            BigDecimal totalRevenueA = orderRepository.countTotalPriceByTime(fromA, toA);
            BigDecimal totalRevenueB = orderRepository.countTotalPriceByTime(fromB, toB);

            if (totalRevenueA == null || totalRevenueA.compareTo(BigDecimal.ZERO) == 0 || totalRevenueB == null) {
                throw new IllegalArgumentException("Revenue cannot be null or 0 to calculate grow rate.");
            }

            // Tính tỷ lệ tăng trưởng
            return totalRevenueB.subtract(totalRevenueA)
                    .divide(totalRevenueA, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }
