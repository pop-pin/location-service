package com.poppin.locationservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class AverageRatingDto {
    private String gu;
    private Double averageRating;
}
