package com.vedran.itsapp.repository;

import com.vedran.itsapp.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository<Stock, String> {
}
