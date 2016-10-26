/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.excel;

import app.ProjectConfig;
import app.model.*;
import app.run.ClassRunner;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static app.excel.POIExcelUtil.*;
/**
 * Created by Administrator on 2016/10/8.
 */
public class ExcelOperatorWithLogic {

    public static MRModel targetProgram;

    /**
     * Create Basic Structure for MR if there is no existing structure
     */
    public static String creatExcelFrame(List<MRModel> mrs,String filePath) throws Exception{

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
        MRModel programInfo = targetProgram;

        /**************Generate Name for Excel*****************/
        String targetFile = getExcelName();
        File newExcel =new File(targetFile);
        newExcel.createNewFile();
        OutputStream out = new FileOutputStream(newExcel);

        /**************Create Sheet for MR Info*****************/
        createMRINFOSheet(mrs, workbook);

        /**************Create Sheet for Input*****************/
        System.out.println("create Excel Frame...");
        XSSFSheet inputsheet = workbook.createSheet("InputSheet");
        XSSFRow row = inputsheet.createRow(1);
        int ParamCount = programInfo.getInputParameterList().size();
        int MRLoop = mrs.size();
        int ParamCellStart = 0;
        for(int i = 0; i < MRLoop + 1; i++){
            ParamCellStart = createParameterTitleCell(inputsheet, ParamCellStart, row, programInfo.getInputParameterList());
        }
        createTCTitle(MRLoop,ParamCount,inputsheet);

        /**************Create Sheet for Output*****************/
        XSSFSheet outputsheet = workbook.createSheet("OutputSheet");
        XSSFRow row2 = outputsheet.createRow(1);
        ParamCount = programInfo.getOutputParameterList().size();
        MRLoop = mrs.size();
        ParamCellStart = 0;
        for(int i = 0; i < MRLoop + 1; i++){
            ParamCellStart = createParameterTitleCell(outputsheet, ParamCellStart, row2, programInfo.getOutputParameterList());
            if(i != 0) {
                ParamCellStart ++;
            }
        }
        createTCTitle(MRLoop,ParamCount,outputsheet);
        workbook.removeSheetAt(0);
        workbook.write(out);
        out.close();

        System.out.println("create finished");
        return targetFile;
    }

    private static void createMRINFOSheet(List<MRModel> mrs, XSSFWorkbook workbook) {
        XSSFSheet MRInfoSheet = workbook.createSheet("MRInfo");
        MRModel mrTemplate = mrs.get(0);
        XSSFRow mrRow = MRInfoSheet.createRow(0);
        mrRow.createCell(0).setCellValue("ProgramName");
        mrRow.createCell(1).setCellValue(mrTemplate.getProgramName());
        mrRow = MRInfoSheet.createRow(1);
        mrRow.createCell(0).setCellValue("Domain");
        mrRow.createCell(1).setCellValue(mrTemplate.getDomain());
        mrRow = MRInfoSheet.createRow(2);
        mrRow.createCell(0).setCellValue("InputParameter");
        mrRow.createCell(1).setCellValue(Parameter.getParameterString(mrTemplate.getInputParameterList()));
        mrRow = MRInfoSheet.createRow(3);
        mrRow.createCell(0).setCellValue("OutputParameter");
        mrRow.createCell(1).setCellValue(Parameter.getParameterString(mrTemplate.getOutputParameterList()));
        mrRow = MRInfoSheet.createRow(4);
        mrRow.createCell(0).setCellValue("MRNumber");
        mrRow.createCell(1).setCellValue(mrs.size());

        int rowIndex = 6;
        for(MRModel model : mrs){
            mrRow = MRInfoSheet.createRow(rowIndex);
            mrRow.createCell(0).setCellValue("MR" + (mrs.indexOf(model) + 1));
            mrRow = MRInfoSheet.createRow(rowIndex+1);
            mrRow.createCell(0).setCellValue("InputRelation");
            mrRow.createCell(1).setCellValue(model.getInputRelation());
            mrRow = MRInfoSheet.createRow(rowIndex + 2);
            mrRow.createCell(0).setCellValue("OutputRelation");
            mrRow.createCell(1).setCellValue(model.getOutputRelation());
            rowIndex += 4;
        }
    }

