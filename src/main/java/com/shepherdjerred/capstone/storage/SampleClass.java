package com.shepherdjerred.capstone.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SampleClass {
  public void createTable() {
    String dir = System.getProperty("user.dir");

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:sqlite://" + dir + "/db.sqlite");

    HikariDataSource ds = new HikariDataSource(config);

    try {
      Connection connection = ds.getConnection();
      connection.createStatement().execute("CREATE TABLE IF NOT EXISTS settings (setting_key VARCHAR(255), setting_value VARCHAR(255));");
      connection.createStatement().execute("INSERT INTO settings VALUES ('my_setting', 'my_value');");
      ResultSet results = connection.createStatement().executeQuery("SELECT * FROM settings");

      while (results.next()) {
        log.info(results.getString(1) + " " + results.getString(2));
      }

      results.close();
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    ds.close();
  }
}
