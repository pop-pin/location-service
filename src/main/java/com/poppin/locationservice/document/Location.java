package com.poppin.locationservice.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "place_info")
@Getter
@Setter
@NoArgsConstructor
public class Location {
    @Id
    private String id;
    private String businessStatus;
    private Geometry geometry;
    private String icon;
    private String iconBackgroundColor;
    private String iconMaskBaseUri;
    private String name;
    private String placeId;
    private PlusCode plusCode;
    private Long priceLevel;
    private Long rating;
    private String reference;
    private String scope;
    private List<String> types;
    private String vicinity;
    private Metadata metadata;

    @Getter
    public static class Geometry {
        private LocationCoordinates location;
        private Viewport viewport;
    }

    @Getter
    public static class LocationCoordinates {
        private Double lat;
        private Double lng;
    }

    @Getter
    public static class Viewport {
        private LocationCoordinates northeast;
        private LocationCoordinates southwest;
    }

    @Getter
    public static class PlusCode {
        private String compoundCode;
        private String globalCode;
    }

    @Getter
    public static class Metadata {
        private String city;
        private String time;
        private Integer pointGeoLatitude;
        private Integer pointGeoLongitude;
        private Double pointLatitude;
        private Double pointLongitude;
        private Double geoLatitude;
        private Double geoLongitude;
        private Integer pointCount;
        private Integer radius;
    }

    @Builder
    public Location(String id, String businessStatus, Geometry geometry, String icon, String iconBackgroundColor, String iconMaskBaseUri, String name, String placeId, PlusCode plusCode, Long priceLevel, Long rating, String reference, String scope, List<String> types, String vicinity, Metadata metadata) {
        this.id = id;
        this.businessStatus = businessStatus;
        this.geometry = geometry;
        this.icon = icon;
        this.iconBackgroundColor = iconBackgroundColor;
        this.iconMaskBaseUri = iconMaskBaseUri;
        this.name = name;
        this.placeId = placeId;
        this.plusCode = plusCode;
        this.priceLevel = priceLevel;
        this.rating = rating;
        this.reference = reference;
        this.scope = scope;
        this.types = types;
        this.vicinity = vicinity;
        this.metadata = metadata;
    }
}

