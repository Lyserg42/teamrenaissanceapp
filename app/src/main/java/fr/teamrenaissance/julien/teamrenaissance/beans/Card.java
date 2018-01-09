package fr.teamrenaissance.julien.teamrenaissance.beans;


public class Card {

    private int cId;
    private String cName;
    private int qty;
    private String img;

    public Card() {
    }

    public Card(int cId, String cName, int qty, String img) {
        this.cId = cId;
        this.cName = cName;
        this.qty = qty;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
