package com.aleksandrbogomolov.ora;

import com.aleksandrbogomolov.ora.helper.DbHelper;
import java.util.List;
import oracle.jdbc.pool.OracleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.concurrent.Callable;

/**
 * Created by bogomolov_av on 26.01.2018
 */
public class Worker implements Callable<String> {

    private static final Logger log = LoggerFactory.getLogger(Worker.class);

    private final String url;

    private final String userName;

    private final String password;

    private final List<String> queries;

    private final String rcName;

    private OracleDataSource dataSource;

    Worker(String url, String userName, char[] password, List<String> queries, String rcName) {
        this.url = url;
        this.userName = userName;
        this.password = String.valueOf(password);
        this.queries = queries;
        this.rcName = rcName;
        try {
            this.dataSource = new OracleDataSource();
        } catch (SQLException e) {
            log.error("Cannot create Oracle data source {}.", e);
        }
    }

    @Override
    public String call() {
        dataSource.setURL("jdbc:oracle:thin:@" + url);
        try (Connection connection = dataSource.getConnection(userName, password)) {
            StringBuilder result = new StringBuilder();
            for (String query : queries) {
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                ResultSetMetaData metaData = resultSet.getMetaData();
                while (resultSet.next()) {
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        if (i > 1) {
                            result.append(" | ");
                        }
                        result.append(DbHelper.getValueAsString(metaData, resultSet, i));
                    }
                    result.append("\n");
                }
            }
            return result.toString();
        } catch (SQLException ex) {
            log.warn(rcName + " - " + ex.getMessage());
        }
        return rcName + ": no result";
    }
}
