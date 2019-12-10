import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

public class queryTimeTester implements BaseTimeTester {
    private Connection dbConnector;
    private Map<Statement,Integer> queryTimes;
    private Method methodToTest;
    private Object[] parameters;

    protected queryTimeTester(Method m,Object[] p, Connection dbCon){
        methodToTest = m;
        dbConnector = dbCon;
        parameters = p;
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

