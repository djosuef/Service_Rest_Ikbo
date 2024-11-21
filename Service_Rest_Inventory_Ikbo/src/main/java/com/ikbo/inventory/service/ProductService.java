package com.ikbo.inventory.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikbo.inventory.entity.Movement;
import com.ikbo.inventory.entity.Product;
import com.ikbo.inventory.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void decreaseProductStock(Long id, BigDecimal quantity, Optional<Movement> movementOld) {
        productRepository.findById(id).ifPresent(product -> {
            product.setStock(product.getStock().subtract(quantity));
            productRepository.save(product);

        });
    }

    public void increaseProductStock(Long id, BigDecimal quantity) {
        productRepository.findById(id).ifPresent(product -> {
            product.setStock(product.getStock().add(quantity));
            productRepository.save(product);
        });
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product insertProduct(Product product) {
        product.setStock(BigDecimal.ZERO);
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    } 


    public List<Product> getProductsByStock(BigDecimal stock) {
        return productRepository.findProductsWithStockGreaterThan(stock);
    }

    public List<Object[]> findProductStates() {
        return productRepository.findProductStates();
    }

   

}
