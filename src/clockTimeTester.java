import java.lang.reflect.Method;

public class clockTimeTester implements BaseTimeTester {
    private Integer time;
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
        //execute test
    }

    @Override
    public String toString() {
        return "clockTimeTester{" + "time=" + time + " for method " + methodToTest + "}";
    }
}
