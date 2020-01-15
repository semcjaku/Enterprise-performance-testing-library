import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface TestMethod {
    public Tester.TestStrategy[] testedValue();
    public int[] indicesOfParameters();
    public int indexOfConnector() default -1;
}
