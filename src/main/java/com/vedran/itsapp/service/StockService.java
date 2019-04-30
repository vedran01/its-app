package com.vedran.itsapp.service;

import com.vedran.itsapp.model.*;
import com.vedran.itsapp.model.embedded.OfficeType;
import com.vedran.itsapp.repository.StockProductRepository;
import com.vedran.itsapp.repository.StockRepository;
import com.vedran.itsapp.util.error.exceptions.BadRequestException;
import com.vedran.itsapp.util.error.exceptions.ResourceNotFoundException;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StockService {
  private final StockRepository stockRepository;
  private final StockProductRepository stockProductRepository;
  private final ProductService productService;

  public StockService(StockRepository stockRepository, StockProductRepository stockProductRepository, ProductService productService) {
    this.stockRepository = stockRepository;
    this.stockProductRepository = stockProductRepository;
    this.productService = productService;
  }


  public Page<Stock> findAll(Pageable pageable){
    return stockRepository.findAll(pageable);
  }

  public Page<StockProduct> findAllByStockId(String id, Pageable pageable){
    return stockProductRepository.findByStockId(id , pageable);
  }

  public Stock findById(String id){
    return stockRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Stock.class, "id", id));
  }

  Stock saveStock(Stock stock){
    return stockRepository.save(stock);
  }


  public StockProduct addProductToStock(ProductTransferRequest request, ItsUser principal){

    Office office = principal.getOffice();

    if(office != null && office.getType().equals(OfficeType.STOCK_OFFICE)){
      Stock stock = office.getStock();
      int quantity = request.getQuantity();
      double purchasePrice = request.getPurchasePrice();
      String productId = request.getProductId();
      Product product = productService.findById(productId);
      StockProduct stockProduct = stockProductRepository.findByProductId(productId)
              .orElse(new StockProduct(stock,product, 0, purchasePrice));

      int _quantity = stockProduct.getQuantity() + quantity;
      stockProduct.setQuantity(_quantity);
      return stockProductRepository.save(stockProduct);
    }
    else throw new BadRequestException("You can not save product to stock");
  }

  @Data
  public static class ProductTransferRequest {
    String productId;
    double purchasePrice;
    int quantity;
  }

}
