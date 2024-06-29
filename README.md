The file LibTester.java contains the main method, i.e., public static void main(String[] args)  method to run the entire program.
The compiled .class files are not provided for storage constraints and readability.
File handling is used to store book and user data. There are 4 such files - 2 .txt files and 2 .bin files.
The .txt files are provided purely for readability. All the data is actually stored in and retrieved from the .bin files.
This is just a mini project and its interface is just the terminal; it does not contain any front-end code.

Here are the details about the actual theme of the project:
- Problem Statement:
Create a library management system that helps perform different tasks such as creating a library account, renewing account, borrowing book, renewing a book, returning a book and donating a book

- Motivation for the problem
To automate library processes using a simple yet efficient user interface, which helps facilitate the aforementioned tasks by automatically updating the respective databases.

OBJECT ORIENTED FEATURES USED:
-	Compile time polymorphism: method overloading
-	Run time polymorphism: method overriding
-	Collections framework: ArrayList, TreeSet, HashSet … and their associated methods
-	Inheritance, multiple inheritance – from class and interface
-	Complete abstraction achieved using an interface
-	Delegation: ArrayList<Book> is an instance variable of Patron
-	File Handling: text files and binary files
-	Encapsulation – using getter and setter methods for all instance variables in patron class
-	Exception handling – 5 different user defined functions used apart from handled built in exceptions
