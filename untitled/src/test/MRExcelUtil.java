package test;

import app.MRDemo;
import jxl.Workbook;
import jxl.write.*;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/25.
 */
public class MRExcelUtil {

    /**
     * Create Basic Structure for MR
     * @param mr
     * @return
     * @throws Exception
     */
    public String creatExcelFrame(MRDemo mr) throws Exception{

        /**************Generate Name for Excel*****************/
        String timeStamp = getExcelName();
        String targetFile = "F:/" + mr.getProgramName() + "_" + mr.getMRnumber() + "_" + timeStamp + ".xls";
        File filewrite=new File(targetFile);
        filewrite.createNewFile();
        OutputStream os=new FileOutputStream(filewrite);
        WritableWorkbook wwb = Workbook.createWorkbook(os);

        /**************Create Sheet for Inputͷ*****************/
        WritableSheet inputws = wwb.createSheet("InputSheet", 1);
        inputws.mergeCells(0,0,mr.getInputNumber()-1,0);
        inputws.mergeCells(mr.getInputNumber(), 0, mr.getInputNumber() * 2 - 1, 0);
        inputws.addCell(new Label(0,0,"source"));
        inputws.addCell(new Label(mr.getInputNumber(),0,"follow-up"));
        for(int i = 0; i < mr.getInputNumber(); i++){
            Label title2 = new Label(i,1,"Input Parameter" + (i+1));
            inputws.addCell(title2);
        }
        int count = 1;
        for(int i = mr.getInputNumber(); i < mr.getInputNumber()*2; i++){
            Label title2 = new Label(i,1,"Input Parameter" + count);
            inputws.addCell(title2);
            count ++;
        }

        /**************Create Sheet for Outputͷ*****************/
        WritableSheet outputws = wwb.createSheet("OutputSheet", 2);
        outputws.mergeCells(0,0,mr.getOutputNumber()-1,0);
        outputws.mergeCells(mr.getOutputNumber(), 0, mr.getOutputNumber() * 2 - 1, 0);
        outputws.addCell(new Label(0,0,"source"));
        outputws.addCell(new Label(mr.getOutputNumber(),0,"follow-up"));
        for(int i = 0; i < mr.getOutputNumber(); i++){
            Label title2 = new Label(i,1,"Output Parameter" + (i+1));
            outputws.addCell(title2);
        }
        count = 1;
        for(int i = mr.getOutputNumber(); i < mr.getOutputNumber()*2; i++){
            Label title2 = new Label(i,1,"Output Parameter" + count);
            outputws.addCell(title2);
            count ++;
        }
        wwb.write();
        wwb.close();
        os.close();

        return targetFile;
    }

    /**
     * generate follow input according to source input and MR
     * @param excelPath
     * @param mr
     * @throws Exception
     */
    public void generateFollowupInput(String excelPath, MRDemo mr) throws Exception {
        String timeStamp = getExcelName();
        String targetFile = "F:/" + mr.getProgramName() + "_" + mr.getMRnumber() + "_" + timeStamp + ".xls";

        File filewrite = new File(excelPath);
        filewrite.createNewFile();
        InputStream in = new FileInputStream(filewrite);
        Workbook wwb = Workbook.getWorkbook(in);
        WritableWorkbook target = Workbook.createWorkbook(new File(targetFile), wwb);


        WritableSheet inputSheet = target.getSheet(1);
        int rows = inputSheet.getRows();
        for(int i = 2; i < rows; i++){
            if(inputSheet.getCell(0,i).getContents() != null){
                System.out.println("Not null");
                String relations = mr.getInputRelation();
                String[] relation = relations.split(";");
                int ParameterCount = 1;
                for(String str : relation){
                    char indexC = (char)(ParameterCount + 64);
                    String formula = str.replace("source-input-" + ParameterCount,"$" + indexC + "$" + (i + 1));
                    formula = formula.replace("follow-input-" + ParameterCount, "");
                    formula = formula.trim();
                    formula = formula.replace("=","");
                    System.out.println(formula);
                    Formula follow = new Formula(mr.getInputNumber() + ParameterCount - 1,(i),formula);
                    inputSheet.addCell(follow);
                    ParameterCount++;
                }
            }
        }
        wwb.close();
        in.close();
        target.write();
        target.close();
    }

