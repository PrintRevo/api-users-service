package com.printrevo.tech.userservice.services.users.command;


import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import com.printrevo.tech.userservice.entities.core.users.repositories.SysUserRepository;
import com.printrevo.tech.userservice.services.users.command.instructions.CreateSysUserCommand;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CreateSysUserService extends CommandBaseService<CreateSysUserCommand, String> {

    private final SysUserRepository sysUserRepository;

    public CreateSysUserService(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    @Override
    public Result<String> execute(CreateSysUserCommand command) {
        var user = new SysUser();
        user.setEmail(command.getEmail());
        user.setCountry(command.getCountry());
        user.setUserStatus(UserStatus.ON_BOARDING);
        var createdUser = sysUserRepository.save(user);
        return new Result.ResultBuilder<String>()
                .withData(createdUser.getId())
                .withStatus(new ResultStatus(HttpStatus.CREATED, "User created successfully"))
                .build();
    }
}
