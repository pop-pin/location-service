package com.poppin.locationservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LocationResponseDto {
    private String id;
    private Double lat;
    private Double lng;
    private String name;
    private Long rating;
    private List<String> types;
    private String vicinity;

}
