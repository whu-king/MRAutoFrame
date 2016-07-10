package test;


import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 2016/6/7.
 */
public class AdderTest extends TestCase {

    private static Adder adder = new  Adder();

    public AdderTest(){}

    @Before
    public void setup(){
        System.out.println("Before Test");
    }

    @After
    public void teardown(){
        System.out.println("After Test");
    }

    @Test
    public void test(){

        Logger logger=Logger.getLogger(this.getClass());
        logger.info("Hello, Log4J");
        assertEquals(5, adder.execute(2, 3));
        System.out.println(this.createResult());

    }

    @Test
    public void test2(){
        assertEquals(5, adder.execute(2, 2));
    }

    @Test
    public void test3(){
        assertEquals(4,adder.execute(2,2));
    }

    @Test
    public void test4(){
        assertEquals(4,adder.execute(2,2));
    }
}
