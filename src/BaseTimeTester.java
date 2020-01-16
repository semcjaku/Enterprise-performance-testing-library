import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;

interface BaseTimeTester {
    public void runTest() throws InvocationTargetException, IllegalAccessException, SQLException, ParseException;
    public String toString();
}
