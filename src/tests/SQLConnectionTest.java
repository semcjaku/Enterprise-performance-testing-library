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
    public void getCustomers () throws SQLException {
        Statement myStmt = myConn.createStatement();
        String sql = "SELECT * FROM classicmodels.customers;";

        ResultSet rs = myStmt.executeQuery(sql);
        String customerName = null;
        while (rs.next()) {
            customerName = rs.getString("customerName");
        }
    }

    @TestMethod(testedValue = {Tester.TestStrategy.QUERY}, indicesOfParameters = {0, 1}, indexOfConnector = 0)
    public String getCustomerByName(String customerName) throws SQLException {
        Statement myStmt = myConn.createStatement();
        String sql = "SELECT * FROM classicmodels.customers WHERE customerName = '" + customerName + "';";

        ResultSet rs = myStmt.executeQuery(sql);
        String customerData = null;
        while (rs.next()) {
            customerData = "Customer: " + rs.getString("customerName") + ", "
                    + "city: " + rs.getString("city");
        }

        return customerData;
    }

    @TestMethod(testedValue = {Tester.TestStrategy.QUERY}, indicesOfParameters = {0, 2}, indexOfConnector = 0)
    public String getCustomersByName(String[] customersNames) throws SQLException {
        Statement myStmt = myConn.createStatement();

        String sql = "";
        String customerData = new String();
        ResultSet rs;
        for (String customerName : customersNames) {
            sql = "SELECT * FROM classicmodels.customers WHERE customerName = '" + customerName + "';";
            rs = myStmt.executeQuery(sql);

            while (rs.next()) {
                customerData = "Customer: " + rs.getString("customerName") + ", "
                        + "city: " + rs.getString("city");
            }
        }

        return customerData;
    }

    @TestMethod(testedValue = {Tester.TestStrategy.QUERY}, indicesOfParameters = {0, 3, 4, 5, 6, 7, 8, 9},
            indexOfConnector = 0)
    public void addCustomer(String address, String city, String contactFirstName,
                            String contactLastName, String country, String customerName,
                            String phone) throws SQLException {

        Statement myStmt = myConn.createStatement();
        String sql = "INSERT INTO classicmodels.customers(customerNumber, customerName, contactLastName, " +
                "contactFirstName, phone, addressLine1, city, country) " +
                "SELECT (SELECT MAX(customerNumber)+1 FROM classicmodels.customers), '" + customerName + "', '" +
                contactLastName + "', '" + contactFirstName + "', '" + phone + "', '" + address + "', '" + city + "', '"
                + country + "';";

        myStmt.executeUpdate(sql);
    }

    @TestMethod(testedValue = {Tester.TestStrategy.QUERY}, indicesOfParameters = {0, 10, 11},
            indexOfConnector = 0)
    public void updateCustomerName (String prevCustomerName, String newCustomerName) throws SQLException {
        Statement myStmt = myConn.createStatement();
        String sql = "UPDATE classicmodels.customers SET customerName = '" + newCustomerName + "' WHERE customerName = '" +
                prevCustomerName + "';";

        myStmt.executeUpdate(sql);
    }

    public static void main(String[] args) throws ClassNotFoundException, ParseException, SQLException, InvocationTargetException, IllegalAccessException {

        SQLConnectionTest connTest = new SQLConnectionTest();

        Connection[] conn = {myConn};
        String[] customersNames = {"La Rochelle Gifts", "Signal Gift Stores"};

        Object[] params = {"admin", "La Rochelle Gifts", customersNames, "Somewhere", "Cracow", "FirstName",
                "Last Name", "Poland", "AGH", "123456789", "PrevName", "CurrName"};

        Tester tester = new Tester(connTest, params, conn);
        tester.performTest();
        tester.showResults();
    }
}
