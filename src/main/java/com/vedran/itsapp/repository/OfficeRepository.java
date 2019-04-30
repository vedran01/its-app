package com.vedran.itsapp.repository;

import com.vedran.itsapp.model.Office;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfficeRepository extends MongoRepository<Office, String> {
}
