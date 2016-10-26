package app;

import app.model.ProgramPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public class ProjectConfig {

    public static  String OriginalExcelPath = "";
    public static  String CompileClassPath = "";
    public static String ProjectDirectory = System.getProperty("user.dir");
    public  static  String ExcelOutputPath = ProjectDirectory;
    public static  ProgramPackage RunningProgram = new ProgramPackage();
    public static final String MRRepo = ProjectDirectory + "\\resource\\MRModelRepository.txt";
    public static final String TemplateExcel = ProjectDirectory + "\\resource\\template.xlsm";
    public static final String VBARepo = ProjectDirectory + "\\resource\\VBAModuleRepository.txt";
    public static final String UserMRExpression = ProjectDirectory + "\\temp\\MRExpression.bas";
    public static final String VBAFileLib = ProjectDirectory + "\\resource\\vbaFile";


    public static void ReadClassRunningConfigFromFile(String path)throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(path));
        RunningProgram.setNeedJars(new String[]{});
        String line = "";
        while((line = reader.readLine())!= null){
            switch (line.trim()){
//                case "@OriginalExcelPath" :
//                    OriginalExcelPath = reader.readLine().trim();
//                    break;
                case "@ProgramMainFilePackageName":
                    RunningProgram.setProgramMainFilePackageName(reader.readLine().trim());
                    break;
                case "@ProgramMainFileDir":
                    RunningProgram.setProgramMainFileDir(reader.readLine().trim());
                    break;
                case "@ProgramMainFileName":
                    RunningProgram.setProgramMainFileName(reader.readLine().trim());
                    break;
                case "@ProgramMainFileInvokedMethodName":
                    RunningProgram.setInvokedMethod(reader.readLine().trim());
                    break;
                case "@ExternalJarsExcludeJDK":
                    List<String> strs = new ArrayList<String>();
                    line = reader.readLine();
                    while(line != null && !line.trim().startsWith("@") && !line.trim().equalsIgnoreCase("")) {
                        if(!line.equalsIgnoreCase("")){
                            strs.add(line.trim());
                        }
                        line = reader.readLine();
                    }
                    RunningProgram.setNeedJars(strs.toArray(new String[strs.size()]));
                    break;
            }
        }

        File dir = new File(path).getParentFile();
        if(dir.isDirectory()){
            ExcelOutputPath = dir.getAbsolutePath();
            CompileClassPath = dir.getAbsolutePath() + "\\classes";
        }
    }
}
