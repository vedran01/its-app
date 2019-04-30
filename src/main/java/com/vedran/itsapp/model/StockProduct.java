package com.vedran.itsapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Document
public class StockProduct extends AbstractDocument{

  @DBRef
  private Stock stock;
  @DBRef
  private Product product;
  @NotNull
  private double purchasePrice;
  @NotNull
  private int quantity;

  public StockProduct(Stock stock,Product product, int quantity, double purchasePrice){
    this.stock = stock;
    this.product = product;
    this.quantity = quantity;
    this.purchasePrice = purchasePrice;
  }

}
