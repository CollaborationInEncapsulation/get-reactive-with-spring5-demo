package com.example.service.gitter.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mention {
    private String screenName;
    private String userId;
    private List<String> userIds;
}
