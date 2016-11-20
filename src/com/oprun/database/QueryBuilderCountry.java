package com.oprun.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderCountry {

	
	public static int countByLabel(String label) {

		int ret = 0;
		
		String str = "SELECT count(*) as count FROM COUNTRY WHERE lower(LABEL) = ? ";
		ResultSet rs = null;
		
		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(str);
			query.setString(1, label.toLowerCase());
			rs = query.executeQuery();
			rs.next();
			ret = rs.getInt(1);
		} catch (Exception e) {
			System.out.println("QueryBuilderCountry.countByLabel(" + label + ") KO : " + e);
		}
		return ret;
	}
	
	public static ResultSet getByLabel(String label ){
		
		String str = "SELECT *  FROM COUNTRY WHERE lower(LABEL) = ? ";
		ResultSet rs = null;
		
		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(str);
			query.setString(1, label.toLowerCase());
			rs = query.executeQuery();
			rs.next();
		} catch (Exception e) {
			System.out.println("QueryBuilderCountry.countByLabel(" + label + ") KO : " + e);
		}
		return rs;
		
	}

	public static void insert( String label ) {

		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(
			"INSERT INTO country (  label ) " + "VALUES ( ? ) ");
			
			query.setString(1, label );
			query.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
