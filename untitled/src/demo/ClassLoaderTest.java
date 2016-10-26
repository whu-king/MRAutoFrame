/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package demo;

import app.ProjectConfig;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
       test();
    }


    public static void test() throws Exception {
        File dir = new File(ProjectConfig.CompileClassPath);
        if (!dir.exists()) { dir.mkdir();}
            URL url = dir.toURI().toURL();
            URL[] urls = new URL[]{url};
            ClassLoader cl = new URLClassLoader(urls);
            Class cls = cl.loadClass("test.Adder");

            Method[] method = cls.getDeclaredMethods();//利用得到的Class对象的自审，返回方法对象集合
            System.out.println("forName:" + cls);
            for (Method me : method) {//遍历该类方法的集合
                System.out.println("方法有:" + me.toString());//打印方法信息
                Class<?>[] params = me.getParameterTypes();
                if(me.getName() == "execute") {
                    Method method1 = cls.getDeclaredMethod("execute", params);
                    Object [] o = new Object[]{1,2};
                    System.out.println(method1.invoke(cls.newInstance(), o));
                }else if(me.getName() == "test3"){
                    Method method1 = cls.getDeclaredMethod("test3", params);
                    int[] ints = new int[]{1,2,3};
                    Object [] o = new Object[]{ints,4};
                   method1.invoke(cls.newInstance(), o);
                }
                for(Class c : params){
                    System.out.println("ParamType:" + c.getName());
                }

            }
//        Method method1 = cls.getDeclaredMethod("execute");
//        Object [] o = new Object[]{1,2};
//        method1.invoke(cls.newInstance(),o);

    }
}