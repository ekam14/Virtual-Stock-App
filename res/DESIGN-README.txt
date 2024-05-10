****** DESIGN CHANGES (Assignment 6) ******
(A) Controller Component
    I. Added new interface features for handling all GUI related portfolio operations. It has functions
    which are invoked in the controllerGUI whenever a user performs a registered event listener described
    in View component for GUI.

    II. Added new class ControllerGUI which implements features interface. A separate controller independent
    of text-based controller was required to handle GUI related view component operations.


(B) Model Component
    I. Addition of InvestmentStrategy enum (DOLLAR_COST_AVERAGING, FIXED_AMOUNT) as addition of enums
    makes it easy to change any application variable, reduces typo errors.

    II. Addition of strategy package which contains its package private strategy interface (operations
    possible using individual investment strategy) and its corresponding implementation. Required as
    part of new feature.

    III. Added new common helper methods in AbstractModel class such as for validating weights and get
    strategy name based on strategy enum. Required as part of new feature as it was needed to validate
    data before applying investment strategy.

    IV. Added new methods in ModelComponent interface such as for validating inputs (used in investment
    strategy for validating user entered input), commission fees/validate file type (made this method
    general as it was being used in both views).

    V. Made the data type of stock quantity in the application from Long to Double. Reason for this
    was to handle cases where a user can apply strategy and get fraction quantity of share as per his
    investment amount.


(C) View Component
    I. Added new classes to support GUI.
    - JFrameView: creates the main page frame, acts as parent classes for other portfolio operation
    frames. It is only point from which the controllerGUI directly communicates and calls other
    portfolio operation frames as per request.
    - Separate class for each portfolio operation: this was done to achieve the single
    responsibility and interface segregation property of the SOLID principles. Any future change
    will only be done in their respective classes/interfaces.

    II. Added new interfaces, each for separate portfolio operation which were broken from the
    previous common view component interface. Reason for this was to fulfill:
    - Single responsibility, as individual portfolio operations are now handled by their own classes
    and interfaces.
    - Interface segregation, only methods required by the client will be accessible to him/her.



****** DESIGN CHANGES (Assignment 5) ******

(A) Controller Component
    I. Added command design pattern for handling all operations: creating, showing, importing & modifying portfolio. For this we have
    added an interface (ControllerCommand) and classes for all controller operations.
    Reason - This will make the addition of any new controller operation easy, as for adding any new operation will require a separate
    class and implementing the ControllerCommand interface (Open for extension, closed for modification).
    Other advantages of this pattern are:
    - Every class now has a single responsibility.
    - Client only has the start method of the controller component, he does not have access to methods he is not required to know
    of (Interface segregation).

    II. Addition of more classes:
    - ClassAttributes: It contains objects of all the common controller component classes such as scanner, appendable output,
    model & view component object. Addition of it removed the task of instantiating these objects each time in every class.
    - IOMethods: It contains all the helper methods required by all the component classes. Made the controller interface less bulky,
    and at the same time helped us achieved Interface segregation.


(B) Model Component
    I. Addition of enums: portfolioType(Flexible/Inflexible), Timeunit(Daily/Monthly/Yearly), TransactionType(Buy, Sell).
    - Addition of enums makes it easy to change any application variable, reduces typo errors.

    II. Change of CompanyStock schema (used to represent 1 stock):
    - Instead of adding 1 unique company stock in an external file, we are now storing individual transactions.
    (Note: cumulative company stocks are shown when user queries it). Storing of transactions made all the date filter
    queries (show portfolio value, cost basis, portfolio performance) efficient. Now, we can easily filter transaction
    based on a date parameter.

    III. Addition of AbstractModel:
    - Contains all the model component helper methods (protected), which were public earlier in the previous version. Hence, giving
    access to methods only which are required.

    IV. Addition of new methods required as per the new features
    - Update file method in PortfolioRepository interface.
    - Get cost basis, update file, get portfolio performance methods in modelComponent interface.

    V. dto
     Pair and Portfolio dto are created for warping relevant data together. It is encapsulation data and used for transferring object
     between view and controller component.

    VI. API Requests
    API integration is completed to get stock Price.

    VII. Model is loosely coupled with API and data source. We are passing API and data source object instance to the model,
    so in future if any new implementation of API or data source is required it can be easily integrated without any changes
    in model implementation.


(C) View Component
    I. Addition of new methods required as per the new features
    - Show cost basis, show modify portfolio message, show portfolio performance methods in viewComponent interface.


****** OVERALL VIEW ******

1. Controller Component
- It is responsible for handling user inputs and knows when to call model and when to send results from model to the view.
- When the application starts, it asks the user whether to see text-based view (default) or GUI (type '1').
- It handles 3 main features:
	(a) Create portfolio => When user enters “1” on the main page, controller expects Company symbol and valid stock quantity from user.
	After this, it gives this data to the model which then saves this data into a 	unique portfolio file according to timestamp.
	(b) Show portfolio => When user enters “2” on the main page, controller expects the portfolio number and the required operation on it
	- Show composition, compute value, compute cost basis or portfolio performance for some date. All these operations makes the model
	to retrieve data from a saved portfolio or make a API request for fetching stock prices for a specified date.
	file. For calculating the portfolio value for a date, the modelComponent queries the pre-saved company’s stock price CSV file for a
	given date.
    (c) Import portfolio => When user enter "3" on the main page, controller expects the external portfolio file path from user. It
    forwards the data to model which validates and perform import of file to application data location.
    (d) Modify portfolio => When user enter "4" on the main page, controller expects the portfolio number and the required operation on it
    - Add or sell a stock. Note: user will be only able to modify flexible portfolios.

2. Model Component
- Contains all the application logic: Caching data, API requests, file reading/saving, calculating the portfolio value/cost basis/portfolio
  performance based on some date parameter, validating user input.

3. View Component
- Supports 2 different interfaces: text-based and GUI.
- Majority of the application features are supported by both, with the only exception that the GUI also supports investment strategies.