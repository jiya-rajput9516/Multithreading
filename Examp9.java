import java.net.SocketTimeoutException;
import java.util.*;
class  Test extends Thread {
    public void run(){
        for(int i=1;i<=5;i++){
        System.out.println("thread task");
    }
}
}
class Test2 extends Thread{
    public void run(){
        for(int i=1;i<=5;i++){
        System.out.println("thread2 task");
    }
}
}
class main{
     public static void main(String[] args) {
        Test t=new Test();
        t.start();
        Test2 t2=new Test2();
        t2.start();
     }
}
