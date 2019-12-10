import java.lang.reflect.Method;

public class clockTimeTester implements BaseTimeTester {
    private Integer time;
    private Method methodToTest;
    private Object[] parameters;

    protected clockTimeTester(Method m, Object[] p){
        methodToTest = m;
        parameters = p;
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
