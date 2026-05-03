package com.jitendra.marketplace.product.service;

import com.jitendra.marketplace.product.dto.CreateProductRequest;
import com.jitendra.marketplace.product.dto.ProductResponse;
import com.jitendra.marketplace.product.dto.UpdateProductRequest;
import com.jitendra.marketplace.product.model.Product;
import com.jitendra.marketplace.product.model.ProductStatus;
import com.jitendra.marketplace.product.repo.ProductRepository;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse addProduct(CreateProductRequest request, String supplierEmail) {
        Product product = new Product(
                request.name().trim(),
                request.description().trim(),
                request.price(),
                request.category().trim(),
                supplierEmail.toLowerCase(),
                parseStatus(request.status())
        );
        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> listPublishedProducts() {
        return productRepository.findByStatus(ProductStatus.PUBLISHED)
                .stream()
                .map(ProductService::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> listSupplierProducts(String supplierEmail) {
        return productRepository.findBySupplierEmailOrderByCreatedAtDesc(supplierEmail.toLowerCase())
                .stream()
                .map(ProductService::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse updateSupplierProduct(Long id, UpdateProductRequest request, String supplierEmail) {
        Product product = productRepository.findByIdAndSupplierEmail(id, supplierEmail.toLowerCase())
                .orElseThrow(() -> new AccessDeniedException("Product not found or unauthorized"));

        product.updateDetails(
                request.name().trim(),
                request.description().trim(),
                request.price(),
                request.category().trim(),
                parseStatus(request.status())
        );

        Product updated = productRepository.save(product);
        return toResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> searchPublishedProducts(String query) {
        String text = query == null ? "" : query.trim();
        if (text.isBlank()) {
            return List.of();
        }
        return productRepository.findByStatusAndNameContainingIgnoreCaseOrStatusAndDescriptionContainingIgnoreCase(
                        ProductStatus.PUBLISHED, text, ProductStatus.PUBLISHED, text
                )
                .stream()
                .map(ProductService::toResponse)
                .toList();
    }

    private ProductStatus parseStatus(String status) {
        return ProductStatus.valueOf(status.trim().toUpperCase());
    }

    private static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStatus() == null ? ProductStatus.DRAFT.name() : product.getStatus().name(),
                product.getSupplierEmail() == null ? "" : product.getSupplierEmail(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
