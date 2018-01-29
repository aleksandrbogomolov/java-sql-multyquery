package com.aleksandrbogomolov.ora;

import com.aleksandrbogomolov.ora.helper.AuthHelper;
import com.aleksandrbogomolov.ora.helper.FileHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    setSystemOut(args);
    final String user = AuthHelper.getUserName();
    final char[] password = AuthHelper.getPassword();
    String[] sqls = FileHelper.readQuery();
    System.out.println(getFormattedCurrentDateTime() + "\n");

    ExecutorService executor = Executors.newFixedThreadPool(10);
    List<Future<String>> resultList = new ArrayList<>();

    FileHelper.parseTns().forEach((key, value) -> {
      resultList.add(executor.submit(new Worker(value, user, password, sqls, key)));
    });

    resultList.forEach(Main::accept);
    executor.shutdown();
  }

  private static String getFormattedCurrentDateTime() {
    return LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
  }

  private static void accept(Future<String> r) {
    try {
      System.out.println(r.get());
    } catch (InterruptedException | ExecutionException e) {
      log.warn("Cannot get result from future {}", e);
    }
  }

  private static void setSystemOut(String[] args) {
    if (args.length > 0) {
      try {
        String currentDir = System.getProperty("user.dir");
        File file = new File(currentDir, args[0]);
        System.setOut(new PrintStream(new FileOutputStream(file)));
      } catch (FileNotFoundException e) {
        log.error("File not found {}", e);
      }
    }
  }
}
