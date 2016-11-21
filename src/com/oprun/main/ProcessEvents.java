package com.oprun.main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.oprun.database.QueryBuilderCountry;
import com.oprun.database.QueryBuilderDepartment;
import com.oprun.database.QueryBuilderDistrict;
import com.oprun.database.QueryBuilderEvent;
import com.oprun.database.QueryBuilderLevel;
import com.oprun.database.QueryBuilderTempHash;
import com.oprun.database.QueryBuilderTown;
import com.oprun.database.QueryBuilderType;

public class ProcessEvents {

	public static void main(String args[]) throws IOException, SQLException {

		// pour chaque hash id trouv�, scan page d�tail
		QueryBuilderEvent.deleteAll();
		System.out.println( "delete all events" );
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

	private static void getDetail(String hashId) throws SQLException {

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
		//date
	        String date     = doc.select("[style~=color:#A00014]").text(); 
	        //level
	        String level    = doc.select("td:contains(Niveau : )").get(0).getElementsByTag("b").text(); 
	        Integer idLevel = 0 ;
	        
	        //types
                String type = doc.select("td:contains(Type : )").get(0).getElementsByTag("b").text();

	        if ( QueryBuilderLevel.countByLabel( level ) == 0 ){
	            QueryBuilderLevel.insert( level );
	        }
	        ResultSet rsLevel = QueryBuilderLevel.getByLabel(level);
                idLevel = rsLevel.getInt("ID");
	        
		Map<String, String> event = new HashMap<>();
		event.put("id_hash", hashId);
		event.put("title", title);
		event.put("deleted", "0");
		event.put("date_event", getDateEvent( date ));
		event.put("id_level", idLevel.toString() );
		event.put("sub_title", subtitle);
		
		//ville / dep / region / pays
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
		
		//types + assoc types
		parseTypes( type );
                
                List<String> listInfosSup = new ArrayList<>(); 
                Elements els = doc.select("[style=text-align:right]"); 
                for (Element el : els) { 
                    if (!el.text().contains(":")) { 
                        parseTypes( el.text() );
                    }
                }
		//saveAssocTypes() ;

	}
	
	
	private static void parseTypes(String type){
	    
	    if ( type == null ){
	        return ;
	    }
	    String types[] = type.trim().split(" - ");
	    
	    for ( String typeFromTab : types ){
	        typeFromTab = typeFromTab.trim();
	        if ( !typeFromTab.equals("") && QueryBuilderType.countByLabel( typeFromTab ) == 0 ){
	            QueryBuilderType.insert( typeFromTab );
	        }
	    }
	}
	
	
	private static String getDateEvent( String date ){
	    
	    date = date.trim();
	    
	    if ( date.indexOf( "/") == 0 ){
	        return null;
	    }
	    
	    date = date.substring( 6, 10 ) + "-" + date.substring( 3, 5 ) + "-" +date.substring( 0, 2 ); 
	    return date;

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
        String district = "";
		String codeDepartment = "";
		
		if ( slash ) {
			
            district = country.split(" / ")[0];
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
		
            // region existe ?
            if (!district.equals("") && QueryBuilderDistrict.countByCode(district) == 0) {
                QueryBuilderDistrict.insert(district, null, idCountry);
            }

		//code postal existe ?
		if ( !codeDepartment.equals("") && QueryBuilderDepartment.countByCode(codeDepartment) == 0 ){
            QueryBuilderDepartment.insert(codeDepartment, null, district);
		}
		
		//ville existe ?
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
