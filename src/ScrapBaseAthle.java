
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapBaseAthle {

    // http://www.commentcamarche.net/forum/affich-2658844-connection-a-mysql-via-java

    // static int indexEl = 0 ;
    // static int ind = 0 ;
    //
    //
    // public static void main(String[] args) throws IOException{
    //
    // Logger LOGGER = Logger.getLogger("ScrapBaseAthle");
    //
    // Document doc = Jsoup.connect("http://bases.athle.com/asp.net/accueil.aspx?frmbase=calendrier").get();
    // Elements newsHeadlines = doc.select("td.datas0");
    // Element tab = doc.getElementById("ctnCalendrier");
    //
    // Elements listTD = tab.getElementsByTag("td");
    //
    // List<Integer> elementsToDisplay = new ArrayList<>();
    // elementsToDisplay.add(1);
    // elementsToDisplay.add(3);
    // elementsToDisplay.add(5);
    // elementsToDisplay.add(7);
    // elementsToDisplay.add(9);
    // elementsToDisplay.add(11);
    // elementsToDisplay.add(13);
    // elementsToDisplay.add(15);
    // elementsToDisplay.add(17);
    // elementsToDisplay.add(19);
    //
    // String id = "";
    // boolean start = false;
    // for ( Element line : listTD){
    //
    // String res = line.html();
    //
    //
    //
    // if ( line.toString().contains("<a title=") ){
    // res = line.getAllElements().get(1).html();
    // }else if ( line.toString().contains("<a href")){
    // res = line.getAllElements().get(1).html();
    // }else if ( line.toString().contains("<em")){
    // try{
    // res = line.getAllElements().get(2).html();
    // }catch( IndexOutOfBoundsException e ){
    // res = line.getAllElements().get(1).html();
    // }
    // }
    //
    // if ( res.contains("paperCom") ){
    // res = "O";
    // }
    // if ( res.contains("paperOff") ){
    // res = "W";
    // }
    // if ( res.contains("paperInc") ){
    // res = "I";
    // }
    // if ( res.contains("paperInc") ){
    // res = "N";
    // }
    // if ( res.contains("paperAnn") ){
    // res = "A";
    // }
    // if ( res.contains("&nbsp;") ){
    // res = "";
    // }
    // if ( res.contains("Label R�gional") ){
    // res = "R";
    // }
    // if ( res.contains("Label International") ){
    // res = "I";
    // }
    // if ( res.contains("Equipe de France") ){
    // res = "F";
    // }
    // if ( res.contains("Label National") ){
    // res = "F";
    // }
    //
    //
    //
    // if (start) {
    // indexEl++;
    //
    // if ( indexEl == 1){
    // id = "";
    // ind++;
    // System.out.println("COURSE "+ind);
    // //System.out.println("ID = "+getIdFromLine(line.toString()));
    // }
    //
    // if ( elementsToDisplay.contains( Integer.valueOf(indexEl) )){
    // System.out.println( indexEl+":"+res );
    // if ( indexEl == 7){
    // id = getIdFromLine(line.toString());
    // }
    // if ( indexEl == 19){
    // System.out.println( "ID JS:"+id );
    // getDetailFromId(id);
    //
    //
    // }
    // }
    // if ( indexEl>=19){
    // indexEl = 0;
    // }
    // }
    //
    // if (res.contains("Lab.")) {
    // start=true;
    // indexEl = 0;
    //
    // }
    //
    //// //label
    //// String res = line.html();
    ////
    //// //label
    //// if ( line.html().contains("bddThrowCompet") && indexEl > 0 ){
    //// //label = true;
    //// res = line.getElementsByTag("a").html();
    //// printEl(res); continue;
    //// }
    ////
    //// if ( line.html().contains("frmcompetition=") ){
    //// res = line.getElementsByTag("img").outerHtml();
    //// ind++;
    //// indexEl = -1 ;
    //// label = false;
    //// System.out.println("\nCOURSE "+ind);
    //// printEl(res); continue;
    //// }
    ////
    //// //date
    //// if ( line.html().contains("&amp;frmdate_a2=") ){
    //// String content = line.getElementsByTag("a").html().trim();
    //// if ( !content.equals("") && content.contains("/")){
    ////
    ////
    //// res = line.getElementsByTag("a").html()+"/2016";
    ////
    //// printEl(res); continue;
    //// }else if ( !content.equals("") ){
    //// res = line.getElementsByTag("a").html();
    //// printEl(res); continue;
    //// }
    //// }
    ////
    //// //Famille
    //// if ( line.html().contains("Frmdate_m2=") ){
    //// res = line.getElementsByTag("a").html()+"";
    //// printEl(res); continue;
    //// }
    ////
    ////
    //// // frmdepartement
    //// if ( line.html().contains("frmdepartement") ){
    //// res = line.getElementsByTag("a").html();
    //// printEl(res); continue;
    //// }
    ////
    //// //frmligue
    //// if ( line.html().contains("frmligue") ){
    //// res =line.getElementsByTag("a").html();
    //// printEl(res); continue;
    //// }
    ////
    //// printEl(res); continue;
    ////
    // }
    //
    // }
    //
    // private static void printEl(String res){
    //
    // Map<Integer, String> labels = new HashMap<Integer,String>();
    // labels.put(0, "status");
    // labels.put(1, "date");
    // labels.put(2, "famille");
    // labels.put(3, "libelle");
    // labels.put(4, "lieu");
    // labels.put(5, "ligue");
    // labels.put(6, "departement");
    // labels.put(7, "type");
    // labels.put(8, "niveau");
    // labels.put(9, "???");
    // labels.put(10, "label");
    //
    //
    //
    //
    //
    // res = res.trim();
    // if ( !res.equals("") && ind > 0){
    //
    // if ( indexEl == 10 ){
    // indexEl = indexEl;
    // }
    //
    // indexEl ++;
    // System.out.println(labels.get(indexEl) + " : " + res);
    //
    // }
    // }
    //
    // private static String getIdFromLine( String line ){
    //
    // String id = "";
    // try{
    // int posFrmcompetition = line.indexOf("bddThrowCompet('")+"bddThrowCompet('".length();
    // int posEnd = line.indexOf("'", posFrmcompetition );
    // id = line.substring(posFrmcompetition, posEnd);
    // }catch( Exception e ){
    // //
    // }
    //
    // return id.trim() ;
    //
    // }
    //
    // private static void getDetailFromId( String id ) throws IOException{
    //
    // if ( null == id || id.equals("")){
    // return;
    // }
    //
    // Document doc = Jsoup.connect("http://bases.athle.com/asp.net/competitions.aspx?base=calendrier&id="+id).get();
    //
    // Elements el = doc.getElementsByClass("titles");
    // String title = el.get(0).childNode(0).toString().trim();
    // String town = el.get(0).childNode(3).childNodes().get(0).toString().trim();
    // String date = doc.getElementsByTag("span").get(1).html().trim();
    // String code = doc.getElementsByTag("b").get(2).html().trim();
    // String niveau = doc.getElementsByTag("b").get(1).html().trim();
    // String type = doc.getElementsByTag("b").get(3).html().trim();
    // //String org = doc.getElementsByTag("td")
    // }
    //

    public static void main(String args[]) throws IOException {

        // scan de chaque page : pour recup IDS
        // scan de 2 pages d' IDS

        List<String> listIdsAppened = new ArrayList<>();
        for (int ind = 0; ind <= 1/* 100 */; ind++) {
            List<String> listIds = getIdHashAndWriteThemIntoFile(ind);
            listIdsAppened.addAll(listIds);
        }

        // lecture des IDS du fichier
        int indLevel = 0 ;
        Map<Integer, String> listLevels = new HashMap<>() ;
        
        for (String hashId : listIdsAppened) {

            // scan page compet
            String url = "http://bases.athle.com/asp.net/competitions.aspx?base=calendrier&id=" + hashId;
            Document doc;

            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                System.out.println("Page compet : parsing KO. HashId = " + hashId);
                continue;
            }

            // general info

            String title = "";

            try {
                title = doc.select("[style~=background:url]").get(0).ownText();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("hash id KO : " + hashId);
                continue;
            }

            String subtitle = doc.select("[style~=color:#000; font-size:15px]").text();
            String date = doc.select("[style~=color:#A00014]").text();
            String code = doc.select("td:contains(Code : )").get(0).getElementsByTag("b").text();
            String niveau = doc.select("td:contains(Niveau : )").get(0).getElementsByTag("b").text();
            String type = doc.select("td:contains(Type : )").get(0).getElementsByTag("b").text();
            


            
            if (!listLevels.containsValue(niveau)) {
                listLevels.put(indLevel, niveau);
                indLevel++;
            }
            

            
            

            List<String> listInfosSup = new ArrayList<>();
            Elements els = doc.select("[style=text-align:right]");
            for (Element el : els) {
                if (!el.text().contains(":")) {
                    listInfosSup.add(el.text());
                }
            }

            boolean hasResultats = false;

            Elements elEp = doc.select("td:contains(Epreuves et Résultats)");
            hasResultats = (elEp != null && elEp.size() > 0);

            List<String> listOrg = new ArrayList<>();

            // organisation
            String organisation = null;
            Elements elsOrg = doc.select("table.linedRed").select("tr");
            int indOrg = 0;

            String propOrg = "";
            String propVal = "";
            boolean contactTechniqueOK = false;

            // TODO: rewrite with ind
            for (Element elOrg : elsOrg) {

                // on démarre au premier el du tab
                if (indOrg <= 0) {
                    indOrg++;
                    continue;
                }

                if (elOrg.select("td").size() >= 2) {
                    if (!cleanText(elOrg.select("td").get(0).text()).equals("")) {
                        propOrg = cleanText(elOrg.select("td").get(0).text());
                    }
                    propVal = cleanText(elOrg.select("td").get(2).text());

                    if (propOrg.equals("Contact Technique")) {
                        contactTechniqueOK = true;
                    }

                    if (contactTechniqueOK && !propOrg.equals("Contact Technique")) {
                        break;
                    }

                    listOrg.add(propOrg + ";" + propVal);
                }

            }

            // épreuves

            Elements elsEpreuves = doc.select("div#imgelement_1");
            if (elsEpreuves.size() > 0) {
                elsEpreuves = elsEpreuves.parents().get(2).select("td");
            }

            for (int ind = 1; ind < elsEpreuves.size(); ind++) {

                int nbTd = elsEpreuves.get(ind).select("td").size();
                if (nbTd > 1) {

                    // System.out.println("lg : " + nbTd);

                } else {
                    String text = cleanText(elsEpreuves.get(ind).select("td").text());
                    if (text.equals("")) {
                        text = cleanText(elsEpreuves.get(ind).select("td").select("img").attr("title"));
                    }

                    if (!text.equals("")) {
                        System.out.println(text);
                    }
                }

            }

            Elements elsContacts = doc.select("td:contains(Inscrite au calendrier par)").select("tr");

            for (int ind = 0; ind < elsContacts.size(); ind++) {

                System.out.println(elsContacts.get(ind).select("td").get(0).text());
                System.out.println(elsContacts.get(ind).select("td").get(2).ownText());
                System.out.println(elsContacts.get(ind).select("td").get(2).select("a").attr("href"));

            }

            
            //DATE KO
            // EVENTS
            int notDeleted = 0 ;
            String lineEvent = code + ";" + hashId + ";" + title + ";" + new Date() + ";" + notDeleted + ";" + date + ";" + findByKey(listLevels, niveau) + "\n";
            writeIntoFile( "event.txt" , lineEvent );

        }

        // LEVELS

        for (Entry<Integer, String> entry : listLevels.entrySet()) {
            writeIntoFile("level.txt", entry.getKey() + ";" + entry.getValue() + "\n");
        }


        System.out.println("fin");

    }

    private static Integer findByKey(Map<Integer, String> testMap, String value) {
        for (Entry<Integer, String> entry : testMap.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
    public static String cleanText(String s) {
        s = s.replace("\u00a0", "").trim();
        return s;
    }

    // gets IDS de chaque page + print dans fichier
    private static List<String> getIdHashAndWriteThemIntoFile(int ind) throws IOException {

        List<String> listIds = new ArrayList<>();
        System.out.println("page" + ind);
        String url = "http://bases.athle.com/asp.net/liste.aspx?frmpostback=true&frmbase=calendrier&frmmode=1&frmespace=0&frmsaison=2016&frmposition=" + ind;

        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Page " + ind + " : parsing KO.");
            return listIds;
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

                listIds.add(linkId);
                writeIntoFile("id_hash.txt", linkId + "\n");
            }
        }
        return listIds;
    }

    private static void writeIntoFile(String file, String line) {

        try {

            Files.write(Paths.get("C:\\temp\\" + file), line.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("line " + line + " : print file KO.");
        }

    }

}
