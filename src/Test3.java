import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class Test3 {

    public Test3(){}
    @TestMethod(testedValue = {Tester.TestStrategy.clock}, indicesOfParameters = {})
    public void PublicMethod() throws InterruptedException {
        System.out.println("I'm in the public method");
        TimeUnit.SECONDS.sleep(1);
    }

    @TestMethod(testedValue = {Tester.TestStrategy.clock}, indicesOfParameters = {})
    private void PrivateMethod()  throws InterruptedException{
        System.out.println("I'm in the protected method");
        TimeUnit.SECONDS.sleep(2);
    }

    @TestMethod(testedValue = {Tester.TestStrategy.clock}, indicesOfParameters = {})
    protected void ProtectedMethod()  throws InterruptedException{
        System.out.println("I'm in the private method");
        TimeUnit.SECONDS.sleep(3);
    }

    public static void main(String[] args) throws InvocationTargetException, SQLException, ParseException, IllegalAccessException {

        Test3 instance = new Test3();



        Tester tester = new Tester(instance,null);

        System.out.println("Trying to access methods..");


        tester.performTest();

        tester.showResults();




    }


}
