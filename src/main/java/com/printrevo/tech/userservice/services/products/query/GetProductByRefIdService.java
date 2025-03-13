package com.printrevo.tech.userservice.services.products.query;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.products.models.Product;
import com.printrevo.tech.userservice.entities.core.products.repositories.ProductsRepository;
import com.printrevo.tech.userservice.services.products.query.instructions.GetProductByRefIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductByRefIdService extends QueryBaseService<GetProductByRefIdQuery, Product> {

    private final ProductsRepository productsRepository;

    @Override
    public Result<Product> execute(GetProductByRefIdQuery query) {
        return productsRepository.findById(query.getProductRefId())
                .map(user -> new Result.ResultBuilder<Product>()
                        .withData(user)
                        .build()
                )
                .orElse(new Result.ResultBuilder<Product>()
                        .withStatus(new ResultStatus(
                                HttpStatus.NOT_FOUND,
                                String.format("Product with refId %s not found", query.getProductRefId()))
                        )
                        .build()
                );
    }
}
