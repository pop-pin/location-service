package com.poppin.locationservice.repository;

import com.poppin.locationservice.document.LocationInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends MongoRepository<LocationInfo, String> {
    LocationInfo findLocationInfoByName(String name);

}
