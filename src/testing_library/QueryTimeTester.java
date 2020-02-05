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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class QueryTimeTester implements BaseTimeTester {
    private Connection dbConnector;
    private HashMap<String,String> queryTimes;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;
    private String user;

    protected QueryTimeTester(final Method m, final Object[] p, final Connection dbCon, final Object obj){
        methodToTest = m;
        dbConnector = dbCon;

        user = (String) p[0];
        if(p.length > 1)
        {
            Object[] tempArray = new Object[p.length - 1];
            for(int i=1;i<p.length;i++)
                tempArray[i-1] = p[i];
            parameters = tempArray;
        }

        instanceofObject = obj;
        queryTimes = new HashMap<>();
    }

    @Override
    public void runTest() {
        try {
            methodToTest.trySetAccessible();

            System.out.println(System.nanoTime() + ", " + new Timestamp(System.nanoTime()) + ", " + System.currentTimeMillis());



            Timestamp invoke_start = new Timestamp(System.currentTimeMillis());
            methodToTest.invoke(instanceofObject, parameters);
            Timestamp invoke_end = new Timestamp(System.currentTimeMillis());

            Statement myStmt = dbConnector.createStatement();
            // get time of query execution
            String testSql = "SELECT start_time, sql_text, query_time FROM mysql.slow_log WHERE user_host LIKE '%" +
                    user + "%' AND start_time BETWEEN '" + invoke_start.toString() + "' AND '" +
                    invoke_end.toString() + "';";
            ResultSet rs = myStmt.executeQuery(testSql);

            String queryExecutionTime, queryText;
            while (rs.next()) {
                queryExecutionTime = rs.getString("query_time");
                String startTime = rs.getString("start_time");
                queryText = rs.getString("sql_text");
                System.out.println("Invoke start: " + invoke_start + ", invoke end: " + invoke_end +
                        ", query start time: " + startTime + ", query: " + queryText);
                queryTimes.put(queryText,queryExecutionTime);
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

