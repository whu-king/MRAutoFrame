/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Administrator on 2016/7/15.
 */
public class ClassRunner {



    public static void RunJavaProgramByReflect(ProgramPackage programPackage,String params,MRDemo mr)throws Exception{

        File dir = new File(ProjectConfig.CompileClassPath);
        if(!dir.exists()) dir.mkdir();
        compileJavaFilesInDir(programPackage.getProgramMainFileDir());
        // Convert File to a URL
        URL url = dir.toURI().toURL();
        URL[] urls = new URL[]{url};
        // Create a new class loader with the directory
        ClassLoader cl = new URLClassLoader(urls);
        Class cls = cl.loadClass(programPackage.getProgramMainFileName().replaceAll("\\.java",""));
        Method method = cls.getMethod(programPackage.getInvokedMethod());
        method.invoke(cls.newInstance(),(Object)new String[]{"1","2"});
        //参数长度不确定
    }


    public static void compileJavaFilesInDir(String sourceFileDir)throws Exception{
        //temporary directory to store .class file
        File dir = new File(ProjectConfig.CompileClassPath);
        if (!dir.exists()) {dir.mkdir();}
        //compile source Files in one directory
        String compileCommond = "javac -d " + dir.getAbsolutePath() + " " + sourceFileDir + "*.java";
        final Process compilePro = Runtime.getRuntime().exec(compileCommond);
        compilePro.waitFor();
    }

    /**
     * compile files in the same Directory and run main class to get result
     **/
    public static String[] RunJavaProgramByProcess(ProgramPackage programPackage, final String[] params, int outputNumber) throws Exception {

        String mainFileName = programPackage.getProgramMainFileName();
        String sourceFileDir = programPackage.getProgramMainFileDir();
        String mainFilePackageName = programPackage.getProgramMainFilePackageName();
        //temporary directory to store .class file
        compileJavaFilesInDir(sourceFileDir);
        //run MainFile
        String packageName = "";
        if(mainFilePackageName != null) packageName = mainFilePackageName + ".";
        String runCommond = "java -cp " + ProjectConfig.CompileClassPath  + " "  + packageName  + mainFileName.replace(".java", "");
        final Process proc = Runtime.getRuntime().exec(runCommond);
        //transport input into process
        OutputStream stdin = proc.getOutputStream();
        if(params != null){
            for (int i = 0; i < params.length ; i++) {
                stdin.write((params[i] + "\n").getBytes());
            }
        }
        stdin.flush();
        //get output from process
        BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        int count = 0;
        String[] outputs = new String[outputNumber];
        for (String line;  (line = stdout.readLine()) != null && count < outputs.length; count++){
            outputs[count] = line;
        }
        proc.waitFor();
        return outputs;
    }
}
