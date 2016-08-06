package app;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
        mr.setMRNumber((int) row.getCell(1).getNumericCellValue());
        row = MRInfosheet.getRow(2);
        String inputInfo = row.getCell(1).getStringCellValue();
        mr.setInputParameterList(Parameter.valueOf(inputInfo));
        row = MRInfosheet.getRow(3);
        String outputInfo = row.getCell(1).getStringCellValue();
        mr.setOutputParameterList(Parameter.valueOf(outputInfo));
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
        String targetFile = getExcelName(mr);
        File newExcel =new File(targetFile);
        newExcel.createNewFile();
        OutputStream out = new FileOutputStream(newExcel);

        /**************Create Sheet for Input*****************/
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
        XSSFSheet inputsheet = workbook.createSheet("InputSheet");
        XSSFRow row = inputsheet.createRow(1);
        int SourceEnd = createParameterTitleCell(inputsheet, 0, row, mr.getInputParameterList());
        int FollowEnd = createParameterTitleCell(inputsheet,SourceEnd,row,mr.getInputParameterList());
        createTCTitle(SourceEnd, FollowEnd - 1, inputsheet);

        /**************Copy input data into new sheet*****************/
        XSSFSheet dataSheet = workbook.getSheetAt(1);
        long rowNum = dataSheet.getLastRowNum() + 1;
        for(int i = 0, dataCount = 0;i < rowNum; i++){
            XSSFRow dataRow = dataSheet.getRow(i);
            if(dataRow == null) continue;
            String cellContent = dataRow.getCell(0).getRawValue();
            if(cellContent != null && !cellContent.equalsIgnoreCase("")){
                XSSFRow inputRow = inputsheet.createRow(2+dataCount);
                for(int paramCount = 0; paramCount < mr.getInputParameterList().size(); paramCount ++){
                    inputRow.createCell(paramCount).setCellValue(getStringValueFromCell(dataRow.getCell(paramCount)));
                }
                dataCount++;
            }
        }

        /**************Create Sheet for Output*****************/
        XSSFSheet outputsheet = workbook.createSheet("OutputSheet");
        XSSFRow row2 = outputsheet.createRow(1);
        SourceEnd = createParameterTitleCell(outputsheet, 0, row2, mr.getOutputParameterList());
        FollowEnd = createParameterTitleCell(outputsheet, SourceEnd, row2, mr.getOutputParameterList());
        createTCTitle(SourceEnd,FollowEnd - 1,outputsheet);
        row2 = outputsheet.getRow(0);
        row2.createCell(FollowEnd).setCellValue("Check Result");

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

    public String generateOutputByProgram(ProgramPackage programFile,String excelPath, MRDemo mr) throws Exception {

        /**************Generate Name for new Excel*****************/
        File dir = new File(excelPath).getParentFile();
        String targetFile = getExcelName(mr);
        File filewrite=new File(targetFile);
        filewrite.createNewFile();
        OutputStream out = new FileOutputStream(filewrite);

        /**************Get input Parameter for Junit Run*****************/
        System.out.println("prepare InputData for Junit");
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelPath));
        XSSFSheet inputSheet = workbook.getSheet("InputSheet");
        XSSFSheet outputSheet = workbook.getSheet("OutputSheet");
        long  rowNum = inputSheet.getPhysicalNumberOfRows();
        List<DataPackage> inputRows = new ArrayList<DataPackage>();
        String[] inputRowSourceAndFollow = new String[mr.getInputParameterList().size()*2];
        for(int i = 2; i < rowNum; i++){
            XSSFRow inputRow = inputSheet.getRow(i);
            String cellContent = String.valueOf(getStringValueFromCell(inputRow.getCell(0)));
            if(cellContent != null && !cellContent.equalsIgnoreCase("")){
                for(int parameterCount = 0 ; parameterCount < mr.getInputParameterList().size()*2; parameterCount++ ){
                    inputRowSourceAndFollow[parameterCount] = getStringValueFromCell(inputRow.getCell(parameterCount));
                }
                inputRows.add(new DataPackage(inputRowSourceAndFollow,i));
            }
            inputRowSourceAndFollow = new String[mr.getInputParameterList().size()*2];
        }
        /**************Run Junit *****************/
        System.out.println("prepare Finished");
        JUnitRunner jUnitRunner = new JUnitRunner();
        jUnitRunner.setUp((DataPackage[])inputRows.toArray(new DataPackage[inputRows.size()]),mr, programFile);
        jUnitRunner.run();
        /**************write back outputData *****************/
        List<DataPackage> followTcOutput = jUnitRunner.getFollowOutput();
        List<DataPackage> sourceTcOutput = jUnitRunner.getSourceOutput();
        for(DataPackage data : sourceTcOutput){
            XSSFRow outputRow = outputSheet.createRow((int) data.getExcelRowNum());
            String[] output = data.getData();
            //todo number of output of java function is only one
            for(int paramaterCount = 0; paramaterCount < mr.getOutputParameterList().size(); paramaterCount ++){
                outputRow.createCell(paramaterCount).setCellValue(output[paramaterCount]);
            }
        }
        for(DataPackage data : followTcOutput){
            XSSFRow outputRow = outputSheet.getRow((int) data.getExcelRowNum());
            String[] output = data.getData();
            for(int paramaterCount = 0; paramaterCount < mr.getOutputParameterList().size(); paramaterCount ++){
                outputRow.createCell(mr.getOutputParameterList().size() + paramaterCount).setCellValue(output[paramaterCount]);
            }
        }

        workbook.write(out);
        out.close();
        System.out.println("Final Excel Path : " + targetFile);
        workbook.close();
        new File(excelPath).delete();
        return targetFile;
    }


    public void checkOutputRelation(String excelPath) throws Exception {
        System.out.println("Check OutputRelation...");
        runVbs("checkOutputRelation", excelPath);
        System.out.println("Check Finished");
    }

    public void readCheckResult(String excelPath,int outputNumber)throws Exception{
        System.out.println("************ Running Result************");
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelPath));
        XSSFSheet outputSheet = workbook.getSheet("OutputSheet");
        long  rowNum = outputSheet.getPhysicalNumberOfRows();
        List<RunningResult> report = new ArrayList<RunningResult>();
        int failCount = 0;
        for(int i = 2; i < rowNum; i++){
            XSSFRow outputRow = outputSheet.getRow(i);
            String cellContent = getStringValueFromCell(outputRow.getCell(outputNumber*2));
            if(cellContent == null) continue;
            RunningResult rr = new RunningResult();
            rr.setRowNum(i);
            if(cellContent.equalsIgnoreCase("OK")){
                rr.setIsSuccess(true);
            }else{
                rr.setIsSuccess(false);
                failCount++;
            }
            report.add(rr);
        }
        System.out.println("total:" + report.size());
        System.out.println("fail:" + failCount);
        System.out.println("************ Running Result************");
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
//
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
//
//
    /**
     * Format of Excel Name : ProgramName-MRNumber-Date
     * @return
     */
    private String getExcelName(MRDemo mr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(df.format(new Date()));
        String timeStamp = df.format(new Date()).toString();
        timeStamp = timeStamp.replace("-","");
        timeStamp = timeStamp.replace(":","");
        timeStamp = timeStamp.replace(" ","");
//        System.out.println(timeStamp);
        String excelName = ProjectConfig.ExcelOutputPath + "\\" +  mr.getProgramName() + "_" + mr.getMRNumber() + "_" + timeStamp + ".xlsm";
        return excelName;
    }

    /**
     * parameter name shown in the row
     * @param sheet
     * @param startCell
     * @param row
     * @param params
     * @return
     */
    private int createParameterTitleCell(XSSFSheet sheet, int startCell, XSSFRow row, List<Parameter> params){
        int currentCell = startCell;
        int rowNum = 1;
        // one parameter own one cell
        for(Parameter param : params){
            String title = param.getName();
            title += "@" + param.getDataType().getName().toUpperCase().charAt(0) ;
            row.createCell(currentCell).setCellValue(title);
            currentCell ++;
//            if(param.getDataType() == Parameter.DataType.ARRAY){
//                String[] length = param.getDataLength().trim().split(" ");
//                if(length.length == 1){
//                    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, currentCell, currentCell + Integer.valueOf(length[0]) - 1));
//                    row.createCell(currentCell).setCellValue(title);
//                    currentCell += Integer.valueOf(length[0]);
//                }else{
//                    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, currentCell, currentCell + Integer.valueOf(length[1]) - 1));
//                    row.createCell(currentCell).setCellValue(title);
//                    currentCell += Integer.valueOf(length[1]);
//                }
//            }else{
//                row.createCell(currentCell).setCellValue(title);
//                currentCell ++;
//            }
        }
        return currentCell;
    }

    /**
     * the title show that the data is of source or follow-up
     * @param SourceEnd
     * @param FollowEnd
     * @param sheet
     */
    private void createTCTitle(int SourceEnd,int FollowEnd,XSSFSheet sheet){
        //over two cell is permitted to merge
        if(SourceEnd > 1){
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, SourceEnd - 1));
            sheet.addMergedRegion(new CellRangeAddress(0, 0,SourceEnd, FollowEnd));
        }
        XSSFRow row = sheet.createRow(0);;
        row.createCell(0).setCellValue("Source");
        row.createCell(SourceEnd).setCellValue("Follow-up");
    }

    private String getStringValueFromCell(XSSFCell cell) {
        try{
            int type = cell.getCellType();
            if(type == XSSFCell.CELL_TYPE_STRING ){
                return cell.getStringCellValue();
            }else{
                return String.valueOf(cell.getNumericCellValue());
            }
        }catch (Exception e ){
            e.printStackTrace();
        }
       return null;
    }
}
