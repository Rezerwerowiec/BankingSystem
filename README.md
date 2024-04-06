# Banking System
* [General informations](#general-informations)
* [Details](#details)

## 1. General informations
Project created for study purposes.
It's a banking application for bank operators, who can add customers and their accounts, make transactions and display data.
The project is written with Visual Studio 2022 using C++ language.

## 2. Details
After we run the program first time, we get a list of options to choose as the bank operator.
The options to choose are basically the most needed to operate between accounts, such like:
- Adding new customers,
- Adding new accounts assigned to certain customer,
- Making transactions between accounts,
- Displaying customer or all customers available data.

After we choose the option, we get clean informations what we have to type there.
Changes are saved after we choose to quit the program. It can be easily changed for any other condition, just with adding `saveToFile()` method in certain places.
We can specify name of data file by changing value of constant variable `DATAFILE_NAME` at the beginning of the program. We can also specify custom delimiters for saved objects inside it.
Passwords are saved with simple encryption for now - it should be changed for normal encryption methods, such like SHA-256 encryption. Adding this is not necessary for this purpose and it could make some mess in the project structure so I just implemented a simple shift encryption.</details>


Author: Damian Piszka
