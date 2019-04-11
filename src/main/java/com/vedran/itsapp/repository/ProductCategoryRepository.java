package com.vedran.itsapp.repository;

import com.vedran.itsapp.model.ProductCategory;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductCategoryRepository extends PagingAndSortingRepository<ProductCategory, String> {
}
