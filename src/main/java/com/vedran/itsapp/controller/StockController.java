package com.vedran.itsapp.controller;

import com.vedran.itsapp.model.ItsUser;
import com.vedran.itsapp.model.StockProduct;
import com.vedran.itsapp.service.StockService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/stock")
public class StockController {
  public final StockService stockService;

  public StockController(StockService stockService) {
    this.stockService = stockService;
  }

  @PreAuthorize("hasAnyRole('ROLE_HEAD_ADMINISTRATOR', 'ROLE_ADMINISTRATOR')")
  @PostMapping("/add")
  public StockProduct addProductToStock(@RequestBody @Valid StockService.ProductTransferRequest request,
                                        @AuthenticationPrincipal ItsUser principal){
    return stockService.addProductToStock(request,principal);
  }
}
