package com.jitendra.marketplace.auth.controller;

import com.jitendra.marketplace.auth.dto.MessageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoleProtectedController {

    @GetMapping("/buyer/dashboard")
    @PreAuthorize("hasRole('BUYER')")
    public MessageResponse buyerEndpoint() {
        return new MessageResponse("Buyer access granted");
    }

    @GetMapping("/supplier/dashboard")
    @PreAuthorize("hasRole('SUPPLIER')")
    public MessageResponse supplierEndpoint() {
        return new MessageResponse("Supplier access granted");
    }
}
