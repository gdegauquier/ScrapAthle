package com.oprun.database;

import java.sql.ResultSet;

import com.mysql.jdbc.PreparedStatement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderStation {

	
	public static ResultSet get() {

		int ret = 0;
		ResultSet result = null;
		
		String str = "SELECT * FROM STATION";
		
		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(str);
			result = query.executeQuery();
		} catch (Exception e) {
			System.out.println("QueryBuilderStation.get() KO : " + e);
		}
		return result;
	}
	
}
