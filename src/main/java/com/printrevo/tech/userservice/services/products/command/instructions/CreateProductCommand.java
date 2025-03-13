package com.printrevo.tech.userservice.services.products.command.instructions;

import com.printrevo.tech.platform.services.Command;
import com.printrevo.tech.platform.validators.Validate;
import com.printrevo.tech.userservice.api.products.body.CreateProductBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Data
@Validate
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductCommand implements Command {

    @NotBlank(message = "Product name is required")
    @NotEmpty(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    @NotEmpty(message = "Product description is required")
    private String description;

    @NotBlank(message = "Product auth server group  is required")
    @NotEmpty(message = "Product auth server group is required")
    private String authGroup;

    @NotBlank(message = "Product base url  is required")
    @NotEmpty(message = "Product base url group is required")
    private String baseUrl;

    private boolean isPublic;

    public CreateProductCommand(CreateProductBody createProductBody) {
        setName(createProductBody.getName());
        setDescription(createProductBody.getDescription());
        setAuthGroup(createProductBody.getAuthGroup());
        setBaseUrl(createProductBody.getBaseUrl());
        setPublic(createProductBody.isPublic());
    }
}
