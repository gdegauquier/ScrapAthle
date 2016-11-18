package com.oprun.helper;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public  class HelperText {

    public static String clean(String s) {
        s = s.replace("\u00a0", "").trim();
        return s;
    }
	
}
