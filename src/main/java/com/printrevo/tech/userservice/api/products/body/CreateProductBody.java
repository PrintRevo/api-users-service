package com.printrevo.tech.userservice.api.products.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductBody {
    private String name;
    private String description;
    private String authGroup;
    private String baseUrl;
    private boolean isPublic;
}
