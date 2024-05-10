# Program Requirements
1. OS required => mac
2. JDK installed

#External jar used=>(Download below jar: https://drive.google.com/drive/folders/16R20we1aQ0M5kOVDCPLdHDqmF_8zhjbt?usp=share_link)
1.joda-time-2.12.1.jar (https://mvnrepository.com/artifact/joda-time/joda-time/2.12.1)
2.json-20220320.jar (https://mvnrepository.com/artifact/org.json/json/20220320)

   #Below are all dependency jar(JFreeChart FX » 2.0.1)
   (https://mvnrepository.com/artifact/org.jfree/org.jfree.chart.fx/2.0.1)
3.org.jfree.chart.fx-2.0.1.jar
4.jfreechart-1.5.3.jar
5.org.jfree.fxgraphics2d-2.1.jar
6.javafx-controls-15.jar
7.javafx-controls-15-mac.jar
8.javafx-graphics-15.jar
9.javafx-graphics-15-mac.jar
10.javafx-base-15.jar
11.javafx-base-15-mac.jar
12.javafx-swing-15.jar
13.javafx-swing-15-mac.jar

(Download below jar: https://drive.google.com/drive/folders/16R20we1aQ0M5kOVDCPLdHDqmF_8zhjbt?usp=share_link)
#External resources for jar: (Download from drive link)
1. Create "lib" folder in same location as Assignment6.jar and place all external jar in it. (lib folder is provided in assignment
   source code, it contains above external jar)
2. Final folder structure should be same as below:
   lib/json-20220320.jar
   lib/joda-time-2.12.1.jar
   lib/org.jfree.chart.fx-2.0.1.jar
   lib/jfreechart-1.5.3.jar
   lib/org.jfree.fxgraphics2d-2.1.jar
   lib/javafx-controls-15.jar
   lib/javafx-controls-15-mac.jar
   lib/javafx-graphics-15.jar
   lib/javafx-graphics-15-mac.jar
   lib/javafx-base-15.jar
   lib/javafx-base-15-mac.jar
   lib/javafx-swing-15.jar
   lib/javafx-swing-15-mac.jar
   Assignment6.jar

# To run program:
1. Download project jar(Test) and follow "#External resources for jar:" steps.
2. Place all external jar in lib folder.
   Folder structure:
   /lib
   /Assignment6.jar

3. Open mac terminal and go to download jar location

3. run following command=>
java -cp "lib/json-20220320.jar;lib/joda-time-2.12.1.jar;lib/org.jfree.chart.fx-2.0.1.jar;lib/jfreechart-1.5.3.jar;lib/org.jfree.fxgraphics2d-2.1.jar;lib/javafx-controls-15.jar;lib/javafx-controls-15-mac.jar;lib/javafx-graphics-15.jar;lib/javafx-graphics-15-mac.jar;lib/javafx-base-15.jar;lib/javafx-base-15-mac.jar;lib/javafx-swing-15.jar;lib/javafx-swing-15-mac.jar" -jar Assignment6.jar

4.Press enter for old text based interface or Press '1' for new interface.

5. New GUI Option:
   a.Create Portfolio: create new portfolio
   b.Show Portfolio: composition, value,  cost basis and Portfolio Performance(bar chart)
   c.Import Portfolio: import new portfolio
   d.Modify Portfolio: buy and sell stocks in existing portfolio
   e.Investment Strategy:  Fixed Amount and Dollar cost strategy application
   f.Exit: to exit screen


#Textbased options(if enter is pressed at 4th step):[Below steps are same as last time]
# Steps for creating a portfolio of 3 stocks -
1. "Main Page" of the application will be shown in terminal display.
   Enter '1' from keyboard and hit enter key.
   *********************** MAIN PAGE *********************
   1. Create portfolio.
   2. Show portfolios.
   3. Import portfolio.
   4. Modify portfolios.
   Enter q or quit to quit the application.
   Enter option: 1

2.Enter number "Flexible" from keyboard and hit enter key.
  Display should look similar to below=>
  *********************** NEW PORTFOLIO ***********************
  Enter portfolio type (Flexible/Inflexible): Flexible

3. Enter number "3" from keyboard and hit enter key.
  Display should look similar to below=>
  *********************** NEW PORTFOLIO ***********************
  Enter portfolio type (Flexible/Inflexible): Flexible
  Enter number of stocks you want to add: 3


4. Enter first companies Symbol(Ticker Symbol) and hit enter key.
  NOTE: This software support Top 482 S&P original ticker symbol present in listing_status.csv.
  File "listing_status.csv" is present in "res/appData/listing_status.csv" path in res folder.
  Example: ORCL

  Display should look similar to below=>
  **********************************
  Enter details for the stock-1
  **********************************
  Company Symbol: ORCL
  Please enter a date in (YYYY-MM-DD) format:

4. Enter date in (YYYY-MM-DD) format. For example, "2014-10-10"
   Display should look similar to below=>
   Please enter a date in (YYYY-MM-DD) format: 2014-10-10
   Company Name: Oracle Corp
   Price: 38.10
   Transaction quantity:

4. Enter quantity of first companies stock to purchase.[Non-zero integer value]
  Example: 10

  Display should look similar to below=>
  **********************************
  Enter details for the stock-1
  **********************************
  Company Symbol: ORCL
  Please enter a date in (YYYY-MM-DD) format:
  Entered date is invalid. Try Again.
  Please enter a date in (YYYY-MM-DD) format: 2014-10-10
  Company Name: Oracle Corp
  Price: 38.10
  Transaction quantity: 10
  Commission Fees % (Must be in range 1-50):

5. Enter commission fees in range 1-50. For example, "2"

  Display should look similar to below=>
  **********************************
  Enter details for the stock-1
  **********************************
  Company Symbol: ORCL
  Please enter a date in (YYYY-MM-DD) format: 2014-10-10
  Company Name: Oracle Corp
  Price: 38.10
  Transaction quantity: 10
  Commission Fees % (Must be in range 1-50): 2
  Purchasing 10 stocks of Oracle Corp for a total of $381.00 at 2.00% commission.

  **********************************
  Enter details for the stock-2
  **********************************
  Company Symbol:


6. Similarly add stocks for 2nd company. For example, "GOOGL"

  Display should look similar to below=>
  **********************************
  Enter details for the stock-2
  **********************************
  Company Symbol: GOOGL
  Please enter a date in (YYYY-MM-DD) format: 2016-11-01
  Company Name: Alphabet Inc - Class A
  Price: 805.48
  Transaction quantity: 12
  Commission Fees % (Must be in range 1-50): 3
  Purchasing 12 stocks of Alphabet Inc - Class A for a total of $9665.76 at 3.00% commission.

  **********************************
  Enter details for the stock-3
  **********************************
  Company Symbol:

7. Similarly add stocks for 3nd company. For example, "BAC"

  Display should look similar to below=>
   **********************************
   Enter details for the stock-3
   **********************************
   Company Symbol: BAC
   Please enter a date in (YYYY-MM-DD) format: 2018-02-03
   Company Name: Bank Of America Corp
   Price: 31.95
   Transaction quantity: 5
   Commission Fees % (Must be in range 1-50): 1
   Purchasing 5 stocks of Bank Of America Corp for a total of $159.75 at 1.00% commission.

   ********************************
   TRANSACTIONS:
   Oracle Corp, quantity: 10, BUY
   Alphabet Inc - Class A, quantity: 12, BUY
   Bank Of America Corp, quantity: 5, BUY
   ********************************

   *********************** MAIN PAGE *********************
   1. Create portfolio.
   2. Show portfolios.
   3. Import portfolio.
   4. Modify portfolios.
   Enter q or quit to quit the application.
   Enter option:

9. Portfolio is created and save in local system with transaction time_stamp and flexible/inflexible
   as postfix in portfolio name.
   For example,
   Portfolio_<TimeStamp>_<Flexible/Inflexible>
   Portfolio_2022-11-16_18:20:51_Flexible

10. Application now has redirected you to Application Main Page.
11. Enter q to exit application.

    Display should look similar to below=>
    Enter option: q
    ***********************************
    Stopping program execution.
    ***********************************

#Steps to query the value and cost basis:
=>Download project jar(Assignment 5) and follow "#External resources for jar:" steps.
1. "Main Page" of the application will be shown in terminal display.
   Enter '2' from keyboard and hit enter key.

   Display should look similar to below=>
   *********************** MAIN PAGE *********************
   1. Create portfolio.
   2. Show portfolios.
   3. Import portfolio.
   4. Modify portfolios.
   Enter q or quit to quit the application.
   Enter option: 2

   *********************** PORTFOLIOS ***********************
   1. Portfolio_2022-11-17_16:56:04_Flexible
   Type M to go to main page.
   Enter portfolio number:

2. Enter portfolio index as portfolio number. Highest index is of recently created portfolio.
  Enter "1" to view details of 1st portfolios in list.

  Display should look similar to below=>
  Enter portfolio number: 24
  Selected portfolio: Portfolio_2022-11-17_16:56:04_Flexible.

  1. Composition of the portfolio.
  2. Value of the portfolio.
  3. Cost basis of the portfolio.
  4. Show portfolio performance.
  What do you want?

3. Enter 2 to get value of portfolio.

  Selected portfolio: Portfolio_2022-11-17_16:56:04_Flexible.

  Display should look similar to below=>
  1. Composition of the portfolio.
  2. Value of the portfolio.
  3. Cost basis of the portfolio.
  4. Show portfolio performance.
  What do you want? 2
  Please enter a date in (YYYY-MM-DD) format:

4.Enter date for which value of the portfolio is required.
  For example: "2021-11-01"

  Display should look similar to below=>
  Please enter a date in (YYYY-MM-DD) format: 2021-11-01
  -----------------------------------------------------------------------------------------------

      CompanyName  |  CompanySymbol  |       Quantity  |     Unit Price  |    Total Value  |Net Commission Fees

  -----------------------------------------------------------------------------------------------
  Alphabet Inc - Class A  |          GOOGL  |             12  |        2869.94  |       34439.28  |         289.97

  Bank Of America Corp  |            BAC  |              5  |          47.85  |         239.25  |           1.60

      Oracle Corp  |           ORCL  |             10  |          94.38  |         943.80  |           7.62

  Value of the portfolio on 2021-11-01 was $35622.33.
  *********************** PORTFOLIOS ***********************
  1. Portfolio_2022-11-17_16:56:04_Flexibl
  Type M to go to main page.
  Enter portfolio number:

5. Enter portfolio index as portfolio number. Highest index is of recently created portfolio.
   Enter "1" to view details of 1st portfolios in list.

   Display should look similar to below=>
   Enter portfolio number: 1
   Selected portfolio: Portfolio_2022-11-17_13:51:46_Flexible.

   1. Composition of the portfolio.
   2. Value of the portfolio.
   3. Cost basis of the portfolio.
   4. Show portfolio performance.
   What do you want?

6. Enter 3 to get the cost basis of the portfolio.

  Display should look similar to below=>
  Selected portfolio: Portfolio_2022-11-17_13:51:46_Flexible.

  1. Composition of the portfolio.
  2. Value of the portfolio.
  3. Cost basis of the portfolio.
  4. Show portfolio performance.
  What do you want? 3
  Please enter a date in (YYYY-MM-DD) format:

7. Enter date to get the cost basis of the portfolio. For example, "2018-11-01"

  Display should look similar to below=>
  Please enter a date in (YYYY-MM-DD) format: 2018-11-01
  Cost basis of the portfolio till 2018-11-01 is $13904.14.
  *********************** PORTFOLIOS ***********************
  1. Portfolio_2022-11-17_16:56:04_Flexible
  Type M to go to main page.
  Enter portfolio number:

8. Enter M to go to main page.

  Display should look similar to below=>
  Type M to go to main page.
  Enter portfolio number: M
  *********************** MAIN PAGE *********************
  1. Create portfolio.
  2. Show portfolios.
  3. Import portfolio.
  4. Modify portfolios.
  Enter q or quit to quit the application.
  Enter option:

9. Enter q to quit the program.

  Display should look similar to below=>
  *********************** MAIN PAGE *********************
  1. Create portfolio.
  2. Show portfolios.
  3. Import portfolio.
  4. Modify portfolios.
  Enter q or quit to quit the application.
  Enter option: q
  ***********************************
  Stopping program execution.
  ***********************************


# Steps to view portfolios:
1. After running "java -jar <jar_name>.jar" command, "Main Page" of the application will be shown in terminal display
   Enter '2' from keyboard and hit enter key.
   Display should look similar to below=>
  *********************** MAIN PAGE *********************
  1. Create portfolio.
  2. Show portfolios.
  3. Import or Export portfolios.
  Enter option: 2
  *********************** PORTFOLIOS ***********************
  1. Portfolio_2022-11-03_16:52:57
  2. Portfolio_2022-11-03_16:55:28
  Type M to go to main page.
  Enter portfolio number:

2. Enter index of given portfolio to open. For example, enter "1" to show "Portfolio_2022-11-03_16:52:57" details
   or enter "2" to show "Portfolio_2022-11-03_16:55:28" details.
   If "1" is entered,

   Display should look similar to below=>
   *********************** PORTFOLIOS ***********************
   1. Portfolio_2022-11-03_16:52:57
   2. Portfolio_2022-11-03_16:55:28
   Type M to go to main page.
   Enter portfolio number: 1
   Selected portfolio: Portfolio_2022-11-03_16:52:57.

   1. Composition of the portfolio.
   2. Value of the portfolio.
   What do you want?

3. Enter "1" for viewing composition of portfolios or "2" get details of portfolios on specific date.
   For example, if "2" is entered and pressed enter key,

   Display should look similar to below=>
   1. Composition of the portfolio.
   2. Value of the portfolio.
   What do you want? 2
   Please enter a date in (YYYY-MM-DD) format:

4. Enter date to get price of stocks on that specific date.
   Note: Enter any date after 1 oct, 2022 in (YYYY-MM-DD) format.
   For example, "2022-10-10" is entered,

   Display should look similar to below=>
   1. Composition of the portfolio.
   2. Value of the portfolio.
   What do you want? 2
   Please enter a date in (YYYY-MM-DD) format: 2022-10-10
   -----------------------------------------------------------------------------------------------

       CompanyName  |  CompanySymbol  |       Quantity  |    BoughtPrice  |    BoughtValue

   -----------------------------------------------------------------------------------------------
       Oracle Corp  |           ORCL  |              1  |          62.57  |          62.57

   Value of the portfolio on 2022-10-10 was $62.57.
   *********************** PORTFOLIOS ***********************
   1. Portfolio_2022-11-03_16:52:57
   2. Portfolio_2022-11-03_16:55:28
   Type M to go to main page.
   Enter portfolio number:

5. Type "M" and hit enter to return to Main Page.

   Display should look similar to below=>
   Type M to go to main page.
   Enter portfolio number: M
   *********************** MAIN PAGE *********************
   1. Create portfolio.
   2. Show portfolios.
   3. Import or Export portfolios.
   Enter option:




#To compile and build project make sure to add given jar specified in "External jar used" as your
compile and runtime dependency. Also, make sure to add below line in MANIFEST.MF in resources/META-INF/MANIFEST.MF=>

Class-Path: lib/json-20220320.jar lib/joda-time-2.12.1.jar lib/org.jfree.chart.fx-2.0.1.jar lib/jfreechart-1.5.3.jar lib/org.jfree.fxgraphics2d-2.1.jar lib/javafx-controls-15.jar lib/javafx-controls-15-mac.jar lib/javafx-graphics-15.jar lib/javafx-base-15.jar lib/javafx-base-15-mac.jar lib/javafx-swing-15.jar lib/javafx-swing-15-mac.jar

#Steps to add external jar in intelliJ(source code running):
1. File->open->select project path and open.
2. Adding external jars
File->project structure->libraries->
    click "+" to add-> click from maven->
    a. search "jfree.chart.fx" and select latest version in dropdown and click ok
    b. search "joda.time" and select latest version in dropdown and click ok
    c. search "json" and select latest version in dropdown and click ok
3.Click apply and ok.