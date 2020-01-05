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

    //long curTicks = Clock.getTicks();



    public clockTimeTester(Method m, Object[] p, Object obj){
        methodToTest = m;
        parameters = p;
        instanceofObject =  obj;
    }

    @Override
    public void runTest() {
       try{
           Clock baseclock = Clock.systemDefaultZone();

           //measuring time in clock
           methodToTest.trySetAccessible();
           Instant instantstart = baseclock.instant();
           methodToTest.invoke(instanceofObject, parameters);
           Instant instantfinish = baseclock.instant();
           // ^^^^^^^^^^^^^^^^^^^^^^^^^^
           // prints
           //System.out.println(instantstart);
           //System.out.println(instantfinish);

           // changing to string to use regex
           String in = instantstart.toString();
           String out = instantfinish.toString();

           // regex spliting to use result.minus later
           String[] in_fragmented = in.split("\\.", 2);
           String[] out_fragmented = out.split("\\.", 2);
           String[] in_fragmented2 = in_fragmented[0].split("\\-", 4);
           String[] in_fragmented3 = in_fragmented2[2].split("\\:", 4);
           String[] in_fragmented4 = in_fragmented3[0].split("T", 2);
           String[] nanos = in_fragmented[1].split("Z",2);


           // testing the correct regex spliting
           /**for (String in_frag2:in_fragmented2){

               System.out.println(in_frag2 + " 2" );
           }
           for (String in_frag3:in_fragmented3){

               System.out.println(in_frag3 + " 3" );
           }
           for (String in_frag4:in_fragmented4){

               System.out.println(in_frag4 + " 4" );
           }
           System.out.println(nanos[0] + " 1 " );
            */

           Instant result = instantfinish;
           //result = result.minus(Integer.parseInt(in_fragmented2[0]), ChronoUnit.YEARS);
           //result = result.minus(Long.parseLong(in_fragmented2[1]), ChronoUnit.MONTHS);
           result = result.minus(Long.parseLong(in_fragmented4[0]), ChronoUnit.DAYS);
           result = result.minus(Long.parseLong(in_fragmented4[1]), ChronoUnit.HOURS);
           result = result.minus(Long.parseLong(in_fragmented3[1]), ChronoUnit.MINUTES);
           result = result.minus(Long.parseLong(in_fragmented3[2]), ChronoUnit.SECONDS);
           result = result.minus(Long.parseLong(nanos[0]), ChronoUnit.NANOS);
           //System.out.println(result);
           String stringresult = result.toString();
           stringresult = stringresult.substring(11);
           ticks = stringresult;



           //System.out.println(in + out);
       }
        catch (Throwable e){
            System.err.println(e);
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
