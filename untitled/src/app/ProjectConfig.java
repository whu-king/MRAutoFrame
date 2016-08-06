package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public class ProjectConfig {

    public  static String ExcelOutputPath = "";
    public static String OriginalExcelPath = "";
    public static ProgramPackage RunningProgram = new ProgramPackage();
//    public static String CompileClassPath = System.getProperty("user.dir") + "\\temp";
    public static String CompileClassPath = "";

    public static void ReadConfigFromFile(String path)throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(path));
        RunningProgram.setNeedJars(new String[]{});
        String line = "";
        while((line = reader.readLine())!= null){
            switch (line.trim()){
                case "@OriginalExcelPath" :
                    OriginalExcelPath = reader.readLine().trim();
                    break;
                case "@FinalExcelOutputDir":
                    ExcelOutputPath = reader.readLine().trim();
                    break;
                case "@CompileClassOutputPath":
                    CompileClassPath =  reader.readLine().trim();
                    break;
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
    }
}
