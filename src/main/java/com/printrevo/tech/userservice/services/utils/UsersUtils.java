package com.printrevo.tech.userservice.services.utils;

import com.printrevo.tech.commonsecurity.helpers.session.SecurityContextHelper;
import com.printrevo.tech.commonsecurity.helpers.session.SessionHelper;
import com.printrevo.tech.userservice.entities.core.users.repositories.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service("usersUtils")
@RequiredArgsConstructor
public class UsersUtils {

    private final SysUserRepository userRepository;

    public boolean isSecurityContextUser(@NonNull String userRefId) {
        return Optional.ofNullable(SecurityContextHelper.getGlobalAuthUserIdentity())
                .map(userRepository::findByAuthServerId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(user -> user.getId().equals(userRefId))
                .isPresent();
    }
}
