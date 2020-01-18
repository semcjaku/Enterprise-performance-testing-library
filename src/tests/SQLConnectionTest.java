package tests;

import testing_library.TestMethod;
import testing_library.Tester;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.ParseException;
import java.util.UUID;

public class SQLConnectionTest {

    public static Connection myConn;

    public SQLConnectionTest() throws SQLException {
        // prepare query connection
        String url = "jdbc:mysql://localhost:3306/classicmodels?autoReconnect=true&serverTimezone=UTC";
        String user = "admin";
        String password = "password";
        myConn = DriverManager.getConnection(url, user, password);
    }

    @TestMethod(testedValue = {Tester.TestStrategy.QUERY}, indicesOfParameters = {0}, indexOfConnector = 0)
    public void getCustomers (String testID) throws SQLException {
        Statement myStmt = myConn.createStatement();
        String sql = "SELECT * FROM classicmodels.customers WHERE '" + testID + "' = '" + testID + "';";

        ResultSet rs = myStmt.executeQuery(sql);
        String customerName = null;
        while (rs.next()) {
            customerName = rs.getString("customerName");
        }
    }

    @TestMethod(testedValue = {Tester.TestStrategy.QUERY}, indicesOfParameters = {1,2}, indexOfConnector = 0)
    public String getCustomerByName(String testID, String customerName) throws SQLException {
        Statement myStmt = myConn.createStatement();
        String sql = "SELECT * FROM classicmodels.customers WHERE customerName = '" + customerName + ""
                + "' AND '" + testID + "' = '" + testID + "';";

        ResultSet rs = myStmt.executeQuery(sql);
        String customerData = null;
        while (rs.next()) {
            customerData = "Customer: " + rs.getString("customerName") + ", "
                    + "city: " + rs.getString("city");
        }

        return customerData;
    }

    public static void main(String[] args) throws ClassNotFoundException, ParseException, SQLException, InvocationTargetException, IllegalAccessException {

        SQLConnectionTest connTest = new SQLConnectionTest();

        Connection[] conn = {myConn};
        String testID_01 = UUID.randomUUID().toString();
        String testID_02 = UUID.randomUUID().toString();

        Object[] params = {testID_01, testID_02, "La Rochelle Gifts",};
        Tester tester = new Tester(connTest, params, conn);
        tester.performTest();
//        tester.showResults();
    }
}
