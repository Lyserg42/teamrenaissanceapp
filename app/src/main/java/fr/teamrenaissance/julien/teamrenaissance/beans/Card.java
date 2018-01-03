package fr.teamrenaissance.julien.teamrenaissance.beans;


public class Card {

    private int cId;
    private String cName;
    private int qty;

    public Card() {
    }

    public Card(int cId, String cName, int qty) {
        this.cId = cId;
        this.cName = cName;
        this.qty = qty;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
