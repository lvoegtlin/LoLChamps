/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import apirequests.APIRequestGetChampions;
import apirequests.APIRequestGetChampionsStats;
import apirequests.APIRequestGetSummonerId;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import model.Champion;
import view.LoLChapStatsBEV;

/**
 *
 * @author larsvoegtlin
 */
public class Controller {

    public static final String APIKEY = "399b32aa-1de2-44e5-bf66-535b44400df9";
    private int summenorId = 0;
    private String summonerName = "";
    private String region;
    private ArrayList<Champion> championsArray = new ArrayList<Champion>();
    private boolean servieAnable = false;

    public Controller() {
    }

    static class SingletonHolder {

        static final Controller INSTANCE = new Controller();
    }

    public static Controller getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getSummonerIdFromServer() {
        APIRequestGetSummonerId api = new APIRequestGetSummonerId();
        try {
            this.summenorId = api.getSummonerId(region, summonerName);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAllChampions(String region) {
        APIRequestGetChampions api = new APIRequestGetChampions();
        this.championsArray = api.getChampions(region);
    }

    public void completeChampionsObjects() {
        APIRequestGetChampionsStats api = new APIRequestGetChampionsStats(championsArray, summenorId, region);
        api.completeChampions();
    }

    public int getSummenorId() {
        return summenorId;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isServieAnable() {
        return servieAnable;
    }

    public void setServieAnable(boolean servieAnable) {
        this.servieAnable = servieAnable;
    }
    
    public ArrayList<Champion> getAllChampions() {
        return championsArray;
    }

    public void setChampionsArray(ArrayList<Champion> championsArray) {
        this.championsArray = championsArray;
    }

    public Champion getChampionByName(String name) {
        Champion result = null;
        for (Champion champ : championsArray) {
            if (champ.getName().matches(name)) {
                result = champ;
            }
        }
        return result;
    }

    /**
     * creates output file .txt
     *
     * @param champ
     */
    public void writeInOutputFile(Champion champ) {
        String path = "";
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        File file = new File(path + "/champ_stats.txt");
        System.out.println(file.getAbsolutePath());

        if (file.exists()) {
            try {
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                writer.println(champ);
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(file, "UTF-8");
                writer.println(champ);
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                writer.close();
            }
        }
    }

    public void clearFile() {
        String path = "";
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        File file = new File(path + "/champ_stats.txt");
        System.out.println(file.getAbsolutePath());

        if (file.exists()) {
            try {
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                writer.println("");
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void saveUsername(String userName){
        Preferences root = Preferences.userNodeForPackage(LoLChapStatsBEV.class);
        root.put("username", userName);
    }
    
    public void saveRegion(String region){
        Preferences root = Preferences.userNodeForPackage(LoLChapStatsBEV.class);
        root.put("region", region);
    }
}
