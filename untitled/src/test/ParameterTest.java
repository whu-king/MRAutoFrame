package test;

import app.Parameter;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Administrator on 2016/7/11.
 */
public class ParameterTest {

    @Test
    public void TestValueOf(){
        String param = " { array[2][2] arr , double in } ";
        List<Parameter> p = Parameter.valueOf(param);
        assertEquals(Parameter.DataType.ARRAY, p.get(0).getDataType());
        assertEquals("2 2 ",p.get(0).getDataLength());
        assertEquals("arr",p.get(0).getName());
        assertEquals(Parameter.DataType.DOUBLE,p.get(1).getDataType());
        assertEquals("in",p.get(1).getName());
        assertEquals("default",p.get(1).getDataLength());

    }
}
