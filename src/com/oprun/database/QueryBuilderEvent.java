package com.oprun.database;

import java.sql.SQLException;

import com.mysql.jdbc.Statement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderEvent {

	public static void deleteAll() throws SQLException {

		Statement query = (Statement) HelperDatabase.getConnection().createStatement();
		query.executeUpdate("DELETE FROM EVENT");

	}

}
