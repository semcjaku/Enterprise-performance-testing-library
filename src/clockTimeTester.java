import java.lang.reflect.Method;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.Clock;
import java.time.*;

public class clockTimeTester implements BaseTimeTester {
    private Long ticks;
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
           Instant instantstart = baseclock.instant();
           methodToTest.invoke(instanceofObject, parameters);
           Instant instantfinish = baseclock.instant();

           System.out.println(instantstart);
           System.out.println(instantfinish);

           String in = instantstart.toString();
           String out = instantfinish.toString();
           String[] in_fragmented = in.split("\\.", 2);
           String[] out_fragmented = out.split("\\.", 2);
           System.out.println("to ->>> " + in_fragmented[1] + "\n---->>> " + out_fragmented[1]);

           //System.out.println(in + out);
       }
        catch (Throwable e){
            System.err.println(e);
        }
    }

    @Override
    public String toString() {
        return "processorTimeTester{" + " ticks = " + ticks + " for method " + methodToTest + "}";
    }



    public void main(String[] args) {
        this.runTest();

    }
}
