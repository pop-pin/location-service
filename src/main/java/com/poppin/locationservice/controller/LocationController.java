package com.poppin.locationservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.poppin.locationservice.document.Location;
import com.poppin.locationservice.dto.response.AverageRatingDto;
import com.poppin.locationservice.dto.response.CustomErrorResponse;
import com.poppin.locationservice.dto.response.LocationResponseDto;
import com.poppin.locationservice.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    /**
     * 검색 및 페이징 1
     */
    @GetMapping("/search")
    public Page<Location> searchLocation(@RequestParam("keyword") String keyword, @RequestParam("page") int page, @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return locationService.searchLocation(keyword, pageable);
    }

    /**
     * 특정 가게 정보 가져오기
     */
    @GetMapping("")
    public ResponseEntity<?> getLocation(@RequestParam("location_id") String id) {
        Optional<Location> locationOpt = locationService.getLocation(id);
        if (locationOpt.isPresent()) {
            LocationResponseDto responseDto = convertToLocationResponseDto(locationOpt.get());
            return ResponseEntity.ok(responseDto);
        } else {
            CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                    .errorCode(404)
                    .message("Location not found with id: " + id)
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    //서울시 구별로 평균 rating
    @GetMapping("/average-rating")
    public ResponseEntity<?> getAverageRatingByGu() {
        List<AverageRatingDto> averageRatings = locationService.calculateAverageRatingByGu();
        if (averageRatings.isEmpty()) {
            CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                    .errorCode(404)
                    .message("Average-rating is not found")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(averageRatings);
    }

    //맵 정보에 서울시의 구별로 평균 rating 추가해서 반환
    @GetMapping("/map-info")
    public ResponseEntity<?> getMapInfo() {
        try {
            JsonNode seoulWithRatings = locationService.getSeoulWithAverageRatings();
            return ResponseEntity.ok(seoulWithRatings);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error reading seoul.json");
        }
    }

    /**
     * 가게 정보 업데이트
     */
//    @PutMapping("/{location_id}")
//    public ResponseEntity<String> updateLocation(@PathVariable("location_id") String id, @RequestBody Location locationDTO) {
//        Optional<Location> savedLocation = locationService.getLocation(id);
//        if (savedLocation.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        Location location = Location.builder()
//                //업데이트 할 필드
//                .build();
//        locationService.updateLocation(id, location);
//        return ResponseEntity.ok(id);
//    }

    /**
     * 가게 삭제
     */
//    @DeleteMapping("/{location_id}")
//    public ResponseEntity<String> deleteLocation(@PathVariable("location_id") String id) {
//        Optional<Location> savedLocation = locationService.getLocation(id);
//        if (savedLocation.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        String deletedLocationId = locationService.deleteLocation(id);
//        return ResponseEntity.ok(deletedLocationId);
//    }

    private LocationResponseDto convertToLocationResponseDto(Location location) {
        // 'geometry.location'에서 위도(latitude)와 경도(longitude) 추출
        Double lat = location.getGeometry() != null && location.getGeometry().getLocation() != null ? location.getGeometry().getLocation().getLat() : null;
        Double lng = location.getGeometry() != null && location.getGeometry().getLocation() != null ? location.getGeometry().getLocation().getLng() : null;

        return LocationResponseDto.builder()
                .id(location.getId())
                .lat(lat)
                .lng(lng)
                .name(location.getName())
                .rating(location.getRating() != null ? location.getRating() : null)
                .types(location.getTypes())
                .vicinity(location.getVicinity())
                .build();
    }

}
