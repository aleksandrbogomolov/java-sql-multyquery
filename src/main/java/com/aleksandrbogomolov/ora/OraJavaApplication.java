package com.aleksandrbogomolov.ora;

import static com.aleksandrbogomolov.ora.helper.FileHelper.parseTns;
import static com.aleksandrbogomolov.ora.helper.FileHelper.readQuery;

import com.aleksandrbogomolov.ora.helper.DbHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import oracle.jdbc.pool.OracleDataSource;

public class OraJavaApplication {

  public static void main(String[] args) throws SQLException {
    OracleDataSource dataSource = new OracleDataSource();
    String[] sqls = readQuery();
    parseTns().forEach((key, value) -> {
      for (String sql : sqls) {
        dataSource.setURL("jdbc:oracle:thin:@" + value);
        try (Connection connection = dataSource.getConnection("bogomolov_av", "")) {
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
