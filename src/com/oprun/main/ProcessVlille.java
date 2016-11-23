package com.oprun.main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import com.oprun.database.QueryBuilderStation;
import com.oprun.database.QueryBuilderStatus;
import com.oprun.helper.HelperDatabase;

public class ProcessVlille {

	public static void main(String args[]) throws IOException, SQLException {

		// scan IDS
		ResultSet rs = QueryBuilderStation.get();
	        if ( rs == null ){
	            System.out.println("KO -- fin");
	        }
		
		while (rs.next()) {
                    int id = rs.getInt("ID");
                    System.out.println("ID\t" + id);
                    getStatsFromStation( id );
                    
                }

		HelperDatabase.closeConnection();
		
		System.out.println("OK -- fin");
		

	}
	

	private static void getStatsFromStation (   int idStation   ){

        String url = "http://vlille.fr/stations/xml-station.aspx?borne="+idStation;
                        

            Document doc;
            try {
                    doc = Jsoup.connect(url).get();
                    doc = Jsoup.parse(doc.html(), "", Parser.xmlParser());
                    
                    QueryBuilderStatus.insert(idStation, 
                            Integer.valueOf(doc.select("bikes").get(0).text()), 
                            Integer.valueOf(doc.select("attachs").get(0).text()), 
                            doc.select("paiement").get(0).text().equals("AVEC_TPE"), 
                            doc.select("lastupd").get(0).text());
                    
            } catch (IOException e) {
                    System.out.println("Station " + idStation + " : parsing KO." + e);
                    return;
            }
	
	}

}
