import java.util.*;
import java.io.*;

public class Book{
    public String title;
    public String id;
    public long isbn;
    public String author;
    public int year;
    public String stage;
    public int by;

    public Book(){}
    public Book(String t, String i, long isbn, String a, int y, String s, int n){
        this.title = t;
        this.id = i;
        this.isbn = isbn;
        this.author = a;
        this.year = y;
        this.stage = s;
        this.by = n;
    }
    
    public static void saveDetails(Book b){
        //text file writing
        File file = new File("BookDataBase.txt");
        try (var fout = new FileOutputStream(file, true)){
            // \n is already printed in last file output
            //convert the data into bytes
            byte[] bytes;
            bytes = (b.title+","+b.id+","+b.isbn+","+b.author+","+b.year+","+b.stage+","+b.by+"\n").getBytes();
            fout.write(bytes);

        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        //binary file writing
        file = new File("BookDataBase.bin");
        try (var dout = new DataOutputStream(new FileOutputStream(file, true))){
            dout.writeUTF(b.title);
            dout.writeUTF(b.id);
            dout.writeLong(b.isbn);
            dout.writeUTF(b.author);
            dout.writeInt(b.year);
            dout.writeUTF(b.stage);
            dout.writeInt(b.by);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
    
    public static void saveDetails(Book[] arr){
        //text file writing
        File file = new File("BookDataBase.txt");
        try (var fout = new FileOutputStream(file)){
            // \n is already printed in last file output
            //convert the data into bytes
            byte[] bytes;
            for (int i=0; i<arr.length; i++){
                Book b = arr[i];
                bytes = (b.title+","+b.id+","+b.isbn+","+b.author+","+b.year+","+b.stage+","+b.by+"\n").getBytes();
                fout.write(bytes);
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        //binary file writing
        file = new File("BookDataBase.bin");
        try (var dout = new DataOutputStream(new FileOutputStream(file))){
            for (int i=0; i<arr.length; i++){
                Book b = arr[i];
                dout.writeUTF(b.title);
                dout.writeUTF(b.id);
                dout.writeLong(b.isbn);
                dout.writeUTF(b.author);
                dout.writeInt(b.year);
                dout.writeUTF(b.stage);
                dout.writeInt(b.by);
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
    
    //by default, we read from binary file only. but for readability, we also have a text file
    public static Book[] readDetails(String filename){
        File file = new File(filename);
        //for validation purposes
        if (file.length()==0) return null;

        List<Book> arr = new ArrayList<>();

        try(DataInputStream din = new DataInputStream(new FileInputStream(file))){
            while(true){
                Book b = new Book();
                b.title = din.readUTF();
                b.id = din.readUTF();
                b.isbn = din.readLong();
                b.author = din.readUTF();
                b.year = din.readInt();
                b.stage = din.readUTF();
                b.by= din.readInt();
                arr.add(b);
            }
        }catch (EOFException exc){
          //System.out.println();
        }
        catch(IOException e){
            //System.out.println(e.getMessage());
        }
        Book[] new_arr = new Book[arr.size()];
        for (int i=0; i<arr.size(); i++){
            new_arr[i] = arr.get(i);
        }
        return new_arr;
    }

    public static Book readDetails(long isbn, String filename){
        File file = new File(filename);
        //function returns null if book not found
        Book b = null;
        try(DataInputStream din = new DataInputStream(new FileInputStream(file))){
            while(true){
                String title = din.readUTF();
                String id = din.readUTF();
                long isbn_temp = din.readLong();
                String author = din.readUTF();
                int year = din.readInt();
                String stage = din.readUTF();
                int n = din.readInt();
                if (isbn == isbn_temp){
                    b = new Book(title, id, isbn_temp, author, year, stage, n);
                    break;
                }
            }
        }catch (EOFException exc){
          //System.out.println();
        }
        catch(IOException e){
            //System.out.println(e.getMessage());
        }
        return b;

    }
    
    public static void updateBookStage(Book book){
        Book[] books_temp = readDetails("BookDataBase.bin");
        for (int i=0; i<books_temp.length; i++){
            if (books_temp[i].isbn == book.isbn){
                books_temp[i] = book;
                break;
            }
        }
        saveDetails(books_temp);
    }

    // public static void main(String[] args) {
    //     Book b1 = new Book("The Hunger Games", "1000",10000,"Suzanne Collins", 9780, "Available", -1);
    //   Book b2 = new Book("Harry Potter", "1001",10001,"JK Rowling", 2003, "Available", -1);
    //   Book b3 = new Book("The Jungle Book", "1002",10002,"Rudyard Kipling", 1993, "Available", -1);
    //     Book b4 = new Book("A Tale of Two Cities", "1003",10003,"Charles Dickens", 1859, "Available", -1);
    //   Book b5 = new Book("The Hobbit", "1004",10004,"J. R. R. Tolkien", 2004, "Available", -1);
    //   Book b6 = new Book("The Da Vinci Code", "1005",10005,"Dan Brown", 1991, "Available", -1);
    //   Book[] b = new Book[6];
    //   b[0] = b1; b[1] = b2; b[2] = b3; b[3] = b4; b[4] = b5; b[5] = b6;
    //   saveDetails(b);
    // }
}   