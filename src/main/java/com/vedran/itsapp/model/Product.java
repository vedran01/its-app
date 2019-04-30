package com.vedran.itsapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vedran.itsapp.model.embedded.ProductAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "products")
public class Product extends AbstractDocument {
  @NotEmpty
  private String title;
  @Size(max = 1024)
  private String description;
  @NotNull
  private double price;
  @NotNull
  private double discount;

  private List<ProductAttribute> attributes;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<String> pictures;

  @DBRef
  private Set<Category> categories;
}
