package demo;

import java.util.Scanner;

/**
 * Created by Administrator on 2016/6/20.
 */
public class Sin {

    public double execute(double f){
        return Math.sin(f);
    }

    public static void main(String[] args){
        System.out.println(new Sin().execute(30));
        System.out.println(new Sin().execute(-30));
    }
}
