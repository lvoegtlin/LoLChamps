/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apirequests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import controller.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Champion;

/**
 *
 * @author larsvoegtlin
 */
public class APIRequestGetChampionsStats {

    private ArrayList<Champion> championsList;
    private int summonerId;
    private String region;

    public APIRequestGetChampionsStats(ArrayList<Champion> championsList, int summonerId, String region) {
        this.championsList = championsList;
        this.summonerId = summonerId;
        this.region = region;
    }

    public void completeChampions() {
        String connection;

        connection = "https://" + this.region + ".api.pvp.net/api/lol/" + this.region + "/v1.3/stats/by-summoner/" + this.summonerId + "/ranked?season=SEASON2016&api_key=" + Controller.APIKEY + "";

        try {
            URL obj = new URL(connection);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            Controller.getInstance().setURLHeaders(con);

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputline;
            StringBuffer response = new StringBuffer();

            while ((inputline = in.readLine()) != null) {
                response.append(inputline);
            }
            in.close();

            if (responseCode == 500) {
                Logger.getLogger(APIRequestGetChampionsStats.class.getName()).log(Level.SEVERE, null, "Server error");
                return;
            }

            Gson gson = new Gson();
            if (response == null) {
                return;
            }

            JsonObject jsonObject = gson.fromJson("" + response, JsonObject.class);
            JsonArray championsArray = jsonObject.getAsJsonArray("champions");
            for (int i = 0; i < championsArray.size(); i++) {
                JsonObject champsAsObject = championsArray.get(i).getAsJsonObject();
                int champIdStats = champsAsObject.get("id").getAsInt();
                double sessionsPlayed = champsAsObject.get("stats").getAsJsonObject().get("totalSessionsPlayed").getAsDouble();


                for (Champion champion : championsList) {
                    if (champIdStats == champion.getId()) {
                        JsonObject jsonStats = champsAsObject.get("stats").getAsJsonObject();

                        DecimalFormat df = new DecimalFormat("0.00");

                        champion.setAssists(df.format((double) (jsonStats.get("totalAssists").getAsDouble() / sessionsPlayed)));
                        champion.setKills(df.format((double) jsonStats.get("totalChampionKills").getAsDouble() / sessionsPlayed));
                        champion.setDeaths(df.format((double) jsonStats.get("totalDeathsPerSession").getAsDouble() / sessionsPlayed));
                        champion.setWins((int) jsonStats.get("totalSessionsWon").getAsInt());
                        champion.setLoses((int) jsonStats.get("totalSessionsLost").getAsInt());
                        champion.setMinionsKilled(df.format((double) jsonStats.get("totalMinionKills").getAsDouble() / sessionsPlayed));
                        champion.setHasStats(true);
                    }

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(APIRequestGetChampionsStats.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
