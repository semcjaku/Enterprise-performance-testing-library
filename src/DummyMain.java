public class DummyMain {
    public static void main(String[] args) {
        DummyMain instance = new DummyMain();
        Object[] params = {1,2};
        Tester testthis = new Tester(instance,params);
        testthis.performTest();
        testthis.showResults();
        testthis.saveResults("C:/Temp/nowy.txt");


    }



    @TestMethod(testedValue = {Tester.TestStrategy.query,Tester.TestStrategy.clock,Tester.TestStrategy.processor}, indicesOfParameters = {})
    public static void printOne() {
        System.out.println("Hello World");
    }

    public void printTwo() {
        printOne();
        printOne();
    }

    @TestMethod(testedValue = {Tester.TestStrategy.clock,Tester.TestStrategy.processor}, indicesOfParameters = {0,1})
    public static void printSum(int a,int b) {
        System.out.println(a+b);
    }

}
