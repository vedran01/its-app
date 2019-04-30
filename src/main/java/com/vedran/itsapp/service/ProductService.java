package com.vedran.itsapp.service;

import com.vedran.itsapp.model.Product;
import com.vedran.itsapp.repository.ProductRepository;
import com.vedran.itsapp.util.error.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Page<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

  public Product findById(String id) {
    return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Product.class, "id",id));
  }

  public Product saveProduct(Product product) {
    return productRepository.save(product);
  }

  public Product updateProduct(String id, Product other){
    Product product = findById(id);
    product.setAttributes(other.getAttributes());
    product.setCategories(other.getCategories());
    product.setTitle(other.getTitle());
    product.setPrice(other.getPrice());
    product.setDiscount(other.getDiscount());
    return productRepository.save(product);
  }

  public void deleteProduct(String id){ productRepository.deleteById(id); }
}
