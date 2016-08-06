package app;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.List;

/**
 * Created by Administrator on 2016/6/12.
 */


public class JUnitRunner {

    private DataPackage[] inputRows,outputRows;
    private MRDemo mr;
    private ProgramPackage programFile;

    public void setUp(DataPackage[] inputRows, MRDemo mr, ProgramPackage programPackage){
        this.inputRows = inputRows;
        this.mr = mr;
        this.programFile = programPackage;
    }

    public void run(){
        /**************Use Junit to Run program and check *****************/
        TestCasePackage.setData(inputRows);
        TestCasePackage.setMr(mr);
        TestCasePackage.setProgramPackage(programFile);
//        TestCasePackage.setOutputData(outputRows);
//        System.out.println("Junit Running...");
        //runClasses will create a new testcase to Run
        Result tr = JUnitCore.runClasses(TestCasePackage.class);
//        System.out.println("Junit Finished");
//        PrintTestResult(tr);
    }

    public static void PrintTestResult(Result tr) {

        System.out.println("************************Test Result**************************");
        System.out.println("failure : " + tr.getFailureCount());

        for(Failure fail : tr.getFailures()){
            System.out.println(fail.getTrace());
        }
        System.out.println("total : " + tr.getRunCount());
        System.out.println("run time : " + tr.getRunTime());
        System.out.println("************************Test Result**************************");
    }

    public List<DataPackage> getFollowOutput(){
        return TestCasePackage.followOutputData;
    }

    public List<DataPackage> getSourceOutput(){
        return TestCasePackage.sourceOutputData;
    }
}
