package com.jitendra.marketplace.auth.dto;

import java.util.List;

public record UserProfileResponse(
        String email,
        String fullName,
        List<String> roles
) {
}
