import java.lang.reflect.Method;

public class clockTimeTester implements BaseTimeTester {
    private Long time;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;

    protected clockTimeTester(Method m, Object[] p, Object obj){
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
            time = searchTime;
            System.out.println(time);
        }
        catch (Throwable e){
            System.err.println(e);
        }
    }

    @Override
    public String toString() {
        return "clockTimeTester{" + " time= " + time + " nanoseconds for method " + methodToTest + "}";
    }
}
