
class Test{
    synchronized void syc(int n){
          for(int i=1;i<=10;i++){
            System.out.println(n*i);
          }
          try{
            Thread.sleep(1000);
          }
          catch(Exception e){
            System.out.println("exception...");
          }
    }
}
/**
 * runnableinterface
 */
public class Mythread extends Thread {
    int t;
    mythread (int t){
        this.t
    }
    t.syc(5);
    
}
public class main{
  public static void main(String[] args) {
    Test obj=new Test();
    Mythread t1=new Mythread(obj);
    t1.start();
  }
    
}