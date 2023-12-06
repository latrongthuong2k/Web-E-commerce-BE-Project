package com.ecommerce.myapp.Services.Implement;

import com.ecommerce.myapp.Exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.Repositories.BillRepository;
import com.ecommerce.myapp.Repositories.Product.ProductRepository;
import com.ecommerce.myapp.Services.validation.EntityExistenceValidatorService;
import com.ecommerce.myapp.Users.Repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class EntityExistenceValidatorServiceImpl implements EntityExistenceValidatorService {
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;
    private final BillRepository billRepository;

    @Override
    public void checkExistUser(Integer userId) {
        if (!appUserRepository.existsById(userId)) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
    }

    @Override
    public void checkExistProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("product with id [%s] not found".formatted(productId));
        }
    }

    @Override
    public void checkExistBill(Integer billId) {
        if (!billRepository.existsById(billId)) {
            throw new ResourceNotFoundException("bill with id [%s] not found".formatted(billId));
        }
    }
}
