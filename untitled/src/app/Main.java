package app;

import java.util.Scanner;

/**
 * Created by Administrator on 2016/6/20.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        String path = "";
        if(args.length > 1){
            for(int i=0;i<args.length;i++)
                if(args[i].equalsIgnoreCase("-c") || args[i].equalsIgnoreCase("-C")){
                    path = args[i+1];
                }
            while(path.trim().equalsIgnoreCase("")){
                System.out.println("please input the path of config file");
                path = new Scanner(System.in).nextLine();
            }
        }else{
            while(path.trim().equalsIgnoreCase("")){
                System.out.println("please input the path of config file");
                path = new Scanner(System.in).nextLine();
            }
        }
        ProjectConfig.ReadConfigFromFile(path);
        ProjectConfig.ExcelOutputPath = ProjectConfig.ExcelOutputPath;
        String excel = ProjectConfig.OriginalExcelPath;
        ProgramPackage programPackage = ProjectConfig.RunningProgram;

        POIExcelUtil poiExcelUtil = new POIExcelUtil();
        MRDemo mr = poiExcelUtil.createMRFromExcel(excel);
        String newExcel = poiExcelUtil.creatExcelFrame(mr, excel);
        poiExcelUtil.generateFollowInputByMR(newExcel);
        String finalExcel = poiExcelUtil.generateOutputByProgram(programPackage,newExcel,mr);
        poiExcelUtil.checkOutputRelation(finalExcel);
        poiExcelUtil.readCheckResult(finalExcel,mr.getOutputParameterList().size());
    }
}
