package com.techcup.ccusersplayers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicInfoResponse {
    private String fullName;
    private String academicProgram;
    private Integer semester;
    private String message;
}