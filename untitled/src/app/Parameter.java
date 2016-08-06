package app;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/11.
 */
public class Parameter {

    private app.Parameter.DataType dataType;
    private String dataLength = "default";
    private String name = "";

    public enum DataType {
        FLOAT("float",1),DOUBLE("double",2),LONG("long",3),INT("int",4),ARRAY("array",5),String("string",6);
        // 成员变量
        private String name;
        private int index;

        // 构造方法
        private DataType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        // 普通方法
        public static String getName(int index) {
            for (DataType c : DataType.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        public static DataType valueFromString(String str){
            for(DataType c : DataType.values()){
                if(c.getName().equalsIgnoreCase(str)){
                    return c;
                }
            }
            return null;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public app.Parameter.DataType getDataType() {
        return dataType;
    }

    public void setDataType(app.Parameter.DataType dataType) {
        this.dataType = dataType;
    }

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public static List<Parameter> valueOf(String str){
        //Format: {ARRAY[2][2] d,LONG l}
        List<Parameter> params = new ArrayList<Parameter>();
        String[] paramStrs = str.trim().replaceAll("\\{","").replaceAll("\\}","").split(",");
        //Format : [number]
        Pattern lengthPattern = Pattern.compile("\\[\\d+\\]",Pattern.CASE_INSENSITIVE);
        for(String par : paramStrs){
            String []mids = par.trim().split(" ");
            Parameter parm = new Parameter();
            StringBuffer length = new StringBuffer();
            parm.setDataType(DataType.valueFromString(mids[0].replaceAll("\\[.*\\]","")));
            Matcher m = lengthPattern.matcher(mids[0]);
            while(m.find()){
                length.append(m.group().replaceAll("\\[","").replaceAll("\\]","") + " ");
            }
            if(length.toString().length() == 0){
                length.append("default");
            }
            parm.setDataLength(length.toString());
            parm.setName(mids[1]);
            params.add(parm);
        }
        return params;
    }
}
