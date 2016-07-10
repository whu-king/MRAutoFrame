package app;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/6/19.
 */
public class POIExcelUtil {

    /**
     * convert MR Information into a MR model
     * @param filePath
     * @return
     */
    public MRDemo createMRFromExcel(String filePath) throws Exception {

        System.out.println("create MR Model from Excel...");
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
        XSSFSheet MRInfosheet = workbook.getSheetAt(0);

        MRDemo mr = new MRDemo();
        XSSFRow row = MRInfosheet.getRow(0);
        mr.setProgramName(row.getCell(1).getStringCellValue());
        row = MRInfosheet.getRow(1);
        mr.setMRnumber((int) row.getCell(1).getNumericCellValue());
        row = MRInfosheet.getRow(2);
        mr.setInputNumber((int) row.getCell(1).getNumericCellValue());
        row = MRInfosheet.getRow(3);
        mr.setOutputNumber((int) row.getCell(1).getNumericCellValue());
        row = MRInfosheet.getRow(4);
        mr.setInputRelation(row.getCell(1).getStringCellValue());
        row = MRInfosheet.getRow(5);
        mr.setOutputRelation(row.getCell(1).getStringCellValue());

        workbook.close();
        System.out.println("create Finished");
        return mr;
    }


    /**
     * Create Basic Structure for MR
     * @param mr
     * @return
     * @throws Exception
     */
    public String creatExcelFrame(MRDemo mr,String filePath) throws Exception{

        System.out.println("create Excel Frame...");
        /**************Generate Name for Excel*****************/
        String timeStamp = getExcelName();
        String targetFile = "F:/" + mr.getProgramName() + "_" + mr.getMRnumber() + "_" + timeStamp + ".xlsm";
        File filewrite=new File(targetFile);
        filewrite.createNewFile();
        OutputStream out = new FileOutputStream(filewrite);

        /**************Create Sheet for Input*****************/
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
        XSSFSheet inputsheet = workbook.createSheet("InputSheet");

        if(mr.getInputNumber() > 1){
            inputsheet.addMergedRegion(new CellRangeAddress(0, 0, 0, mr.getInputNumber() - 1));
            inputsheet.addMergedRegion(new CellRangeAddress(0, 0, mr.getInputNumber(), mr.getInputNumber() * 2 - 1));
        }
        XSSFRow row = inputsheet.createRow(0);;
        row.createCell(0).setCellValue("Source");
        row.createCell(mr.getInputNumber()).setCellValue("Follow-up");
        row = inputsheet.createRow(1);
        for(int i = 0; i < mr.getInputNumber(); i++){
            String title = "Input Parameter" + (i+1);
            row.createCell(i).setCellValue(title);
        }
        for(int i = 0; i < mr.getInputNumber(); i++){
            String title = "Input Parameter" + (i+1);
            row.createCell(i + mr.getInputNumber()).setCellValue(title);
        }

        /**************Copy input data into new sheet*****************/
        XSSFSheet dataSheet = workbook.getSheetAt(1);
        long rowNum = dataSheet.getLastRowNum() + 1;
        for(int i = 0, dataCount = 0;i < rowNum; i++){
            XSSFRow dataRow = dataSheet.getRow(i);
            if(dataRow == null) continue;
            String cellContent = String.valueOf(dataRow.getCell(0).getNumericCellValue());
            if(cellContent != null && !cellContent.equalsIgnoreCase("")){
                XSSFRow inputRow = inputsheet.createRow(2+dataCount);
                for(int paramCount = 0; paramCount < mr.getInputNumber(); paramCount ++){
                    inputRow.createCell(paramCount).setCellValue(dataRow.getCell(paramCount).getNumericCellValue());
                }
                dataCount++;
            }
        }

        /**************Create Sheet for Output*****************/
        XSSFSheet outputsheet = workbook.createSheet("OutputSheet");
        if(mr.getOutputNumber() > 1){
            outputsheet.addMergedRegion(new CellRangeAddress(0,0,0,mr.getOutputNumber()-1));
            outputsheet.addMergedRegion(new CellRangeAddress(0,0,mr.getOutputNumber(),mr.getOutputNumber()*2 - 1));
            outputsheet.addMergedRegion(new CellRangeAddress(0,0,mr.getOutputNumber()*2,mr.getOutputNumber()*3 - 1));
        }
        row = outputsheet.createRow(0);
        row.createCell(0).setCellValue("Source");
        row.createCell(mr.getOutputNumber()).setCellValue("Follow-up-Expected");
        row.createCell(mr.getOutputNumber()*2).setCellValue("Follow-up-Real");
        row = outputsheet.createRow(1);
        for(int i = 0; i < mr.getOutputNumber(); i++){
            String title = "Output Parameter" + (i+1);
            row.createCell(i).setCellValue(title);
        }
        for(int i = 0; i < mr.getOutputNumber(); i++){
            String title = "Output Parameter" + (i+1);
            row.createCell(i + mr.getOutputNumber()).setCellValue(title);
        }
        for(int i = 0; i < mr.getOutputNumber(); i++){
            String title = "Output Parameter" + (i+1);
            row.createCell(i + mr.getOutputNumber()*2).setCellValue(title);
        }

        workbook.removeSheetAt(1);
        workbook.write(out);
        out.close();
//        System.out.println("xlsm created successfully..");
//        System.out.println("excel Path : " + targetFile);
        System.out.println("create finished");
        return targetFile;
    }

