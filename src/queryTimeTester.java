import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class queryTimeTester implements BaseTimeTester {
    private Connection dbConnector;
    private HashMap<String,Long> queryTimes;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;

    protected queryTimeTester(Method m,Object[] p, Connection dbCon, Object obj){
        methodToTest = m;
        dbConnector = dbCon;
        parameters = p;
        instanceofObject = obj;
        queryTimes = new HashMap<>();
    }

    @Override
    public void runTest() {
        try {
            methodToTest.trySetAccessible();
            methodToTest.invoke(instanceofObject, parameters);

            Statement myStmt = dbConnector.createStatement();
            // get time of query execution
            String testSql = "SELECT query_time FROM mysql.slow_log WHERE sql_text LIKE '%" + parameters[0] + "%';";
            ResultSet rs = myStmt.executeQuery(testSql);

            String queryExecutionTime, queryText;
            while (rs.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                // convert query time to milliseconds
                queryExecutionTime = rs.getString("query_time");
                queryText = rs.getString("sql_text");
                Date date = sdf.parse("1970-01-01 " + queryExecutionTime);

                queryTimes.put(queryText,date.getTime());
            }
        } catch (Throwable e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "queryTimeTester{" + "queryTimes=" + queryTimes.toString() + " for method " + methodToTest + '}';
    }
}

