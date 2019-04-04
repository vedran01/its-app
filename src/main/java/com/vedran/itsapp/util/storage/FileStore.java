package com.vedran.itsapp.util.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStore {
  void storeFile(MultipartFile file, String path);

}
