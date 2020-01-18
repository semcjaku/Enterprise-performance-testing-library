package tests;

import testing_library.TestMethod;
import testing_library.Tester;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class Test2 {
    public Test2(){

    }
    @TestMethod(testedValue = {Tester.TestStrategy.CLOCK,Tester.TestStrategy.PROCESSOR}, indicesOfParameters = {0})
    public void DelaySec(int time) throws InterruptedException {
        TimeUnit.SECONDS.sleep(time);
    }


    public static void main(String[] args) throws InterruptedException, InvocationTargetException, SQLException, ParseException, IllegalAccessException {

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

        System.out.println("Testing with TestingLibrary.Tester...");
        tester.performTest();
        System.out.println("Done");

        System.out.println("Execution time according to TestingLibrary.Tester:");
        tester.showResults();
        System.out.println("Clock execution time according to System.nanoTime: ");
        System.out.println(searchTime +" nanosecdonds");

    }

}
