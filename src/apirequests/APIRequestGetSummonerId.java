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
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author larsvoegtlin
 */
public class APIRequestGetSummonerId {

    /**
     * Returns the ID of the summoner by summoner name and region.
     *
     * @param region
     * @param summonerName
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public int getSummonerId(String region, String summonerName) throws MalformedURLException, IOException {
        int result = -1;
        String connection;
        String summonerNameWithOutSpez = summonerName.replaceAll("%20", "");

        connection = "https://"+ region +".api.pvp.net/api/lol/" + region + "/v1.4/summoner/by-name/" + summonerName + "?api_key=" + Controller.APIKEY + "";

        URL obj = new URL(connection);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();

        switch (responseCode) {
            case 200: {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline;
                StringBuffer response = new StringBuffer();

                while ((inputline = in.readLine()) != null) {
                    response.append(inputline);
                }
                in.close();
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson("" + response, JsonObject.class);
                result = jsonObject.getAsJsonObject(summonerNameWithOutSpez).getAsJsonPrimitive("id").getAsInt();
                break;
            }
            case 404: {
                result = 0;
                break;
            }
            case 500: {
                result = 1;
                break;
            }
            case 503: {
                result = 2;
                break;
            }
        }

        return result;
    }
}
