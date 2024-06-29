import java.util.*;


class BookNotFoundException extends Exception{
    public String getMessage(){
        return "Book not found";
    }
}
class PatronNotFoundException extends Exception{
    public String getMessage(){
        return "Patron details not found";
    }
}
class InvalidIdException extends Exception{
    public String getMessage(){
        return "Incorrect ID format";
    }
}
class BookNotSuitableException extends Exception{}


public class LibTester {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        //menu driven format
        int option;
        //decaring varibales to be used inside the case statements
        int id;
        Patron person;
        TreeSet<Integer> ids;
        do{
            System.out.println("-------------------------------------------------------");
            System.out.println("                   ;-; LibMan ;-;");
            System.out.println("-------------------------------------------------------");
            System.out.println("Welcome to our library management system!\nChoose any of the following options:");
            System.out.println("\t1. Subscribe to Plan");
            System.out.println("\t2. Renew Membership");
            System.out.println("\t3. Borrow book");
            System.out.println("\t4. Renew book");
            System.out.println("\t5. Return book");
            System.out.println("\t6. Get defaulters");
            System.out.println("\t7. Unsubscribe");
            System.out.println("\t8. Donate a book");
            System.out.println("\t0. To exit");
            System.out.print("Choose any of the following options (Enter 0,1,2,3,4,5,6,7,8) : ");
            option = sc.nextInt();
            

            switch (option){
                case 0:
                    break;

                case 1:
                    sc.nextLine();
                    System.out.print("Enter name : ");
                    String name = sc.nextLine();
                    System.out.print("Enter phone number : ");
                    String phoneNumber = sc.nextLine();
                    int membershipid = Patron.getUniqueMembershipId("PatronDataBase.bin");
                    System.out.println("The unique membership id assigned is : "+membershipid);
                    System.out.print("Enter 'Student' or 'WorkingProfessional' : ");
                    String subscriptionType  = sc.nextLine();
                    if (subscriptionType.equals("Student")){
                        Student s = new Student(name, phoneNumber, membershipid, false, false, 0);
                        s.renewPlan(0);
                        s.paymentStatus = true;
                        Patron.savePatronDetails(s);
                    }
                    else{
                        WorkingProfessional s = new WorkingProfessional(name, phoneNumber, membershipid, false, false, 0);
                        s.renewPlan(0);
                        Patron.savePatronDetails(s);
                    }
                    System.out.println("Your account has been successfully created!\n");
                    break;

                case 2:
                    sc.nextLine();
                    id = -1;
                    System.out.print("Enter your membership id : ");
                    String sTemp = sc.nextLine();
                    try{
                        if (sTemp.length()>5) throw new InvalidIdException();
                        id = Integer.parseInt(sTemp);
                    }
                    catch(NumberFormatException eformat){
                        System.out.println(eformat.getMessage());
                    } catch (InvalidIdException e) {
                        System.out.println(e.getMessage());
                    }
                    if (id==-1) break;
                    
                    //verify in the patron database
                    ids = Patron.getAllMembershipIds("PatronDataBase.bin");
                    boolean tempFlag = true;
                    try{
                        if (!ids.contains(id)) throw new PatronNotFoundException();
                    }
                    catch (PatronNotFoundException patexc){
                        System.out.println(patexc.getMessage());
                        System.out.println("If you dont have an ID, you must create an account first by chosing option 1");
                        tempFlag = false;
                    }
                    if (!tempFlag) break;
                    //so here, id is valid
                    Patron p = Patron.readPatronDetails(id, "PatronDataBase.bin");
                    System.out.println("Let's renew your membership");
                    System.out.print("How many days ago did you renew : ");
                    int days = sc.nextInt();
                    p.renewPlan(days);
                    break;
                
                case 3:
                    sc.nextLine();
                    id = -1;
                    System.out.print("Enter your membership id : ");
                    String sTemp2 = sc.nextLine();
                    try{
                        if (sTemp2.length()>5) throw new InvalidIdException();
                        id = Integer.parseInt(sTemp2);
                    }
                    catch(NumberFormatException eformat){
                        System.out.println(eformat.getMessage());
                    } catch (InvalidIdException e) {
                        System.out.println(e.getMessage());
                    }
                    if (id==-1) break;
                    
                    //borrowing a book
                    boolean tempFlag4 = true;
                    try{
                        System.out.print("Do you want to search for book by title or isbn? Enter 'title' or 'isbn' : ");
                        Book b123=null;
                        String response = sc.nextLine();
                        if (response.equalsIgnoreCase("title")){
                            System.out.print("Enter book title : ");
                            b123 = Operations.search(sc.nextLine());
                        }
                        else{
                            System.out.print("Enter isbn : ");
                            long templong = sc.nextLong();
                            b123 = Operations.search(templong);
                        }
                        if (b123==null) throw new BookNotFoundException();
                        person = Patron.readPatronDetails(id,"PatronDataBase.bin");
                        person.borrow(b123);
                    }
                    catch (BookNotFoundException e1Book){
                        System.out.println(e1Book.getMessage());
                        tempFlag4 = false;
                    }
                    if (!tempFlag4) {
                        System.out.println("\n");break;
                    }
                    break;
                
                case 4:
                    sc.nextLine();
                    id = -1;
                    System.out.print("Enter your membership id : ");
                    String sTemp3 = sc.nextLine();
                    try{
                        if (sTemp3.length()>5) throw new InvalidIdException();
                        id = Integer.parseInt(sTemp3);
                    }
                    catch(NumberFormatException eformat){
                        System.out.println(eformat.getMessage());
                    } catch (InvalidIdException e) {
                        System.out.println(e.getMessage());
                    }
                    if (id==-1) break;
                    
                    //start renewal process
                    boolean tempFlag5 = true;
                    Book btemp2 = null;
                    try{
                        System.out.print("Do you want to search for book by title or isbn? Enter 'title' or 'isbn' : ");
                        String response = sc.nextLine();
                        if (response.equalsIgnoreCase("title")){
                            System.out.print("Enter book title : ");
                            btemp2 = Operations.search(sc.nextLine());
                        }
                        else{
                            System.out.print("Enter isbn : ");
                            long templong = sc.nextLong();
                            btemp2 = Operations.search(templong);
                        }
                        if (btemp2==null) throw new BookNotFoundException();
                    }
                    catch (BookNotFoundException e1Book){
                        System.out.println(e1Book.getMessage());
                        tempFlag5 = false;
                    }
                    if (!tempFlag5) {
                        System.out.println("\n");break;
                    }
                    //here, title is valid, and id is valid
                    //now, check if the user already has the book that they wish to renew
                    person = Patron.readPatronDetails(id,"PatronDataBase.bin");
                    if (btemp2.by != person.membershipid){
                        System.out.println("You can only renew a book if you already have it");
                        break;
                    }
                    int days1;
                    System.out.print("How many days ago did you renew  : ");
                    days1 = sc.nextInt();
                    person.renew(btemp2, days1);
                    System.out.println("\n");
                    break;
                
                case 5:
                    sc.nextLine();
                    id = -1;
                    System.out.print("Enter your membership id : ");
                    String sTemp4 = sc.nextLine();
                    try{
                        if (sTemp4.length()>5) throw new InvalidIdException();
                        id = Integer.parseInt(sTemp4);
                    }
                    catch(NumberFormatException eformat){
                        System.out.println(eformat.getMessage());
                    } catch (InvalidIdException e) {
                        System.out.println(e.getMessage());
                    }
                    if (id==-1) break;
                    
                    //here, id is valid
                     boolean tempFlag6 = true;
                    Book btemp3 = null;
                    try{
                        System.out.print("Do you want to search for book by title or isbn? Enter 'title' or 'isbn' : ");
                        String response = sc.nextLine();
                        if (response.equalsIgnoreCase("title")){
                            System.out.print("Enter book title : ");
                            btemp3 = Operations.search(sc.nextLine());
                        }
                        else{
                            System.out.print("Enter isbn : ");
                            long templong = sc.nextLong();
                            btemp3 = Operations.search(templong);
                        }
                        if (btemp3==null) throw new BookNotFoundException();
                    }
                    catch (BookNotFoundException e1Book){
                        System.out.println(e1Book.getMessage());
                        tempFlag6 = false;
                    }
                    if (!tempFlag6) {
                        System.out.println("\n");break;
                    }
                    //here, btemp3 is valid
                    person = Patron.readPatronDetails(id,"PatronDataBase.bin");
                    //now, return
                    person.returnBook(btemp3);
                    break;
                
                case 6:
                    sc.nextLine();
                    System.out.print("Enter Library Password : ");
                    if (!sc.nextLine().equals("LIBRARY")){
                        System.out.println("Incorrect password");
                        break;
                    }
                    Patron.getDefaulters("PatronDataBase.bin");
                    break;

                case 7:
                    boolean flag4 = true;
                    System.out.print("Enter your membership id : ");
                    id = sc.nextInt();
                    try{
                        ids = Patron.getAllMembershipIds("PatronDataBase.bin");
                        if (!ids.contains(id)) throw new PatronNotFoundException();
                        person = Patron.readPatronDetails(id,"PatronDataBase.bin");
                    }
                    catch(PatronNotFoundException epat1){
                        System.out.println(epat1.getMessage());
                        System.out.println("If you do not have an ID, create an account through step 1");
                        flag4 = false;
                    }
                    if (!flag4) break;
                    //here, id is valid
                    person = Patron.readPatronDetails(id,"PatronDataBase.bin");
                    Patron[] ps = Patron.readPatronDetails("PatronDataBase.bin");
                    List<Patron> finalList = new ArrayList<>();
                    for (int i=0; i<ps.length; i++){
                        if (ps[i].getMembershipid() == id) continue;
                        finalList.add(ps[i]);
                    }
                    //now, finalList does not have the member removed,  i.e., shobith
                    Patron[] arr = new Patron[finalList.size()];
                    for (int i=0; i<finalList.size(); i++) arr[i] = finalList.get(i);
                    //copied to array
                    // now write array to patron file
                    Patron.savePatronDetails(arr);
                    //now, update book database, with all the person's borrowed books to -1
                    Book[] booksArr = Book.readDetails("BookDataBase.bin");
                    for (int i=0; i<booksArr.length; i++){
                        if (booksArr[i].by == id){
                            //set to available state
                            booksArr[i].stage = "Available";
                            booksArr[i].by = -1;
                        }
                    }
                    Book.saveDetails(booksArr);
                    //books changed
                    System.out.println(person.name+" has been successfully removed\n");
                    break;
                
                case 8:
                    System.out.println("You do not need to verify account to donate a book");
                    System.out.println("Go to the librarian to verify the condition of the book");
                    sc.nextLine();
                    System.out.print("Enter Library Password : ");
                    if (!sc.nextLine().equals("LIBRARY")){
                        System.out.println("Incorrect password");
                        break;
                    }
                    System.out.print("Is condition valid : ");
                    try{
                        if (!sc.nextLine().equalsIgnoreCase("yes")) throw new BookNotSuitableException();
                    }
                    catch(BookNotSuitableException exsuitable){
                            System.out.println("Sorry, Book not in good condition\n\n");
                            break;
                    }
                    System.out.println("process to donate book");
                    System.out.print("Enter book title : ");
                    String nameTemp = sc.nextLine();
                    System.out.print("Enter book author : ");
                    String authorTemp = sc.nextLine();
                    System.out.print("Enter book isbn : ");
                    long isbnTemp = sc.nextLong();
                    //check if book is already present
                    Book[] booksTemp3 = Book.readDetails("BookDataBase.bin");
                    HashSet<Long> hashset = new HashSet<>();
                    for (Book b : booksTemp3) hashset.add(b.isbn);
                    if (hashset.contains(isbnTemp)){
                        System.out.println("\nWe already have this book. Thanks anyways\n\n");
                        break;
                    }
                    //else continue
                    System.out.print("Enter book year : ");
                    int yearTemp = sc.nextInt();
                    //generate a new id for book
                    Book[] booksTemp2 = Book.readDetails("BookDataBase.bin");
                    TreeSet<Integer> treesetTemp = new TreeSet<>();
                    for (Book b : booksTemp2) treesetTemp.add(Integer.parseInt(b.id));
                    String idTemp = Integer.toString(treesetTemp.last()+1);
                    //create new book object, and add it to book database
                    Book.saveDetails(new Book(nameTemp, idTemp, isbnTemp, authorTemp, yearTemp, "Available", -1));
                    //display message
                    System.out.println("Thanks for your donation !\n\n");
                    break;

                default:
                    System.out.println("Invalid entry");
                    break;
            }
        }while (option!=0);
        System.out.println("Thank you for using our Library!");
        sc.close();
    }
    
}
