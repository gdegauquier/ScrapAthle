package com.oprun.helper;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class HelperDatabase {

	private static Connection conn = null;

	public static Connection getConnection() {

		if (null != conn) {
			return conn;
		}

		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser("root");
		dataSource.setPassword("");
		dataSource.setDatabaseName("oprun");
		dataSource.setServerName("localhost");

		try {
			conn = (Connection) dataSource.getConnection();
		} catch (SQLException e) {
			System.out.println("Connection to DB KO " + e);
		}

		return conn;
	}

}
