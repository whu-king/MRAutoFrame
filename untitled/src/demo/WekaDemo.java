///*
// * Copyright (c) 2016.
// * Author:WangChen
// */
//
//package demo;
//
//import weka.classifiers.lazy.IBk;
//import weka.core.Instances;
//import weka.core.converters.ConverterUtils.DataSource;
//
//import java.io.*;
//
///**
// * Created by Administrator on 2016/7/17.
// */
//public class WekaDemo {
//
//    public static void main(String[] args) throws Exception {
//        WekaDemo wekaDemo = new WekaDemo();
//        String basicPath = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\Expr\\inputs\\f_cases\\KNN\\MR-0\\arff\\";
//        wekaDemo.KNN(3,basicPath + "tr_3_mr0.arff",basicPath + "t_3_mr0.arff");
//    }
//
//    public String KNN(int k, String TrainPath,String testPath) throws Exception{
//        Instances data = readData(TrainPath);
//        IBk KNN = buildClassfier(data,k);
//        return applyClassfierOnTestData(testPath,KNN);
//    }
//
//    private Instances readData(String filePath)throws Exception{
//        DataSource source = new DataSource(filePath);
//        Instances data = source.getDataSet();
//        // setting class attribute if the data format does not provide this information
//        // For example, the XRFF format saves the class attribute information as well
//        if (data.classIndex() == -1)
//            data.setClassIndex(data.numAttributes() - 1);
//        return data;
//    }
//
//    private IBk buildClassfier (Instances data,int k) throws Exception{
//
//        String[] options = new String[2];
//        options[0] = "-K";
//        options[1] = String.valueOf(k);
//        IBk ibk = new IBk();          // new instance of tree
//        ibk.setOptions(options);
//        ibk.buildClassifier(data);   // build classifier
//        return ibk;
//    }
//
//    private String applyClassfierOnTestData(String testDataPath, IBk ibk) throws Exception {
//
//        // load unlabeled data
//        Instances unlabeled = new Instances(
//                new BufferedReader(
//                        new FileReader(testDataPath)));
//
//        // create result file name
//        String resultFilePath = testDataPath.split(".")[0] + "_result.arff";
//        File file = new File(resultFilePath);
//
//
//        // set class attribute
//        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
//
//        // create copy
//        Instances labeled = new Instances(unlabeled);
//
//        // label instances
//        for (int i = 0; i < unlabeled.numInstances(); i++) {
//            double clsLabel = ibk.classifyInstance(unlabeled.instance(i));
//            labeled.instance(i).setClassValue(clsLabel);
//        }
//        // save labeled data
//        BufferedWriter writer = new BufferedWriter(
//                new FileWriter(file));
//
//        writer.write(labeled.toString());
//        writer.newLine();
//        writer.flush();
//        writer.close();
//        return resultFilePath;
//    }
//}
