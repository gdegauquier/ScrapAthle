package com.oprun.main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.oprun.database.QueryBuilderCountry;
import com.oprun.database.QueryBuilderDepartment;
import com.oprun.database.QueryBuilderEvent;
import com.oprun.database.QueryBuilderTempHash;
import com.oprun.database.QueryBuilderTown;

public class ProcessEvents {

	public static void main(String args[]) throws IOException, SQLException {

		// pour chaque hash id trouvé, scan page détail
		QueryBuilderEvent.deleteAll();
		ResultSet rs = QueryBuilderTempHash.get();
		int ind = 0;

		while (rs.next()) {
			String id = rs.getString("ID");
			System.out.println(ind + "\t" + id);
			getDetail(id);
			ind++;
		}

		System.out.println("fin main");
		return;

	}

	private static void getDetail(String hashId) {

		boolean exist = QueryBuilderEvent.countByIdHash(hashId) > 0;
		String url = "http://bases.athle.com/asp.net/competitions.aspx?base=calendrier&id=" + hashId;

		Document doc;

		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			System.out.println("Page compet : parsing KO. HashId = " + hashId);
			return;
		}

		String title = "";

		try {
			title = doc.select("[style~=background:url]").get(0).ownText();
		} catch (IndexOutOfBoundsException e) {
			System.out.println("hash id KO, no title found : " + hashId);
			return;
		}

		//subtitle
		String subtitle = doc.select("[style~=color:#000; font-size:15px]").text(); 
		
		Map<String, String> event = new HashMap<>();
		event.put("id_hash", hashId);
		event.put("title", title);
		event.put("deleted", "0");
		event.put("date_event", "2018-12-05");
		event.put("id_level", "0");
		event.put("sub_title", subtitle);
		
		int idTown = parseEventSubtitle( subtitle );
		event.put("id_town", idTown+""); 
		
		
		
		
		
		
		
		
		
		//pays
		//ville
		//code postal
		//code - code postal

		if (exist) {
			QueryBuilderEvent.update(event);
		} else {
			QueryBuilderEvent.insert(event);
		}

	}
	
	
	private static int parseEventSubtitle(String subTitle){
		
		
		//VALBERG (C-A / 006)
		//Hong Kong (REPUBLIQUE POPULAIRE DE CHINE)
		
		subTitle = subTitle.trim();
		
		if ( subTitle.equals("")){
			return 0;
		}
		
		boolean parLeft  = subTitle.indexOf("(") > 0;
		boolean parRight = subTitle.indexOf(")") > 0;
		boolean slash    = subTitle.indexOf("/") > 0;
		
		
		String country = subTitle.substring( subTitle.indexOf("(")+1, subTitle.indexOf(")") ).trim();
		String town = subTitle.substring(0, subTitle.indexOf("(")).trim() ;
		String department = "";
		String codeDepartment = "";
		
		if ( slash ) {
			
			department = country.split(" / ")[0];
			codeDepartment = country.split(" / ")[1];
			country = "France";
			
		}
		
		//pays existe ?
		int countCountry = QueryBuilderCountry.countByLabel( country );
		if ( countCountry == 0 ){
			QueryBuilderCountry.insert( country );
		}
		
		//get ID PAYS
		ResultSet rs = QueryBuilderCountry.getByLabel( country );
		int idCountry;
		try {
			idCountry = rs.getInt("id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			idCountry = 0;
		}
		
		//code postal existe ?
		if ( !codeDepartment.equals("") && QueryBuilderDepartment.countByCode(codeDepartment) == 0 ){
			QueryBuilderDepartment.insert( codeDepartment, department , null, 1 );
		}
		
		if (  !town.equals("") && QueryBuilderTown.countByLabel( town ) == 0  ){
			QueryBuilderTown.insert( town , codeDepartment.equals("") ? null : codeDepartment , idCountry);
		}
		
		//retour de la ville 
		ResultSet rsTown = QueryBuilderTown.getByLabel( town );
		try {
			return rsTown.getInt("id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	

}
