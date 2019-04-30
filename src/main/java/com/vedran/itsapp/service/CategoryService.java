package com.vedran.itsapp.service;

import com.vedran.itsapp.model.Category;
import com.vedran.itsapp.repository.CategoryRepository;
import com.vedran.itsapp.util.error.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public Page<Category> findAll(Pageable pageable){
    return categoryRepository.findAll(pageable);
  }

  public Category findById(String id){
    return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Category.class, "id", id));
  }

  public Category saveCategory(Category category){
    if(category.getParent() != null){
      String parentId = category.getParent().getId();
      Category parent = findById(parentId);
      category.setParent(parent);
    }
    return categoryRepository.save(category);
  }

  public Category updateCategory(String id , Category other){
    Category category = findById(id);
    if(other.getParent() != null){
      String parentId = other.getParent().getId();
      Category parent = findById(parentId);
      category.setParent(parent);
    }
    category.setName(other.getName());
    return categoryRepository.save(category);
  }

  public void deleteCategory(String id) {
    Category category = findById(id);
    categoryRepository.delete(category);
  }
}
