package Demo;

/**
 * Created by Paser on 2019/2/14.
 */
public class Main {
    public static void main(String[] args) {
        Worker worker=new Worker();
        Thread thread=new Thread(worker);
        thread.start();
    }
}
