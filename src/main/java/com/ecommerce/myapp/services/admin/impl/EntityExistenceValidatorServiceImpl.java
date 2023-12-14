package com.ecommerce.myapp.services.admin.impl;

import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.repositories.AppUserRepository;
import com.ecommerce.myapp.repositories.BillRepository;
import com.ecommerce.myapp.repositories.product.ProductRepository;
import com.ecommerce.myapp.validation.EntityExistenceValidatorService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
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
