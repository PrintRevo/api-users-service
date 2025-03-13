package com.printrevo.tech.userservice.entities.core.products.querybuilders;


import com.printrevo.tech.platform.db.*;
import org.apache.commons.math3.stat.descriptive.summary.Product;

import java.util.ArrayList;

public class ProductQueryBuilder extends AbstractQueryBuilder {

    private ProductQueryBuilder() {
    }

    public static SearchQuery createSearchQuery(TransactionFilter transactionFilter) {
        var reqParams = transactionFilter.getReqParam();

        var searchCriteriaList = new ArrayList<>(ClassFieldSearchQueryBuilder
                .createClassFieldSearchCriteria(Product.class, reqParams
                        , transactionFilter.getSearchTerm(), true));

        var joinColumnProps = new ArrayList<JoinColumnProps>();

        return createBaseSortedSearchQuery(joinColumnProps, searchCriteriaList, transactionFilter);
    }
}
