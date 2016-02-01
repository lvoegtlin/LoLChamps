/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apirequests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Champion;

/**
 *
 * @author larsvoegtlin
 */
public class APIRequestGetChampions {

    /**
     * Returns an Arraylist of all Champions
     *
     * @param region region from which you want the champion list.
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    public ArrayList<Champion> getChampions(String region) {
        ArrayList<Champion> result = new ArrayList<Champion>();
        String connection;
        String championName;
        int championId;

        connection = "https://global.api.pvp.net/api/lol/static-data/" + region + "/v1.2/champion?api_key=" + Controller.APIKEY + "";
        try {
            URL obj = new URL(connection);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputline;
            StringBuffer response = new StringBuffer();

            while ((inputline = in.readLine()) != null) {
                response.append(inputline);
            }
            in.close();

            if (responseCode == 503) {
                result.add(new Champion(0, "false"));
            } else {
                Gson gson = new Gson();
                if (response != null) {
                    JsonObject jsonObject = gson.fromJson("" + response, JsonObject.class);
                    JsonObject championsObject = jsonObject.getAsJsonObject("data");
                    
                    for (Map.Entry<String, JsonElement> entry : championsObject.entrySet()) {
                        JsonObject championObject = entry.getValue().getAsJsonObject();
                        championId = championObject.get("id").getAsInt();
                        championName = championObject.get("name").getAsString().toLowerCase();
                        Champion champ = new Champion(championId, championName);
                        champ.setShowName(championObject.get("name").getAsString());
                        result.add(champ);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(APIRequestGetChampions.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

}
