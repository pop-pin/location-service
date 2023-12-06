package com.poppin.locationservice.service;

import com.poppin.locationservice.document.Location;
import com.poppin.locationservice.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public Location getLocationByName(String name) {
        return locationRepository.findLocationByName(name);
    }

    public Optional<Location> getLocation(String id) {
        return locationRepository.findById(id);
    }

    public Page<Location> searchLocation(String name, Pageable pageable) {
        return locationRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public void updateLocation(String id, Location updatedLocation) {
        Optional<Location> optionalLocation = locationRepository.findById(id);

        if (optionalLocation.isPresent()) {
            Location location = optionalLocation.get();
            //업데이트 할 필드
        } else {
            throw new IllegalStateException("가게 정보 업데이트에 실패하였습니다.");
        }
    }

    @Transactional
    public String deleteLocation(String id) {
        locationRepository.deleteById(id);
        return id;
    }

}
