package com.aleksandrbogomolov.ora.helper;

import java.io.Console;

/**
 * Created by bogomolov_av on 25.01.2018
 */
public class AuthHelper {

  private static final Console console = System.console();

  public static String getUserName() {
    return console.readLine("Enter user name: ");
  }

  public static char[] getPassword() {
    return console.readPassword("Enter password: ");
  }
}
