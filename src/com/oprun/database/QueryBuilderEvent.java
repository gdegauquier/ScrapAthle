package com.oprun.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderEvent {

	public static void deleteAll() throws SQLException {

		Statement query = (Statement) HelperDatabase.getConnection().createStatement();
		query.executeUpdate("DELETE FROM EVENT");

	}

	public static int countByIdHash(String idHash) {

		int ret = 0;
		ResultSet result = null;

		try {
			Statement query = (Statement) HelperDatabase.getConnection().createStatement();
			result = query.executeQuery("SELECT count(*) as count FROM EVENT WHERE ID_HASH = '" + idHash + "'");
			result.next();
			ret = result.getInt(1);
		} catch (SQLException e) {
			System.out.println("countByIdHash(" + idHash + ") KO : " + e);
		}
		return ret;
	}

	public static void insert(Map<String,String> values) {

		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(
			"INSERT INTO EVENT  (  id_hash, title, deleted, date_event,id_level, sub_title, id_town ) "
							  + "VALUES ( ?, ?, ?, ?, ?, ?, ? ) ");
			
			query.setString(1, values.get("id_hash")  );
			query.setString(2, values.get("title")  );
			query.setInt(3, Integer.valueOf(values.get("deleted"))  );
			query.setString(4, values.get("date_event")  );
			query.setInt(5, Integer.valueOf(values.get("id_level"))  );
			query.setString(6, values.get("sub_title")  );
			query.setInt(7, Integer.valueOf(values.get("id_town"))  );
			query.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void update(Map<String,String> values) {

		String valuesQuery = " TITLE = ?, DELETED = ?, DATE_EVENT = ?, ID_LEVEL = ?, SUB_TITLE = ?, ID_TOWN = ? ";

		try {

			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(
					"UPDATE EVENT SET " + valuesQuery + 
					" WHERE ID_HASH = '" + values.get("id_hash")  + "'");
					
					query.setString(1, values.get("title")  );
					query.setInt(2, Integer.valueOf(values.get("deleted"))  );
					query.setString(3, values.get("date_event")  );
					query.setInt(4, Integer.valueOf(values.get("id_level"))  );
					query.setString(5, values.get("sub_title")  );
					query.setInt(6, Integer.valueOf(values.get("id_town"))  );
					
					query.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
