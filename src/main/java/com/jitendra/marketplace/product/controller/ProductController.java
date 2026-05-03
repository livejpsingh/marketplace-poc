package com.jitendra.marketplace.product.controller;

import com.jitendra.marketplace.auth.dto.MessageResponse;
import com.jitendra.marketplace.product.dto.ProductResponse;
import com.jitendra.marketplace.product.service.ProductService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listProducts() {
        return ResponseEntity.ok(productService.listPublishedProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam("q") String query) {
        return ResponseEntity.ok(productService.searchPublishedProducts(query));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationError() {
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid request payload"));
    }
}
