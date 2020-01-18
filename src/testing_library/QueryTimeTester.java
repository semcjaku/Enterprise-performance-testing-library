///////////////////////////////////////////////////////////////////////////////
// Title:            Enterprise Performance Testing Library
// Authors:          Jakub Semczyszyn, Piotr Walat, Daniel Rubak, Jan Zasadny
// License:          BSD; for more info see README.md file
///////////////////////////////////////////////////////////////////////////////

package testing_library;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class QueryTimeTester implements BaseTimeTester {
    private Connection dbConnector;
    private HashMap<String,Long> queryTimes;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;

    protected QueryTimeTester(final Method m, final Object[] p, final Connection dbCon, final Object obj){
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
            String testSql = "SELECT sql_text, query_time FROM mysql.slow_log WHERE sql_text LIKE '%" + parameters[0] + "%';";
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
        String testResult = "";
        for (Map.Entry me : queryTimes.entrySet()) {
            testResult += "TestingLibrary.queryTimeTester{Method: " + methodToTest.getName() + ", ";
            testResult += "Query: \"" + me.getKey() + "\", ";
            testResult += "Execution time: " + me.getValue() + "}\n";
        }

        return testResult;
    }
}

