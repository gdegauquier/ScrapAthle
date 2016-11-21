package com.oprun.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderLevel {

    public static void deleteAll() throws SQLException {
        Statement query = (Statement) HelperDatabase.getConnection().createStatement();
        query.executeUpdate("DELETE FROM LEVEL");
    }

    public static int countByLabel(String label) {

        int ret = 0;
        ResultSet result = null;

        String str = "SELECT count(*) as count FROM LEVEL WHERE lower(LABEL) = ? ";

        try {
            PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(str);
            query.setString(1, label.toLowerCase());
            result = query.executeQuery();
            result.next();
            ret = result.getInt(1);
        } catch (Exception e) {
            System.out.println("QueryBuilderLevel.countByLabel(" + label + ") KO : " + e);
        }
        return ret;
    }

    public static void insert(String label){
        
        try {
            PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement("INSERT INTO LEVEL ( LABEL, DELETED ) VALUES (  ?, 0 ) ");
            query.setString(1, label);
            query.executeUpdate();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static ResultSet getByLabel(String label) {

        int ret = 0;
        ResultSet result = null;
        
        String str = "SELECT * FROM LEVEL WHERE lower(LABEL) = ? ";
        
        try {
                PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(str);
                query.setString(1, label.toLowerCase());
                result = query.executeQuery();
                result.next();
        } catch (Exception e) {
                System.out.println("QueryBuilderLevel.getByLabel(" + label + ") KO : " + e);
        }
        return result;
    }
	        

}
