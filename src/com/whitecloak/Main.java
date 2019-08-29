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

    public static void main(String[] args) {

            try {
                getAllItemsSoldInAlphabeticalOrder(Path.of("branches"))
                        .stream()
                        .forEach(System.out::println);
            } catch(IOException e) {
                e.printStackTrace();
            }

            System.out.println("Cebu total sales: " + getTotalSalesByBranch(Path.of("branches","cebu.csv")));
            System.out.println("Davao total sales: " + getTotalSalesByBranch(Path.of("branches", "davao.csv")));
            System.out.println("Manila total sales: " + getTotalSalesByBranch(Path.of("branches", "manila.csv")));

            try {
                System.out.println("All branches total sale: " + getAllTotalSales(Path.of("branches")));

                System.out.println("All branches total sale in year 2016" + getAllTotalSalesByYear(Path.of("branches"), 2016));

                System.out.println("Month where Fruits are sold the most: "
                        + getMonthWhereSoldMostByItemType(Path.of("branches"), "Fruits"));

                System.out.println("Most sold item in 2012: "+ getMostSoldItemTypeByYear(Path.of("branches"), 2012));

                System.out.println("Month where most number of units sold: "+getMonthWhereSoldMostItems(Path.of("branches")));

            } catch(IOException e) {
                e.printStackTrace();
            }

            System.out.println("Most sold item in Manila: "+getMostSoldItemTypeByBranch(Path.of("branches","manila.csv")));
            System.out.println("Most sold item in Cebu: "+getMostSoldItemTypeByBranch(Path.of("branches","cebu.csv")));
            System.out.println("Most sold item in Davao: "+getMostSoldItemTypeByBranch(Path.of("branches", "davao.csv")));

    }

    public static Set<String> getAllItemsSoldInAlphabeticalOrder(Path directoryPath) throws IOException {
        Set<String> allItemsSorted = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(OrderUtil::mapToOrderObject)
                .map(Order::getItemType)
                .collect(Collectors.toCollection(TreeSet::new));
        return allItemsSorted;
    }

    public static BigDecimal getTotalSalesByBranch(Path filePath)  {
        BigDecimal branchTotalSales = CsvFileReader.read(filePath)
                .map(OrderUtil::mapToOrderObject)
                .map(OrderUtil::computeOrderTotal)
                .reduce(BigDecimal::add)
                .get();
        return branchTotalSales;
    }

    public static BigDecimal getAllTotalSales(Path directoryPath) throws IOException {
        BigDecimal allTotalSales = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(OrderUtil::mapToOrderObject)
                .map(OrderUtil::computeOrderTotal)
                .reduce(BigDecimal::add)
                .get();
        return allTotalSales;
    }

    public static BigDecimal getAllTotalSalesByYear(Path directoryPath, int year) throws IOException {
        BigDecimal allTotalSalesByYear = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(OrderUtil::mapToOrderObject)
                .filter(o -> o.getOrderDateYear() == year)
                .map(OrderUtil::computeOrderTotal)
                .reduce(BigDecimal::add)
                .get();
       return allTotalSalesByYear;
    }

    public static String getMonthWhereSoldMostByItemType(Path directoryPath, String itemType) throws IOException {
        String monthWhereSoldMostByItemType = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(OrderUtil::mapToOrderObject)
                .filter(o -> o.getItemType().equals(itemType))
                .collect(
                        Collectors.groupingBy(Order::getOrderDateMonth, Collectors.summingInt(Order::getUnitsSold))
                ).entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();
        return monthWhereSoldMostByItemType;
    }

    public static String getMostSoldItemTypeByYear(Path directoryPath, int year) throws IOException {
        String mostSoldItemTypeByYear = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(OrderUtil::mapToOrderObject)
                .filter(o -> o.getOrderDateYear() == year)
                .collect(
                        Collectors.groupingBy(Order::getItemType, Collectors.summingInt(Order::getUnitsSold))
                ).entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();
        return mostSoldItemTypeByYear;
    }

    public static String getMonthWhereSoldMostItems(Path directoryPath) throws IOException {
        String monthWhereSoldMostItems = CsvFileReader.readAllFilesInDirectory(directoryPath)
                .map(OrderUtil::mapToOrderObject)
                .collect(
                        Collectors.groupingBy(Order::getOrderDateMonth, Collectors.summingInt(Order::getUnitsSold))
                ).entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();
        return monthWhereSoldMostItems;
    }

    public static String getMostSoldItemTypeByBranch(Path filePath) {
        String itemType = CsvFileReader.read(filePath)
                .map(OrderUtil::mapToOrderObject)
                .collect(
                        Collectors.groupingBy(Order::getItemType, Collectors.summingInt(Order::getUnitsSold))
                ).entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();
        return itemType;
    }

}


