    /**
     * call program to generate output
     * @param programName
     * @param programDir
     * @param excelPath
     * @param mr
     * @throws Exception
     */
    public void generateOutput(String programName,String programDir,String mainProgramPackage,String excelPath, MRDemo mr) throws Exception {

        String timeStamp = getExcelName();
        String targetFile = "F:/" + mr.getProgramName() + "_" + mr.getMRnumber() + "_" + timeStamp + ".xls";
        File filewrite=new File(excelPath);
        filewrite.createNewFile();
        InputStream in = new FileInputStream(filewrite);
        Workbook wwb = Workbook.getWorkbook(in);
        WritableWorkbook target = Workbook.createWorkbook(new File(targetFile), wwb);
        WritableSheet inputSheet = target.getSheet(1);
        WritableSheet outputSheet = target.getSheet(2);
        int rows = inputSheet.getRows();

        //get input Parameter for EXE
        String[] sourceTcInput = new String[mr.getInputNumber()],sourceTcOutput = new String[mr.getOutputNumber()];
        String[] followTcInput = new String[mr.getInputNumber()],followTcOutput = new String[mr.getOutputNumber()];
        for(int i = 2; i < rows; i++){
            if(inputSheet.getCell(0,i).getContents() != null){
                for(int parameterCount = 0 ; parameterCount < mr.getInputNumber(); parameterCount++ ){
                    sourceTcInput[parameterCount] = inputSheet.getCell(parameterCount,i).getContents();
                    followTcInput[parameterCount] = inputSheet.getCell(parameterCount + mr.getInputNumber(),i).getContents();
                }
                sourceTcOutput = callExternalProgram(programName, programDir,mainProgramPackage, sourceTcInput,mr.getOutputNumber());
                followTcOutput = callExternalProgram(programName,programDir,mainProgramPackage,sourceTcOutput,mr.getOutputNumber());
                for(int paramaterCount = 0; paramaterCount < mr.getOutputNumber(); paramaterCount ++){
                    outputSheet.addCell(new Label(paramaterCount,i,sourceTcOutput[paramaterCount]));
                    outputSheet.addCell(new Label(paramaterCount + mr.getOutputNumber(),i,followTcOutput[paramaterCount]));
                }
            }
        }
        wwb.close();
        in.close();
        target.write();
        target.close();


    }

    /**
     * compile files in the same Directory and run main class to get result
     * @param mainFileName
     * @param sourceFileDir
     * @param mainFilePackageName
     * @param params
     * @param outputNumber
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String[] callExternalProgram(String mainFileName,String sourceFileDir, String mainFilePackageName, final String[] params, int outputNumber) throws IOException, InterruptedException {

        //temporary directory to store .class file
        File dir = new File(System.getProperty("user.dir") + "/temp");
        if (!dir.exists()) {dir.mkdir();}

        //compile source Files in one directory
        String compileCommond = "javac -d " + dir.getAbsolutePath() + " " + sourceFileDir + "*.java";
        final Process compilePro = Runtime.getRuntime().exec(compileCommond);
        compilePro.waitFor();

        //run MainFile
        String packageName = "";
        if(mainFilePackageName != null) packageName = mainFilePackageName + ".";
        String runCommond = "java -cp " + dir.getAbsolutePath()  + " "  + packageName  + mainFileName.replace(".java", "");
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
        BufferedReader stdout = new BufferedReader(new InputStreamReader
                (proc.getInputStream()));
        int count = 0;
        String[] outputs = new String[outputNumber];
        for (String line;  (line = stdout.readLine()) != null && count < outputs.length; count++){
//            System.out.println(line);
            outputs[count] = line;
        }
        proc.waitFor();
        return outputs;
    }

    public void checkOutput(String excelPath){

    }

    /**
     * @deprecated
     * @param mr
     * @param wwb
     * @throws WriteException
     */
    private void createMRInfoSheet(MRDemo mr, WritableWorkbook wwb) throws WriteException {
        WritableSheet mrInfows = wwb.createSheet("MR INFO", 0);
        Label inputLabel = new Label(0,0,"Input Relation");
        Label inputValue = new Label(1,0,mr.getInputRelation());
        Label outputLabel = new Label(0,1,"outputRealtion");
        Label outputValue = new Label(1,1,mr.getOutputRelation());
        mrInfows.addCell(inputLabel);
        mrInfows.addCell(inputValue);
        mrInfows.addCell(outputLabel);
        mrInfows.addCell(outputValue);
    }

    /**
     * Format of Excel Name : ProgramName-MRNumber-Date
     * @return
     */
    private String getExcelName() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()));
        String timeStamp = df.format(new Date()).toString();
        timeStamp = timeStamp.replace("-","");
        timeStamp = timeStamp.replace(":","");
        timeStamp = timeStamp.replace(" ","");
        System.out.println(timeStamp);
        return timeStamp;
    }

}
