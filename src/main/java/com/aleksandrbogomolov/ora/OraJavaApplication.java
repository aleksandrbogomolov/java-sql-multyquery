package com.aleksandrbogomolov.ora;

import static com.aleksandrbogomolov.ora.helper.FileHelper.parseTns;
import static com.aleksandrbogomolov.ora.helper.FileHelper.readQuery;

import com.aleksandrbogomolov.ora.helper.DbHelper;
import java.io.BufferedInputStream;
import java.io.Console;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Scanner;
import oracle.jdbc.pool.OracleDataSource;

public class OraJavaApplication {

  public static void main(String[] args) throws SQLException {
    Console console = System.console();
    String user = null;
    char[] password = new char[0];
    if (console != null) {
      user = console.readLine("Enter user name: ");
      password = console.readPassword("Enter password: ");
    }
    OracleDataSource dataSource = new OracleDataSource();
    String[] sqls = readQuery();
    System.out.println(
        LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
            + "\n");
    String finalUser = user;
    char[] finalPassword = password;
    parseTns().forEach((key, value) -> {
      for (String sql : sqls) {
        dataSource.setURL("jdbc:oracle:thin:@" + value);
        try (Connection connection = dataSource.getConnection(finalUser,
            String.valueOf(finalPassword))) {
          PreparedStatement statement = connection.prepareStatement(sql);
          ResultSet resultSet = statement.executeQuery();
          ResultSetMetaData metaData = resultSet.getMetaData();
//          StringBuilder columnName = new StringBuilder();
//          for (int i = 1; i <= metaData.getColumnCount(); i++) {
//            if (i > 1) {
//              columnName.append(" | ");
//            }
//            columnName.append(metaData.getColumnName(i));
//          }
//          System.out.println(columnName.toString());
          while (resultSet.next()) {
            StringBuilder result = new StringBuilder();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
              if (i > 1) {
                result.append(" | ");
              }
              result.append(DbHelper.getValueAsString(metaData, resultSet, i));
            }
            System.out.println(result);
          }
        } catch (SQLException ex) {
          System.out.println(key + " - " + ex.getMessage());
        }
      }
      System.out.println();
    });
  }
}
