/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apirequests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controller.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Champion;

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
            con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:26.0) Gecko/20100101 Firefox/26.0");
            con.setRequestProperty("Referer", "http://www.lolnexus.com/EUW/search?name=ecrop&region=EUW");
            con.setRequestProperty("Host", "www.lolnexus.com");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Accept-Language", "de-de,de;q=0.8,en-us;q=0.5,en;q=0.3");
            con.setRequestProperty("Accept", "*/*");
            con.setRequestMethod("GET");
            con.setDoInput(true);

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
            html = html.replaceAll("           <a class=\"curse-voice-indicator\" href=\"http://beta.cursevoice.com/.utm_source=lolnexus&utm_medium=playericon&utm_campaign=openbeta\">\\r\\n", "");
            html = html.replaceAll("               <div class=\"cv-tooltip\">\\r\\n", "");
            html = html.replaceAll("                   <img src=\"http://static-noxia.cursecdn.com/1-0-5288-24525/skins/noxia/images/cv-horiz-tooltip.png\" />\\r\\n", "");
            html = html.replaceAll("                   <span>Try it now!</span>\\r\\n", "");
            html = html.replaceAll("                   <p>This player uses Curse Voice to communicate with their team.</p>\\r\\n", "");
            html = html.replaceAll("               </div>\\r\\n", "");
            html = html.toLowerCase();
            System.out.println(html);
            Pattern p = Pattern.compile("(?<=(" + URLDecoder.decode(userName, "UTF-8") + "</span>\\r\\n        </a>\\r\\n        \\r\\n           </a>\\r\\n        \\r\\n     </td>\\r\\n    <td class=\"champion\">\\r\\n        <i class=\"icon champions-lol-28)).*?(?=(\"></i>\\r\\n))");
            Matcher m = p.matcher(html);

            if (m.find()) {//dr-mundo etc./wukung/ in lolnexus file not DrMundo MonkeyKing!!!! 
                String currentChampName = m.group(0).trim();
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
