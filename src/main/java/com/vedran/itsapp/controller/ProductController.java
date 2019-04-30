package com.vedran.itsapp.controller;

import com.vedran.itsapp.model.Product;
import com.vedran.itsapp.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  Page<Product> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                        @RequestParam(required = false, defaultValue = "10") int size){
    return productService.findAll(PageRequest.of(page,size));
  }

  @GetMapping("/{id}")
  Product findById(@PathVariable String id){
    return productService.findById(id);
  }

  @PreAuthorize("hasAnyRole('ROLE_HEAD_ADMINISTRATOR', 'ROLE_ADMINISTRATOR')")
  @PostMapping
  Product saveProduct(@RequestBody @Valid Product product){
    return productService.saveProduct(product);
  }

  @PutMapping("/{id}")
  Product updateProduct(@PathVariable String id, @RequestBody @Valid Product product){
    return productService.updateProduct(id,product);
  }

  // TODO RETURN RESPONSE ENTITY NO CONTENT
  @DeleteMapping("/{id}")
  void deleteProduct(@PathVariable String id){
    productService.deleteProduct(id);
  }
}
