import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {
    public Map< String,Pattern> patternsMap = new HashMap< String,Pattern>();

    public QueryParser() {
        patternsMap.put("select", Pattern.compile("^SELECT", Pattern.CASE_INSENSITIVE));
        patternsMap.put("insert", Pattern.compile("^INSERT", Pattern.CASE_INSENSITIVE));
        patternsMap.put("update", Pattern.compile("^UPDATE", Pattern.CASE_INSENSITIVE));
        patternsMap.put("delete", Pattern.compile("^DELETE", Pattern.CASE_INSENSITIVE));
    }

    public static void main(String[] args) {
        QueryParser queryParser = new QueryParser();
        String testID = UUID.randomUUID().toString();

        String sql = "SELECT * FROM classicmodels.customers;";
        String modifiedQuery = queryParser.appendUUIDtoQuery(sql, testID);
        System.out.println(modifiedQuery);

        sql = "select * from classicmodels.customers WHERE a = b;";
        modifiedQuery = queryParser.appendUUIDtoQuery(sql, testID);
        System.out.println(modifiedQuery);

        sql = "DELETE FROM t1;";
        modifiedQuery = queryParser.appendUUIDtoQuery(sql, testID);
        System.out.println(modifiedQuery);

        sql = "DELETE t1, t2 FROM t1 INNER JOIN t2 INNER JOIN t3 WHERE t1.id=t2.id AND t2.id=t3.id;";
        modifiedQuery = queryParser.appendUUIDtoQuery(sql, testID);
        System.out.println(modifiedQuery);

        sql = "DELETE FROM a1, a2 USING t1 AS a1 INNER JOIN t2 AS a2 WHERE a1.id=a2.id;";
        modifiedQuery = queryParser.appendUUIDtoQuery(sql, testID);
        System.out.println(modifiedQuery);

        sql = "DELETE FROM a1, a2 USING t1 AS a1 INNER JOIN t2 AS a2 WHERE a1.id=a2.id LIMIT 10;";
        modifiedQuery = queryParser.appendUUIDtoQuery(sql, testID);
        System.out.println(modifiedQuery);

        sql = "INSERT INTO tbl_name (col1,col2) VALUES(15,col1*2);";
        modifiedQuery = queryParser.appendUUIDtoQuery(sql, testID);
        System.out.println(modifiedQuery);

        sql = "INSERT INTO classicmodels.customers(customerNumber, customerName, contactLastName, contactFirstName, " +
                "phone, addressLine1, city, country) " +
                "SELECT 505, 'AGH', 'Daniel', 'Rubak', '123456789', 'Somewhere', 'Cracow', 'Poland';";
        modifiedQuery = queryParser.appendUUIDtoQuery(sql, testID);
        System.out.println(modifiedQuery);
    }

    public String appendUUIDtoQuery(String sql, String uuid) {
        // TODO: code refactoring, patternsMap probably can be just a list,
        //  number of cases in switch statement can be smaller

        String queryType = null;
        for (Map.Entry<String,Pattern> entry : patternsMap.entrySet()) {
            Matcher matcher = entry.getValue().matcher(sql);
            if ( matcher.find() ) {
                queryType = entry.getKey();
                break;
            }
        }

        String modifiedQuery = null;
        Pattern whereClausePattern = Pattern.compile("");
        Matcher matcher;

        // remove ; from the end of string to avoid errors during append to query
        if ( sql.endsWith(";") ) {
            sql = sql.substring(0, sql.length() - 1);
        }

        switch(queryType) {
            case "select":
                // where clause exists in query
                whereClausePattern = Pattern.compile("(^SELECT.*WHERE )(.*)",Pattern.CASE_INSENSITIVE);
                matcher = whereClausePattern.matcher(sql);
                if ( matcher.matches() ) {
                    modifiedQuery = matcher.group(1) + uuid + " = " + uuid + " AND " + matcher.group(2);
                    break;
                }

                // where clause does not exists in query
                whereClausePattern = Pattern.compile("(^SELECT.*FROM [a-zA-Z0-9$_]*\\.?[a-zA-Z0-9$_]*)(;|.*)",
                        Pattern.CASE_INSENSITIVE);
                matcher = whereClausePattern.matcher(sql);
                if ( matcher.matches() ) {
                    modifiedQuery = matcher.group(1) + " WHERE " + uuid + " = " + uuid + matcher.group(2);
                    break;
                }

            case "insert":
                // insert record using values list
                whereClausePattern = Pattern.compile("(^INSERT.*)(VALUES\\()([^\\)]*)\\)(.*)",
                        Pattern.CASE_INSENSITIVE);
                matcher = whereClausePattern.matcher(sql);
                if ( matcher.matches() ) {
                    modifiedQuery = matcher.group(1) + " SELECT " + matcher.group(3) + " WHERE " + uuid + " = "
                            + uuid + " " + matcher.group(4);
                    break;
                }

                // insert record using SELECT statement with WHERE clause
                whereClausePattern = Pattern.compile("(^INSERT.*SELECT.*WHERE )(.*)",
                        Pattern.CASE_INSENSITIVE);
                matcher = whereClausePattern.matcher(sql);
                if ( matcher.matches() ) {
                    modifiedQuery = matcher.group(1) + uuid + " = " + uuid + " AND " + matcher.group(2);
                    break;
                }

                // insert record using SELECT statement without WHERE clause
                modifiedQuery = sql +  " WHERE " + uuid + " = " + uuid;
                break;

            case "update":
                // where clause exists in query
                whereClausePattern = Pattern.compile("(^UPDATE.*WHERE )(.*)",Pattern.CASE_INSENSITIVE);
                matcher = whereClausePattern.matcher(sql);
                if ( matcher.matches() ) {
                    modifiedQuery = matcher.group(1) + uuid + " = " + uuid + " AND " + matcher.group(2);
                    break;
                }

                // where clause does not exists in query
                whereClausePattern = Pattern.compile("(.*)( ORDER.*| LIMIT.*)",Pattern.CASE_INSENSITIVE);
                matcher = whereClausePattern.matcher(sql);
                if ( matcher.matches() ) {
                    modifiedQuery = matcher.group(1) + " WHERE " + uuid + " = " + uuid + matcher.group(2) + ";";
                    break;
                } else {
                    modifiedQuery = sql + " WHERE " + uuid + " = " + uuid + ";";
                    break;
                }

            case "delete":
                // where clause exists in query
                whereClausePattern = Pattern.compile("(^DELETE.*WHERE )(;|.*)",Pattern.CASE_INSENSITIVE);
                matcher = whereClausePattern.matcher(sql);
                if ( matcher.matches() ) {
                    modifiedQuery = matcher.group(1) + uuid + " = " + uuid + " AND " + matcher.group(2);
                    break;
                }

                // where clause does not exists in query
                whereClausePattern = Pattern.compile("(^DELETE.*FROM [a-zA-Z0-9$_]*\\.?[a-zA-Z0-9$_]*)(;|.*)",
                        Pattern.CASE_INSENSITIVE);
                matcher = whereClausePattern.matcher(sql);
                if ( matcher.matches() ) {
                    modifiedQuery = matcher.group(1) + " WHERE " + uuid + " = " + uuid + matcher.group(2);
                    break;
                }
        }

        return modifiedQuery;
    }
}
