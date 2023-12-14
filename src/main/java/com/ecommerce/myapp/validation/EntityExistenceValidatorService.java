package com.ecommerce.myapp.validation;


public interface EntityExistenceValidatorService {
    void checkExistUser(Integer userId);

    void checkExistProduct(Integer productId);

    void checkExistBill(Integer productId);
}
