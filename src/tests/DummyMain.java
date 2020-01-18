package tests;

import testing_library.TestMethod;
import testing_library.Tester;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;

public class DummyMain {
    public static void main(String[] args) throws InvocationTargetException, SQLException, ParseException, IllegalAccessException {
        DummyMain instance = new DummyMain();
        Object[] params = {1,2};
        Tester testthis = new Tester(instance,params);
        testthis.performTest();
        testthis.showResults();
        testthis.saveResults("C:/Temp/nowy.txt");


    }


    @TestMethod(testedValue = {Tester.TestStrategy.QUERY, Tester.TestStrategy.CLOCK, Tester.TestStrategy.PROCESSOR}, indicesOfParameters = {})
    private static void printOne() {
        System.out.println("Hello World");
    }

    public void printTwo() {
        printOne();
        printOne();
    }

    @TestMethod(testedValue = {Tester.TestStrategy.CLOCK,Tester.TestStrategy.PROCESSOR}, indicesOfParameters = {0,1})
    public static void printSum(int a,int b) {
        System.out.println(a+b);
    }

}
