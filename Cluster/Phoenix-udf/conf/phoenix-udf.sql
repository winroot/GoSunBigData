create function FACECOMP(float[], varchar) returns FLOAT  as 'com.hzgc.cluster.spark.phoenix.udf.FaceCompFunc' using jar 'hdfs://hzgc/user/phoenix/udf/facecomp/phoenix-udf-2.3.0.jar';