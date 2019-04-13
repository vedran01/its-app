package com.vedran.itsapp.controller;

import com.vedran.itsapp.model.ProductCategory;
import com.vedran.itsapp.service.ProductCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("product-category")
public class ProductCategoryController {

  private final ProductCategoryService service;

  public ProductCategoryController(ProductCategoryService service) {
    this.service = service;
  }

  @GetMapping
  Page<ProductCategory> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = "10") int size){
    return service.findAll(PageRequest.of(page,size));
  }

  @GetMapping("/{id}")
  ProductCategory findById(@PathVariable String id){
    return service.findById(id);
  }

  @PostMapping
  ResponseEntity<ProductCategory> saveProductCategory(@Valid @RequestBody ProductCategory category){
    ProductCategory productCategory = service.save(category);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(productCategory.getId()).toUri();
    return ResponseEntity.created(location).body(productCategory);
  }

}
