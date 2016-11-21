package com.oprun.database;

import java.sql.ResultSet;

import com.mysql.jdbc.PreparedStatement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderDistrict {

	
	public static int countByCode(String code) {

		int ret = 0;
		ResultSet result = null;
		
        String str = "SELECT count(*) as count FROM DISTRICT WHERE lower(CODE) = ? ";
		
		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(str);
			query.setString(1, code.toLowerCase());
			result = query.executeQuery();
			result.next();
			ret = result.getInt(1);
		} catch (Exception e) {
            System.out.println("QueryBuilderDistrict.countByCode(" + code + ") KO : " + e);
		}
		return ret;
	}

    public static void insert(String code, String label, int idCountry) {

		try {
			PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(
                    "INSERT INTO DISTRICT values(  ?,  ?, ?  ) ");
			
			query.setString(1, code );
            query.setString(2, label);
            query.setInt(3, idCountry);
			
			query.executeUpdate();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
