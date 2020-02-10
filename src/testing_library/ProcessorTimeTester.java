///////////////////////////////////////////////////////////////////////////////
// Title:            Enterprise Performance Testing Library
// Authors:          Jakub Semczyszyn, Piotr Walat, Daniel Rubak, Jan Zasadny
// License:          BSD; for more info see README.md file
///////////////////////////////////////////////////////////////////////////////

package testing_library;

import java.lang.reflect.Method;

public class ProcessorTimeTester implements BaseTimeTester {
    private Long timeNanos, timeMilis;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;

    protected ProcessorTimeTester(final Method m, final Object[] p, final Object obj){
        methodToTest = m;
        parameters = p;
        instanceofObject =  obj;
    }

    @Override
    public void runTest() {
        try{
            methodToTest.trySetAccessible();
            long startTime = System.nanoTime();
            methodToTest.invoke(instanceofObject, parameters);
            long searchTime = System.nanoTime();

            long startTime1 = System.currentTimeMillis();
            methodToTest.invoke(instanceofObject, parameters);
            long searchTime1 = System.currentTimeMillis();

            timeNanos = searchTime-startTime;
            timeMilis = searchTime1-startTime1;
        }
        catch (Throwable e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "TestingLibrary.processorTimeTester{Method: " + methodToTest.getName() + ", Execution time:\n" + timeNanos + " nanoseconds (non thread safe)" + "\n"
                + timeMilis + " miliseconds (thread safe)}\n";
    }
}
