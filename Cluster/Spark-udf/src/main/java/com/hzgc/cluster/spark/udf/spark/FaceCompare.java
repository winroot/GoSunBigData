package com.hzgc.cluster.spark.udf.spark;

import com.hzgc.cluster.spark.udf.custom.CustomFunction;
import org.apache.hadoop.hive.ql.exec.UDF;

public class FaceCompare extends UDF {
    private CustomFunction function = new CustomFunction();

    public double evaluate(String currentFeature, String historyFeature) {
        if (currentFeature != null && historyFeature != null) {
            return function.featureCompare(currentFeature, historyFeature);
        }
        return 0;
    }
}
