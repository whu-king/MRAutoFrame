/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.run;

import app.ProjectConfig;
import app.model.ProgramPackage;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Administrator on 2016/7/15.
 */
public class ClassRunner {

    public static String RunJavaProgramByReflect(ProgramPackage programPackage,String params[])throws Exception{
        //todo return type is solid as String,using <T> to extend
        addJar(programPackage.getNeedJars());
        File dir = new File(ProjectConfig.CompileClassPath);
        if(!dir.exists()) dir.mkdir();
        compileJavaFilesInDir(programPackage.getProgramMainFileDir(),programPackage.getNeedJars());
        // Convert File to a URL
        URL url = dir.toURI().toURL();
        URL[] urls = new URL[]{url};
        // Create a new class loader with the directory
        ClassLoader cl = new URLClassLoader(urls);
//        cl(new File(path).toURI().toURL());
        String fullClassName = "";
        if(programPackage.getProgramMainFilePackageName().equalsIgnoreCase("") ||
                programPackage.getProgramMainFilePackageName() == null){
            fullClassName = programPackage.getProgramMainFileName().replaceAll("\\.java","");
        }else{
            fullClassName = programPackage.getProgramMainFilePackageName() + "." + programPackage.getProgramMainFileName().replaceAll("\\.java","");
        }
        Class cls = cl.loadClass(fullClassName);
        Method[] method = cls.getDeclaredMethods();
        for (Method me : method) {

            if(me.getName().equalsIgnoreCase(programPackage.getInvokedMethod())) {
                Class<?>[] paramtypes = me.getParameterTypes();
                //method reload?
                if(params.length != paramtypes.length) throw new Exception("Parameter count mismatch");
                System.out.println("Method Name :" + me.toString());
                Method invokedMethod = cls.getDeclaredMethod(programPackage.getInvokedMethod(),paramtypes);
                invokedMethod.setAccessible(true);
                Object[] objects = getParamObjects(params,paramtypes);
                //todo get the type of return and make String from the type
                String result = String.valueOf(invokedMethod.invoke(cls.newInstance(), objects));
                System.out.println("result:" + result);
                return result;
            }
        }
        return "";
    }

    public static void addJar(String[] jars) throws Exception {
        URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
        add.setAccessible(true);
        for(String jar : jars){
            add.invoke(classloader, new Object[] { new File(jar).toURI().toURL() });
        }
    }



    private static Object[] getParamObjects(String[] params, Class<?>[] paramtypes) throws Exception {
        Object[] objects = new Object[params.length];
        for(int i = 0; i < params.length; i++){
//            System.out.println(paramtypes[i].getName());
            //Todo use dynamic class running to simplify the code
            switch (paramtypes[i].getName()){
                case "int" :
                    double j = Double.valueOf(params[i]);
                    objects[i] = (int)j;
                    break;
                case "double" :
                    double d = Double.valueOf(params[i]);
                    objects[i] = d;
                    break;
                case "float" :
                    float f = Float.valueOf(params[i]);
                    objects[i] = f;
                    break;
                case "long" :
                    double dd = Double.valueOf(params[i]);
                    objects[i] = (long)dd;
                    break;
                case "java.lang.String" :
                    objects[i] = params[i];
                    break;
                case "[I" :
                    String[] strs = params[i].trim().replaceAll("\\{","").replaceAll("\\}","").split(",");
                    int[] ints = new int[strs.length];
                    for(int k = 0; k < strs.length; k++ ){
                        ints[k] = Integer.valueOf(strs[k]);
                    }
                    objects[i] = ints;
                    break;
                case "[D":
                    String[] strs2 = params[i].trim().replaceAll("\\{","").replaceAll("\\}","").split(",");
                    double[] doubles = new double[strs2.length];
                    for(int k = 0; k < strs2.length; k++ ){
                        doubles[k] = Double.valueOf(strs2[k]);
                    }
                    objects[i] = doubles;
                    break;
                case "[[D":
                    //{{2,3},{3,4}}
                    String p = params[i].trim();
                    String[] strs3 = p.substring(1,p.length()-2).split("},");
                    int column = strs3[0].replaceAll("\\{","").replaceAll("\\}","").split(",").length;
                    double[][] matrix = new double[strs3.length][column];
                    for(int row = 0; row < strs3.length; row++){
                        String[] strs4 = strs3[row].replaceAll("\\{","").replaceAll("\\}","").split(",");
                        for(int col = 0; col < strs4.length; col++){
                            matrix[row][col] = Double.valueOf(strs4[col]);
                        }
                    }
                    objects[i] = matrix;
                    break;
            }
        }

        return objects;
    }


    public static void compileJavaFilesInDir(String sourceFileDir,String[] jars)throws Exception{
        //temporary directory to store .class file
        File dir = new File(ProjectConfig.CompileClassPath);
        if (!dir.exists()) {dir.mkdir();}

        //built a file contain all sourcefile path
        String listFileName = sourceFileDir + "\\sourceCodeList.txt";
        File sourceCodeList = new File(listFileName);
        if(sourceCodeList.exists()) {
            sourceCodeList.delete();
            sourceCodeList.createNewFile();
        }
        AddFilepathIntoTxt(sourceFileDir,sourceCodeList);

        //built a cmd commond
        String compileCommond = "";
        if(jars.length == 0){
             compileCommond = "javac -d " + dir.getAbsolutePath() + " @" + sourceCodeList.getAbsolutePath();
        }else{
            StringBuffer sb = new StringBuffer();
            for(String jar : jars){
                if(jar.indexOf(jar) != jars.length-1){
                    sb.append(jar + ";");
                }else{
                    sb.append(jar);
                }
            }
             compileCommond = "javac -d " + dir.getAbsolutePath() + " " + "-classpath " + sb.toString() + " " + "@" + sourceCodeList.getAbsolutePath();
        }

        //compile source Files in one directory
        final Process compilePro = Runtime.getRuntime().exec(compileCommond);
        compilePro.waitFor();

    }

    private static void AddFilepathIntoTxt(String sourceFileDir, File sourceCodeList) throws IOException {
        File dir = new File(sourceFileDir);
        File[] files = dir.listFiles();
        StringBuffer sourceFileList = new StringBuffer();
        for(File file : files){
            if(file.isDirectory()) AddFilepathIntoTxt(file.getAbsolutePath(),sourceCodeList);
            else {
                if(file.getName().endsWith("java")) sourceFileList.append(file.getAbsolutePath() + "\n\r");
            }
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sourceCodeList,true));
        bufferedWriter.write(sourceFileList.toString());
        bufferedWriter.close();
    }

    /**
     * compile files in the same Directory and run main class to get result : all in cmd
     **/
    public static String[] RunJavaProgramByProcess(ProgramPackage programPackage, final String[] params, int outputNumber) throws Exception {

        String mainFileName = programPackage.getProgramMainFileName();
        String sourceFileDir = programPackage.getProgramMainFileDir();
        String mainFilePackageName = programPackage.getProgramMainFilePackageName();
        //temporary directory to store .class file
        compileJavaFilesInDir(sourceFileDir,programPackage.getNeedJars());
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
