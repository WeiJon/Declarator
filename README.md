# Declarator

I wrote this program partly to practice coding after graduating from AW Academy's Java bootcamp, 
but I also had the need to simplify tax calculations for automated forex trading.

Download and put the files from the demo-folder into the same folder and try the program out!

Functional overview

1. Within the trading platform I save the trade reports for the desired time period as html-files.
2. The program then uses a web scraping method to collect table data from the report-files and stores every individual trade in a list.
3. A method goes through the list of trades and determines the latest closing date and the earliest opening date -14 days(for overhead if the exact date isn't    present in the search in the next step). 
4. These dates are then inserted to a hardcoded url that points to the swedish central banks(Sveriges Riksbank) search function for currency rates.
5. The same kind of web scraping method used earlier are then used to collect the daily rates of four predetermined currencies needed for the calculations, and their corresponding date, and stores it in a list.
6. The trades are then linked to the relevant rates based on the specific opening and closing dates for that trade, and sent to a method that redirects the trade and rates to different methods based on currency pair and trade direction (buy/sell). 
7. The method the trade and rates are sent to then do the required calculations (such as amount, sell and buy price in SEK etc.) and stores and returns this data in another object type.
8.The new, calculated trades, are stored in a new list.
9. This new list is then returned to another class that splits the list into four new list based on what the trade's base currency is.
10. After this the trades from the four lists of trades are converted to strings and inserted to tables (along with other information such as the sum of the amounts and prices, and also the affected time period etc.) which are then compiled to a html-file with hard coded html structure and styling.
11. The created html-file is the executed and all the information is presented in a web browser.


