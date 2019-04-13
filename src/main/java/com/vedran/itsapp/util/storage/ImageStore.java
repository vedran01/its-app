package com.vedran.itsapp.util.storage;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log
@Component
public class ImageStore implements FileStore {

  private Path basePath;

  public ImageStore(@Value("${its.file-store.basePath:uploads}") String path){
    this.basePath = Paths.get("").toAbsolutePath()
            .resolve(path);
  }

  public String storeImage(MultipartFile file, String path, String id){
    Path base = basePath.resolve(path);
    if(Files.notExists(base)){
      try {
        Files.createDirectories(base);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    String pictureName = resolvePictureName(id,file);
    storeFile(file,path + pictureName);
    return pictureName;
  }

  public boolean deleteImage(String path){
    try {
      return Files.deleteIfExists(basePath.resolve(path));
    }
    catch (IOException e) {
      return false;
    }
  }

  public void storeFile(MultipartFile file, String path){

    try {
      Files.copy(file.getInputStream(), basePath.resolve(path));
    }
    catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }

  }

  private String resolvePictureName(String name ,MultipartFile file){
    String originalFilename = file.getOriginalFilename();

    if(originalFilename  != null){

      if(originalFilename.endsWith("jpg")){
        return name + ".jpg";
      }
      else if(originalFilename.endsWith("png")){
        return name + ".png";
      }
      else if(originalFilename.endsWith("jpeg")){
        return name + ".jpeg";
      }
      else
      {
        throw new RuntimeException("Bad image extension");
      }
    }
    else
    {
      throw new NullPointerException("No image present");
    }

  }

  @PostConstruct
  public void createDirectories() throws IOException {
    if(Files.notExists(basePath)){
      log.info("Creating base directory" + basePath.toString());
      Files.createDirectories(basePath);
    }
  }
}
