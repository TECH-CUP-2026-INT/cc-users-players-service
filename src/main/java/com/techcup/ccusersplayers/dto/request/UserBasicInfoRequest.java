package com.techcup.ccusersplayers.dto.request;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class UserBasicInfoRequest {
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String fullName;
    
    private String academicProgram;
    
    private Integer semester;
}