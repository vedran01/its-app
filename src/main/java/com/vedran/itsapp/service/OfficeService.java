package com.vedran.itsapp.service;

import com.vedran.itsapp.model.Office;
import com.vedran.itsapp.model.Stock;
import com.vedran.itsapp.repository.OfficeRepository;
import com.vedran.itsapp.util.error.exceptions.ResourceNotFoundException;
import com.vedran.itsapp.util.storage.ImageStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OfficeService {

  private final OfficeRepository officeRepository;
  private final StockService stockService;
  private final ImageStore imageStore;


  public OfficeService(OfficeRepository officeRepository, StockService stockService, ImageStore imageStore) {
    this.officeRepository = officeRepository;
    this.stockService = stockService;
    this.imageStore = imageStore;
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
    office.setPicture("office-picture.png");
    return officeRepository.save(office);

  }

  public Office updateOffice(String id, Office other){
    Office office = findById(id);
    office.setName(other.getName());
    office.setAddress(other.getAddress());
    office.setContact(other.getContact());
    return officeRepository.save(office);
  }

  public Office updatePicture(String id, MultipartFile picture){
    Office office = findById(id);
    imageStore.deleteImage("offices/" + office.getPicture());
    String pictureName = imageStore.storeImage(picture, "offices/", office.getId());
    office.setPicture(pictureName);
    return officeRepository.save(office);
  }

  public void deleteOffice(String id){
    officeRepository.deleteById(id);
  }
}
