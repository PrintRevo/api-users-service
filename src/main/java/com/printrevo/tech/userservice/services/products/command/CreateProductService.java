package com.printrevo.tech.userservice.services.products.command;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.products.models.Product;
import com.printrevo.tech.userservice.entities.core.products.repositories.ProductsRepository;
import com.printrevo.tech.userservice.services.products.command.instructions.CreateProductCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CreateProductService extends CommandBaseService<CreateProductCommand, String> {

    private final ProductsRepository productsRepository;

    @Override
    public Result<String> execute(CreateProductCommand command) {

        var product = new Product();
        product.setName(command.getName());
        product.setDescription(command.getDescription());
        product.setAuthGroup(command.getAuthGroup());
        product.setBaseUrl(command.getBaseUrl());
        product.setPublic(command.isPublic());

        try {
            productsRepository.save(product);

            return new Result.ResultBuilder<String>()
                    .withData(product.getId())
                    .build();
        } catch (Exception e) {
            return new Result.ResultBuilder<String>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                            "unexpected error occurred"))
                    .build();
        }
    }
}
