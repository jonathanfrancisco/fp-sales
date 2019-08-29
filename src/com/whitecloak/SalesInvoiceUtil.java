package com.whitecloak;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalesInvoiceUtil {

    private static String[] csvToArray(String commaSeparatedValues) {
        return commaSeparatedValues.split(",");
    }

    public static SalesInvoice mapToSalesInvoice(String commaSeparatedValues) {
        String[] values = csvToArray(commaSeparatedValues);
        String itemType = values[0];
        String stringDate = values[1];
        int unitsSold = Integer.parseInt(values[2]);
        BigDecimal unitPrice = new BigDecimal(values[3]);

        SalesInvoice salesInvoice = new SalesInvoice();
        salesInvoice.setItemType(itemType);
        salesInvoice.setOrderDate(LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("M/d/yyyy")));
        salesInvoice.setUnitsSold(unitsSold);
        salesInvoice.setUnitPrice(unitPrice);
        return salesInvoice;
    }

    public static BigDecimal computeSalesInvoiceTotal (SalesInvoice salesInvoice) {
        int unitsSold = salesInvoice.getUnitsSold();
        BigDecimal unitPrice = salesInvoice.getUnitPrice();
        return unitPrice.multiply(BigDecimal.valueOf(unitsSold));
    }

}
