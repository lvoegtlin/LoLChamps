/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apirequests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controller.Controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Champion;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author larsvoegtlin
 */
public class APIRequestGetCurrentChampion {

    /**
     * Returns the current champion name if user is ingame.
     *
     * If the user is not ingame the method will return "NOT INGAME" and if the
     * service isn't available it returns "FLASE".
     *
     * @param userName
     * @return currentChampionName
     */
    public String getCurrentChampion(String userName) {
        String result = "";
        String connection;

        connection = "http://www.lolnexus.com/ajax/get-game-info/EUW.json?name=" + userName;

        try {
            URL obj = new URL(connection);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            Controller.getInstance().setURLHeaders(con);

            int responseCode = con.getResponseCode();

            if (responseCode == 403) {
                return "FLASE";
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputline;
            StringBuffer response = new StringBuffer();

            while ((inputline = in.readLine()) != null) {
                response.append(inputline);
            }
            in.close();

            Gson gson = new Gson();
            if (response == null) {
                return "FLASE";
            }

            JsonObject jsonObject = gson.fromJson("" + response, JsonObject.class);
            boolean controll = jsonObject.get("successful").getAsBoolean();

            if (!controll) {
                String html = jsonObject.get("html").getAsString();

                if (html.contains("is not currently in a game")) {
                    result = "NOT INGAME";
                }
                if (html.contains("There are currently no connections available to perform this search. Please try again later.")) {
                    result = "FALSE";
                }
            } //dr-mundo etc./wukung/ in lolnexus file not DrMundo MonkeyKing!!!! 

            String html = jsonObject.get("html").getAsString();
            html = html.toLowerCase();

            Document doc = Jsoup.parse(html);
            Elements el = doc.getElementsByClass("searched");

            String championString = "";
            for (Element e : el) {
                Document doc2 = Jsoup.parse(e.html());
                Elements el2 = doc2.getElementsByClass("icon");
                for (Element e2 : el2){
                    championString = e2.attr("class");
                }
            }

            String[] champSplit = championString.split(" ");

            if (champSplit.length == 3) {//dr-mundo etc./wukung/ in lolnexus file not DrMundo MonkeyKing!!!!
                String currentChampName = champSplit[2];
                if (currentChampName.contains("-")) {
                    currentChampName = currentChampName.replace("-", "");
                }
                if (currentChampName.contains("wukong")) {
                    currentChampName = "MonkeyKing";
                }

                result = currentChampName.toLowerCase();
            }
        } catch (IOException ex) {
            Logger.getLogger(APIRequestGetChampions.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }
}