    public void generateFollowInputByMR(String excelPath) throws Exception {
        System.out.println("generate Follow Input by MR...");
        runVbs("generateFollowInput", excelPath);
        System.out.println("generate Finished");
    }

    public void generateFollowOutputByMR(String excelPath) throws Exception {
        System.out.println("generate Follow Output by MR...");
        runVbs("generateFollowOutput", excelPath);
        System.out.println("generate Finished");
    }

    public String generateSourceOutputByProgram(ProgramPackage programfile,String excelPath,MRDemo mr) throws IOException, InterruptedException {

        System.out.println("generate Source Output By Program...");
        /**************Generate Name for new Excel*****************/
        String timeStamp = getExcelName();
        File dir = new File(excelPath).getParentFile();
        String targetFile = "F:/" + mr.getProgramName() + "_" + mr.getMRnumber() + "_" + timeStamp + ".xlsm";
        File filewrite=new File(targetFile);
        filewrite.createNewFile();
        OutputStream out = new FileOutputStream(filewrite);

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelPath));
        XSSFSheet inputSheet = workbook.getSheet("InputSheet");
        XSSFSheet outputSheet = workbook.getSheet("OutputSheet");
        long  rowNum = inputSheet.getPhysicalNumberOfRows();
        double[] sourceOutput ;
        double[] sourceInput = new double[mr.getInputNumber()];

        for(int i = 2; i < rowNum; i++){
            XSSFRow inputRow = inputSheet.getRow(i);
            String cellContent = String.valueOf(inputRow.getCell(0).getNumericCellValue());
            if(cellContent != null && !cellContent.equalsIgnoreCase("")){
                for(int parameterCount = 0 ; parameterCount < mr.getInputNumber(); parameterCount++ ){
                    sourceInput[parameterCount] = inputRow.getCell(parameterCount).getNumericCellValue();
                }
                sourceOutput = callExternalProgram(programfile,sourceInput,mr.getOutputNumber());
                XSSFRow outputRow = outputSheet.createRow(i);
                for(int parameterCount = 0; parameterCount < mr.getOutputNumber(); parameterCount++){
                   outputRow.createCell(parameterCount).setCellValue(sourceOutput[parameterCount]);
                }
            }
        }

        workbook.write(out);
        out.close();
