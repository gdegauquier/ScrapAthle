package com.oprun.database;

import java.sql.ResultSet;

import com.mysql.jdbc.PreparedStatement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderDepartment {

    public static int countByCode(String code) {

        int ret = 0;
        ResultSet result = null;

        String str = "SELECT count(*) as count FROM DEPARTMENT WHERE lower(CODE) = ? ";

        try {
            PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(str);
            query.setString(1, code.toLowerCase());
            result = query.executeQuery();
            result.next();
            ret = result.getInt(1);
        } catch (Exception e) {
            System.out.println("QueryBuilderDepartment.countByCode(" + code + ") KO : " + e);
        }
        return ret;
    }

    public static void insert(String code, String label, String codeDistrict) {

        try {
            PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement("INSERT INTO DEPARTMENT values(  ?,  ?, ?  ) ");

            query.setString(1, code);
            query.setString(2, label);
            query.setString(3, codeDistrict);

            query.executeUpdate();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
