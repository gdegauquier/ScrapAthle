package com.oprun.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderTempHash {

	public static void insert(String id) {

		try {
			Statement query = (Statement) HelperDatabase.getConnection().createStatement();
			query.executeUpdate("INSERT INTO TEMP_HASH VALUES ( '" + id + "' )");
		} catch (SQLException e) {
			System.out.println("INSERT temp_hash KO : " + e);
		}
	}

	public static void deleteAll() {

		try {
			Statement query = (Statement) HelperDatabase.getConnection().createStatement();
			query.executeUpdate("DELETE FROM TEMP_HASH");
		} catch (SQLException e) {
			System.out.println("DELETE temp_hash KO : " + e);
		}
	}

	public static ResultSet get() {

		ResultSet result = null;
		try {
			Statement query = (Statement) HelperDatabase.getConnection().createStatement();
			result = query.executeQuery("SELECT * FROM TEMP_HASH LIMIT 50"); // à
																				// retirer
																				// quand
																				// process
																				// OK
		} catch (SQLException e) {
			System.out.println("get() temp_hash KO : " + e);
		}
		return result;
	}

}
