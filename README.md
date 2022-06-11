# Starter tasks
 
This is a Spring Boot project including file Database and Hibernate API

Database Classes

- Model classes are included in model package (ORM db classes). Use these classes to map new tables to java class
- Services: Use these classes to access database and perform queries and implement your business logic.
- Controllers: Use these classes to handle request and response (Json converts automatically as long as there is no recursive relation)

# STEPS TODO before submit 
1. Remove .git folder from project after pull
1. Create bare bone repo in github or BitBucket 
1. Push your final work into repo.
1. Email your repo url to asafaie@contal.net.au
1. Use Intellij preferably (https://www.jetbrains.com/idea/download/#section=mac) or Eclipse (https://www.eclipse.org/downloads/packages/)
 
 
## Implementation
Two classes already in model represent Database Tables; Hibernate creates Database Tables automatically as long as you add correct annotation to each class and properties.

1. CustomerAccount
1. BankTransaction

### CustomerAccount Table Setup
1. id
1. firstName : String
1. lastName : String
1. accountNumber : int
1. accountBalance : double
 
### BankTransaction Table Setup
1. id
1. customerAccount
1. transactionAmount
1. transactionDate
 
 
## Tasks:
#### STEP 1: Implement Models inside model package (classes already exist )
1. Add require properties to each class.
1. Each CustomAccount has multiple BankTransaction
### STEP 2: Implement BankService (complete all TODO ) 
#### Warning : Database Query only should be implemented inside this class, NOT CONTROLLER.
2. Use BankService class
2. Add a method that accepts CustomerAccount as an argument and saves into database and returns the ID.
2. Add a method that accepts accountNumber as an argument and returns the Bank account information.
2. Implement rest of TODO Methods.  
2. (Advanced Task) Add a Method to accept json ` { accountNumber: "" } ` and return account balance at every transaction Date (Get all the existing transactions, add Entry Sets to map by using Transaction date as Key and sum of transaction totals until That day for value  ).
### STEP 3: Implement BankController (/banks) 
#### ***Avoid Using Database in this class (Implement new Method in service class if you have to do extra query )
3. Use BankController 
3. Add post request to accept json ` {"firstName":"John" , "lastName" : "Sina" ,"accountBalance" : 2000 } ` , assign random account number , save into database and return generated account number ,Implement AccountNameGenerator function inside AccountNumberUtil class to create new account number.
3. Add Post Request to accept account number and return account details. (/banks/account)
3. Add a post Request to accept the account number and amount and create a bank transaction with a negative or positive amount . (/banks/addtransaction)
3. Add Post Request to accept json ` { accountNumber: "" } ` and return list of last 10 transactions. (/banks/transactions)
3. Add Post request to accept json ` { accountNumber: "" } ` and return account existing balance. (/banks/balance)

## Be sure you check the TODO list in your IDE before you submit to be sure you don't miss any task.