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



<<<<<<< Updated upstream
    @TestMethod(testedValue = {Tester.TestStrategy.query, Tester.TestStrategy.clock, Tester.TestStrategy.processor}, indicesOfParameters = {})
    public static void printOne() {
=======
    @TestMethod(testedValue = {Tester.TestStrategy.query,Tester.TestStrategy.clock,Tester.TestStrategy.processor}, indicesOfParameters = {})
    private static void printOne() {
>>>>>>> Stashed changes
        System.out.println("Hello World");
    }

    public void printTwo() {
        printOne();
        printOne();
    }

    @TestMethod(testedValue = {Tester.TestStrategy.clock,Tester.TestStrategy.processor}, indicesOfParameters = {0,1})
    public static void printSum(int a,int b) {
        System.out.println(a+b);
    }

}
