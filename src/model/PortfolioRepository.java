package model;

import java.util.HashMap;
import java.util.List;

/**
 * This interface contains possible operations on a portfolio saved in a file.
 */
public interface PortfolioRepository {

  /**
   * Method to save company stock in an external file.
   * @param fileType portfolio type.
   * @param stocks Company stock details.
   * @return company stock.
   */
  String saveCompanyStock(String fileType, List<CompanyStock> stocks);

  /**
   * Method for getting the list of portfolio saved.
   *
   * @return list of saved portfolios.
   */
  List<String> getListOfPortfolio();

  /**
   * Method for getting the company stock details saved in a portfolio.
   *
   * @param filename filename of the portfolio.
   * @return Company stock details saved in a file.
   */
  HashMap<String, List<CompanyStock>> getPortfolio(String filename);

  /**
   * Method for saving portfolio data in a file.
   *
   * @param filename Name of the file.
   * @param stocks   Portfolio data.
   */
  void saveFile(String filename, List<CompanyStock> stocks);

  /**
   * Method for reading the file's content.
   *
   * @param filePath Path of the file.
   * @return Returns file's content in a hashMap.
   */
  HashMap<String, List<CompanyStock>> readFile(String filePath);

  /**
   * Method for editing a portfolio file for single record.
   * @param filename filename in which portfolio is stored.
   * @param stock new stock.
   */
  void updateFile(String filename, List<CompanyStock> stock);

}
