package com.vedran.itsapp.service;

import com.vedran.itsapp.model.Office;
import com.vedran.itsapp.model.Stock;
import com.vedran.itsapp.repository.OfficeRepository;
import com.vedran.itsapp.util.error.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OfficeService {

  private final OfficeRepository officeRepository;
  private final StockService stockService;

  public OfficeService(OfficeRepository officeRepository, StockService stockService) {
    this.officeRepository = officeRepository;
    this.stockService = stockService;
  }

  public Page<Office> findAll(Pageable pageable){
    return officeRepository.findAll(pageable);
  }

  public Office findById(String id){
    return officeRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException(Office.class, "id",id));
  }

  public Office saveOffice(Office office){
    Stock stock = stockService.saveStock(new Stock());
    office.setStock(stock);
    return officeRepository.save(office);

  }

  public Office updateOffice(String id, Office other){
    Office office = findById(id);
    office.setName(other.getName());
    office.setAddress(other.getAddress());
    office.setContact(other.getContact());
    return officeRepository.save(office);
  }

  public void deleteOffice(String id){
    officeRepository.deleteById(id);
  }
}
