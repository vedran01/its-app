package com.vedran.itsapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = false)
@Document
public class ProductCategory extends AbstractDocument {

  @NotEmpty
  private String name;

  @NotEmpty
  @Min(value = 1)
  @Max(value = 3)
  private short level;

  @DBRef
  private ProductCategory parent;
}
