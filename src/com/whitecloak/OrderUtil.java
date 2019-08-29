package com.whitecloak;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrderUtil {

    private static String[] csvToArray(String commaSeparatedValues) {
        return commaSeparatedValues.split(",");
    }

    public static Order mapToOrderObject(String commaSeparatedValues) {
        String[] values = csvToArray(commaSeparatedValues);
        String itemType = values[0];
        String stringDate = values[1];
        int unitsSold = Integer.parseInt(values[2]);
        BigDecimal unitPrice = new BigDecimal(values[3]);

        Order order = new Order();
        order.setItemType(itemType);
        order.setOrderDate(LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("M/d/yyyy")));
        order.setUnitsSold(unitsSold);
        order.setUnitPrice(unitPrice);
        return order;
    }

    public static BigDecimal computeOrderTotal (Order order) {
        int unitsSold = order.getUnitsSold();
        BigDecimal unitPrice = order.getUnitPrice();
        return unitPrice.multiply(BigDecimal.valueOf(unitsSold));
    }

}
