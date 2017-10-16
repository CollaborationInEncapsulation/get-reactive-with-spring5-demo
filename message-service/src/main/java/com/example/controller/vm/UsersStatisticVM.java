package com.example.controller.vm;

import lombok.Value;

@Value
public class UsersStatisticVM {
    private UserVM mostActive;

    private UserVM mostMentioned;
}
