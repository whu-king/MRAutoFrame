package test;

/**
 * Created by Administrator on 2016/7/15.
 */
public class ObjectTest {

    public static void main(String[] args){
        Object[] obj = new Object[5];
        obj[0] = new Integer(5);
        obj[1] = new String("test");
        obj[2] = new String[][]{{"arr1"},{"arr2"}};
        obj[3] = new Integer[][]{{2,3},{2,4}};
        obj[4] = new Integer[]{1,2};

        for(Object object : obj){
            System.out.println(object);
        }

        for(Integer i : (Integer[])obj[4]){
            System.out.println(i);
        }
    }
}
