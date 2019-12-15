import java.lang.reflect.Method;

public class processorTimeTester implements BaseTimeTester {
    private Long time_nanos, time_milis;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;

    protected processorTimeTester(Method m, Object[] p, Object obj){
        methodToTest = m;
        parameters = p;
        instanceofObject =  obj;
    }

    @Override
    public void runTest() {
        try{
            long startTime = System.nanoTime();
            methodToTest.invoke(instanceofObject, parameters);
            long searchTime = System.nanoTime()-startTime;
            time_nanos = searchTime;
            System.out.println(time_nanos);

            long startTime1 = System.currentTimeMillis();
            methodToTest.invoke(instanceofObject, parameters);
            long searchTime1 = System.currentTimeMillis()-startTime1;
            time_milis = searchTime1;
            System.out.println(time_milis);
        }
        catch (Throwable e){
            System.err.println(e);
        }
    }

    @Override
    public String toString() {
        return "processorTimeTester{" + "\n time = " + time_nanos + " nanoseconds (non thread safe)" + "\n"
                + " time = " + time_milis + " miliseconds (thread safe) \n for method *" + methodToTest + "*}\n";
    }
}
