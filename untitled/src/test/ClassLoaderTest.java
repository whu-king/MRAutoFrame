/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package test;

import app.ClassRunner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {

        String classPath = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame";
        ClassRunner.compileJavaFilesInDir(classPath);
    }


    public void test()throws Exception{
        File dir = new File(System.getProperty("user.dir") + "/temp");
        if (!dir.exists()) {dir.mkdir();}

// Convert File to a URL
        URL url = dir.toURI().toURL();
        URL[] urls = new URL[]{url};
        // Create a new class loader with the directory

//        Class thisclass = this.getClass();
//        ClassLoader theCL = thisclass.getClassLoader();
//        theCL.loadClass("java.util.Scanner");
        // Load in the class; Test2.class should be located in
        // the directory file:/D：\test\zy\
        ClassLoader cl = new URLClassLoader(urls);
        Class cls = cl.loadClass("Sub");

        Method[] method=cls.getDeclaredMethods();//利用得到的Class对象的自审，返回方法对象集合
        System.out.println("forName:"+cls);
        for(Method me:method){//遍历该类方法的集合
            System.out.println("方法有:" + me.toString());//打印方法信息
            Object[] o = new Object[]{new String[]{"1","2"}};
            me.invoke(cls.newInstance(),(Object)new String[]{"1","2"});
        }
    }
}
