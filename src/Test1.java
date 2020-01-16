import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;

public class Test1 {
    private int a;
    private int b;

    public Test1(int a_,int b_){
        this.a=a_;
        this.b=b_;
    }




    @TestMethod(testedValue = {Tester.TestStrategy.clock,Tester.TestStrategy.processor}, indicesOfParameters = {})
    private int Field(){
        return a*b;
    }

    public static void main(String[] args) throws InvocationTargetException, SQLException, ParseException, IllegalAccessException {
        int a=2;
        int b=4;
        Test1 instance = new Test1(a,b);
        Object[] params = {a,b};
        Tester tester1 = new Tester(instance,null);

        System.out.println("Calculating Field of a " + a + "x"+ b +" square...");

        System.out.println("Testing with nanoTime...");
        long startTime = System.nanoTime();
        instance.Field();
        long searchTime = System.nanoTime()-startTime;
        System.out.println("Done");

        System.out.println("Testing with Tester...");
        tester1.performTest();
        System.out.println("Done");

        System.out.println("Execution time according to Tester:");
        tester1.showResults();
        System.out.println("Clock execution time according to System.nanoTime: ");
        System.out.println(searchTime +" nanosecdonds");





    }
}
