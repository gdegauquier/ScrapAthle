package com.oprun.main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.oprun.database.QueryBuilderEvent;
import com.oprun.database.QueryBuilderTempHash;

public class ProcessEvents {

	public static void main(String args[]) throws IOException, SQLException {

		// pour chaque hash id trouvé, scan page détail
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

}
