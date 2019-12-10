import java.lang.reflect.Method;

public class processorTimeTester implements BaseTimeTester {
    private Integer ticks;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;
    ;

    protected processorTimeTester(Method m, Object[] p, Object obj){
        methodToTest = m;
        parameters = p;
        instanceofObject =  obj;
    }

    @Override
    public void runTest() {
        //execute test
    }

    @Override
    public String toString() {
        return "processorTimeTester{" + "ticks=" + ticks + " for method " + methodToTest + "}";
    }
}
