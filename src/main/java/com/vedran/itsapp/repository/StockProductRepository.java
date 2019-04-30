package com.vedran.itsapp.repository;

import com.vedran.itsapp.model.StockProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StockProductRepository  extends MongoRepository<StockProduct, String> {
  Optional<StockProduct> findByProductId(String id);
  Page<StockProduct> findByStockId(String id, Pageable pageable);
  List<StockProduct> findAllByProductIdAndStockId(Set<String> productId, String stockId);
}
