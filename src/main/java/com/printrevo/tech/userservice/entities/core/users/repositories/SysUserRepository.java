package com.printrevo.tech.userservice.entities.core.users.repositories;


import com.printrevo.tech.userservice.entities.core.users.constants.UserStatus;
import com.printrevo.tech.userservice.entities.core.users.models.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, String>, JpaSpecificationExecutor<SysUser>
        , PagingAndSortingRepository<SysUser, String> {
    Optional<SysUser> findByAuthServerId(String authServerId);

    Optional<SysUser> findByEmailAndUserStatus(String email, UserStatus userStatus);
}
