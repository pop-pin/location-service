package com.poppin.locationservice.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "location") // 실제 몽고 DB 컬렉션 이름
@Getter
@Setter
@NoArgsConstructor
public class Location {
    @Id
    private String id;
    private String name;
    private Double rating;
    private String category;
    private Integer price; // price range is an integer (0-4)
    private String areaCode;
    private String worldCode;
    private String roadName;
    private Double latitude;
    private Double longitude;

    @Builder
    public Location(String id, String name, Double rating, String category, Integer price, String areaCode, String worldCode, String roadName, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.category = category;
        this.price = price;
        this.areaCode = areaCode;
        this.worldCode = worldCode;
        this.roadName = roadName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
