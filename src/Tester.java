import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

public class Tester {
    private enum TestStrategy{processor,clock,query}
    private enum TestType{all,selected}

    private TestType typeOfTests;
    protected Map<Method,TestStrategy> methodsToTest;
    protected String[] testResults;
    protected Object classToTest;
    protected Connection dbConnector;

    public Tester(Object userClass) {
        //reset all fields
        //check annotations and set typeOfTests
        //check annotations and fill methodsToTest
    }

    public void performTest(){
        //for each Method in methodsToTest create function tester corresponding to TestStrategy
        //for each function tester do functiontester.runTest() and add functiontester.toString() to testResults
    }

    public void showResults(){
        //display each String from testResults
    }

    public void saveResults(String path){
        //write testResults into file specified with path
    }
}
