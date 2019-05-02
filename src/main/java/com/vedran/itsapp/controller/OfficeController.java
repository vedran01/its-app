package com.vedran.itsapp.controller;

import com.vedran.itsapp.model.Office;
import com.vedran.itsapp.service.OfficeService;
import com.vedran.itsapp.util.ResponseBody;
import com.vedran.itsapp.util.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

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
  public ResponseEntity<ResponseBody<Office>> saveOffice(@RequestBody @Valid Office office){
    Office _office = officeService.saveOffice(office);
    URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .buildAndExpand(String.format("/office/%s", office.getId()))
            .toUri();
    return ResponseEntity.created(uri).body(new ResponseBody<>("Office has been created", _office));
  }


  @PutMapping("/{id}")
  public ResponseBody<Office> updateOffice(@PathVariable String id, @RequestBody Office other){
    Office office = officeService.updateOffice(id, other);
    return new ResponseBody<>("Office has been updated", office);
  }

  @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseBody<String> updatePicture(@PathVariable String id, @RequestPart("picture") MultipartFile file){
    Office office = officeService.updatePicture(id, file);
    return new ResponseBody<>("Office picture has been uploaded",office.getPicture());
  }

  @DeleteMapping("/{id}")
  public Response deleteOffice(@PathVariable String id){
    officeService.deleteOffice(id);
    return new Response("Office has been deleted");
  }
}
