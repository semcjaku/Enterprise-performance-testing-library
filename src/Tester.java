import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Tester {
    public enum TestStrategy{processor,clock,query}

    protected List<MethodTestWrapper> methodsToTest;
    protected List<String> testResults;
    protected Class classToTest;
    protected Object instanceOfClass;
    protected Connection dbConnector;

    public Tester(Object userClass, Object[] methodsParameters) {
        //reset fields
        testResults = new ArrayList<>();
        methodsToTest = new ArrayList<>();
        dbConnector = null;
        testResults = null;
        try {
            classToTest = userClass.getClass();
            instanceOfClass = userClass;

            //check annotations and fill methodsToTest
            Method[] classMethods = classToTest.getDeclaredMethods();
            Annotation[] methodsAnnotations = new Annotation[classMethods.length];
            for(int i=0;i<classMethods.length;i++) {
                methodsAnnotations[i] = classMethods[i].getDeclaredAnnotation(TestMethod.class);
            }

            for(int i=0;i<methodsAnnotations.length;i++) {
                if(methodsAnnotations[i] instanceof TestMethod) {
                    //for each element in array of strategies build wrapper and insert into methodsToTest
                    for(TestStrategy str : ((TestMethod)methodsAnnotations[i]).testedValue()) {
                        MethodTestWrapper wrap = new MethodTestWrapper();
                        wrap.m = classMethods[i];
                        wrap.strategy = str;

                        int[] paramIdx = ((TestMethod)methodsAnnotations[i]).indicesOfParameters();
                        wrap.parameters = new Object[paramIdx.length];
                        for(int p=0;p<paramIdx.length;p++) {
                            wrap.parameters[p] = methodsParameters[paramIdx[p]];
                        }

                        methodsToTest.add(wrap);
                    }
                }
            }
        }
        catch(Throwable e) {
            System.err.println(e.getMessage());
            e.getStackTrace();
        }
    }

    public void setDbConnector(Connection dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void performTest(){
        //for each Method in methodsToTest create function tester corresponding to TestStrategy
        List<BaseTimeTester> testerList = new ArrayList<>();
        for(MethodTestWrapper method : methodsToTest) {
            if(method.strategy.equals(TestStrategy.processor)) {
                testerList.add(new processorTimeTester(method.m,method.parameters));
            }
            else if (method.strategy.equals(TestStrategy.clock)) {
                testerList.add(new clockTimeTester(method.m,method.parameters));
            }
            else {
                testerList.add(new queryTimeTester(method.m,method.parameters,dbConnector));
            }
        }
        //for each function tester do functiontester.runTest() and add functiontester.toString() to testResults
        for(BaseTimeTester tester : testerList){
            tester.runTest();
            testResults.add(tester.toString());
        }
    }

    public void showResults(){
        //display each String from testResults
        for(String result : testResults) {
            System.out.println(result);
        }
    }

    public void saveResults(String path){
        //write testResults into file specified with path
        Path file = Paths.get(path);
        try  {
            Files.write(file, testResults, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            e.getStackTrace();
        }

    }
}