//        System.out.println("xlsm created successfully..");
//        System.out.println("excel Path : " + targetFile);
        workbook.close();
        new File(excelPath).delete();
        System.out.println("generate Finished");
        return targetFile;
    }

    public String generateFollowOutputByProgram(ProgramPackage programFile,String excelPath, MRDemo mr) throws Exception {

        /**************Generate Name for new Excel*****************/
        String timeStamp = getExcelName();
        File dir = new File(excelPath).getParentFile();
        String targetFile = "F:/" + mr.getProgramName() + "_" + mr.getMRnumber() + "_" + timeStamp + ".xlsm";
        File filewrite=new File(targetFile);
        filewrite.createNewFile();
        OutputStream out = new FileOutputStream(filewrite);

        /**************Get input Parameter and expectedOutput for Junit Run*****************/
        System.out.println("prepare Data for Junit");
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelPath));
        XSSFSheet inputSheet = workbook.getSheet("InputSheet");
        XSSFSheet outputSheet = workbook.getSheet("OutputSheet");
        long  rowNum = inputSheet.getPhysicalNumberOfRows();
        List<DataPackage> inputRows = new ArrayList<DataPackage>(),expectedOutput = new ArrayList<DataPackage>();
        double[] inputRowSourceAndFollow = new double[mr.getInputNumber()*2],expecteRow = new double[mr.getOutputNumber()];
        for(int i = 2; i < rowNum; i++){
            XSSFRow inputRow = inputSheet.getRow(i);
            String cellContent = String.valueOf(inputRow.getCell(0).getNumericCellValue());
            if(cellContent != null && !cellContent.equalsIgnoreCase("")){
                for(int parameterCount = 0 ; parameterCount < mr.getInputNumber()*2; parameterCount++ ){
                    inputRowSourceAndFollow[parameterCount] = inputRow.getCell(parameterCount).getNumericCellValue();
                }
                inputRows.add(new DataPackage(inputRowSourceAndFollow,i));
            }
            inputRowSourceAndFollow = new double[mr.getInputNumber()*2];
        }
        rowNum = outputSheet.getPhysicalNumberOfRows();
        for(int i = 2; i < rowNum; i++){
            XSSFRow outputRow = outputSheet.getRow(i);
            String cellContent = String.valueOf(outputRow.getCell(0).getNumericCellValue());
            if(cellContent != null && !cellContent.equalsIgnoreCase("")){
                for(int parameterCount = 0 ; parameterCount < mr.getOutputNumber(); parameterCount++ ){
                    expecteRow[parameterCount] = outputRow.getCell(parameterCount*3 + 1).getNumericCellValue();
                }
                expectedOutput.add(new DataPackage(expecteRow, i));
            }
            expecteRow = new double[mr.getOutputNumber()];
        }
        System.out.println("prepare Finished");
        JUnitRunner jUnitRunner = new JUnitRunner();
        jUnitRunner.setUp((DataPackage[])inputRows.toArray(new DataPackage[inputRows.size()]),mr,
                programFile,(DataPackage[])expectedOutput.toArray(new DataPackage[expectedOutput.size()]));
        jUnitRunner.run();
        List<DataPackage> followTcOutput = jUnitRunner.getFollowOutput();
        for(DataPackage data : followTcOutput){
            XSSFRow outputRow = outputSheet.getRow((int) data.getExcelRowNum());
            double[] output = data.getData();
            for(int paramaterCount = 0; paramaterCount < mr.getOutputNumber(); paramaterCount ++){
                outputRow.createCell(mr.getOutputNumber()*2 + paramaterCount).setCellValue(output[paramaterCount]);
            }
        }

        workbook.write(out);
        out.close();
        System.out.println("Final Excel Path : " + targetFile);
        workbook.close();
        new File(excelPath).delete();
        return targetFile;
    }


    /**
     * compile files in the same Directory and run main class to get result
     * @param programPackage
     * @param params
     * @param outputNumber
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public double[] callExternalProgram(ProgramPackage programPackage, final double[] params, int outputNumber) throws IOException, InterruptedException {

        String mainFileName = programPackage.getProgramMainFileName();
        String sourceFileDir = programPackage.getProgramMainFileDir();
        String mainFilePackageName = programPackage.getProgramMainFilePackageName();

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
        double[] outputs = new double[outputNumber];
        for (String line;  (line = stdout.readLine()) != null && count < outputs.length; count++){
//            System.out.println(line);
            outputs[count] = Double.valueOf(line);
        }
        proc.waitFor();
        return outputs;
    }

    public void runVbs(String vbsName,String excelPath) throws Exception {

        //directory for .vbs file
        File dir = new File(System.getProperty("user.dir") + "/vbs");
        if (!dir.exists()) {dir.mkdir();}

        //create a certain vbs to execute corresponding micro in excel
        StringBuffer vbsCommond = new StringBuffer();
        vbsCommond.append("Set oExcel = createobject('Excel.Application')");
        String FileName = dir.getAbsolutePath() +"\\" + vbsName + ".vbs";
        File vbsFile = new File(FileName);
        if(vbsFile.exists()){
            vbsFile.delete();
        }
        vbsFile.createNewFile();
        writerLine(FileName,"Set objExcel = CreateObject(\"Excel.Application\")");
        writerLine(FileName," objExcel.Visible = false ");
        writerLine(FileName,"Set objWorkbook = objExcel.Workbooks.Open (\""+excelPath+"\")");
        writerLine(FileName," objExcel.Run \" " + vbsName +"\"");
        writerLine(FileName," objWorkbook.Save");
        writerLine(FileName, "objWorkbook.Close ");
        writerLine(FileName, "objExcel.Quit ");
        writerLine(FileName, "Set objWorkbook = nothing");
        writerLine(FileName, "Set objExcel= nothing ");
        String[] cpCmd  = new String[]{"wscript", FileName};
        Process process = Runtime.getRuntime().exec(cpCmd);
        process.waitFor();

        // why is this not working
//        String cpCmd = "cmd " + dir.getAbsolutePath() + "\\gfi.vbs " + excelPath;
//        final Process compilePro = Runtime.getRuntime().exec(cpCmd);
//        compilePro.waitFor();
    }

    public  void writerLine(String path, String contents) {
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


    /**
     * Format of Excel Name : ProgramName-MRNumber-Date
     * @return
     */
    private String getExcelName() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(df.format(new Date()));
        String timeStamp = df.format(new Date()).toString();
        timeStamp = timeStamp.replace("-","");
        timeStamp = timeStamp.replace(":","");
        timeStamp = timeStamp.replace(" ","");
//        System.out.println(timeStamp);
        return timeStamp;
    }
}
