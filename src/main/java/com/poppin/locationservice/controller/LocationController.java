package com.poppin.locationservice.controller;

import com.poppin.locationservice.document.LocationInfo;
import com.poppin.locationservice.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getLocationById(@PathVariable String id) {
        LocationInfo locationInfo = locationService.getLocationInfoByName(id);

        Map<String, Object> response = new HashMap<>();
        if (locationInfo != null) {
            response.put("status", "success");
            response.put("message", "Location fetched successfully");
            response.put("data", locationInfo);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Location not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
