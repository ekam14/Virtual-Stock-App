# Virtual-Stock-App

## Introduction
Using this application, one can virtually buy/sell all the available company's stocks on the US stock market. All the application code is written in Java.

### Features:
My application supports 2 user interfaces:
(a) Terminal text-based
(b) Graphical user interface (GUI)

(I) The below features are common and available in both text based and graphical user interface:
1. User can enter one or more stocks to create one or more portfolios. Portfolios can be of Flexible or Inflexible type. A flexible portfolio can be modified whereas a inflexible portfolio cannot be edited.
2. One can see the composition of saved portfolios.
3. Can get the value of a portfolio for a date.
4. User entered portfolio is being saved.
5. User can load an external portfolio by providing a path. External file has to be valid csv file with standard data format. Example of external csv file is provided in zip file.
6. User can buy/sell a stock on any date to/from a flexible type portfolio only.
7. User can see cost basis (money invested) for a selected portfolio.
8. Commission fees is calculated on every buy/sell transaction which the user will enter for every transaction and it must be between 1-50.
9. Stock prices are being fetched from the AlphaVantage API.
10. User can see the performance of a portfolio for a date range.

(II) Graphical user interfaces specific features:
1. User can perform 2 investment strategies: fixed amount in existing portfolio and dollar cost averaging for creating a new portfolio.

### Technologies used
1. Java
2. Java Swing for GUI