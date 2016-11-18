package com.oprun.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.oprun.database.QueryBuilderTempHash;

public class old_ProcessStart {

	// http://www.commentcamarche.net/forum/affich-2658844-connection-a-mysql-via-java

	public static void main(String args[]) throws IOException, SQLException {

		// scan de chaque page : pour recup IDS

		QueryBuilderTempHash.deleteAll();

		for (int ind = 0; ind <= 100; ind++) {
			insertIdHash(ind);
		}

		System.out.println("fin main");
		return;

		// lecture des IDS du fichier
		/*
		 * 
		 * int indLevel = 0 ; Map<Integer, String> listLevels = new HashMap<>()
		 * ;
		 * 
		 * for (String hashId : listIdsAppened) {
		 * 
		 * // scan page compet String url =
		 * "http://bases.athle.com/asp.net/competitions.aspx?base=calendrier&id="
		 * + hashId; Document doc;
		 * 
		 * try { doc = Jsoup.connect(url).get(); } catch (IOException e) {
		 * System.out.println("Page compet : parsing KO. HashId = " + hashId);
		 * continue; }
		 * 
		 * // general info
		 * 
		 * String title = "";
		 * 
		 * try { title = doc.select("[style~=background:url]").get(0).ownText();
		 * } catch (IndexOutOfBoundsException e) {
		 * System.out.println("hash id KO : " + hashId); continue; }
		 * 
		 * String subtitle =
		 * doc.select("[style~=color:#000; font-size:15px]").text(); String date
		 * = doc.select("[style~=color:#A00014]").text(); String code =
		 * doc.select("td:contains(Code : )").get(0).getElementsByTag("b").text(
		 * ); String niveau =
		 * doc.select("td:contains(Niveau : )").get(0).getElementsByTag("b").
		 * text(); String type =
		 * doc.select("td:contains(Type : )").get(0).getElementsByTag("b").text(
		 * );
		 * 
		 * 
		 * 
		 * 
		 * if (!listLevels.containsValue(niveau)) { listLevels.put(indLevel,
		 * niveau); indLevel++; }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * List<String> listInfosSup = new ArrayList<>(); Elements els =
		 * doc.select("[style=text-align:right]"); for (Element el : els) { if
		 * (!el.text().contains(":")) { listInfosSup.add(el.text()); } }
		 * 
		 * boolean hasResultats = false;
		 * 
		 * Elements elEp = doc.select("td:contains(Epreuves et Résultats)");
		 * hasResultats = (elEp != null && elEp.size() > 0);
		 * 
		 * List<String> listOrg = new ArrayList<>();
		 * 
		 * // organisation String organisation = null; Elements elsOrg =
		 * doc.select("table.linedRed").select("tr"); int indOrg = 0;
		 * 
		 * String propOrg = ""; String propVal = ""; boolean contactTechniqueOK
		 * = false;
		 * 
		 * // TODO: rewrite with ind for (Element elOrg : elsOrg) {
		 * 
		 * // on démarre au premier el du tab if (indOrg <= 0) { indOrg++;
		 * continue; }
		 * 
		 * if (elOrg.select("td").size() >= 2) { if
		 * (!cleanText(elOrg.select("td").get(0).text()).equals("")) { propOrg =
		 * cleanText(elOrg.select("td").get(0).text()); } propVal =
		 * cleanText(elOrg.select("td").get(2).text());
		 * 
		 * if (propOrg.equals("Contact Technique")) { contactTechniqueOK = true;
		 * }
		 * 
		 * if (contactTechniqueOK && !propOrg.equals("Contact Technique")) {
		 * break; }
		 * 
		 * listOrg.add(propOrg + ";" + propVal); }
		 * 
		 * }
		 * 
		 * // épreuves
		 * 
		 * Elements elsEpreuves = doc.select("div#imgelement_1"); if
		 * (elsEpreuves.size() > 0) { elsEpreuves =
		 * elsEpreuves.parents().get(2).select("td"); }
		 * 
		 * for (int ind = 1; ind < elsEpreuves.size(); ind++) {
		 * 
		 * int nbTd = elsEpreuves.get(ind).select("td").size(); if (nbTd > 1) {
		 * 
		 * // System.out.println("lg : " + nbTd);
		 * 
		 * } else { String text =
		 * cleanText(elsEpreuves.get(ind).select("td").text()); if
		 * (text.equals("")) { text =
		 * cleanText(elsEpreuves.get(ind).select("td").select("img").attr(
		 * "title")); }
		 * 
		 * if (!text.equals("")) { System.out.println(text); } }
		 * 
		 * }
		 * 
		 * Elements elsContacts =
		 * doc.select("td:contains(Inscrite au calendrier par)").select("tr");
		 * 
		 * for (int ind = 0; ind < elsContacts.size(); ind++) {
		 * 
		 * System.out.println(elsContacts.get(ind).select("td").get(0).text());
		 * System.out.println(elsContacts.get(ind).select("td").get(2).ownText()
		 * );
		 * System.out.println(elsContacts.get(ind).select("td").get(2).select(
		 * "a").attr("href"));
		 * 
		 * }
		 * 
		 * 
		 * //DATE KO // EVENTS int notDeleted = 0 ; String lineEvent = code +
		 * ";" + hashId + ";" + title + ";" + new Date() + ";" + notDeleted +
		 * ";" + date + ";" + findByKey(listLevels, niveau) + "\n";
		 * writeIntoFile( "event.txt" , lineEvent );
		 * 
		 * }
		 * 
		 * // LEVELS
		 * 
		 * for (Entry<Integer, String> entry : listLevels.entrySet()) {
		 * writeIntoFile("level.txt", entry.getKey() + ";" + entry.getValue() +
		 * "\n"); }
		 * 
		 * 
		 */

	}

	// gets IDS de chaque page + print dans fichier
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

	private static void writeIntoFile(String file, String line) {

		try {

			Files.write(Paths.get("C:\\temp\\" + file), line.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println("line " + line + " : print file KO.");
		}

	}

}
