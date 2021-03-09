package com.example.onlinemedicine.Models;

public class Model {
    String name,discprice,price,medicineid;





    public Model() {

    }

    public Model(String name, String discprice, String price, String medicineid) {
        this.name = name;
        this.discprice = discprice;
        this.price = price;
        this.medicineid = medicineid;
    }

    public String getMedicineid() {
        return medicineid;
    }

    public void setMedicineid(String medicineid) {
        this.medicineid = medicineid;
    }

    public String getDiscprice() {
        return discprice;
    }

    public void setDiscprice(String discprice) {
        this.discprice = discprice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
