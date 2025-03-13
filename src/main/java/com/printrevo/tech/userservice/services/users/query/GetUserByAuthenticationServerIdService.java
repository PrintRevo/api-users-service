package com.printrevo.tech.userservice.services.users.query;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.QueryBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import com.printrevo.tech.userservice.entities.core.users.repositories.SysUserRepository;
import com.printrevo.tech.userservice.services.users.query.instructions.GetUserByAuthenticationServerIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByAuthenticationServerIdService
        extends QueryBaseService<GetUserByAuthenticationServerIdQuery, SysUser> {

    private final SysUserRepository userRepository;

    @Override
    public Result<SysUser> execute(GetUserByAuthenticationServerIdQuery command) {
        return userRepository.findByAuthServerId(command.getAuthenticationServerId())
                .map(user -> new Result.ResultBuilder<SysUser>().withData(user).build())
                .orElse(new Result.ResultBuilder<SysUser>()
                        .withStatus(new ResultStatus(HttpStatus.NOT_FOUND, "User not found")).build());
    }
}
