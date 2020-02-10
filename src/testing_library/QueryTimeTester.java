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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryTimeTester implements BaseTimeTester {
    private Connection dbConnector;
    private HashMap<String,String> queryTimes;
    private Method methodToTest;
    private Object[] parameters;
    private Object instanceofObject;
    private String user;

    protected QueryTimeTester(final Method m, final Object[] p, final Connection dbCon, final String username, final Object obj){
        methodToTest = m;
        dbConnector = dbCon;
        user = username;
        parameters = p;
        instanceofObject = obj;
        queryTimes = new HashMap<>();
    }

    @Override
    public void runTest() {
        try {
            methodToTest.trySetAccessible();

            Statement myStmt = dbConnector.createStatement();
            String getLastLogTimeQuery = "SELECT start_time FROM mysql.slow_log ORDER BY start_time DESC LIMIT 1;";
            ResultSet rs = myStmt.executeQuery(getLastLogTimeQuery);

            String invoke_start = "";
            while (rs.next()) {
                invoke_start = rs.getString("start_time");
            }

            methodToTest.invoke(instanceofObject, parameters);

            // get time of query execution
            String testSql = "SELECT start_time, sql_text, query_time FROM mysql.slow_log WHERE user_host LIKE '%" +
                    user + "%' AND start_time >= '" + invoke_start + "';";
            rs = myStmt.executeQuery(testSql);

            String queryExecutionTime, queryText;
            Pattern logTablePattern = Pattern.compile(".*slow_log.*", Pattern.CASE_INSENSITIVE);
            Pattern setPattern = Pattern.compile(".*autocommit.*", Pattern.CASE_INSENSITIVE);
            while (rs.next()) {
                queryText = rs.getString("sql_text");
                if (logTablePattern.matcher(queryText).matches() || setPattern.matcher(queryText).matches() )
                    continue;

                queryExecutionTime = rs.getString("query_time");
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

