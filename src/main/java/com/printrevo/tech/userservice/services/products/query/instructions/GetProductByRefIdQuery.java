package com.printrevo.tech.userservice.services.products.query.instructions;

import com.printrevo.tech.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductByRefIdQuery implements Query {

    @NotEmpty(message = "Product reference id is required")
    @NotNull(message = "Product reference id can not be null")
    private String productRefId;
}
