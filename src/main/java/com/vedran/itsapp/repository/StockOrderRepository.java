package com.vedran.itsapp.repository;

import com.vedran.itsapp.model.StockOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockOrderRepository extends MongoRepository<StockOrder, String> {
}
