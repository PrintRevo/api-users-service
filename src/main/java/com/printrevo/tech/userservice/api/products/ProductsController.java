package com.printrevo.tech.userservice.api.products;

import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityGroup;
import com.printrevo.tech.platform.api.Response;
import com.printrevo.tech.platform.api.ResponseConverter;
import com.printrevo.tech.platform.db.TransactionFilter;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.api.products.body.CreateProductBody;
import com.printrevo.tech.userservice.entities.core.products.dto.ProductDto;
import com.printrevo.tech.userservice.services.products.command.CreateProductService;
import com.printrevo.tech.userservice.services.products.command.instructions.CreateProductCommand;
import com.printrevo.tech.userservice.services.products.query.GetKeyCloakUserGroupQueryService;
import com.printrevo.tech.userservice.services.products.query.GetProductsPageByIdentifierQueryService;
import com.printrevo.tech.userservice.services.products.query.instructions.GetKeyCloakUserGroups;
import com.printrevo.tech.userservice.services.products.query.instructions.GetProductByIdentifierQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = "v1/products", produces = "application/json")
@Slf4j
public record ProductsController(
        GetProductsPageByIdentifierQueryService getProductsPageByIdentifierQueryService,
        CreateProductService createProductService,
        GetKeyCloakUserGroupQueryService keyCloakUserGroupQueryService
) {

    @PostMapping
    @Operation(summary = "Create a new product")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<Response<String>> createProduct(@RequestBody CreateProductBody createProductBody) {
        return new ResponseConverter().convert(
                createProductService.decorate(new CreateProductCommand(createProductBody))
        );
    }

    @GetMapping
    @Operation(summary = "Query all products by identifier")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Response<Page<ProductDto>>> queryUsers(
            @RequestParam Map<String, String> fieldSearchQuery) {
        var params = new HashMap<String, Object>(fieldSearchQuery);
        return new ResponseConverter().convertPage(
                getProductsPageByIdentifierQueryService.decorate(
                        new GetProductByIdentifierQuery(new TransactionFilter(params)))
        );
    }

    @GetMapping("/user-list")
    @Operation(summary = "Query all the visible product for a current logged-in user")
    @ApiResponse(content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Response<Page<ProductDto>>> queryUserProducts(
            @RequestParam Map<String, String> pagination) {
        var params = new HashMap<String, Object>(pagination);

        Result<List<SecurityGroup>> userGroups = keyCloakUserGroupQueryService.decorate(new GetKeyCloakUserGroups());
        var authGroups = userGroups.getData().stream()
                .map(SecurityGroup::getName)
                .toList();
        params.put("isPublic", true);
        params.put("authGroup",authGroups);
        return new ResponseConverter().convertPage(
                getProductsPageByIdentifierQueryService
                        .decorate(new GetProductByIdentifierQuery(new TransactionFilter(params)))
        );
    }
}