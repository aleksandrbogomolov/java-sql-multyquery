package com.aleksandrbogomolov.ora.helper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by bogomolov_av on 18.11.2017
 */
public class DbHelper {

  public static String getValueAsString(ResultSetMetaData metaData, ResultSet set, int column)
      throws SQLException {
    int type = metaData.getColumnType(column);
    switch (type) {
      case Types.VARCHAR:
        return set.getString(column);
      case Types.CHAR:
        return set.getString(column);
      case Types.NUMERIC:
        return String.valueOf(set.getLong(column));
      case Types.DATE:
        return String.valueOf(set.getDate(column));
      case Types.TIMESTAMP:
        return String.valueOf(set.getTimestamp(column));
      case Types.CLOB:
        return set.getString(column);
      default:
        throw new SQLException("Unknown type of column");
    }
  }
}