    /**
     * Format of Excel Name : ProgramName-Date
     */
    private static String getExcelName() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(df.format(new Date()));
        String timeStamp = df.format(new Date()).toString();
        timeStamp = timeStamp.replace("-", "_");
        timeStamp = timeStamp.replace(":","_");
        timeStamp = timeStamp.replace(" ","_");
//        System.out.println(timeStamp);
        String excelName = ProjectConfig.ExcelOutputPath + "\\" +  targetProgram.getProgramName()  + "_" + timeStamp + ".xlsm";
        return excelName;
    }

    /**
     * create titles for different parameters
     */
    private static int createParameterTitleCell(XSSFSheet sheet, int startCell, XSSFRow row, List<Parameter> params){
        int currentCell = startCell;
        // one parameter own one cell
        for(Parameter param : params){
            String title = param.getName();
            title += "@" + param.getDataType().getName().toUpperCase().charAt(0) ;
            row.createCell(currentCell).setCellValue(title);
            currentCell ++;
        }
        return currentCell;
    }

    /**
     * create title for different test case in input Sheet
     */
    private static void createTCTitle(int loop, int ParamCount, XSSFSheet sheet){
        //over two cell is permitted to merge
        int startCell = 0;
        XSSFRow row = sheet.createRow(0);
        for(int i = 0; i < loop + 1; i++){
            if(ParamCount > 1){
                sheet.addMergedRegion(new CellRangeAddress(0, 0, startCell, startCell + ParamCount));
            }
            if(i == 0){
                row.createCell(startCell).setCellValue("Source");
            }else{
                row.createCell(startCell).setCellValue("MR-" + i + "-Follow");
                if(sheet.getSheetName().equalsIgnoreCase("OutputSheet")){
                    row.createCell(startCell + ParamCount).setCellValue("Check-Result");
                    startCell++;
                }
            }
            startCell += ParamCount;
        }

    }

    public static String writeTestDataIntoExcel(String dataPath, String excelPath) throws IOException {
        File dataFile = new File(dataPath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelPath));

        /**************Generate Name for Excel*****************/
        String targetFile = getExcelName();
        File newExcel =new File(targetFile);
        newExcel.createNewFile();
        OutputStream out = new FileOutputStream(newExcel);

        XSSFSheet inputsheet = workbook.getSheet("InputSheet");
        XSSFRow row = null;

        String line = "";
        int lineCount = 0;
        while((line = bufferedReader.readLine()) != null
                &&  !line.equalsIgnoreCase("")){
            String[] datas = line.split(" ");
            row = inputsheet.createRow(lineCount + 2);
            writeDataIntoOneRow(datas, row);
            lineCount++;
        }
        bufferedReader.close();
        workbook.write(out);
        out.close();
        workbook.close();
        System.out.println("Write Data -ExcelFile : " + targetFile);
        (new File(excelPath)).delete();
        return targetFile;
    }

    public static String generateOutputByProgram(ProgramPackage programFile,String excelPath) throws Exception {

        /**************Generate Name for new Excel*****************/
        String targetFile = getExcelName();
        File filewrite=new File(targetFile);
        filewrite.createNewFile();
        OutputStream out = new FileOutputStream(filewrite);

        /**************Get input Parameter for Program Run*****************/
        System.out.println("prepare InputData for Program");
        int MRNumber = targetProgram.getMRNumber();
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelPath));
        XSSFSheet inputSheet = workbook.getSheet("InputSheet");
        XSSFSheet outputSheet = workbook.getSheet("OutputSheet");
        int inputSize = targetProgram.getInputParameterList().size();
        int outputSize = targetProgram.getOutputParameterList().size();
        long  rowNum = inputSheet.getPhysicalNumberOfRows();

        /**************Create blank Row for output*****************/
        for(int i = 2; i < rowNum; i++){
            XSSFRow inputRow = inputSheet.getRow(i);
            String cellContent = String.valueOf(getStringValueFromCell(inputRow.getCell(0)));
            if(cellContent != null && !cellContent.equalsIgnoreCase("")){
               outputSheet.createRow(i);
            }
        }

        /**************Run Program and Write back output to Excel*****************/
        for(int MRLoop = 0; MRLoop < MRNumber + 1; MRLoop++){
            List<DataPackage> inputRows = new ArrayList<DataPackage>();
            List<DataPackage> outputRows = new ArrayList<DataPackage>();
            String []inputOfOneTC = new String[inputSize];

            for(int i = 2; i < rowNum; i++){
                XSSFRow inputRow = inputSheet.getRow(i);
                String cellContent = String.valueOf(getStringValueFromCell(inputRow.getCell(0)));
                if(cellContent != null && !cellContent.equalsIgnoreCase("")){
                    for(int cellIndex = MRLoop * inputSize, k = 0 ; cellIndex < (MRLoop+1) * inputSize; cellIndex++,k++ ){
                        inputOfOneTC[k] = getStringValueFromCell(inputRow.getCell(cellIndex));
                    }
                    inputRows.add(new DataPackage(inputOfOneTC,i));
                }
                inputOfOneTC = new String[inputSize];
            }

            for(DataPackage inputData : inputRows){
                long rowIndex = inputData.getExcelRowNum();
                String[] inputRow = inputData.getData();
                String outputRow = ClassRunner.RunJavaProgramByReflect(programFile,inputRow);
                outputRows.add(new DataPackage(new String[]{outputRow},rowIndex));
            }
            for(DataPackage data : outputRows){
                XSSFRow outputRow = outputSheet.getRow((int) data.getExcelRowNum());
                String[] output = data.getData();
                if(MRLoop == 0){//compute result for source input
                    for(int paramaterCount = 0; paramaterCount < targetProgram.getOutputParameterList().size(); paramaterCount ++){
                        outputRow.createCell(paramaterCount).setCellValue(output[paramaterCount]);
                    }
                }else{
                    for(int cellIndex = (MRLoop - 1) * (outputSize + 1) + 1,k = 0;
                        cellIndex < (MRLoop - 1) * (outputSize + 1) + 1 + outputSize; cellIndex ++, k++){
                        outputRow.createCell(cellIndex).setCellValue(output[k]);
                    }
                }
            }
        }

        workbook.write(out);
        out.close();
        System.out.println("Final Excel Path : " + targetFile);
        workbook.close();
        (new File(excelPath)).delete();
        return targetFile;
    }


    public static void checkOutputRelation(String excelPath) throws Exception {
        System.out.println("Check OutputRelation...");
        runVBA("checkOutputRelation", excelPath);
        System.out.println("Check Finished");
    }

    public static void generateFollowInputByMR(String excelPath) throws Exception {
        System.out.println("generate Follow Input by MR...");
        runVBA("generateFollowInput", excelPath);
        System.out.println("generate Finished");
    }

    public static void readCheckResult(String excelPath)throws Exception{

        System.out.println("************ Running Result************");
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelPath));
        XSSFSheet outputSheet = workbook.getSheet("OutputSheet");
        long  rowNum = outputSheet.getPhysicalNumberOfRows();
        int MRNumber = targetProgram.getMRNumber();
        int outputNumber = targetProgram.getOutputParameterList().size();
        List<RunningResult> successInAll = new ArrayList<RunningResult>();
        List<RunningResult> failsInAll = new ArrayList<RunningResult>();
        String resultInfo = "";
        for(int MRLoop = 1; MRLoop <= MRNumber; MRLoop++){
            List<RunningResult> success = new ArrayList<RunningResult>();
            List<RunningResult> fails = new ArrayList<RunningResult>();
            for(int i = 2; i < rowNum; i++){
                XSSFRow outputRow = outputSheet.getRow(i);
                String cellContent = getStringValueFromCell(outputRow.getCell(
                        outputNumber - 1 + (outputNumber + 1) * MRLoop ));
                if(cellContent == null || cellContent.equalsIgnoreCase("")) continue;
                RunningResult rr = new RunningResult();
                rr.setRowNum(i);
                if(cellContent.equalsIgnoreCase("OK")){
                    rr.setIsSuccess(true);
                    success.add(rr);
                    successInAll.add(rr);
                }else{
                    rr.setIsSuccess(false);
                    fails.add(rr);
                    failsInAll.add(rr);
                }
            }
            resultInfo += "MR" + MRLoop + ":\n";
            resultInfo += "    total:" + (success.size() + fails.size()) + "\n";
            resultInfo += "    fail:" + fails.size() + "\n";

            if(fails.size() != 0){
                resultInfo += "    failRowNum : ";
                for(RunningResult rr : fails){
                    resultInfo += (rr.getRowNum() + " ");
                }
            }
        }
        System.out.println("OverAll Result:");
        System.out.println("    total: " + (successInAll.size() + failsInAll.size()));
        System.out.println("    fail: " + failsInAll.size());
        System.out.println("Specific Result:");
        System.out.println(resultInfo);

        System.out.println("\n" + "************ Running Result************");
    }

    public static void constructTargetProgramFromReplicateExcel(String filePath) throws Exception {

        System.out.println("create Target Program from Excel...");
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
        XSSFSheet MRInfosheet = workbook.getSheetAt(0);

        MRModel mr = new MRModel();
        XSSFRow row = MRInfosheet.getRow(0);
        mr.setProgramName(row.getCell(1).getStringCellValue());
        row = MRInfosheet.getRow(1);
        mr.setDomain(row.getCell(1).getStringCellValue());
        row = MRInfosheet.getRow(2);
        mr.setInputParameterList(Parameter.valueOf(row.getCell(1).getStringCellValue()));
        row = MRInfosheet.getRow(3);
        mr.setOutputParameterList(Parameter.valueOf(row.getCell(1).getStringCellValue()));
        row = MRInfosheet.getRow(4);
        mr.setMRNumber((int)Double.parseDouble(POIExcelUtil.getStringValueFromCell(row.getCell(1))));
        workbook.close();
        System.out.println("create Finished");
        targetProgram = mr;

    }
}
