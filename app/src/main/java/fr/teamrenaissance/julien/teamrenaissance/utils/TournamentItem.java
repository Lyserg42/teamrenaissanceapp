package fr.teamrenaissance.julien.teamrenaissance.utils;

import java.util.ArrayList;
import java.util.List;


public class TournamentItem {
    private int id ;
    private String value = "";

    public TournamentItem(){

    }

    public TournamentItem(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public int id(){
        return id;
    }

   public List<TournamentItem> emprunter_tournamentItems(){

        List<TournamentItem> tournaments = new ArrayList<>();
        tournaments.add(new TournamentItem(11, "GP Madrid"));
        tournaments.add(new TournamentItem(12, "GP London"));
        tournaments.add(new TournamentItem(13, "PT Bilbao"));

        return tournaments;
    }

    public List<TournamentItem> mesPrete_tournamentItems(){

        List<TournamentItem> tournaments = new ArrayList<>();
        tournaments.add(new TournamentItem(-100, "Tous les tournois"));
        tournaments.add(new TournamentItem(11, "GP Madrid"));
        tournaments.add(new TournamentItem(12, "GP London"));
        tournaments.add(new TournamentItem(13, "PT Bilbao"));

        return tournaments;
    }
}
