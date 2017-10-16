package com.example.controller.vm;

import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Value
public class MessageVM {
    @NotNull
    @NonNull
    private String id;
    @NotNull
    @NonNull
    private String text;
    @NotNull
    @NonNull
    private String html;
    @NonNull
    @NotNull
    private String username;
    @NonNull
    @NotNull
    private String userAvatarUrl;
    @NotNull
    @NonNull
    private Date sent;
}
