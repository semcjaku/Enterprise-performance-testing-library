import java.lang.reflect.Method;

public class processorTimeTester implements BaseTimeTester {
    private Integer ticks;
    private Method methodToTest;

    protected processorTimeTester(Method m){
        methodToTest = m;
    }

    @Override
    public void runTest() {
        //execute test
    }

    @Override
    public String toString() {
        return "processorTimeTester{" + "ticks=" + ticks + " for method " + methodToTest + "}\n";
    }
}
