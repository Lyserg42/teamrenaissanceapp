package fr.teamrenaissance.julien.teamrenaissance.beans;

import java.util.List;

public class Tournament {
    String date;
    String tName;
    int tId;
    List<LoanBorrow> borrowedCards;
    List<LoanBorrow> lentCards;
    List<Card> demands;

    public Tournament() {
    }

    public Tournament(String date, String tName, int tId, List<LoanBorrow> borrowedCards, List<LoanBorrow> lentCards, List<Card> demands) {
        this.date = date;
        this.tName = tName;
        this.tId = tId;
        this.borrowedCards = borrowedCards;
        this.lentCards = lentCards;
        this.demands = demands;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public List<LoanBorrow> getBorrowedCards() {
        return borrowedCards;
    }

    public void setBorrowedCards(List<LoanBorrow> borrowedCards) {
        this.borrowedCards = borrowedCards;
    }

    public List<LoanBorrow> getLentCards() {
        return lentCards;
    }

    public void setLentCards(List<LoanBorrow> lentCards) {
        this.lentCards = lentCards;
    }

    public List<Card> getDemands() {
        return demands;
    }

    public void setDemands(List<Card> demands) {
        this.demands = demands;
    }
}
