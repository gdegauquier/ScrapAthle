package com.oprun.main;

import java.io.IOException;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.oprun.database.QueryBuilderTempHash;

public class ProcessTempHash {

	public static void main(String args[]) throws IOException, SQLException {

		// scan de chaque page : pour recup IDS
		QueryBuilderTempHash.deleteAll();

		for (int ind = 0; ind <= 100; ind++) {
			insertIdHash(ind);
		}

		System.out.println("fin main");
		return;

	}

	// gets IDS de chaque page
	private static void insertIdHash(int ind) throws IOException {

		System.out.println("page" + ind);
		String url = "http://bases.athle.com/asp.net/liste.aspx?frmpostback=true&frmbase=calendrier&frmmode=1&frmespace=0&frmsaison=2016&frmposition="
				+ ind;

		Document doc;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			System.out.println("Page " + ind + " : parsing KO." + e);
			return;
		}

		Elements links = doc.select("a[href]"); // a with href

		for (Element link : links) {
			String linkId = link.attributes().get("href");

			if (linkId.contains("bddThrowCompet")) {
				String keyToFind = "javascript:bddThrowCompet('";
				int posBegin = linkId.indexOf(keyToFind);
				int length = keyToFind.length();
				int posEnd = linkId.indexOf("', 0)");

				linkId = linkId.substring(posBegin + length, posEnd);

				QueryBuilderTempHash.insert(linkId);

			}
		}

	}

}
