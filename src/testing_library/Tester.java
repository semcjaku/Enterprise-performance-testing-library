///////////////////////////////////////////////////////////////////////////////
// Title:            Enterprise Performance Testing Library
// Authors:          Jakub Semczyszyn, Piotr Walat, Daniel Rubak, Jan Zasadny
// License:          BSD; for more info see README.md file
///////////////////////////////////////////////////////////////////////////////

package testing_library;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Tester {
    public enum TestStrategy{PROCESSOR, CLOCK, QUERY}

    protected List<MethodTestWrapper> methodsToTest;
    protected List<String> testResults;
    protected Class classToTest;
    protected Object instanceOfClass;

    public Tester(final Object userClass, final Object[] methodsParameters) {
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
                        if(str == TestStrategy.QUERY)
                        {
                            throw new IllegalArgumentException("Connection to database not provided");
                        }
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
        }
    }

    public Tester(final Object userClass, final Object[] methodsParameters, final Connection[] dbConnectors) {
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
                        if(str == TestStrategy.QUERY)
                        {
                            int connIdx = ((TestMethod)methodsAnnotations[i]).indexOfConnector();
                            wrap.dbc = dbConnectors[connIdx];
                            wrap.user = ((TestMethod)methodsAnnotations[i]).dbUser();
                        }
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
        }
    }

    private List<BaseTimeTester> generateTesters() {
        List<BaseTimeTester> testerList = new ArrayList<>();
        for(MethodTestWrapper method : methodsToTest) {
            if(method.strategy.equals(TestStrategy.PROCESSOR)) {
                testerList.add(new ProcessorTimeTester(method.m,method.parameters,instanceOfClass));
            }
            else if (method.strategy.equals(TestStrategy.CLOCK)) {
                testerList.add(new ClockTimeTester(method.m,method.parameters,instanceOfClass));
            }
            else {
                testerList.add(new QueryTimeTester(method.m,method.parameters,method.dbc,method.user,instanceOfClass));
            }
        }
        return testerList;
    }

    public void performTest() throws SQLException, IllegalAccessException, ParseException, InvocationTargetException {
        //for each Method in methodsToTest create function tester corresponding to TestStrategy
        testResults = new ArrayList<>();
        List<BaseTimeTester> testerList = this.generateTesters();
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

    public void saveResults(final String path){
        //write testResults into file specified with path
        File file = new File(path);
        Path filepath = file.toPath();
        try  {
            if(!file.exists()) {
                file.createNewFile();
            }
            Files.write(filepath, testResults, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
