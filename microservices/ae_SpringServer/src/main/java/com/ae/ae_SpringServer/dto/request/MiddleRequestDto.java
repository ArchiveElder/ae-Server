package com.ae.ae_SpringServer.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MiddleRequestDto {
    @NotNull
    private String wide;
}
