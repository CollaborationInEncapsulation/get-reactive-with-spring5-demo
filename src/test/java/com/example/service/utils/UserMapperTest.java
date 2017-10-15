package com.example.service.utils;


import com.example.controller.vm.UserVM;
import com.example.domain.User;
import com.example.service.impl.utils.UserMapper;
import org.junit.Assert;
import org.junit.Test;

public class UserMapperTest {

    @Test
    public void shouldCorrectlyMapDomainModelToViewModel() {
        Assert.assertEquals(
                new UserVM("0", "0"),
                UserMapper.toViewModelUnits(User.of("0", "0", "0"))
        );
    }

    @Test
    public void shouldReturnNullViewModelOnNullableDomainModel() {
        Assert.assertEquals(
                null,
                UserMapper.toViewModelUnits(null)
        );
    }
}
