package com.whitecloak;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {

        Path allBranchCsv = Path.of("branches");
        Path cebuCsv = Path.of("branches", "cebu.csv");
        Path manilaCsv = Path.of("branches", "manila.csv");
        Path davaoCsv = Path.of("branches", "davao.csv");

        Set<String> items = getAllItemsSoldInAlphabeticalOrder(allBranchCsv);
        items.forEach(System.out::println);

        BigDecimal cebuTotalSales = getTotalSalesByBranch(cebuCsv);
        System.out.println("CEBU total sales: " + cebuTotalSales);

        BigDecimal manilaTotalSales = getTotalSalesByBranch(manilaCsv);
        System.out.println("MANILA total sales: " + manilaTotalSales);

        BigDecimal davaoTotalSales = getTotalSalesByBranch(davaoCsv);
        System.out.println("DAVAO total sales: " + davaoTotalSales);

        BigDecimal allBranchTotalSales = getAllTotalSales(allBranchCsv);
        System.out.println("All branches total sale: " + allBranchTotalSales);

        BigDecimal allBranchTotalSalesByYear = getAllTotalSalesByYear(allBranchCsv, 2016);
        System.out.println("All branches total sale in year 2016: " + allBranchTotalSalesByYear);

        String monthFruitSoldMost = getMonthWhereSoldMostByItemType(allBranchCsv, "Fruits");
        System.out.println("Month where Fruits are sold the most: " + monthFruitSoldMost);

        String mostSoldItemIn2012 = getMostSoldItemTypeByYear(allBranchCsv, 2012);
        System.out.println("Most sold item in 2012: " + mostSoldItemIn2012);

        String monthMostNumberUnitsSold = getMonthWhereSoldMostItems(allBranchCsv);
        System.out.println("Month where most number of units sold: " + monthMostNumberUnitsSold);

        String manilaMostSoldItem = getMostSoldItemTypeByBranch(manilaCsv);
        System.out.println("Most sold item in MANILA: " + manilaMostSoldItem);

        String cebuMostSoldItem = getMostSoldItemTypeByBranch(cebuCsv);
        System.out.println("Most sold item in CEBU: " + cebuMostSoldItem);

        String davaoMostSoldItem = getMostSoldItemTypeByBranch(davaoCsv);
        System.out.println("Most sold item in DAVAO: " + davaoMostSoldItem);

    }

    public static Set<String> getAllItemsSoldInAlphabeticalOrder(Path directoryPath) throws IOException {
        Set<String> allItemsSorted = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(SalesInvoiceUtil::mapToSalesInvoice)
                .map(SalesInvoice::getItemType)
                .collect(Collectors.toCollection(TreeSet::new));
        return allItemsSorted;
    }

    public static BigDecimal getTotalSalesByBranch(Path filePath) {
        BigDecimal branchTotalSales = CsvFileReader.read(filePath)
                .map(SalesInvoiceUtil::mapToSalesInvoice)
                .map(SalesInvoiceUtil::computeSalesInvoiceTotal)
                .reduce(BigDecimal::add)
                .get();
        return branchTotalSales;
    }

    public static BigDecimal getAllTotalSales(Path directoryPath) throws IOException {
        BigDecimal allTotalSales = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(SalesInvoiceUtil::mapToSalesInvoice)
                .map(SalesInvoiceUtil::computeSalesInvoiceTotal)
                .reduce(BigDecimal::add)
                .get();
        return allTotalSales;
    }

    public static BigDecimal getAllTotalSalesByYear(Path directoryPath, int year) throws IOException {
        BigDecimal allTotalSalesByYear = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(SalesInvoiceUtil::mapToSalesInvoice)
                .filter(s -> s.getOrderDateYear() == year)
                .map(SalesInvoiceUtil::computeSalesInvoiceTotal)
                .reduce(BigDecimal::add)
                .get();
        return allTotalSalesByYear;
    }

    public static String getMonthWhereSoldMostByItemType(Path directoryPath, String itemType) throws IOException {
        String monthWhereSoldMostByItemType = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(SalesInvoiceUtil::mapToSalesInvoice)
                .filter(s -> s.getItemType().equals(itemType))
                .collect(
                        Collectors.groupingBy(
                                SalesInvoice::getOrderDateMonth,
                                Collectors.summingInt(SalesInvoice::getUnitsSold)
                        )
                ).entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getKey();
        return monthWhereSoldMostByItemType;
    }

    public static String getMostSoldItemTypeByYear(Path directoryPath, int year) throws IOException {
        String mostSoldItemTypeByYear = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(SalesInvoiceUtil::mapToSalesInvoice)
                .filter(s -> s.getOrderDateYear() == year)
                .collect(
                        Collectors.groupingBy(
                                SalesInvoice::getItemType,
                                Collectors.summingInt(SalesInvoice::getUnitsSold)
                        )
                ).entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getKey();
        return mostSoldItemTypeByYear;
    }

    public static String getMonthWhereSoldMostItems(Path directoryPath) throws IOException {
        String monthWhereSoldMostItems = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(SalesInvoiceUtil::mapToSalesInvoice)
                .collect(
                        Collectors.groupingBy(
                                SalesInvoice::getOrderDateMonth,
                                Collectors.summingInt(SalesInvoice::getUnitsSold)
                        )
                ).entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getKey();
        return monthWhereSoldMostItems;
    }

    public static String getMostSoldItemTypeByBranch(Path filePath) {
        String itemType = CsvFileReader.read(filePath)
                .map(SalesInvoiceUtil::mapToSalesInvoice)
                .collect(
                        Collectors.groupingBy(
                                SalesInvoice::getItemType,
                                Collectors.summingInt(SalesInvoice::getUnitsSold)
                        )
                ).entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getKey();
        return itemType;
    }

}


























