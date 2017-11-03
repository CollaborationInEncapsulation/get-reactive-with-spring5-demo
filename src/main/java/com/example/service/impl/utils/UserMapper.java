package com.example.service.impl.utils;

import com.example.controller.vm.UserVM;
import com.example.domain.User;
import org.springframework.util.StringUtils;

public final class UserMapper {
    private UserMapper() {
    }

    public static UserVM toViewModelUnits(User domainUser) {
        if (domainUser == null) {
            return null;
        }

        return new UserVM(
                domainUser.getId(),
                StringUtils.isEmpty(domainUser.getName()) ? domainUser.getDisplayName() : domainUser.getName()
        );
    }
}
