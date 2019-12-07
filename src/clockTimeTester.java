import java.lang.reflect.Method;

public class clockTimeTester implements BaseTimeTester {
    private Integer time;
    private Method methodToTest;

    protected clockTimeTester(Method m){
        methodToTest = m;
    }

    @Override
    public void runTest() {
        //execute test
    }

    @Override
    public String toString() {
        return "clockTimeTester{" + "time=" + time + " for method " + methodToTest + "}\n";
    }
}
