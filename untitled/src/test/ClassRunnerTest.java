/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package test;

import app.ClassRunner;
import app.ProgramPackage;
import app.ProjectConfig;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

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

        ClassRunner.compileJavaFilesInDir(programPackage.getProgramMainFileDir());
        ClassRunner.RunJavaProgramByReflect(programPackage, new String[]{"{{2,3},{3,4}}"});

//       new ClassRunner().test(programPackage);

    }

}
