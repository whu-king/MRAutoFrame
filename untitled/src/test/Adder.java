package test;

/**
 * Created by Administrator on 2016/6/7.
 */
public class Adder {

    Adder(){}

    public int execute(int add1,int add2){
        System.out.println("in Adder");
        return add1 + add2;
    }

    public static void print(){
        System.out.println("Adder");
    }
}
