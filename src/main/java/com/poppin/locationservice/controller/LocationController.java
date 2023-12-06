package com.poppin.locationservice.controller;

import com.poppin.locationservice.document.Location;
import com.poppin.locationservice.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/location")
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getLocationById(@PathVariable String id) {
        Location locationInfo = locationService.getLocationByName(id);

        Map<String, Object> response = new HashMap<>();
        if (locationInfo != null) {
            response.put("status", "success");
            response.put("message", "Location.java fetched successfully");
            response.put("data", locationInfo);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Location.java not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

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
    @GetMapping("/{location_id}")
    public ResponseEntity<Location> getLocation(@PathVariable("location_id") String id) {
        Optional<Location> location = locationService.getLocation(id);
        if (location.isPresent()) {
            return ResponseEntity.ok(location.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 가게 정보 업데이트
     */
    @PutMapping("/{location_id}")
    public ResponseEntity<String> updateLocation(@PathVariable("location_id") String id, @RequestBody Location locationDTO) {
        Optional<Location> savedLocation = locationService.getLocation(id);
        if (savedLocation.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Location location = Location.builder()
                //업데이트 할 필드
                .build();
        locationService.updateLocation(id, location);
        return ResponseEntity.ok(id);
    }

    /**
     * 가게 삭제
     */
    @DeleteMapping("/{location_id}")
    public ResponseEntity<String> deleteLocation(@PathVariable("location_id") String id) {
        Optional<Location> savedLocation = locationService.getLocation(id);
        if (savedLocation.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        String deletedLocationId = locationService.deleteLocation(id);
        return ResponseEntity.ok(deletedLocationId);
    }

}
