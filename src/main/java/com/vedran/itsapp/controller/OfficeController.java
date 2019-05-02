package com.vedran.itsapp.controller;

import com.vedran.itsapp.model.Office;
import com.vedran.itsapp.service.OfficeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/office")
public class OfficeController {

  private final OfficeService officeService;

  public OfficeController(OfficeService officeService) {
    this.officeService = officeService;
  }


  @GetMapping
  public Page<Office> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                              @RequestParam(required = false, defaultValue = "10") int size){
    return officeService.findAll(PageRequest.of(page,size));
  }

  @GetMapping("/{id}")
  public Office findById(@PathVariable String id){
    return officeService.findById(id);
  }

  @PostMapping
  public Office saveOffice(@RequestBody @Valid Office office){
    return officeService.saveOffice(office);
  }


  @PutMapping("/{id}")
  public Office updateOffice(@PathVariable String id, @RequestBody Office other){
    return officeService.updateOffice(id,other);
  }

  @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Office updatePicture(@PathVariable String id, @RequestPart("picture") MultipartFile file){
    return officeService.updatePicture(id, file);
  }

  @DeleteMapping("/{id}")
  public void deleteOffice(@PathVariable String id){
    officeService.deleteOffice(id);
  }
}
