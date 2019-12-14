import java.lang.reflect.Method;
import java.lang.reflect.Method;
import java.util.Date;
import java.time.Clock;
import java.time.*;




public class processorTimeTester implements BaseTimeTester {
    private Long ticks;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;

    //long curTicks = Clock.getTicks();



    public processorTimeTester(Method m, Object[] p, Object obj){
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
           //System.out.println(ticks);
       }
        catch (Throwable e){
            System.err.println(e);
        }
    }

    @Override
    public String toString() {
        return "processorTimeTester{" + " ticks= " + ticks + " for method " + methodToTest + "}";
    }



    public void main(String[] args) {
        this.runTest();

    }
}
