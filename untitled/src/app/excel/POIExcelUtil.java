package app.excel;

import app.ProjectConfig;
import app.model.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static app.fileUtil.Transformation.writerLine;

/**
 * Created by Administrator on 2016/6/19.
 */
public class POIExcelUtil {

    public static void runVBA(String vbsName, String excelPath) throws Exception {

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
        writerLine(FileName, " objExcel.Visible = false ");
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

    public static void importVBAModule(String vbaModulePath,String excelPath) throws Exception{
        VBAModule module = new VBAModule();
        module.setLocalPath(vbaModulePath);
        importVBAModule(module,excelPath);
    }

    public static void importVBAModule(VBAModule vbaModule,String excelPath) throws Exception {
        List<VBAModule> modules = new ArrayList();
        modules.add(vbaModule);
        importVBAModule(modules,excelPath);
    }

    public static void importVBAModule(List<VBAModule> vbaModules, String excelPath) throws Exception {

        if(vbaModules.size() == 0) return;

        File dir = new File(System.getProperty("user.dir") + "\\vbs");
        if (!dir.exists()) {dir.mkdir();}
        String FileName = dir.getAbsolutePath() +"\\importVBAModule.vbs";
        File vbsFile = new File(FileName);
        if(vbsFile.exists()){
            vbsFile.delete();
        }

        String[] libpaths = new String[vbaModules.size()];
        for(int i = 0; i < vbaModules.size(); i++){
            String localPath = vbaModules.get(i).getLocalPath();
            if(localPath.equalsIgnoreCase("default")){
                libpaths[i] = ProjectConfig.VBAFileLib + "\\" + vbaModules.get(i).getName() + ".bas";
            }else{
                libpaths[i] = vbaModules.get(i).getLocalPath();
            }

        }
        StringBuffer libpathStrings = new StringBuffer();
        libpathStrings.append("libs = array(");
        for(String path : libpaths){
            libpathStrings.append("\"" + path + "\",");
        }
        libpathStrings.deleteCharAt(libpathStrings.length()-1);//delete last comma
        libpathStrings.append(")");

        StringBuffer vbsCommond = new StringBuffer();
        vbsCommond.append("Set oExcel = createobject('Excel.Application')");
        vbsFile.createNewFile();
        writerLine(FileName, "Set objExcel = CreateObject(\"Excel.Application\")");
        writerLine(FileName, " objExcel.Visible = false ");
        writerLine(FileName,"Dim libs,libpath");
        writerLine(FileName,libpathStrings.toString());
        writerLine(FileName,"Set objWorkbook = objExcel.Workbooks.Open (\""+ excelPath+"\")");
        writerLine(FileName," For Each libpath in libs");
        writerLine(FileName,"  objExcel.VBE.ActiveVBProject.VBComponents.Import libpath");
        writerLine(FileName,"Next");
        writerLine(FileName," objWorkbook.Save");
        writerLine(FileName, "objWorkbook.Close ");
        writerLine(FileName, "objExcel.Quit ");
        writerLine(FileName, "Set objWorkbook = nothing");
        writerLine(FileName, "Set objExcel= nothing ");
        String[] cpCmd  = new String[]{"wscript", FileName};
        Process process = Runtime.getRuntime().exec(cpCmd);
        process.waitFor();
    }

    public static String getStringValueFromCell(XSSFCell cell) {
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

    public static void writeDataIntoOneRow(String[] datas, XSSFRow row){
        for(int i = 0; i < datas.length; i++){
            if(datas[i].equalsIgnoreCase("")) {
                i--;
                continue;
            }
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(datas[i].trim());
            System.out.println("Cell Value:" + cell.getStringCellValue());
        }
    }
}
