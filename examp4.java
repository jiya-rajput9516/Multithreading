class Task{
    synchronized  public void m1() {
     try{
       for(int i=1; i<=5; i++){
        System.out.println("Hello..");
        Thread.sleep(1000);
       }
     }
     catch(Exception e){
       e.printStackTrace();
     }
    }
}
class First extends Thread{
    Task task;
    First (Task task){
        this. task=task;
    }
      public void run(){
        task.m1();
      }
}
class second extends Thread{
    Task task;
    second (Task task){
        this. task=task;
    }
      public void run(){
        task.m1();
      }
}
 public class Examp1 {
         public static void main(String[] args) {
            Task task=new Task();
            First t1=new First(task);
            second t2=new second(task);
            t1.start();
            t2.start();
         }
 }