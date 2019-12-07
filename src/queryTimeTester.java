import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

public class queryTimeTester implements BaseTimeTester {
    private Connection dbConnector;
    private Map<Statement,Integer> queryTimes;
    private Method methodToTest;

    protected queryTimeTester(Method m, Connection dbCon){
        methodToTest = m;
        dbConnector = dbCon;
    }

    @Override
    public void runTest() {
        //execute test
    }

    @Override
    public String toString() {
        return "queryTimeTester{" + "queryTimes=" + queryTimes + " for method " + methodToTest + '}';
    }
}

