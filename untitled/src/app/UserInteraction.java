/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app;

import app.excel.ExcelOperatorWithLogic;
import app.fileUtil.Transformation;
import app.model.VBAModule;
import app.repository.MRDescriptionRepo;
import app.repository.MRDescriptionRepoLocalImpl;
import app.model.MRModel;
import app.model.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Administrator on 2016/9/22.
 */
public class UserInteraction {

    static MRDescriptionRepo mrRepo = new MRDescriptionRepoLocalImpl();

    public static boolean IsUserSelectReplicateMode(){
        System.out.println("*************************** Select Mode ************************************");
        System.out.println("1. replicate ");
        System.out.println("   I have a excel with data and MR, I want to rerun the testing");
        System.out.println("2. construct");
        System.out.println("   I am new here, I want to follow instructions to create a new one");
        System.out.println(" please input the number of mode");
        Scanner sc = new Scanner(System.in);
        int number = 0;
        String line = sc.nextLine();
        while(number == 0){
            if(!line.equalsIgnoreCase("")) {
                number = Integer.valueOf(line);
                return number==1?true:false;
            }
            line = sc.nextLine();
        }
        return false;
    }

    public static MRModel ConstructTargetProgramFromUserInput(){

        MRModel mrModel = new MRModel();
        System.out.println("*************************** Accept Target Program ************************************");
        System.out.println("please input the program you want to test, first line is the name, and second ");
        System.out.println("is the domain, next is input parameter like 'paramType1 paramName1, pt2 pn2'");
        System.out.println(" the last is output parameter");
        System.out.println("Example : ");
        System.out.println("          cos");
        System.out.println("          math");
        System.out.println("          double input");
        System.out.println("          double output");
        Scanner sc = new Scanner(System.in);
        //todo to be more robust
        mrModel.setProgramName(sc.nextLine());
        mrModel.setDomain(sc.nextLine());
        mrModel.setInputParameterList(Parameter.valueOf(sc.nextLine()));
        mrModel.setOutputParameterList(Parameter.valueOf(sc.nextLine()));
        return mrModel;
    }

    public static String getConfigFilePathFrom(String[] args){
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
        return path;
    }

    public static void showMRDescriptionsToUsers(List<MRModel> MRDescriptions) throws Exception {

        String MRModelShowIn = System.getProperty("user.dir") + "\\MRRecommend.txt";
        System.out.println("***************************** Recommend MRDescription **********************************");
        System.out.println("follows are MR recommended for you");
        int count = 1;
        for(MRModel model : MRDescriptions){
            String relation = "  InputRelation : " + model.getInputRelation() +
                     "\n  OutputRelation : " + model.getOutputRelation();
            System.out.println("MR" + count + " : \n" + relation);
            count++;
//            Transformation.String2File(relation, MRModelShowIn);
        }
//        System.out.println("Open File " + MRModelShowIn + " to See MRModel recommended");
        System.out.println("You can input the number of MR  to reuse it, " +
                "each number separated by a space, \n use '#' to end input");
    }

    public static List<MRModel> acceptUserChoosingOfMRDescription(List<MRModel> MRDescriptions){
        Scanner sc = new Scanner(System.in);
        List<Integer> mrNumbers = new ArrayList<Integer>();
        String input = sc.next();
        while(!input.equalsIgnoreCase("#")){
            mrNumbers.add(Integer.valueOf(input));
            input = sc.next();
        }
        List<MRModel> MRUsedByUser = new ArrayList<MRModel>();
        for(Integer i : mrNumbers){
            MRUsedByUser.add(MRDescriptions.get((i-1)));
        }
        return MRUsedByUser;
    }

    public static List<MRModel> acceptMRDescriptionMadeByUser(){
        System.out.println("******************************** Input Your Own MR Description ********************************");
        System.out.println("you can input your own MR like format before,\n" +
                "first line is input relation and second is output relation,\neach MR separated by " +
                "a empty line, use '#' in a single line to end input");
        System.out.println("Example:");
        System.out.println("    inputParameter add by one");
        System.out.println("    output keep same");
        MRModel model = new MRModel();
        List<MRModel> MRMadeByUser = new ArrayList<MRModel>();
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int lineCount = 0;
        while(!line.equalsIgnoreCase("#")){
            if(!line.equalsIgnoreCase("")){
                if(lineCount % 2 == 0) {
                    model = new MRModel();
                    model.setInputRelation(line);
                }else{
                    model.setOutputRelation(line);
                    MRMadeByUser.add(model);
                }
                lineCount++;
            }
            line = sc.nextLine();
        }
        return MRMadeByUser;
    }

    public static void addUserMadeMRToRepo(List<MRModel> MRMadeByUser, MRModel targetProgram) throws Exception {
        for(MRModel model : MRMadeByUser){
            model.copyProgramInfoFrom(targetProgram);
            mrRepo.addNew(model);
        }
    }

    public static void showVBAModulesToUsers(List<VBAModule> modules) throws Exception {

        System.out.println("***************************** Recommend MR Implementation **********************************");
        System.out.println("follows are VBAModule recommended for you");
        int count = 1;
        for(VBAModule vba : modules){
            String vbaString = "  FileName : " + vba.getName() +
                    "\n  OperationOnType : " + vba.getOperationOnDataType() +
                    "\n  Description : " + vba.getMethodDescription() +
                    "\n  FilePath : " + vba.getLocalPath();
            System.out.println("VBAModule " + count + " : \n" + vbaString);
            count++;
        }
        System.out.println("You can input the number of VBAModule  to reuse it in new testing, " +
                "each number separated by a space, \n use '#' to end input");
    }

