package com.poppin.locationservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class CustomErrorResponse {
    private int errorCode;
    private String message;
}
// 생성자, getter, setter

