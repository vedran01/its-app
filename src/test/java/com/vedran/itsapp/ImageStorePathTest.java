package com.vedran.itsapp;

import lombok.extern.java.Log;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log
public class ImageStorePathTest {

  @Test
  public void testPath() throws IOException {
    Path base = Paths.get("").toAbsolutePath();
    log.info(base.toString());

    Path user = Paths.get("/user");
    log.info(base.resolve(user).toString());

    //Files.createDirectories(base.resolve(user));

    //File file = new File(base.resolve(user).resolve("text.txt").toString());

    //Files.copy(file.toPath(), base.resolve(user).resolve("text2.txt"));

  }
}
