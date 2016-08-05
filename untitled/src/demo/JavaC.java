/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package demo;

import app.ProjectConfig;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/4.
 */
public class JavaC {

    @Test
    public void testatFile() throws IOException, InterruptedException {
        ProjectConfig.ExcelOutputPath = "";
        File file = new File(ProjectConfig.CompileClassPath);
        if(!file.exists()) file.mkdir();
        String compileCommond = "";
        String source = "C:\\Users\\Administrator\\Desktop\\MRDEMO\\test\\knn\\codesourceCodeList.txt";
        compileCommond = "javac -d " + ProjectConfig.CompileClassPath + " -classpath C:\\Users\\Administrator\\Desktop\\MRDEMO\\test\\knn\\code\\lib\\java-cup.jar;" +
                "C:\\Users\\Administrator\\Desktop\\MRDEMO\\test\\knn\\code\\lib\\JFlex.jar" + " @" + source;
        //compile source Files in one directory
        final Process compilePro = Runtime.getRuntime().exec(compileCommond);
        compilePro.waitFor();
    }
}