    public static List<VBAModule> acceptUserChoosingOfVBA(List<VBAModule> vbaModules){
        Scanner sc = new Scanner(System.in);
        List<Integer> mrNumbers = new ArrayList<Integer>();
        String input = sc.next();
        while(!input.equalsIgnoreCase("#")){
            mrNumbers.add(Integer.valueOf(input));
            input = sc.next();
        }
        List<VBAModule> VBAUsedByUser = new ArrayList<VBAModule>();
        for(Integer i : mrNumbers){
            VBAUsedByUser.add(vbaModules.get((i - 1)));
        }
        return VBAUsedByUser;
    }

    public static VBAModule ConstructMRExpressionFromUserInput() throws Exception {
        System.out.println("*********************** Write MR Expression for Excel Running ****************************");
        System.out.println("now you should combine MRDescription and MR implementation in the follows format:");
        System.out.println("first line is for input relation, and second is for output relation");
        System.out.println("these two line compose one MR Expression that will run in Excel");
        System.out.println("Each expression separated by a empty line, use # in a single line to end input");
        System.out.println("Example : ");
        System.out.println("    follow-inputParaName = Function(source-inputParaName);...");
        System.out.println("    follow-outputParaName = Function2(source-outputParaName);...");

        StringBuffer vbaCode =  new StringBuffer();
        vbaCode.append("Public Function WriteConcreteMR()\n");

        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int lineCount = 0;
        while(!line.equalsIgnoreCase("#")){
            int mrSerialNum = lineCount/2 + 1;
            if(!line.equalsIgnoreCase("")){
                if(lineCount % 2 == 0) {
                    vbaCode.append("    set MR" + mrSerialNum + "= New MR\n");
                    vbaCode.append("    MR" + mrSerialNum + ".inputRelation = " + "\"" + line.trim() + "\"\n");
                }else{
                    vbaCode.append("    MR" + mrSerialNum + ".outputRelation = " + "\"" + line.trim() + "\"\n");
                    vbaCode.append("    Set MRGroup(" + mrSerialNum + ") = MR" + mrSerialNum + "\n");
                }
                lineCount++;
            }
            line = sc.nextLine();
        }
        vbaCode.append("End Function\n");

        String state = "";
        int mrCount = lineCount/2;
        for(int i = 0; i < mrCount; i++){
            state += "Public MR" + (i+1) +" As MR\n";
        }
        state += "Public MRGroup(" + mrCount + ")" + " As MR\n";
        String finalVBACode = state + vbaCode.toString();

        File expression = new File(ProjectConfig.UserMRExpression);
        if(expression.exists()) expression.delete();
        Transformation.String2File(finalVBACode, ProjectConfig.UserMRExpression);
        VBAModule mrExpression = new VBAModule();
        mrExpression.setLocalPath(ProjectConfig.UserMRExpression);
        return mrExpression;
    }

    public static List<VBAModule> constructVBAModuleFromUserInput(){
        System.out.println("*********************** Input Your Own VBA Module to Implement MR ****************************");
        System.out.println("you can provide VBA Module written by yourself ");
        System.out.println("first line is the file name, and second is for data type which the operation performing on");
        System.out.println("third line is to describe the method included in the module, last line is the file path");
        System.out.println("Each expression separated by a empty line, use # in a single line to end input");
        System.out.println("Example : ");
        System.out.println("          common");
        System.out.println("          number");
        System.out.println("          include sin,cos,tan function etc");
        System.out.println("          d:\\myVBAFile\\common.bas");

        VBAModule module = new VBAModule();
        List<VBAModule> modules = new ArrayList();
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int lineCount = 0;
        while(!line.equalsIgnoreCase("#")){
            if(!line.equalsIgnoreCase("")){
                if(lineCount % 4 == 0) {
                    module = new VBAModule();
                    module.setName(line);
                }else if(lineCount % 4 == 1){
                    module.setOperationOnDataType(line);
                }else if(lineCount % 4 == 2){
                    module.setMethodDescription(line);
                }else{
                    module.setLocalPath(line);
                    modules.add(module);
                }
                lineCount++;
            }
            line = sc.nextLine();
        }
        return modules;
    }

    public static String ImportTestDataFromUserDataFile(String excelPath) throws IOException {
        System.out.println("*********************** Input Test Data File Path ****************************");
        System.out.println("input test data path and the data will be imported into the excel for running");
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String filePath = line;
        while(filePath.equalsIgnoreCase("")){
            filePath = sc.nextLine();
        }
        String newExcel = ExcelOperatorWithLogic.writeTestDataIntoExcel(filePath, excelPath);
        return newExcel;
    }

    public static String GetExcelPathForRunning(){
        System.out.println("*********************** Input Excel File Path ****************************");
        System.out.println("the excel stored MR and input data, waiting to be run");
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String filePath = line;
        while(filePath.equalsIgnoreCase("")){
            filePath = sc.nextLine();
        }
        return filePath;
    }

    public static void main(String[] args){
        ConstructTargetProgramFromUserInput();
    }
}
