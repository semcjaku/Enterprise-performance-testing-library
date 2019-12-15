import java.lang.reflect.Method;

public class testingofprocessor {
    public int a, b;


    public testingofprocessor(){
        this.a = 1;
        this.b = 1;
    }
    public testingofprocessor(int aa, int bb){
        this.a = aa;
        this.b = bb;
    }

    public void methodpub(int aa, int bb){
        for(int i=0;i<100000000;i++){
            a = a + b;
            b = a - b;
        }
    }


    private void methodpriv(int aa , int bb){
        for(int i=0;i<100000;i++){
            a = a + b;
            b = a - b;
        }
    }


    protected void methodprot(int aa, int bb){
        for(int i=0;i<100000;i++){
            a = a + b;
            b = a - b;
        }
    }


    public static void main(String[] args) throws Exception {
        int a = 1,
            b = 2,
            c = 3,
            d = 4;
        Object[] val1 = {a, b};

        testingofprocessor test = new testingofprocessor();
        Class obj = test.getClass();

        Method[] methods = obj.getMethods();
        Method methodpub = methods[1];


        processorTimeTester proc = new processorTimeTester(methodpub, val1, test);
        proc.runTest();
        System.out.println(proc.toString());


        clockTimeTester clock = new clockTimeTester(methodpub, val1, test);
        clock.runTest();
        System.out.println(clock.toString());


    }
}
