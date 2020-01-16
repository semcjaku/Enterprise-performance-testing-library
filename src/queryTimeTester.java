import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class queryTimeTester implements BaseTimeTester {
    private Connection dbConnector;
    private Map<Statement,Integer> queryTimes;
    private Method methodToTest;
    private Object[] parameters;
    private Object[][] params_groups;
    private Object instanceofObject;

    protected queryTimeTester(Method m,Object[] p, Connection dbCon, Object obj){
        methodToTest = m;
        dbConnector = dbCon;
        parameters = p;
        instanceofObject = obj;
    }

    @Override
    public void runTest() {
        try {
            System.out.println(methodToTest.getName());
            methodToTest.trySetAccessible();
            methodToTest.invoke(instanceofObject, parameters);

            Statement myStmt = dbConnector.createStatement();
            // get time of query execution
            String testSql = "SELECT query_time FROM mysql.slow_log WHERE sql_text LIKE '%" + parameters[0] + "%';";
            ResultSet rs = myStmt.executeQuery(testSql);

            String queryExecutionTime = null;
            while (rs.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                // convert query time to milliseconds
                queryExecutionTime = rs.getString("query_time");
                Date date = sdf.parse("1970-01-01 " + queryExecutionTime);
                System.out.println("In milliseconds: " + date.getTime());
            }
        } catch (Throwable e) {
            System.err.println(e);
        }
    }

    @Override
    public String toString() {
        return "queryTimeTester{" + "queryTimes=" + queryTimes + " for method " + methodToTest + '}';
    }
}

