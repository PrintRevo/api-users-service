package com.printrevo.tech.userservice.services.users.query;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import com.printrevo.tech.userservice.entities.core.users.repositories.SysUserRepository;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByRefIdQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GetUserByRefIdService extends QueryBaseService<GetUserByRefIdQuery, SysUser> {

    private final SysUserRepository sysUserRepository;

    public GetUserByRefIdService(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    @Override
    public Result<SysUser> execute(GetUserByRefIdQuery query) {
        return sysUserRepository.findById(query.getUserRefId())
                .map(user -> new Result.ResultBuilder<SysUser>()
                        .withData(user)
                        .build()
                )
                .orElse(new Result.ResultBuilder<SysUser>()
                        .withIsFailed(true)
                        .withStatus(new ResultStatus(
                                HttpStatus.NOT_FOUND,
                                String.format("User with refId %s not found", query.getUserRefId()))
                        )
                        .build()
                );
    }
}
