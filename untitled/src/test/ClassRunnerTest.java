/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package test;

import app.run.ClassRunner;
import app.model.ProgramPackage;
import app.ProjectConfig;
import org.junit.Test;

/**
 * Created by Administrator on 2016/7/16.
 */
public class ClassRunnerTest {

    @Test
    public void testReflect() throws Exception {

        String file = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame";
        ProgramPackage programPackage = new ProgramPackage();
        programPackage.setProgramMainFilePackageName("test");
        programPackage.setProgramMainFileDir(file);
        programPackage.setProgramMainFileName("MatrixDet.java");
        programPackage.setInvokedMethod("mathDeterminantCalculation");

        ClassRunner.RunJavaProgramByReflect(programPackage, new String[]{"{{2,3},{3,4}}"});

//       new ClassRunner().test(programPackage);

    }

    @Test
    public void testCompileWeka()throws Exception{
        String file = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame";
        String[] jars = new String[]{"C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\weka.jar"};
        ProgramPackage programPackage = new ProgramPackage();
        programPackage.setProgramMainFilePackageName("demo");
        programPackage.setProgramMainFileDir(file);
        programPackage.setProgramMainFileName("WekaDemo.java");
        programPackage.setInvokedMethod("KNN");
        programPackage.setNeedJars(jars);

        String basicPath = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\Expr\\inputs\\f_cases\\KNN\\MR-0\\arff\\";
        String[] params = new String[]{"3",basicPath + "tr_5_mr0.arff",basicPath + "t_5_mr0.arff"};
//        ClassRunner.compileJavaFilesInDir(programPackage.getProgramMainFileDir(),jars);
        ClassRunner.addJar(jars);
        ClassRunner.RunJavaProgramByReflect(programPackage, params);
    }

    @Test
    public void testCompile() throws Exception {
        ProjectConfig.CompileClassPath = "C:\\Users\\Administrator\\Desktop\\MRDEMO\\test\\knn\\classes";
        String dir = "C:\\Users\\Administrator\\Desktop\\MRDEMO\\test\\knn\\code";
        ClassRunner.compileJavaFilesInDir(dir,new String[]{});
    }

}
