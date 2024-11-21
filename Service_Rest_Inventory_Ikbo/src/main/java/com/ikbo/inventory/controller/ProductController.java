package com.ikbo.inventory.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikbo.inventory.entity.Product;
import com.ikbo.inventory.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/productswithstock")
    public List<Product> getProducts() {
        return productService.getProductsByStock(BigDecimal.ZERO);
    }

    @GetMapping("/products")
    public List<Product> getProductsByStock() {
        return productService.getProducts();
    }

    @GetMapping("/product/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping(path = "/product_insert", consumes={"application/json"})
    public Product insertProduct(@RequestBody Product product) {
        return productService.insertProduct(product);
    }

    @GetMapping("/productstate")
    public List<Object[]> getPoductStates() {
        return productService.findProductStates();
    }   
}
