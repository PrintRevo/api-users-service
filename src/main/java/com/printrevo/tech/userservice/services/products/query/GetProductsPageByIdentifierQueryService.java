package com.printrevo.tech.userservice.services.products.query;

import com.printrevo.tech.platform.db.SpecificationBuilder;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.products.models.Product;
import com.printrevo.tech.userservice.entities.core.products.querybuilders.ProductQueryBuilder;
import com.printrevo.tech.userservice.entities.core.products.repositories.ProductsRepository;
import com.printrevo.tech.userservice.services.products.query.instructions.GetProductByIdentifierQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GetProductsPageByIdentifierQueryService
        extends QueryBaseService<GetProductByIdentifierQuery, Page<Product>> {

    private final ProductsRepository productsRepository;

    @Override
    public Result<Page<Product>> execute(GetProductByIdentifierQuery query) {

        var transactionFilter = query.getTransactionFilter();

        var searchQuery = ProductQueryBuilder.createSearchQuery(transactionFilter);

        var userSpecification = new SpecificationBuilder<Product>()
                .bySearchQuery(searchQuery);

        if (transactionFilter.getReqParam().containsKey("authGroup")) {
            userSpecification = (root, query1, criteriaBuilder) ->
                    criteriaBuilder.or(criteriaBuilder.isTrue(root.get("isPublic")), root.get("authGroup").in(transactionFilter.getReqParam().get("authGroup")));
        }
        var page = productsRepository.findAll(userSpecification
                , transactionFilter.getPageRequest(searchQuery));

        return new Result.ResultBuilder<Page<Product>>()
                .withData(page).build();
    }
}
