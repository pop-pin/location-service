package com.poppin.locationservice.service;

import com.poppin.locationservice.document.LocationInfo;
import com.poppin.locationservice.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationInfo getLocationInfoByName(String name) {
        return locationRepository.findLocationInfoByName(name);
    }

}
