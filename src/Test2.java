import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;
public class Test2 {
    public Test2(){

    }
    @TestMethod(testedValue = {Tester.TestStrategy.clock,Tester.TestStrategy.processor}, indicesOfParameters = {0})
    public void DelaySec(int time) throws InterruptedException {
        TimeUnit.SECONDS.sleep(time);
    }


    public static void main(String[] args) throws InterruptedException {

        Test2 instance = new Test2();

        int time=5;
        Object[] params = {time};
        Tester tester = new Tester(instance,params);

        System.out.println("Calculating a "+time+" second delay...");

        System.out.println("Testing with nanoTime...");
        long startTime = System.nanoTime();
        instance.DelaySec(time);
        long searchTime = System.nanoTime()-startTime;
        System.out.println("Done");

        System.out.println("Testing with Tester...");
        tester.performTest();
        System.out.println("Done");

        System.out.println("Execution time according to Tester:");
        tester.showResults();
        System.out.println("Clock execution time according to System.nanoTime: ");
        System.out.println(searchTime +" nanosecdonds");

    }

}
