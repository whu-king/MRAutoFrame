/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.fileUtil;

import java.io.*;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Transformation {

    //warning : write model is insert, not overwrite
    public static void String2File(String str,String path) throws Exception{
        File f = new File(path);
        if(!f.exists()) f.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(f,true));
        bw.write(str);
        bw.close();
    }


    public static String File2String(String filePath) throws IOException {
        return File2String(new File(filePath));
    }
    public static String File2String(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while(( line = br.readLine()) != null){
            sb.append(line);
        }
        return sb.toString();
    }

    public static void writerLine(String path, String contents) {
        try {
            FileWriter fileWriter = new FileWriter(path, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(contents);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException ioe) {
        }
    }
}
