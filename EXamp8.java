class A{

    synchronized  public void m1(B obj) {
     try{
       for(int i=1; i<=5; i++){
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(1000);
        obj.p1();
       }
        
     }
     catch(Exception e){
       e.printStackTrace();
     }
    }
    synchronized  public void m2() {
     try{
       for(int i=1; i<=5; i++){
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(1000);
       }
     }
     catch(Exception e){
       e.printStackTrace();
     }
}
}
class B{
    synchronized  public void p1( A obj1) {
     try{
       for(int i=1; i<=5; i++){
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(1000);
       }
        obj1.m1();
     }
     catch(Exception e){
       e.printStackTrace();
     }
    }
}
synchronized  public void p2( ) {
     try{
       for(int i=1; i<=5; i++){
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(1000);
       }
     }
     catch(Exception e){
       e.printStackTrace();
     }
    }
class First extends Thread{
    //Task task=new Task();
    B obj;
     //B obj= new B();
    First (B obj){
        this. obj=obj;
    }
      public void run(){
         B obj=new B();
        obj.m1(obj);
      }
}
class second extends Thread{
    A obj1;
    
    second (A obj1){
        this. obj1=obj1;
    }
      public void run(){
        A obj1=new A();
        obj1.p1(obj1);
      }
}
 public class EXamp8 {
         public static void main(String[] args) {

            A obj1=new A();
            B obj=new B();
            First t1=new First(obj);
            second t2=new second(obj1);
            t1.setName("m1...");
            t1.setName("m2...");

            t1.start();
            t2.start();
         }
 }

