package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import model.enums.TransactionType;

/**
 * This class represents a Portfolio repository object which is used to save and retrieve stock
 * portfolio from a file.
 */
public class PortfolioRepositoryImpl implements PortfolioRepository {
  private final String fileUrl = "/Applications/portfolios/";

  /**
   * Constructs a PortfolioRepositoryImpl object and initializes the fileUrl in which files will be
   * saved.
   */
  public PortfolioRepositoryImpl() {
    try {
      Path path = Paths.get(fileUrl);
      Files.createDirectories(path);
    } catch (IOException io) {
      // pass;
    }
  }

  /**
   * Helper method for format of data saved in a file.
   *
   * @param companyStock Stock details for a company
   * @return String format for data.
   */
  private String formatter(CompanyStock companyStock) {
    return companyStock.toString();
  }

  /**
   * Helper method to find a unique file name required for saving the stock portfolio data.
   *
   * @return Portfolio file name.
   */
  private String createPortfolio(String fileType) {
    FileWriter fileWriter;
    String filename = "";
    try {
      SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
      String date = dataFormat.format(new Date());
      filename = "Portfolio_" + date + "_" + fileType;
      fileWriter = new FileWriter(fileUrl + filename);
      BufferedWriter bw = new BufferedWriter(fileWriter);
      String headers = "Date,CompanyName,CompanySymbol,Quantity,Unit Price,Total Value,"
              + "Commission Fees,Type";
      bw.write(headers);
      bw.close();
    } catch (IOException ex) {
      //throw new RuntimeException(ex);
    }
    return filename;
  }

  @Override
  public String saveCompanyStock(String fileType, List<CompanyStock> stocks) {
    String filename = createPortfolio(fileType);
    saveFile(filename, stocks);
    return filename;
  }

  @Override
  public void saveFile(String filename, List<CompanyStock> stocks) {
    FileWriter fileWriter;
    try {
      fileWriter = new FileWriter(
          fileUrl + filename,
          true);
      BufferedWriter bw = new BufferedWriter(fileWriter);
      for (CompanyStock companyStock : stocks) {
        bw.newLine();
        bw.write(formatter(companyStock));
      }
      bw.newLine();
      bw.close();
      Cache.objectStore.put(filename, readFile(fileUrl + filename));
    } catch (IOException ex) {
      // throw new RuntimeException(ex);
    }
  }


  @Override
  public void updateFile(String filename, List<CompanyStock> stocks) {
    FileWriter fileWriter;
    try {
      fileWriter = new FileWriter(
          fileUrl + filename,
          true);
      BufferedWriter bw = new BufferedWriter(fileWriter);
      for (CompanyStock companyStock : stocks) {
        bw.write(formatter(companyStock));
        bw.newLine();
      }
      bw.close();

      Cache.objectStore.put(filename, readFile(fileUrl + filename));
    } catch (IOException ex) {
      // throw new RuntimeException(ex);
    }
  }

  @Override
  public HashMap<String, List<CompanyStock>> readFile(String filePath) {
    HashMap<String, List<CompanyStock>> stocks = new HashMap<>();
    Scanner sc;
    try {
      sc = new Scanner(new File(filePath));
      if (sc.hasNextLine()) {
        sc.nextLine();
      }
      while (sc.hasNextLine()) {
        String[] result = sc.nextLine().split(",");

        String date;
        String companySymbol;
        Double quantity;
        Double boughtPrice;
        Double boughtValue;
        Double commissionFees;
        TransactionType type;
        String companyName = Cache.companyList.get(result[2]);
        try {
          date = result[0];
          companySymbol = result[2];
          quantity = Double.parseDouble(result[3]);
          boughtPrice = Double.parseDouble(result[4]);
          boughtValue = Double.parseDouble(result[5]);
          commissionFees = Double.parseDouble(result[6]);
          type = TransactionType.valueOf(result[7]);
        } catch (NumberFormatException nfe) {
          return null;
        }
        if (companyName == null || companyName.isEmpty() || !companyName.equals(
            result[1]) || quantity < 0 || boughtPrice < 0
            || commissionFees < 0) {
          return null;
        }

        CompanyStock stock = new CompanyStockImpl(date, companyName, companySymbol, quantity,
            boughtPrice, boughtValue, type, commissionFees);

        if (!stocks.containsKey(companySymbol)) {
          List<CompanyStock> newList = new ArrayList<>();
          newList.add(stock);
          stocks.put(companySymbol, newList);
        } else {
          stocks.get(companySymbol).add(stock);
        }
      }
      sc.close();
    } catch (FileNotFoundException ex) {
      // throw new RuntimeException(ex);
    }

    for (String symbol : stocks.keySet()) {
      HashMap<String, Double> qntyDate = new HashMap<>();

      for (CompanyStock s : stocks.get(symbol)) {
        int sign = s.getType() == TransactionType.SELL ? -1 : 1;
        Double currentQnty = s.getQuantity() * sign;
        String currentDate = s.getDate();

        if (!qntyDate.containsKey(currentDate)) {
          qntyDate.put(currentDate, currentQnty);
        } else {
          qntyDate.put(currentDate, qntyDate.get(currentDate) + currentQnty);
        }
      }

      ArrayList<String> sortedKeys = new ArrayList<>(qntyDate.keySet());
      Collections.sort(sortedKeys);

      long currQnty = 0;

      for (String el : sortedKeys) {
        currQnty += qntyDate.get(el);

        if (currQnty < 0) {
          return null;
        }
      }
    }

    return stocks;
  }

  @Override
  public List<String> getListOfPortfolio() {
    File file = new File(fileUrl);
    String[] fileNames = file.list();
    if (fileNames == null || fileNames.length == 0) {
      return Collections.emptyList();
    }
    List<String> list = Arrays.asList(fileNames);
    list.remove(".DS_Store");
    return list;
  }

  @Override
  public HashMap<String, List<CompanyStock>> getPortfolio(String filename) {
    if (Objects.equals(filename, ".DS_Store")) {
      return new HashMap<>();
    }
    return readFile(fileUrl + filename);
  }

}
