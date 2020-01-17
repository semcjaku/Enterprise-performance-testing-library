import java.lang.reflect.Method;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.time.Clock;
import java.time.*;

public class clockTimeTester implements BaseTimeTester {
    private String ticks;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;

    public clockTimeTester(Method m, Object[] p, Object obj){
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
           String out = instantfinish.toString();

           String[] in_fragmented = in.split("\\.", 2);
           String[] out_fragmented = out.split("\\.", 2);
           String[] in_fragmented2 = in_fragmented[0].split("\\-", 4);
           String[] in_fragmented3 = in_fragmented2[2].split("\\:", 4);
           String[] in_fragmented4 = in_fragmented3[0].split("T", 2);
           String[] nanos = in_fragmented[1].split("Z",2);


           Instant result = instantfinish;
           result = result.minus(Long.parseLong(in_fragmented4[0]), ChronoUnit.DAYS);
           result = result.minus(Long.parseLong(in_fragmented4[1]), ChronoUnit.HOURS);
           result = result.minus(Long.parseLong(in_fragmented3[1]), ChronoUnit.MINUTES);
           result = result.minus(Long.parseLong(in_fragmented3[2]), ChronoUnit.SECONDS);
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
        return "clockTimeTester{" + " \nclock time = " + ticks + " \nfor method *" + methodToTest + "*}\n";
    }

    public void main(String[] args) {
        this.runTest();

    }
}
