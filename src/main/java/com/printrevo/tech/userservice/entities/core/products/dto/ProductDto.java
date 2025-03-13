package com.printrevo.tech.userservice.entities.core.products.dto;

public record ProductDto(
        String name,
        String description,
        String authGroup,
        String baseUrl,
        boolean isPublic
) {
}
