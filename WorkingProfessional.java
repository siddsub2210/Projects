public class WorkingProfessional extends Patron /*implements Operations */{
    protected int maxBooks = 3;
    protected float maxTime = 31f;
    protected final float finePerDay = 35.0f;
    protected final float DISCOUNT = 0f;

    // Constructor for Student class
    public WorkingProfessional(String n, String pn, int m, boolean p, boolean d, int n_times){
        super(n, pn, m, "WorkingProfessional", p, d,n_times);
        this.name = n;
        this.phoneNumber = pn;
        this.membershipid = m;
        this.subscriptionType = "WorkingProfessional";
        this.paymentStatus = p;
        this.isDefaulter = d;
        this.n = n_times;
        //assign the values to books[] array
        Book[] booksTemp = Book.readDetails("BookDataBase.bin");
        for (int i=0; i<booksTemp.length; i++){
            if (booksTemp[i].by == m){
                //add to books array
                this.books.add(booksTemp[i]);
            }
        }
    }

    // Method to borrow a book
    public void borrow(Book book) throws BookNotFoundException{
        Book[] booksTemp = Book.readDetails("BookDataBase.bin");
        int counter = 0;
        for (int i=0; i<booksTemp.length; i++){
            if (booksTemp[i].by == this.getMembershipid()) counter++;
        }
        if (counter>=this.maxBooks){
            System.out.println("You have already borrowed the maximum books : "+this.maxBooks+"\n\n");
            return;
        }
        Book bTemp = Book.readDetails(book.isbn, "BookDataBase.bin");
        if (bTemp==null){
            throw new BookNotFoundException();
        }
        else{
            //check book stage
            if (bTemp.stage.equals("Borrowed")){
                //book is already borrowed
                System.out.println("Book already borrowed. Feel free to borrow any other book\n\n");
            }
            else{
                System.out.println("Book is available");
                book.by = this.membershipid;
                book.stage = "Borrowed";
                //update book stage in the books database
                Book.updateBookStage(book);
                //add book to borrowed books array of student
                this.books.add(book);
                System.out.println("Book successfully borrowed!\n\n");
            }
        }
    }

    // Method to renew a book for a specified number of months
    public void renew(Book book, int months){
        try{
            if (months > this.maxTime)throw new InvalidTimeException();
        }
        catch (InvalidTimeException et1){
            System.out.println(et1.getMessage());
            this.n++;
            System.out.printf("Renew time exceeded by %d days\n", (int)(months-this.maxTime));
            this.fineAmount = (months-this.maxTime) * this.finePerDay;
            System.out.println("Fine amount to be paid is = "+this.fineAmount);
            System.out.println("This is the "+n+"th time you have missed the renewal");
            //defaulter, if n>3;
            if (n>3){
                this.isDefaulter = true;
                System.out.println("You have been blacklisted as a defaulter");
            }
        }
        Patron[] patArr = Patron.readPatronDetails("PatronDataBase.bin");
        //update defaulter status for patron
        for (int i=0; i<patArr.length; i++){
            if (patArr[i].membershipid == this.membershipid){
                patArr[i] = this;
                break;
            }
        }
        //rewrite into file to update is defaulter
        Patron.savePatronDetails(patArr);
        System.out.println(book.title+" by "+book.author+" has been succesfully renewed");
    }

    //return a book
    public void returnBook(Book book){
        // remove from books array
        this.books.remove(book);
        // set the book to available state in the books database file
        Book[] bookArr = Book.readDetails("BookDataBase.bin");
        for (int i=0; i<bookArr.length; i++){
            if (book.isbn == bookArr[i].isbn){
                bookArr[i].stage = "Available";
                bookArr[i].by = -1;
                break;
            }
        }
        //rewrite into books database
        Book.saveDetails(bookArr);
        System.out.println("The book "+book.title+" by "+book.author+" has been successfully returned\n");
    }

    // Method to get insights based on membership
    public void getInsights() {
        // Placeholder logic to get insights
        System.out.println("Insights for Working Professional: ");
        System.out.println("Name : "+this.name);
        System.out.println("ID : "+this.membershipid);
        System.out.println("Phone Number : "+this.phoneNumber);
        System.out.println("Subscription Type : "+this.subscriptionType);
        System.out.println("Payment Status : "+this.paymentStatus);
        System.out.println("Is Defaulter : "+this.isDefaulter);
        System.out.println("Books borrowed: ");
        for (Book b : this.books){
            System.out.println("\t"+b.title+" ~ "+b.author);
        }
    }
    
    //method to renew plan
    public void renewPlan(int months){
        // Assuming there is a method in the Book class to extend the due date
        if (months > this.maxTime) this.paymentStatus = false;
        if (!this.getPaymentStatus()){
            System.out.println("Go to the library counter to pay");
            System.out.println("Membership fee for working professionals is "+Patron.FEES);
            System.out.println("Total  = "+(Patron.FEES- Patron.FEES*this.DISCOUNT));
            this.paymentStatus = true;
            // then update database
            Patron[] prat = Patron.readPatronDetails("PatronDataBase.bin");
            for (int i=0; i<prat.length; i++){
                if (prat[i].getMembershipid() == this.membershipid){
                    prat[i].paymentStatus = true; break;
                }
            }
            Patron.savePatronDetails(prat);
            System.out.println("Account successfully renewed!\n\n");
        }
        else{
            System.out.println("Your account is still valid for "+(180-months)+" days\n\n");
        }
    }

    public boolean getPaymentStatus(){
        return this.paymentStatus;
    }
    public static void main(String[] args) {
        
    }
}