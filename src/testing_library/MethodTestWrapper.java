///////////////////////////////////////////////////////////////////////////////
// Title:            Enterprise Performance Testing Library
// Authors:          Jakub Semczyszyn, Piotr Walat, Daniel Rubak, Jan Zasadny
// License:          BSD; for more info see README.md file
///////////////////////////////////////////////////////////////////////////////

package testing_library;

import java.lang.reflect.Method;
import java.sql.Connection;

public class MethodTestWrapper {
    protected Method m;
    protected Tester.TestStrategy strategy;
    protected Object[] parameters;
    protected Connection dbc;
    protected String user;

    public MethodTestWrapper() {}
}
