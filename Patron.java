import java.util.*;
import java.io.*;


abstract class Patron implements Operations{
    protected final static float FEES = 5000f;
    protected String name;
    protected String phoneNumber;
    protected int membershipid;
    protected int maxBooks;
    protected float maxTime;
    protected List<Book> books=new ArrayList<>();
    protected float fineAmount;
    protected String subscriptionType;
    protected boolean paymentStatus;
    protected int memberhipCharges;
    protected final int CAUTIONDEPOSIT=500;
    protected boolean isDefaulter;
    protected float finePerDay;
    //number of times, book renewed out of date
    protected int n;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public int getMembershipid() {
        return membershipid;
    }
    public void setMembershipid(int membershipid) {
        this.membershipid = membershipid;
    }
    public boolean isDefaulter() {
        return isDefaulter;
    }
    public void setDefaulter(boolean isDefaulter) {
        this.isDefaulter = isDefaulter;
    }
    public int getN() {
        return n;
    }
    public void setN(int n) {
        this.n = n;
    }

    //public Patron(){}
    public Patron(String n, String pn, int m, String s, boolean p, boolean d, int n_times){
        this.name = n;
        this.phoneNumber = pn;
        this.membershipid = m;
        this.subscriptionType = s;
        this.paymentStatus = p;
        this.isDefaulter = d;
        this.n = n_times;
    }

    //static method to return a unique membership numbers
    public static int getUniqueMembershipId(String filename){
        File file = new File(filename);
        //function returns null if book not found
        TreeSet<Integer> treeset = new TreeSet<>();
        try(DataInputStream din = new DataInputStream(new FileInputStream(file))){
            while(true){
                din.readUTF();
                din.readUTF();
                treeset.add(din.readInt());
                din.readUTF();
                din.readBoolean();
                din.readBoolean();
                din.readInt();
            }
        }catch (EOFException exc){
          //System.out.println();
        }
        catch(IOException e){
            //System.out.println(e.getMessage());
        }
        return treeset.last()+1;
    }

    //get an array of membership ids
    public static TreeSet<Integer> getAllMembershipIds(String filename){
        File file = new File(filename);
        //function returns null if book not found
        //using treeset so it is automatically sorted
        TreeSet<Integer> treeset = new TreeSet<>();
        try(DataInputStream din = new DataInputStream(new FileInputStream(file))){
            while(true){
                din.readUTF();
                din.readUTF();
                treeset.add(din.readInt());
                din.readUTF();
                din.readBoolean();
                din.readBoolean();
                din.readInt();
            }
        }catch (EOFException exc){
          //System.out.println();
        }
        catch(IOException e){
            //System.out.println(e.getMessage());
        }
        return treeset;
    }


