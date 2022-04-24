package com.example.amigosforexception;

public class CartItems {
    String itemName, itemCode, skuCode;
    int quantity, price, itemMrp;

    CartItems(String itemCode, String itemName, String skuCode, int quantity, int price, int itemMrp){
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.price = price;
        this.skuCode = skuCode;
    }

    public String getItemCode() { return itemCode; }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public int getItemMrp() {
        return itemMrp;
    }
}
