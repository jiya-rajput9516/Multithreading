
//*************BANK*********************
import java.io.*;
import java.util.Scanner;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;


class DataStore implements Serializable {
    private static final long serialVersionUID = 10L;
    private String cName, accNum, mobNum, gmlId;
    private double amt;

    public DataStore(String cName, String accNum, String mobNum, String gmlId, double amt) {
        this.cName = cName;
        this.accNum = accNum;
        this.mobNum = mobNum;
        this.gmlId = gmlId;
        this.amt = amt;
    }

    public String toString() {
        return String.format("\t%-20s %-20s %-20s %-20s %.3f\n", cName, accNum, mobNum, gmlId, amt);
    }
}

//-------------------------------------------------------------------
class BankProj {
  private static final String RESET = "\u001B[0m";
  private static final String YELLOW = "\u001B[33m";
  private static final String RED = "\u001B[31m";
  private static final String GREEN = "\u001B[32m";
  private static final String BLUE = "\u001B[34m"; 
  private String cName,accNum,mobNum,gmlId;
  private double amt;
  
  private static int index=-1;
  public BankProj(){
    
  }
  //obj[index]=new BankProj(cN,cAN,mobN,gM,amt);
  public BankProj(String cName,String accNum,String mobNum,String gmlId,double amt){
    this.cName=cName;
    this.accNum=accNum;
    this.mobNum=mobNum;
    this.gmlId=gmlId;
    this.amt=amt;
  }
  
  public void setCName(String cName){
    this.cName=cName;
  }
  public void setMobNo(String mobNum){
    this.mobNum=mobNum;
  }
   public void setCG(String gmlId){
    this.gmlId=gmlId;
  }
  
