package com.vedran.itsapp.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
public abstract class AbstractDocument {
  @Id
  private String id;
  @CreatedDate
  private Date created;
  @LastModifiedDate
  private Date modified;
}
