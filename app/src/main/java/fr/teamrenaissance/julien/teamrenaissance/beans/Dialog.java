package fr.teamrenaissance.julien.teamrenaissance.beans;


import java.io.Serializable;
import java.util.List;

public class Dialog implements Serializable {
    private int tId;
    private int uId;
    private String type;
    private String title;
    private List<Card> cards;

    public Dialog() {
    }

    public Dialog(int tId, int uId, String type, String title, List<Card> cards) {
        this.tId = tId;
        this.uId = uId;
        this.type = type;
        this.title = title;
        this.cards = cards;
    }

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
