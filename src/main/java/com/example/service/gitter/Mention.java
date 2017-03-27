package com.example.service.gitter;

import lombok.Value;

import java.util.List;

@Value
public class Mention {
    private String screenName;
    private String userId;
    private List<String> userIds;
}
