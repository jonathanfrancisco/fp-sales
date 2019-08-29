package com.whitecloak;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {

    private String itemType;
    private LocalDate orderDate;
    private int unitsSold;
    private BigDecimal unitPrice;


    public Order() { }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getOrderDateMonth() {
        return orderDate.getMonth().name();
    }

    public int getOrderDateYear() {
        return orderDate.getYear();
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(int unitsSold) {
        this.unitsSold = unitsSold;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

}

