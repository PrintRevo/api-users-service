package com.printrevo.tech.userservice.entities.core.users.querybuilders;


import com.printrevo.tech.platform.db.*;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;

import java.util.ArrayList;

public class UserQueryBuilder extends AbstractQueryBuilder {

    private UserQueryBuilder() {
    }

    public static SearchQuery createSearchQuery(TransactionFilter transactionFilter) {
        var reqParams = transactionFilter.getReqParam();

        var searchCriteriaList = new ArrayList<>(ClassFieldSearchQueryBuilder
                .createClassFieldSearchCriteria(SysUser.class, reqParams
                        , transactionFilter.getSearchTerm(), true));

        var joinColumnProps = new ArrayList<JoinColumnProps>();

        return createBaseSortedSearchQuery(joinColumnProps, searchCriteriaList, transactionFilter);
    }
}
