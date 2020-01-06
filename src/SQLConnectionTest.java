import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class SQLConnectionTest {

    public Connection myConn;

    public SQLConnectionTest() throws SQLException {
        // prepare query connection
        String url = "jdbc:mysql://localhost:3306/classicmodels?autoReconnect=true&serverTimezone=UTC";
        String user = "admin";
        String password = "password";
        myConn = DriverManager.getConnection(url, user, password);
    }

    @TestMethod(testedValue = {Tester.TestStrategy.query}, indicesOfParameters = {0})
    public void getCustomers () throws SQLException {
        Statement myStmt = myConn.createStatement();
        String sql = "SELECT * FROM classicmodels.customers;";

        ResultSet rs = myStmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString("customerName"));
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, ParseException, SQLException {

        SQLConnectionTest connTest = new SQLConnectionTest();

        Object[] params = {"sql"};
        connTest.getCustomers();
        Tester tester = new Tester(connTest, params);
        tester.performTest();
//
//        // get time of query execution
//        String testSql = "SELECT query_time FROM mysql.slow_log WHERE sql_text LIKE '%" + testID + "%';";
//        rs = myStmt.executeQuery(testSql);
//
//        String queryExecutionTime = null;
//        while (rs.next()) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//            // convert query time to milliseconds
//            queryExecutionTime = rs.getString("query_time");
//            Date date = sdf.parse("1970-01-01 " + queryExecutionTime);
//            System.out.println("In milliseconds: " + date.getTime());
//        }
    }
}
