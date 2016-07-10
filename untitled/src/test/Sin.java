package test;

import java.util.Scanner;

/**
 * Created by Administrator on 2016/6/20.
 */
public class Sin {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        float f = Float.valueOf(sc.next());
        System.out.println(Math.sin(f));
    }
}