   public void setCAll(String cName,String mobNum,String gmlId){
    this.cName=cName;
    this.mobNum=mobNum;
    this.gmlId=gmlId;
  }
  //0 Reading the Object from the file
      static void read(){
        System.out.println("\t\t************\n");
        System.out.println("\n\n\tCustomer Name\t    AccNo.\t\t  MobileNo.     \t Gmail\t\t     Amount    ");
        try {
            FileInputStream fis =new FileInputStream("BankData.txt");
            ObjectInputStream ois =new ObjectInputStream(fis);
            try {
                while(true){
                 DataStore objD = (DataStore)ois.readObject();
                 System.out.println(objD.toString());
                }
            } 
            catch (EOFException e) {
               System.out.println("\tEnd of file reached, stop reading");
            }
            ois.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("An Error Occured in read operation");
        }
      }
  
  
  //1 Application
  private static final String password = "Abc123";
  static void application( BankProj obj[]){
    Scanner sc = new Scanner(System.in);
    while(true){
    System.out.println("\n\n\t\t\t\t\t***WELCOME TO BANK APPLICATION***\n\n");
    System.out.println("\t-----------------\t\t\t-------------------\t\t\t-----------------");
    System.out.println(YELLOW+"\t 1) Admin Login   \t\t\t 2) Customer Login   \t\t\t   0) Exit  "+RESET);
    System.out.println("\t-----------------\t\t\t-------------------\t\t\t-----------------\n");
    System.out.print("\t\tEnter the choice: ");
    int ch=sc.nextInt();
    switch(ch){
      case 1:
      for (int i = 0; i <= 5; i++) {
       System.out.print("\t\tEnter the password: ");
       String adminPassword = sc.next();
       if (password.equals(adminPassword)) {
         System.out.println("\n\t\t------------------------YOU ARE SUCCESSFULLY LOGIN---------------------------");
         break;
      } 
      else if (i>=5) {       System.out.println("\t\t---------------------------------------------------------");
        System.out.println("\t\tYou've attempted multiple times. Please try again later.");
        System.out.println("---------------------------------------------------------");
         break;
      } 
      else{
        System.out.println(RED+"\t\tWARNING: WRONG PASSWORD"+RESET);
      }
     } 
      admin(obj);
     break;
      
      case 2:
       customer(obj); 
      break;
      
      case 0:
        read();
        System.out.println(GREEN+"\t\t---SUCCESSFULLY EXIT---"+RESET);
        System.out.println(GREEN+"\t\t---THANKS FOR VISIT---"+RESET);
        System.exit(0);
      break;
      
      default:
        System.out.println(RED+"\t\t---Invalid choise---"+RESET);
    }
   }
  }
  //2------------------- Admin page------------------------
  static void admin( BankProj obj[]){
    Scanner sc = new Scanner(System.in);
    while(true){
     System.out.println("\n\n\t\t\t\t\t***WELCOME TO ADMIN PAGE***\n\n");
     System.out.println("\t-----------------\t-----------------\t-----------------\t-----------------\t-----------------");
     System.out.println(YELLOW+"\t1)Create Customer\t2)View Account\t\t3)Remove/Block\t\t4)View ALL \t\t0)Exit/Logout"+RESET);
     System.out.println("\t-----------------\t-----------------\t-----------------\t-----------------\t-----------------");
     System.out.print("\t\tEnter the choice: ");
     int ch=sc.nextInt();
     switch(ch){
      case 1:
        System.out.println("\t---------------------------------------------------");
        System.out.println(YELLOW+"\tOpening blance is 5000"+RESET);
        System.out.println("\t---------------------------------------------------");
        System.out.print("\tEnter the customer name: ");
        sc.nextLine();
        String cN=sc.nextLine();
        System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);
        System.out.print("\tCustomer account number: ");
        String cAN=sc.nextLine();
        System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);
        System.out.print("\tEnter the mobile number: ");
        String mobN=sc.nextLine();
        System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);
        System.out.print("\tEnter Customer GmailID: ");
        String gM=sc.nextLine();
        System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);
        System.out.print("\tEnter the minimum account opening amount: ");
        double amt=sc.nextDouble();
        System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);      
        if(amt<5000){
          System.out.println(RED+"\t---------------- SORRY I DONT OPEN YOUR ACCOUNT PLESE DEPOSITE MINIMUM AMMOUNT ATLEST 5000----------------"+RESET);
          continue;
        }
   else{
    // System.out.println(index);
    ++index;
    // System.out.println(index);
    obj[index]=new BankProj(cN,cAN,mobN,gM,amt);
    //Data stored in file
    try{
/*     File f = new File("BankData.txt");
     if(!f.exists())
       f.createNewFile();
     FileOutputStream fos = new FileOutputStream(f,true);
     ObjectOutputStream oos = new ObjectOutputStream(fos);
     DataStore dst = new DataStore(cN,cAN,mobN,gM,amt);
     oos.writeObject(dst);
     oos.close();
     System.out.println("\t\t----------Data stored----------");*/
     Path path = Paths.get("BankData.txt");
     appendToFile(path, ""+cN+"  "+cAN+"  "+mobN+"  "+gM+"  "+amt+"\n");
    }
    catch(IOException e){
      e.printStackTrace();
      System.out.println("An Error Occured in write operation");
    }
    System.out.println(YELLOW+"\t----------------SUCCESSFULLY  YOUR ACCOUNT IS----------------"+RESET);
   }
   break;
      case 2:
      String acc;
      boolean flag=true;
       System.out.println("\n");
       if(index==-1)
         System.out.println("\tNo Account Present");
       else{ 
        System.out.println("\t   View Account ");
        System.out.println(YELLOW+"\t-----------------------------------------------------------------"+RESET);
        System.out.print("\tenter account number: ");
        sc.nextLine();
        acc=sc.nextLine();
        flag=true;
        for(int i=0;i<=index;i++){
          if(acc.equals(obj[i].accNum)){
          flag=false;
          System.out.println("\n\n\tCustomer Name\t    AccNo.\t\t  MobileNo.     \t Gmail\t\t     Amount    ");
           System.out.println(YELLOW+"\t------------------------------------------------------------------------------------------------------------------"+RESET);
          System.out.printf("\t%-20s %-20s %-20s %-20s %.3f\n ",obj[i].cName,obj[i].accNum,obj[i].mobNum,obj[i].gmlId,obj[i].amt);
        break;
        }
      }
      if(flag)
        System.out.println(RED+"\tNot Found"+RESET);
       }
       break;
       case 3:
        flag=true;
        int pos=0;
        if(index==-1){
          System.out.println("\tNo Account Present");
        }
         else{
           System.out.print("\tenter account number: ");
           sc.nextLine();
           acc=sc.nextLine();
           for(int i=0;i<=index;i++){
             if(acc.equals(obj[i].accNum)){
             flag=false;
           // System.out.println(YELLOW+"\t----Found----\n"+RESET);
             pos=i;
             break;
           }
         }
         if(flag)
           System.out.println(RED+"\tNot Found"+RESET);
         else{
           for(int i=pos;i<index;i++)
             obj[i]=obj[i+1];
           index=index-1;
           System.out.println(YELLOW+"\t----Account Removed /Block----\n"+RESET);	
        }
      }
       break;
     
         case 4:
           System.out.println("\n");
           if(index==-1)
             System.out.println("\tNo Account Present");
           else{ 
             System.out.println("\tCustomer Name\t    AccNo.\t\t  MobileNo.     \t Gmail\t\t     Amount    ");
         System.out.println(YELLOW+"\t------------------------------------------------------------------------------------------------------------------"+RESET);
      for(int i=0;i<=index;i++){
        System.out.printf("\t%-20s %-20s %-20s %-20s %.3f\n ",obj[i].cName,obj[i].accNum,obj[i].mobNum,obj[i].gmlId,obj[i].amt);
         System.out.println(YELLOW+"\t------------------------------------------------------------------------------------------------------------------"+RESET);
	   }
	}
        break;
      case 0:
        System.out.println(GREEN+"\t\t---SUCCESSFULLY EXIT ADMIN PAGE---"+RESET);
        System.out.println(GREEN+"\t\t---THANKS FOR VISIT---"+RESET);
      break;
      
      default:
        System.out.println(RED+"\t\t---Invalid choise---"+RESET);
    }
    if(ch==0)
      break;
   }
  }
}
//=========================================================

   

  //3------------------- Customer page---------------------------
  static void customer( BankProj obj[]){
    Scanner sc = new Scanner(System.in);
    while(true){
     System.out.println("\n\n\t\t\t\t\t***WELCOME TO CUSTOMER PAGE***\n\n");
     System.out.println("\t-----------------\t-----------------\t-----------------\t---------------------");
     System.out.println(YELLOW+"\t1)View Details\t\t2)Update Details\t3)Apply for loan\t4)View loan statement"+RESET);
     System.out.println("\t-----------------\t-----------------\t-----------------\t---------------------");
     System.out.println("\n\n\t\t-----------------\t----------------------\t\t-----------------");
     System.out.println(YELLOW+"\t\t5)Transfer Money\t6)Transaction History\t\t0)Exist/Logout"+RESET);
     System.out.println("\t\t-----------------\t----------------------\t\t-----------------");
     System.out.print("\n\n\t\tEnter the choice: ");
     int ch=sc.nextInt();
     switch(ch){
      case 1:
      String acc;
      boolean flag=true;
       System.out.println("\n");
       if(index==-1)
         System.out.println(RED+"\tNo Account Present"+RESET);
       else{ 
        System.out.println("\t   View Details ");
        System.out.println(YELLOW+"\t-----------------------------------------------------------------"+RESET);
        System.out.print("\tenter account number: ");
        sc.nextLine();
        acc=sc.nextLine();
        flag=true;
        for(int i=0;i<=index;i++){
          if(acc.equals(obj[i].accNum)){
          flag=false;
          System.out.println("\n\n\tCustomer Name\t    AccNo.\t\t  MobileNo.     \t Gmail\t\t     Amount    ");
           System.out.println(YELLOW+"\t------------------------------------------------------------------------------------------------------------------"+RESET);
          System.out.printf("\t%-20s %-20s %-20s %-20s %.3f\n ",obj[i].cName,obj[i].accNum,obj[i].mobNum,obj[i].gmlId,obj[i].amt);
        break;
        }
      }
      if(flag)
        System.out.println(RED+"\tNot Found"+RESET);
       }
       break;
       case 2:
        if(index==-1)
         System.out.println(RED+"\tNo Account Present"+RESET);
        else{
          while(true){
            System.out.println("\n\n\t\t----------Update Details----------");
            System.out.print("\tenter account number: ");
            acc=sc.next();
            flag=true;
            for(int i=0;i<=index;i++){
              if(acc.equals(obj[i].accNum)){
              flag=false;
              System.out.println("\t-----------------\t-----------------\t-----------------\t-----------------\t-----------------");
              System.out.println(YELLOW+"\t1)Customer Name\t\t2)MobileNo.\t\t3)Gmail    \t\t4)Update All \t\t0)No Change"+RESET);
              System.out.println("\t-----------------\t-----------------\t-----------------\t-----------------\t-----------------");
              System.out.print("\t\tEnter the choice: ");
              ch=sc.nextInt();
              switch(ch){
               case 1:
                System.out.print("\tEnter the customer name: ");
                sc.nextLine();
                String cN=sc.nextLine();
                System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);
                obj[i].setCName(cN);
                System.out.println(GREEN+"\t\t---Customer Name Updation---"+RESET);
                break;
               case 2:
                System.out.print("\tEnter the mobile number: ");
                String mobN=sc.next();
                System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);
                obj[i].setMobNo(mobN);
                System.out.println(GREEN+"\t\t---MobileNo. Updation---"+RESET);
                break;
               case 3:
                System.out.print("\tEnter Customer GmailID: ");
                String gM=sc.next();
                System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);           
	 	System.out.println(GREEN+"\t\t---Gmail Updation---"+RESET);      
                obj[i].setCG(gM);	   
	 	break;
	       case 4:
	  	System.out.print("\tEnter the customer name: ");
	   	sc.nextLine();
	   	cN=sc.nextLine();
                System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);
	   	System.out.print("\tEnter the mobile number: ");
                mobN=sc.next();
                System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);
	   	System.out.print("\tEnter Customer GmailID: ");
                gM=sc.next();
                System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);  
                obj[i].setCAll(cN, mobN,gM);
                System.out.println(GREEN+"\t\t---Update All Updation---"+RESET);          
                break;
               case 0:
                System.out.println(GREEN+"\t\t---No Updation ---"+RESET);
                System.out.println(GREEN+"\t\t---THANKS U---"+RESET);
                break;
      
               default:
                System.out.println(RED+"\t\t---Invalid choise---"+RESET);
               }
               
               //UPATE FILE HERE	
               	replaceLines(acc, obj[i]);
               break;
             }
           }
         
         if(flag){
           System.out.println(RED+"\tNot Found"+RESET);
           break;
         }
		  if(ch==0||ch==1||ch==2||ch==3||ch==4 )
           break;
       }
     }
      break;
    case 3:
      if(index==-1){
         System.out.println(RED+"\tNo Account Present"+RESET);
         System.out.println(RED+"\tFirst Open Account by Admin,On Admin Page"+RESET);
      }  
        else{
          while(true){
            System.out.println("\n\n\t\t----------Apply for Loan----------");
            System.out.print("\tenter account number: ");
            acc=sc.next();
            flag=true;
            for(int i=0;i<=index;i++){
              if(acc.equals(obj[i].accNum)){
              flag=false;
              System.out.println("\t-----------------\t-----------------\t-----------------");
              System.out.println(YELLOW+"\t1)Personal Loan\t\t2)Education Loant\t   0) Exit  "+RESET);
              System.out.println("\t-----------------\t-----------------\t-----------------");
              System.out.print("\t\tEnter the choice: ");
              ch=sc.nextInt();
              switch(ch){
                case 1:
                  System.out.println("\n\n\t\t\t-----------------");
                  System.out.println(YELLOW+"\t\t\t  Personal Loan"+RESET);
                  System.out.println("\t\t\t-----------------");
                  System.out.println(
                        "\t*Loan offer Starting from Min.RS 50,001/- to Min.RS 5,00,000/-\n\n\t* Interest rate Starting from Min.12.40% to Max.15.90%\n\n\t* Age between 21 t0 57 \n\n\t* Loan Repayment period from Min.18 months tp Max.36 months\n\n\t* Processing charges: 2% of the amount (Min.RS 1000/- Max.RS 10,000)\n");
                System.out.println("\t-----------------\t-----------------");
                System.out.println("\t     (1)YES  \t\t     (2)NO");
                System.out.println("\t-----------------\t-----------------");
                System.out.print("\tEnter the choice: ");
                int n1 = sc.nextInt();
                switch (n1) {
                   case 1:
                        System.out.println(YELLOW+"\tApply for loan Enter the details: "+RESET);
                        System.out.print("\tEnter the moblie number: ");
                        String mb = sc.next();
                        System.out.println(YELLOW+"\t-------------------------------------------"+RESET);
                        System.out.print("\tEnter the Adhar Number: ");
                        String ad = sc.next();
                        System.out.println(YELLOW+"\t-------------------------------------------"+RESET);
                        System.out.print("\tEnter the PIN card Number: ");
                        String pn = sc.next();
                        System.out.println(YELLOW+"\t-------------------------------------------"+RESET);
                        System.out.print("\tEnter the  age: ");
                        int ag = sc.nextInt();
                        System.out.println(YELLOW+"\t-------------------------------------------"+RESET);
                        System.out.print("\tEnter the Amount for loan: ");
                        double am = sc.nextDouble();
                        System.out.println(YELLOW+"\t-------------------------------------------"+RESET);
                        System.out.print("\tEnter the todays date: ");
                        sc.nextLine();
                        String date= sc.nextLine();
                        int Duration=sc.nextInt();
                         //obj[i]=new BankProj(cN,cAN,mobN,gM,amt);

                        if((ag>=21 && ag<=57) && (am>=50001 && am<=500000)){
                            System.out.println(GREEN+"\tLOAN IS SUCCESSFULLY PASSED... "+RESET);
                            double m=Duration*12.0;
                             double emi=(am/m);
                             System.out.println("EMI:"+emi);

                        }
                         

                        else 
                            System.out.println(RED+"\t LOAN IS FAILED... "+RESET);
                        break;
                            
                   case 2:
                        System.out.println(RED+"\tYour are Exit personal loan page.."+RESET);
                        break;

                }
                  break;
                case 2:
                  System.out.println("\n\n\t\t\t-----------------");
                  System.out.println(YELLOW+"\t\t\t  Education Loan"+RESET);
                  System.out.println("\t\t\t-----------------");
                  System.out.println(
                        "\t*Loan offer Starting from Min.RS 50,000/- to Min.RS 1Cr./-\n\n\t* Age 18 to 25 \n\n\t* Interest rate Starting from Min.10.10% to Max.12.00%\n\n\t* Loan Repayment period from Min.18 months tp Max.36 months\n\n\t* Processing charges: 2% of the amount (Min.RS 1000/- Max.RS 10,000)");
                System.out.println("\t-----------------\t-----------------");
                System.out.println("\t     (1)YES  \t\t     (2)NO");
                System.out.println("\t-----------------\t-----------------");
                System.out.print("\tEnter the choice: ");
                int n2 = sc.nextInt();
                  
                switch (n2) {

                    case 1:
                        System.out.println(YELLOW+"\tApply for loan Enter the details "+RESET);
                        System.out.print("\tEnter the moblie number: ");
                        String mb = sc.next();
                        System.out.println(YELLOW+"\t-------------------------------------------"+RESET);
                        System.out.print("\tEnter the Adhar Number: ");
                        String ad = sc.next();
                        System.out.println(YELLOW+"\t-------------------------------------------"+RESET);
                        System.out.print("\tLetter for principle: ");
                        char ch1=sc.next().charAt(0);
                        System.out.println(YELLOW+"\t-------------------------------------------"+RESET);
                        System.out.print("\tEnter the  age: ");
                        int ag = sc.nextInt();
                        System.out.println(YELLOW+"\t-------------------------------------------"+RESET);
                        System.out.print("\tEnter the Amount for loan: ");
                        double am = sc.nextDouble();

                           if((ag>=18 && ag<=25) && (am>=50000 && am<=10000000) && (ch1=='y' || ch1=='Y'))
                             System.out.println(GREEN+"\t LOAN IS SUCCESSFULLY PASSED... "+RESET);
                         else 
                            System.out.println(RED+"\tLOAN IS FAILED... "+RESET);
                        break;
                    

                         case 2:
                            System.out.println(RED+"\tYour are Exit Education loan page.."+RESET);
                            break;
                }
                  
                  break; 
                case 0:
                  System.out.println(GREEN+"\t\t---Exit from Apply for Loan Page---"+RESET);
                  System.out.println(GREEN+"\t\t---THANKS U---"+RESET);
                  break;
      
                default:
                  System.out.println(RED+"\t\t---Invalid choise---"+RESET);
             }
           break;
          }
         }
         if(ch==0||ch==1||ch==2)
           break;
         if(flag){
           System.out.println(RED+"\tNot Found"+RESET);
		   System.out.println(RED+"\tFirst open Account by Admin,On Admin page"+RESET);
           break;
         }
       }
     }
      break;
      
     case 4:
      System.out.println("\tView Loan Statement");
        if(index==-1){
         System.out.println(RED+"\n\tNo Account Present"+RESET);
        System.out.print("\tenter account number: ");
        sc.nextLine();
        acc=sc.nextLine();
        flag=true;
        for(int i=0;i<=index;i++){
          if(acc.equals(obj[i].accNum)){
          flag=false;
          System.out.println("\n\n\tCustomer Name\t    AccNo.\t\t  MobileNo.     \t Gmail\t\t     Amount    ");
           System.out.println(YELLOW+"\t------------------------------------------------------------------------------------------------------------------"+RESET);
          System.out.printf("\t%-20s %-20s %-20s %-20s %.3f\n ",obj[i].cName,obj[i].accNum,obj[i].mobNum,obj[i].gmlId,obj[i].amt);
        break;
        }
      }
      if(flag)
        System.out.println(RED+"\tNot Found"+RESET);
       }
      break;
     
     case 5:

    //   System.out.println("\n\n\t\t\t\t-----------------------------------------------------");
    //                 System.out.println(YELLOW + "\t\t\t\t\t\tTransaction History " + RESET);
    //                 System.out.println("\t\t\t\t-----------------------------------------------------\n");
    //                 customer.printTransactionHistory();
    //                 break;



      System.out.println("\tTransfer Money");
       while (true) {
      System.out.println("------------------------------------------------------");
      System.out.println(
          ">>menu<<\n1.Deposite money\n2.Withrawal money\n3.Current Amount\n 4.Transfore money\n 5.Exit");
      System.out.println("---------------------------------------------------------");
      System.out.println("Enter the your choice");
      int ch2 = sc.nextInt();
      switch (ch2) {
        case 1:
          System.out.println("Deposite Money:-");
          System.out.println("enter the Deposite money");
          double amount = sc.nextDouble();
          //obj.deposit(amount);
          break;

        case 2:
          System.out.println("Withrawl money:-");
          System.out.println("enter the Deposite money::-");
          double amount1 = sc.nextDouble();
          //obj.Withdrawal(amount1);
          break;

        case 3:
          System.out.println("-----:Current account:------");
          //obj.Currentdisplay();
          break;

          case 4:
               System.out.println("transfer money");
               System.out.println("Enter the cosutmer name");
               String cname=sc.nextLine();
                String cname1=sc.nextLine();
               System.out.println("Enter the cosutomer Accout number");
               String Caccontno=sc.nextLine();
               System.out.println("ENter the Transfer Amount");
               int Amount=sc.nextInt();
          
              //  int  Amount = sc.nextInt();
          //obj.Withdrawal(Amount);
            break;
        case 5:
          System.out.println("Thankyou you this using Application....");
          System.exit(0);
          break;
        default:
          System.out.println("Invailed choice...");
      }
    }

    
     case 6:
      System.out.println("\tTransaction History");
      break;
     
     case 0:
        System.out.println(GREEN+"\t\t---SUCCESSFULLY EXIT CUSTOMER PAGE---"+RESET);
        System.out.println(GREEN+"\t\t---THANKS FOR VISIT---"+RESET);
      break;
      
      default:
        System.out.println(RED+"\t\t---Invalid choise---"+RESET);
    }
    if(ch==0 )
      break;
   }
  }
  //4 Main function
  public static void main(String args[]) {
    Scanner sc = new Scanner(System.in);
    BankProj obj[]=new BankProj[50];   
     application(obj);
}

    // Java 7
    private static void appendToFile(Path path, String content)
				throws IOException {


        // if file not exists, create and write to it
	// otherwise append to the end of the file
        Files.write(path, content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);

    }
    
    // read file one line at a time
// replace line as you read the file and store updated lines in StringBuffer
// overwrite the file with the new lines
public static void replaceLines(String accountNumber, BankProj bankObject) {
    try {
        // input the (modified) file content to the StringBuffer "input"
        BufferedReader file = new BufferedReader(new FileReader("BankData.txt"));
        StringBuffer inputBuffer = new StringBuffer();
        String line;

        while ((line = file.readLine()) != null) {
        if(line.contains(accountNumber)){
        line = ""+bankObject.cName+"  "+bankObject.accNum+"  "+bankObject.mobNum+"  "+bankObject.gmlId+"  "+bankObject.amt+"\n";
        }
            
            inputBuffer.append(line);
            inputBuffer.append('\n');
        }
        file.close();

        // write the new string with the replaced line OVER the same file
        FileOutputStream fileOut = new FileOutputStream("BankData.txt");
        fileOut.write(inputBuffer.toString().getBytes());
        fileOut.close();

    } catch (Exception e) {
        System.out.println("Problem reading file.");
    }
}

