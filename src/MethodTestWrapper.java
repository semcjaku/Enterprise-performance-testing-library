import java.lang.reflect.Method;
import java.sql.Connection;

public class MethodTestWrapper {
    protected Method m;
    protected Tester.TestStrategy strategy;
    protected Object[] parameters;
    protected Connection dbc;
}
