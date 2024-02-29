class Task1{
    synchronized public void m1(Task2 obj){
        System.out.println("M1 in execution with "+Thread.currentThread().getName());
        obj.p2();
    }
    synchronized public void m2(){
        System.out.println("M2 Executing");
    } 
}
class Task2{
    synchronized public void p1(Task1 obj){
        System.out.println("P1 in execution with "+Thread.currentThread().getName());
        obj.m2();
    }
    synchronized public void p2(){
        System.out.println("P2 Executing");
    } 
}

class First extends Thread{
    Task1 task1;
    Task2 task2;
    public First(Task1 task1,Task2 task2){
        this.task1=task1;
        this.task2=task2;
    }
    public void run(){
        task1.m1(task2);
    }
}
class Second extends Thread{
    Task2 task2;
    Task1 task1;
    public Second(Task2 task2, Task1 task1){
        this.task2=task2;
        this.task1=task1;
    }
    public void run(){
        task2.p1(task1);
    }
}

public class EXAM8{
    public static void main(String[] args) {
        Task1 a=new Task1();
        Task2 b=new Task2();
        First t1=new First(a,b);
        Second t2=new Second(b,a);
        t1.setName("Thread-1");
        t2.setName("Thread-2");

        t1.start();
        t2.start();
}
}