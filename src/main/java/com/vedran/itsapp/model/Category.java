package com.vedran.itsapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "product-categories")
public class Category extends AbstractDocument {
  @NotEmpty
  private String name;
  @DBRef
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Category parent;
}
