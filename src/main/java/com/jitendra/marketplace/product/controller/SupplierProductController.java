package com.jitendra.marketplace.product.controller;

import com.jitendra.marketplace.auth.dto.MessageResponse;
import com.jitendra.marketplace.product.dto.CreateProductRequest;
import com.jitendra.marketplace.product.dto.ProductResponse;
import com.jitendra.marketplace.product.dto.UpdateProductRequest;
import com.jitendra.marketplace.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/supplier/products")
public class SupplierProductController {

    private final ProductService productService;

    public SupplierProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<ProductResponse> addProduct(
            @Valid @RequestBody CreateProductRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ProductResponse created = productService.addProduct(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ProductResponse updated = productService.updateSupplierProduct(id, request, userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<List<ProductResponse>> listMyProducts(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(productService.listSupplierProducts(userDetails.getUsername()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationError() {
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid request payload"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MessageResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(ex.getMessage()));
    }
}
