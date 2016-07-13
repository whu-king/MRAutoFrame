package test;

import java.text.DecimalFormat;

/***
 * N������ʽ���
 * @author imlilu
 *
 */
 class MatrixDet {

    public static void main(String[] args) {
//		double[][] test = {{2,1,-1},{4,-1,1},{201,102,-99}}; 			���Ϊ-18
//		double[][] test = {{1,1,-1,3},{-1,-1,2,1},{2,5,2,4},{1,2,3,2}};  ���Ϊ33
//		double[][] test = {{1,0,-1,2},{-2,1,3,1},{0,1,0,-1},{1,3,4,-2}}; ���Ϊ31
        //���Ϊ12
        double[][] test = {{1,-1,2,-3,1},{-3,3,-7,9,-5},{2,0,4,-2,1},{3,-5,7,-14,6},{4,-4,10,-10,2}};
        double result;
        try {
            result = mathDeterminantCalculation(test);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("������ȷ������ʽ����");
        }
    }

    /***
     * ������ʽ���㷨
     * @param value ��Ҫ�������ʽ
     * @return ����Ľ��
     */
    public static double mathDeterminantCalculation(double[][] value) throws Exception{
        if (value.length == 1) {
            //������ʽΪ1�׵�ʱ���ֱ�ӷ��ر���
            return value[0][0];
        }else if (value.length == 2) {
            //�������ʽΪ���׵�ʱ��ֱ�ӽ��м���
            return value[0][0]*value[1][1]-value[0][1]*value[1][0];
        }
        //������ʽ�Ľ�������2ʱ
        double result = 1;
        for (int i = 0; i < value.length; i++) {
            //�������Խ���λ�õ���ֵ�Ƿ���0�����������Ը�������е��������ҵ�һ�в�Ϊ0�Ľ��е���
            if (value[i][i] == 0) {
                value = changeDeterminantNoZero(value,i,i);
                result*=-1;
            }
            for (int j = 0; j <i; j++) {
                //�ÿ�ʼ������е���λΪ0����Ϊ������ʽ
                //���Ҫ�������Ϊ0����Լ�����һ��λ�ã�������ʡȥ�˼���
                if (value[i][j] == 0) {
                    continue;
                }
                //���Ҫ��Ҫ���������0��������һ�н��е���
                if (value[j][j]==0) {
                    double[] temp = value[i];
                    value[i] = value[i-1];
                    value[i-1] = temp;
                    result*=-1;
                    continue;
                }
                double  ratio = -(value[i][j]/value[j][j]);
                value[i] = addValue(value[i],value[j],ratio);
            }
        }
        DecimalFormat df = new DecimalFormat(".##");
        return Double.parseDouble(df.format(mathValue(value,result)));
    }

    /**
     * ��������ʽ�Ľ��
     * @param value
     * @return
     */
    public static double mathValue(double[][] value,double result) throws Exception{
        for (int i = 0; i < value.length; i++) {
            //����Խ�������һ��ֵΪ0��ȫ��Ϊ0��ֱ�ӷ��ؽ��
            if (value[i][i]==0) {
                return 0;
            }
            result *= value[i][i];
        }
        return result;
    }

    /***
     * ��i��֮ǰ��ÿһ�г���һ��ϵ����ʹ�ô�i�еĵ�i��֮ǰ�������û�Ϊ0
     * @param currentRow ��ǰҪ�������
     * @param frontRow i��֮ǰ�ı�������
     * @param ratio Ҫ���Ե�ϵ��
     * @return ��i��i��֮ǰ�����û�Ϊ0����µ���
     */
    public static double[] addValue(double[] currentRow,double[] frontRow, double ratio)throws Exception{
        for (int i = 0; i < currentRow.length; i++) {
            currentRow[i] += frontRow[i]*ratio;
        }
        return currentRow;
    }

    /**
     * ָ���е�λ���Ƿ�Ϊ0�����ҵ�һ����Ϊ0��λ�õ��н���λ�õ��������û���򷵻�ԭ����ֵ
     * @param determinant ��Ҫ���������ʽ
     * @param line Ҫ��������
     * @param row Ҫ�жϵ���
     */
    public static double[][] changeDeterminantNoZero(double[][] determinant,int line,int row)throws Exception{
        for (int j = line; j < determinant.length; j++) {
            //�����е���
            if (determinant[j][row] != 0) {
                double[] temp = determinant[line];
                determinant[line] = determinant[j];
                determinant[j] = temp;
                return determinant;
            }
        }
        return determinant;
    }
}

