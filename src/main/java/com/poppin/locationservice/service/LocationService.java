package com.poppin.locationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poppin.locationservice.document.Location;
import com.poppin.locationservice.dto.response.AverageRatingDto;
import com.poppin.locationservice.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final MongoTemplate mongoTemplate;

//    public Location getLocationByName(String name) {
//        return locationRepository.findLocationByName(name);
//    }

    public Optional<Location> getLocation(String id) {
        return locationRepository.findById(id);
    }

    public Page<Location> searchLocation(String name, Pageable pageable) {
        return locationRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public List<AverageRatingDto> calculateAverageRatingByGu() {
        Map<String, String> guNameMapping = Map.ofEntries(
                Map.entry("강남구", "Gangnam-gu"), Map.entry("강동구", "Gangdong-gu"),
                Map.entry("강북구", "Gangbuk-gu"), Map.entry("강서구", "Gangseo-gu"),
                Map.entry("관악구", "Gwanak-gu"), Map.entry("광진구", "Gwangjin-gu"),
                Map.entry("구로구", "Guro-gu"), Map.entry("금천구", "Geumcheon-gu"),
                Map.entry("노원구", "Nowon-gu"), Map.entry("도봉구", "Dobong-gu"),
                Map.entry("동대문구", "Dongdaemun-gu"), Map.entry("동작구", "Dongjak-gu"),
                Map.entry("마포구", "Mapo-gu"), Map.entry("서대문구", "Seodaemun-gu"),
                Map.entry("서초구", "Seocho-gu"), Map.entry("성동구", "Seongdong-gu"),
                Map.entry("성북구", "Seongbuk-gu"), Map.entry("송파구", "Songpa-gu"),
                Map.entry("양천구", "Yangcheon-gu"), Map.entry("영등포구", "Yeongdeungpo-gu"),
                Map.entry("용산구", "Yongsan-gu"), Map.entry("은평구", "Eunpyeong-gu"),
                Map.entry("종로구", "Jongno-gu"), Map.entry("중구", "Jung-gu"),
                Map.entry("중랑구", "Jungnang-gu")
        );

        String regexPattern = String.join("|", guNameMapping.keySet()) + "|" + String.join("|", guNameMapping.values());

        AggregationExpression regexExpression = context -> new Document("$regexFind",
                new Document("input", "$vicinity")
                        .append("regex", "(" + regexPattern + ")$")
        );

        ProjectionOperation projectionOperation = Aggregation.project()
                .and(regexExpression).as("guMatch")
                .andInclude("rating");

        ProjectionOperation mappingProjection = Aggregation.project()
                .and(new AggregationExpression() {
                    @Override
                    public Document toDocument(AggregationOperationContext context) {
                        Document switchExpr = new Document("$switch", new Document("branches", Arrays.asList(
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Gangnam-gu"))).append("then", "강남구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Gangdong-gu"))).append("then", "강동구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Gangbuk-gu"))).append("then", "강북구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Gangseo-gu"))).append("then", "강서구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Gwanak-gu"))).append("then", "관악구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Gwangjin-gu"))).append("then", "광진구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Guro-gu"))).append("then", "구로구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Geumcheon-gu"))).append("then", "금천구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Nowon-gu"))).append("then", "노원구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Dobong-gu"))).append("then", "도봉구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Dongdaemun-gu"))).append("then", "동대문구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Dongjak-gu"))).append("then", "동작구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Mapo-gu"))).append("then", "마포구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Seodaemun-gu"))).append("then", "서대문구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Seocho-gu"))).append("then", "서초구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Seongdong-gu"))).append("then", "성동구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Seongbuk-gu"))).append("then", "성북구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Songpa-gu"))).append("then", "송파구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Yangcheon-gu"))).append("then", "양천구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Yeongdeungpo-gu"))).append("then", "영등포구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Yongsan-gu"))).append("then", "용산구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Eunpyeong-gu"))).append("then", "은평구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Jongno-gu"))).append("then", "종로구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Jung-gu"))).append("then", "중구"),
                                new Document("case", new Document("$eq", Arrays.asList("$guMatch.match", "Jungnang-gu"))).append("then", "중랑구")
                        )).append("default", "Unknown"));
                        return new Document("$cond", Arrays.asList(
                                new Document("$ne", Arrays.asList("$guMatch.match", null)),
                                switchExpr,
                                "Unknown"
                        ));
                    }
                }).as("gu")
                .andInclude("rating");

        Aggregation aggregation = Aggregation.newAggregation(
                projectionOperation,
                mappingProjection,
                Aggregation.match(Criteria.where("gu").ne("Unknown")),
                Aggregation.group("gu")
                        .avg("rating").as("averageRating"),
                Aggregation.project("averageRating").and("gu").previousOperation()
        );

        AggregationResults<AverageRatingDto> results = mongoTemplate.aggregate(aggregation, Location.class, AverageRatingDto.class);
        return results.getMappedResults();
    }

    public JsonNode getSeoulWithAverageRatings() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("seoul.json");
        JsonNode root = mapper.readTree(resource.getInputStream());

        Map<String, Double> ratingsMap = calculateAverageRatingByGu().stream()
                .collect(Collectors.toMap(AverageRatingDto::getGu, AverageRatingDto::getAverageRating));

        for (JsonNode feature : root.path("features")) {
            ObjectNode properties = (ObjectNode) feature.path("properties");
            String sigKorNm = properties.path("SIG_KOR_NM").asText();
            Double averageRating = ratingsMap.getOrDefault(sigKorNm, 0.0);
            properties.put("SIG_AVERAGE_RATING", averageRating);
        }

        return root;
    }

    //GeoJson 형식으로 데이터를 변환하면 사용가능
    //이 방식이 더 정확도가 높고 연산 시간도 빠르다
//    public List<Location> findLocationsNear(double latitude, double longitude, double distanceKm) {
//        Point point = new Point(longitude, latitude);
//        Distance distance = new Distance(distanceKm, Metrics.KILOMETERS);
//
//        Query query = new Query(Criteria.where("geometry.location").nearSphere(point).maxDistance(distance.getNormalizedValue()));
//        return mongoTemplate.find(query, Location.class);
//    }

    public List<Location> findLocationsNear(double latitude, double longitude, double distanceKm) {
        // Fetch all locations (not recommended for large datasets)
        List<Location> allLocations = mongoTemplate.findAll(Location.class);

        // Filter based on simplified distance calculation
        return allLocations.stream()
                .filter(location -> {
                    double dLat = location.getGeometry().getLocation().getLat() - latitude;
                    double dLng = location.getGeometry().getLocation().getLng() - longitude;
                    // Approximating Earth's curvature by using a constant factor; not accurate for large distances
                    double a = Math.pow(dLat, 2) + Math.pow(dLng, 2);
                    double distance = Math.sqrt(a) * 111; // Roughly converting degrees to kilometers
                    return distance <= distanceKm;
                })
                .collect(Collectors.toList());
    }



//    @Transactional
//    public void updateLocation(String id, Location updatedLocation) {
//        Optional<Location> optionalLocation = locationRepository.findById(id);
//
//        if (optionalLocation.isPresent()) {
//            Location location = optionalLocation.get();
//            //업데이트 할 필드
//        } else {
//            throw new IllegalStateException("가게 정보 업데이트에 실패하였습니다.");
//        }
//    }
//
//    @Transactional
//    public String deleteLocation(String id) {
//        locationRepository.deleteById(id);
//        return id;
//    }

}
