package com.oprun.database;

import java.sql.ResultSet;

import com.mysql.jdbc.PreparedStatement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderType {

	
	public static int countByLabel(String label) {

		int ret = 0;
		ResultSet result = null;
		
		String str = "SELECT count(*) as count FROM TYPE WHERE lower(LABEL) = ? ";
		
		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(str);
			query.setString(1, label.toLowerCase());
			result = query.executeQuery();
			result.next();
			ret = result.getInt(1);
		} catch (Exception e) {
			System.out.println("QueryBuilderType.countByLabel(" + label + ") KO : " + e);
		}
		return ret;
	}
	

	public static void insert( String label ) {

		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(
			"INSERT INTO TYPE ( LABEL, DELETED ) values(  ?, 0  ) ");
			
			query.setString(1, label );
			query.executeUpdate();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
