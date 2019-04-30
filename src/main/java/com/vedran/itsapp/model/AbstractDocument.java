package com.vedran.itsapp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractDocument {
  @Id
  @EqualsAndHashCode.Include
  private String id;
  @CreatedDate
  private Date created;
  @LastModifiedDate
  private Date modified;
}

