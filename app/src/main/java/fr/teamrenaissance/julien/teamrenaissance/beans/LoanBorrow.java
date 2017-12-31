package fr.teamrenaissance.julien.teamrenaissance.beans;

import java.util.List;

public class LoanBorrow {
    int uId;
    String uName;
    List<Card> cards;

    public LoanBorrow() {
    }

    public LoanBorrow(int uId, String uName, List<Card> cards) {
        this.uId = uId;
        this.uName = uName;
        this.cards = cards;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
