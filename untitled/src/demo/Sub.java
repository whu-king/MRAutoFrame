package demo;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Administrator on 2016/6/8.
 */
public class Sub {

    public static void main(String[] args){

        File file = new File("C:\\test.txt");
        Scanner input = new Scanner(System.in);
        int number1 = Integer.valueOf(input.next());
        System.out.println("number1" + number1);
        int number2 = Integer.valueOf(input.next());
        System.out.println(number2);
        System.out.println(number1 - number2);

    }
}
