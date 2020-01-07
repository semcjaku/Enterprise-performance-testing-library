import java.io.File;
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

    public Tester(Object userClass, Object[] methodsParameters) {
        //reset fields
        testResults = null;
        methodsToTest = new ArrayList<>();
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
                        wrap.dbc = null;
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

    public Tester(Object userClass, Object[] methodsParameters, Connection[] dbConnectors) {
        //reset fields
        testResults = null;
        methodsToTest = new ArrayList<>();
        testResults = null;
        try {
            classToTest = userClass.getClass();
            instanceOfClass = userClass;

            //check annotations and fill methodsToTest
            Method[] classMethods = classToTest.getDeclaredMethods();
            Annotation[] methodsAnnotations = new Annotation[classMethods.length];
            for(int i=0;i<classMethods.length;i++) {
                methodsAnnotations[i] = classMethods[i].getDeclaredAnnotation(TestMethod.class);
                classMethods[i].trySetAccessible();
            }

            for(int i=0;i<methodsAnnotations.length;i++) {
                if(methodsAnnotations[i] instanceof TestMethod) {
                    //for each element in array of strategies build wrapper and insert into methodsToTest
                    for(TestStrategy str : ((TestMethod)methodsAnnotations[i]).testedValue()) {
                        MethodTestWrapper wrap = new MethodTestWrapper();
                        wrap.m = classMethods[i];
                        wrap.strategy = str;

                        int[] paramIdx = ((TestMethod)methodsAnnotations[i]).indicesOfParameters();
                        int connIdx = ((TestMethod)methodsAnnotations[i]).indexOfConnector();
                        wrap.parameters = new Object[paramIdx.length];
                        for(int p=0;p<paramIdx.length;p++) {
                            wrap.parameters[p] = methodsParameters[paramIdx[p]];
                        }
                        wrap.dbc = dbConnectors[connIdx];

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

    public void performTest(){
        //for each Method in methodsToTest create function tester corresponding to TestStrategy
        testResults = new ArrayList<>();
        List<BaseTimeTester> testerList = new ArrayList<>();
        for(MethodTestWrapper method : methodsToTest) {
            if(method.strategy.equals(TestStrategy.processor)) {
                testerList.add(new processorTimeTester(method.m,method.parameters,instanceOfClass));
            }
            else if (method.strategy.equals(TestStrategy.clock)) {
                testerList.add(new clockTimeTester(method.m,method.parameters,instanceOfClass));
            }
            else {
                testerList.add(new queryTimeTester(method.m,method.parameters,method.dbc,instanceOfClass));
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
        File file = new File(path);
        Path filepath = file.toPath();
        try  {
            if(!file.exists())
                file.createNewFile();
            Files.write(filepath, testResults, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            e.getStackTrace();
        }

    }
}
