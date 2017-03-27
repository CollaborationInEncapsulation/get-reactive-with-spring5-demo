package com.example.controller.vm;

import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Value
public class UserVM {
    @NotNull
    @NonNull
    private String id;
    @NotNull
    @NonNull
    private String name;
}
