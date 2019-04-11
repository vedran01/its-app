package com.vedran.itsapp.service;

import com.vedran.itsapp.model.ProductCategory;
import com.vedran.itsapp.repository.ProductCategoryRepository;
import com.vedran.itsapp.util.error.BadRequestException;
import com.vedran.itsapp.util.error.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

  private final ProductCategoryRepository repository;

  public ProductCategoryService(ProductCategoryRepository repository) {
    this.repository = repository;
  }

  public ProductCategory findById(String id){
    return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ProductCategory.class, "id", id));
  }

  public Page<ProductCategory> findAll(Pageable pageable){
    return repository.findAll(pageable);
  }

  public ProductCategory save(ProductCategory productCategory){

    if(productCategory.getLevel() == 1)
      return repository.save(productCategory);

    ProductCategory parent = productCategory.getParent();

    if(parent != null && parent.getId() != null){
      parent = findById(parent.getId());

      if(parent.getLevel() == productCategory.getLevel() -1){
        productCategory.setParent(parent);
        return repository.save(productCategory);
      }
      else{
        throw new BadRequestException(String.format("Child category with level %d can not have parent with level %d.",
                productCategory.getLevel(), parent.getLevel()));
      }
    }
    else
    {
      throw new BadRequestException("Parent category id should be defined.");
    }

  }

}
