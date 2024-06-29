public interface Operations{
    static Book search(String author, int flag){
        Book[] bookArr = Book.readDetails("BookDataBase.bin");
        //search by wuthor
        for (Book b : bookArr){
            if (b.author.equals(author)){
                return b;
            }
        }
        return null;
    }
    
    static Book search(long isbn){
        Book[] bookArr = Book.readDetails("BookDataBase.bin");
        //search by isbn
        for (Book b : bookArr){
            if (b.isbn == isbn){
                return b;
            }
        }
        return null;
    }
    
    static Book search(String title){
        Book[] bookArr = Book.readDetails("BookDataBase.bin");
        //search by title
        for (Book b : bookArr){
            if (b.title.equals(title)){
                return b;
            }
        }
        return null;
    }

    void renewPlan(int months);
    void returnBook(Book book);
    boolean getPaymentStatus();
    void borrow (Book b) throws BookNotFoundException;
    void renew (Book b, int months);
    
}