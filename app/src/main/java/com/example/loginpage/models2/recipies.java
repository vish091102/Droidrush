package com.example.loginpage.models2;

public class recipies {
int pic;
String name,price,checkout;


    public recipies(int pic, String name, String price, String checkout) {
        this.pic = pic;
        this.name = name;
        this.price = price;
        this.checkout = checkout;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
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

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }
}
