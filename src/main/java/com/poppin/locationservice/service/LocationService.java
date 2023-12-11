package com.poppin.locationservice.service;

import com.poppin.locationservice.document.Location;
import com.poppin.locationservice.dto.response.AverageRatingDto;
import com.poppin.locationservice.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.util.List;
import java.util.Optional;

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
        AggregationExpression regexExpression = context -> new Document("$regexFind",
                new Document("input", "$vicinity")
                        .append("regex", "([가-힣]+구|[A-Za-z]+-gu)")
        );

        ProjectionOperation projectionOperation = Aggregation.project()
                .and(regexExpression).as("guMatch")
                .andInclude("rating");

        Aggregation aggregation = Aggregation.newAggregation(
                projectionOperation,
                Aggregation.project().and("guMatch.match").as("gu").andInclude("rating"),
                Aggregation.match(Criteria.where("gu").exists(true)),
                Aggregation.group("gu").avg("rating").as("averageRating"),
                Aggregation.project("averageRating").and("gu").previousOperation()
        );

        AggregationResults<AverageRatingDto> results = mongoTemplate.aggregate(aggregation, Location.class, AverageRatingDto.class);
        return results.getMappedResults();
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
