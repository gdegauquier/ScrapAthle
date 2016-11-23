package com.oprun.database;

import com.mysql.jdbc.PreparedStatement;
import com.oprun.helper.HelperDatabase;

public class QueryBuilderStatus {

	
    public static void insert(int stationId, int nbBikes, int nbPlaces, boolean isWithPT, String lastUpdate) {

        try {
                PreparedStatement query = (PreparedStatement) HelperDatabase.getConnection().prepareStatement(
                                        "INSERT INTO STATUS ( count_bikes, count_attachs, payment_terminal, last_update, id_station )"
                                        + "values ( ?, ?, ?, ?, ? ) ");

                query.setInt(1, nbBikes);
                query.setInt(2, nbPlaces);
                query.setInt(3, isWithPT ? 1 : 0);
                query.setString(4, lastUpdate);
                query.setInt(5, stationId);
                
                query.executeUpdate();
                
        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }

}
	
	
	
}
