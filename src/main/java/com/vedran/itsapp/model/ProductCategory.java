package com.vedran.itsapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;
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

  @Min(value = 1)
  @Max(value = 4)
  private int level;

  @Transient
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String _parentId;

  @DBRef
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private ProductCategory parent;
}
