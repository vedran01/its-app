package com.vedran.itsapp.controller;

import com.vedran.itsapp.model.Category;
import com.vedran.itsapp.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/product-category")
public class CategoryController {
  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  Page<Category> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                         @RequestParam(required = false, defaultValue = "10") int size){
    return categoryService.findAll(PageRequest.of(page,size));
  }

  @GetMapping("/{id}")
  Category findById(@PathVariable String id){
    return categoryService.findById(id);
  }

  @PostMapping
  Category saveCategory(@Valid @RequestBody Category category){
    return categoryService.saveCategory(category);
  }

  @PutMapping("/{id}")
  Category updateCategory(@PathVariable String id, @Valid @RequestBody Category category){
    return categoryService.updateCategory(id,category);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteCategory(@PathVariable String id){
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}