    public static void savePatronDetails(Patron pat){
        File file = new File("PatronDataBase.txt");
        try(FileOutputStream fout = new FileOutputStream(file, true)){
            byte[] bytes;
            bytes = (pat.name+","+pat.phoneNumber+","+pat.membershipid+","+pat.subscriptionType+","+pat.paymentStatus+","+pat.isDefaulter+","+pat.n+"\n").getBytes();
            fout.write(bytes);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        //binary file writing
        file = new File("PatronDataBase.bin");
        try (var dout = new DataOutputStream(new FileOutputStream(file, true))){
            dout.writeUTF(pat.name);
            dout.writeUTF(pat.phoneNumber);
            dout.writeInt(pat.membershipid);
            dout.writeUTF(pat.subscriptionType);
            dout.writeBoolean(pat.paymentStatus);
            dout.writeBoolean(pat.isDefaulter);
            dout.writeInt(pat.n);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void savePatronDetails(Patron[] pat){
        File file = new File("PatronDataBase.txt");
        try(FileOutputStream fout = new FileOutputStream(file)){
            byte[] bytes;
            for (int i=0; i<pat.length; i++){
                bytes = (pat[i].name+","+pat[i].phoneNumber+","+pat[i].membershipid+","+pat[i].subscriptionType+","+pat[i].paymentStatus+","+pat[i].isDefaulter+","+pat[i].n+"\n").getBytes();
                fout.write(bytes);
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        //binary file writing
        file = new File("PatronDataBase.bin");
        try (var dout = new DataOutputStream(new FileOutputStream(file))){
            for (int i=0; i<pat.length; i++){
                dout.writeUTF(pat[i].name);
                dout.writeUTF(pat[i].phoneNumber);
                dout.writeInt(pat[i].membershipid);
                dout.writeUTF(pat[i].subscriptionType);
                dout.writeBoolean(pat[i].paymentStatus);
                dout.writeBoolean(pat[i].isDefaulter);
                dout.writeInt(pat[i].n);
            } 
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static Patron readPatronDetails(int membershipid, String filename){
        File file = new File(filename);
        //function returns null if book not found
        Patron b = null;
        try(DataInputStream din = new DataInputStream(new FileInputStream(file))){
            while(true){
                String name = din.readUTF();
                String phoneNumber = din.readUTF();
                int membershipid_temp = din.readInt();
                String subscriptionType = din.readUTF();
                boolean paymentStatus = din.readBoolean();
                boolean isDefaulter = din.readBoolean();
                int n_times = din.readInt();
                if (membershipid == membershipid_temp){
                    if (subscriptionType.equals("Student")){
                        b = new Student(name,phoneNumber, membershipid, paymentStatus, isDefaulter, n_times);
                        break;
                    }
                    else{
                        b = new WorkingProfessional(name,phoneNumber, membershipid, paymentStatus, isDefaulter, n_times);
                        break;
                    }
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

    public static Patron[] readPatronDetails(String filename){
        File file = new File(filename);
        //function returns null if book not found
        ArrayList<Patron> l = new ArrayList<>();
        try(DataInputStream din = new DataInputStream(new FileInputStream(file))){
            while(true){
                String name = din.readUTF();
                String phoneNumber = din.readUTF();
                int membershipid_temp = din.readInt();
                String subscriptionType = din.readUTF();
                boolean paymentStatus = din.readBoolean();
                boolean isDefaulter = din.readBoolean();
                int n_times = din.readInt();
                if (subscriptionType.equals("Student")){
                    l.add(new Student(name,phoneNumber, membershipid_temp, paymentStatus, isDefaulter, n_times));
                }
                else{
                    l.add(new WorkingProfessional(name,phoneNumber, membershipid_temp, paymentStatus, isDefaulter, n_times));
                }
            }
        }catch (EOFException exc){
          //System.out.println();
        }
        catch(IOException e){
            //System.out.println(e.getMessage());
        }
        Patron[] arr = new Patron[l.size()];
        for (int i=0; i<arr.length; i++){
            arr[i] = l.get(i);
        }
        return arr;
    }

    public static  void getDefaulters(String filename){
        Scanner sc = new Scanner (System.in);
       File file = new File(filename);
        //function returns null if book not found
        List<Patron> l = new ArrayList<>();
        try(DataInputStream din = new DataInputStream(new FileInputStream(file))){
            while(true){
                String name = din.readUTF();
                String phoneNumber = din.readUTF();
                int membershipid_temp = din.readInt();
                String subscriptionType = din.readUTF();
                boolean paymentStatus = din.readBoolean();
                boolean isDefaulter = din.readBoolean();
                int n_times = din.readInt();
                if (isDefaulter){
                    Patron pobj;
                    if (subscriptionType.equals("Student")){
                        pobj = new Student(name, phoneNumber, membershipid_temp, paymentStatus, isDefaulter, n_times);
                        l.add(pobj);
                    }
                    else{
                        pobj = new WorkingProfessional(name, phoneNumber, membershipid_temp, paymentStatus, isDefaulter, n_times);
                        l.add(pobj);
                    }
                }
            }
        }catch (EOFException exc){
          //System.out.println();
        }
        catch(IOException e){
            //System.out.println(e.getMessage());
        }
        System.out.print("Do you want to view the defaulters soeted based on 'name', 'membership id' or number of times late renewal('n') : ");
        String response = sc.nextLine();
        if (response.equalsIgnoreCase("name")){
            Collections.sort(l, new PatronComparator_Name());
        }
        else if (response.equalsIgnoreCase("membership id")){
            Collections.sort(l, new PatronComparator_MembershipId());
        }
        else Collections.sort(l, new PatronComparator_N());
        //aftter sorting, print the people
        System.out.println("\n\nDisplaying Defaulters:\n");
        System.out.println("________________________________________________");
        System.out.println("     NAME      "+"|"+" PHONE NUMBER  "+"|"+"      ID       "+"|");
        System.out.println("________________________________________________");
        for (int i=0; i<l.size(); i++){
            System.out.printf("%-15s|", l.get(i).name);
            System.out.printf("   %s  |", l.get(i).phoneNumber);
            System.out.printf("%-15s|\n", l.get(i).membershipid);
        }
        System.out.println("________________________________________________\n\n");
    }

    public abstract void getInsights();
    public abstract void renewPlan(int months);

        public static void main(String[] args) {
            
        }
}

class PatronComparator_N implements Comparator<Patron>{
    public int compare(Patron p1, Patron p2){
        if (p1.getN()> p2.getN()) return 1;
        else return -1;
    }
}
class PatronComparator_Name implements Comparator<Patron>{
    public int compare(Patron p1, Patron p2){
        return p1.getName().compareTo(p2.getName());
    }
}
class PatronComparator_MembershipId implements Comparator<Patron>{
    public int compare(Patron p1, Patron p2){
        if (p1.getMembershipid()> p2.getMembershipid()) return 1;
        else return -1;
    }
}