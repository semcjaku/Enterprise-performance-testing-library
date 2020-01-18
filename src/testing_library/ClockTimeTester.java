///////////////////////////////////////////////////////////////////////////////
// Title:            Enterprise Performance Testing Library
// Authors:          Jakub Semczyszyn, Piotr Walat, Daniel Rubak, Jan Zasadny
// License:          BSD; for more info see README.md file
///////////////////////////////////////////////////////////////////////////////

package testing_library;

import java.lang.reflect.Method;
import java.time.temporal.ChronoUnit;
import java.time.*;

public class ClockTimeTester implements BaseTimeTester {
    private String ticks;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;

    public ClockTimeTester(final Method m, final Object[] p, final Object obj){
        methodToTest = m;
        parameters = p;
        instanceofObject =  obj;
    }

    @Override
    public void runTest() {
       try{
           Clock baseclock = Clock.systemDefaultZone();

           methodToTest.trySetAccessible();
           Instant instantstart = baseclock.instant();
           methodToTest.invoke(instanceofObject, parameters);
           Instant instantfinish = baseclock.instant();

           String in = instantstart.toString();

           String[] inFragmented = in.split("\\.", 2);
           String[] inFragmented2 = inFragmented[0].split("\\-", 4);
           String[] inFragmented3 = inFragmented2[2].split("\\:", 4);
           String[] inFragmented4 = inFragmented3[0].split("T", 2);
           String[] nanos = inFragmented[1].split("Z",2);


           Instant result = instantfinish;
           result = result.minus(Long.parseLong(inFragmented4[0]), ChronoUnit.DAYS);
           result = result.minus(Long.parseLong(inFragmented4[1]), ChronoUnit.HOURS);
           result = result.minus(Long.parseLong(inFragmented3[1]), ChronoUnit.MINUTES);
           result = result.minus(Long.parseLong(inFragmented3[2]), ChronoUnit.SECONDS);
           result = result.minus(Long.parseLong(nanos[0]), ChronoUnit.NANOS);

           String stringresult = result.toString();
           stringresult = stringresult.substring(11);
           ticks = stringresult;

       }
        catch (Throwable e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "TestingLibrary.clockTimeTester{" + " \nclock time = " + ticks + " \nfor method *" + methodToTest + "*}\n";
    }

    public void main(final String[] args) {
        this.runTest();

    }
}
