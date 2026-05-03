package com.jitendra.marketplace.product.repo;

import com.jitendra.marketplace.product.model.Product;
import com.jitendra.marketplace.product.model.ProductStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByStatusAndNameContainingIgnoreCaseOrStatusAndDescriptionContainingIgnoreCase(
            ProductStatus statusForName,
            String name,
            ProductStatus statusForDescription,
            String description
    );
    List<Product> findBySupplierEmailOrderByCreatedAtDesc(String supplierEmail);
    Optional<Product> findByIdAndSupplierEmail(Long id, String supplierEmail);
}
