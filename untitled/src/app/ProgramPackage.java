package app;

/**
 * Created by Administrator on 2016/6/20.
 */
public class ProgramPackage  {

    private String programMainFileName;
    private String programMainFilePackageName;
    private String programMainFileDir;

    public String getProgramMainFileName() {
        return programMainFileName;
    }

    public void setProgramMainFileName(String programMainFileName) {
        this.programMainFileName = programMainFileName;
    }

    public String getProgramMainFileDir() {
        return programMainFileDir;
    }

    public void setProgramMainFileDir(String programMainFileDir) {
        this.programMainFileDir = programMainFileDir;
    }

    public String getProgramMainFilePackageName() {
        return programMainFilePackageName;
    }

    public void setProgramMainFilePackageName(String programMainFilePackageName) {
        this.programMainFilePackageName = programMainFilePackageName;
    }
}
