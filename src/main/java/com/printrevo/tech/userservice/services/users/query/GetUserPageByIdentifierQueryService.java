package com.printrevo.tech.userservice.services.users.query;

import com.printrevo.tech.platform.db.SpecificationBuilder;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.dto.SysUserDto;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import com.printrevo.tech.userservice.entities.core.users.querybuilders.UserQueryBuilder;
import com.printrevo.tech.userservice.entities.core.users.repositories.SysUserRepository;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByIdentifierQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GetUserPageByIdentifierQueryService
        extends QueryBaseService<GetUserByIdentifierQuery, Page<SysUserDto>> {

    private final SysUserRepository userRepository;

    @Override
    public Result<Page<SysUserDto>> execute(GetUserByIdentifierQuery query) {

        var transactionFilter = query.getTransactionFilter();

        var searchQuery = UserQueryBuilder.createSearchQuery(transactionFilter);

        var userSpecification = new SpecificationBuilder<SysUser>()
                .bySearchQuery(searchQuery);

        var page = userRepository.findAll(userSpecification
                , transactionFilter.getPageRequest(searchQuery));

        var userDTOs = page.getContent().stream()
                .map(SysUser::toDTO).toList();

        var pageable = new PageImpl<>(userDTOs, page.getPageable(), page.getTotalElements());

        return new Result.ResultBuilder<Page<SysUserDto>>()
                .withData(pageable).build();
    }
}
