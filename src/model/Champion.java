/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author larsvoegtlin
 */
public class Champion {

    private int id;
    private String name;
    private String ShowName;
    private String deaths;
    private String kills;
    private String assists;
    private int wins;
    private int loses;
    private String minionsKilled;
    private boolean hasStats;
    private boolean blank = false;

    public Champion(int id, String name) {
        this.id = id;
        this.name = name;
        this.assists = "0.0";
        this.kills = "0.0";
        this.deaths = "0.0";
        this.minionsKilled = "0.0";
        this.hasStats = false;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the deaths
     */
    public String getDeaths() {
        return deaths;
    }

    /**
     * @param deaths the deaths to set
     */
    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    /**
     * @return the kills
     */
    public String getKills() {
        return kills;
    }

    /**
     * @param kills the kills to set
     */
    public void setKills(String kills) {
        this.kills = kills;
    }

    /**
     * @return the assists
     */
    public String getAssists() {
        return assists;
    }

    /**
     * @param assists the assists to set
     */
    public void setAssists(String assists) {
        this.assists = assists;
    }

    /**
     * @return the wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * @param wins the wins to set
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * @return the loses
     */
    public int getLoses() {
        return loses;
    }

    /**
     * @param loses the loses to set
     */
    public void setLoses(int loses) {
        this.loses = loses;
    }

    /**
     * @return the minionsKilled
     */
    public String getMinionsKilled() {
        return minionsKilled;
    }

    /**
     * @param minionsKilled the minionsKilled to set
     */
    public void setMinionsKilled(String minionsKilled) {
        this.minionsKilled = minionsKilled;
    }

    public boolean isHasStats() {
        return hasStats;
    }

    public void setHasStats(boolean hasStats) {
        this.hasStats = hasStats;
    }

    public void setBlank(boolean blank) {
        this.blank = blank;
    }

    public String getShowName() {
        return ShowName;
    }

    public void setShowName(String ShowName) {
        this.ShowName = ShowName;
    }

    @Override
    public String toString() {
        if (blank) {
            return "";
        } else {
            return ShowName + ":  " + wins + " - " + loses + "  " + kills + "/" + deaths + "/" + assists + "  CS: " + minionsKilled;
        }
    }
    
    public String toLable() {
        if (blank) {
            return "";
        } else {
            return ShowName + ": <br><br> " + wins + " - " + loses + "  <br>" + kills + "/" + deaths + "/" + assists + "<br>  CS: " + minionsKilled;
        }
    }
}
