package com.oprun.database;

import java.sql.ResultSet;

import com.mysql.jdbc.PreparedStatement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderTown {

	
	public static int countByLabel(String label) {

		int ret = 0;
		ResultSet result = null;
		
		String str = "SELECT count(*) as count FROM TOWN WHERE lower(LABEL) = ? ";
		
		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(str);
			query.setString(1, label.toLowerCase());
			result = query.executeQuery();
			result.next();
			ret = result.getInt(1);
		} catch (Exception e) {
			System.out.println("QueryBuilderTown.countByLabel(" + label + ") KO : " + e);
		}
		return ret;
	}
	
	public static ResultSet getByLabel(String label) {

		int ret = 0;
		ResultSet result = null;
		
		String str = "SELECT * FROM TOWN WHERE lower(LABEL) = ? ";
		
		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(str);
			query.setString(1, label.toLowerCase());
			result = query.executeQuery();
			result.next();
		} catch (Exception e) {
			System.out.println("QueryBuilderTown.getByLabel(" + label + ") KO : " + e);
		}
		return result;
	}

	public static void insert( String label, String codeDep, int idCountry ) {

		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(
			"INSERT INTO TOWN ( label, code_department, id_country )values(  ?, ?, ?  ) ");
			
			query.setString(1, label );
			query.setString(2, codeDep );
			query.setInt(3, idCountry);
			
			query.executeUpdate();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
