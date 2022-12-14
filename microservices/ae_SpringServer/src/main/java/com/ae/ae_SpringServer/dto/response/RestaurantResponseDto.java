package com.ae.ae_SpringServer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
public class RestaurantResponseDto {
    private int bistroId;
    private String category;
    @NotNull
    private String name;
    private String roadAddr;
    private String lnmAddr;
    private String telNo;
    @NotNull
    private String la;
    @NotNull
    private String lo;
}
