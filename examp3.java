
class Task{
  public void m1(){
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
  First(Task task){
    this.task=task;
  }
  public void run(){
    task.m1();
  }
}
class Second extends Thread{
  Task task;
  Second(Task task){
    this.task=task;
  }
  public void run(){
    task.m1();
  }
}
class  Examp2{
    public static void main(String args[]){
        Task task=new Task();
        First t1 = new First(task);
        Second t2 = new Second(task);
        t1.start();
        t2.start();
}
}
