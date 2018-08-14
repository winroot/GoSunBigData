package com.hzgc.compare.worker;

import com.hzgc.compare.worker.common.FaceObject;
import com.hzgc.compare.worker.common.SearchResult;
import com.hzgc.compare.worker.common.tuple.Quintuple;
import com.hzgc.compare.worker.compare.ComparatorsImpl2;
import com.hzgc.compare.worker.memory.cache.MemoryCacheImpl;
import com.hzgc.compare.worker.util.FaceObjectUtil;
import com.hzgc.compare.worker.util.UuidUtil;
import javafx.util.Pair;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CreateRecordsToCach {
    @Test
    public void test() throws IOException {
        createRecords(1, 1000);
        List<Quintuple<String, String, String, String, float[]>> records = new ArrayList<>();
        float[] feature = {2.7653352f,-1.3292398f,2.6569615f,3.700091f,2.9411087f,-1.758579f,1.2162389f,-1.7825190f,-1.9533582f,-2.0009017f,-0.99858224f,0.18149188f,-6.3290005f,3.3145254f,-1.9816633f,-0.20250496f,0.16505913f,0.31730723f,1.473586f,1.9500446f,-1.4933753f,1.6594956f,-1.8026383f,5.633249f,2.372508f,1.2905023f,-0.5723133f,-0.7291366f,1.6329529f,1.4453602f,0.26887998f,3.9709718f,2.3833423f,-0.3647722f,-2.9880457f,-5.5151396f,1.1633518f,-1.1747609f,-0.21553133f,2.9004588f,0.64252895f,2.4399018f,2.354013f,-3.670747f,-2.7852771f,1.4544017f,1.5065868f,0.72757804f,-1.5417806f,2.8648334f,-0.3423499f,-2.4470563f,1.515271f,2.5044894f,-1.69942f,-1.6961052f,2.9602168f,5.5269966f,-2.9452055f,-2.972444f,2.5911155f,1.2111247f,0.20778736f,-0.7349568f,-2.1663837f,-0.17182606f,-1.44816f,-0.8806062f,0.85458213f,1.8361077f,-1.7915673f,2.989545f,0.99268675f,0.24031545f,-1.4499967f,3.6345518f,-2.9482133f,-4.253239f,-0.65338135f,-3.905844f,3.869744f,2.5043418f,-2.3573747f,1.9163326f,4.332797f,1.9841931f,1.0757723f,-4.0241246f,-5.0586267f,-0.2999334f,-3.2990422f,-1.6410576f,1.1088148f,6.920575f,-2.8748739f,1.4719776f,-0.8044903f,3.0877817f,-2.255823f,4.463098f,-0.35177296f,0.2845745f,2.8799367f,0.68923426f,0.20315686f,-3.780165f,1.498102f,-3.091504f,-1.4834932f,0.58502907f,-7.5831404f,-2.5341494f,-1.8416555f,3.2448673f,3.6626484f,-2.4510334f,-3.1914253f,-3.935338f,0.119649276f,-2.3033667f,5.0173006f,-1.3060232f,4.6361284f,3.0200357f,-0.20742023f,-3.8795838f,-4.5463033f,2.8194723f,-4.8931994f,-2.4878402f,-3.5381587f,4.7198524f,0.7580483f,1.2136879f,-0.059485674f,3.7978508f,-4.0867686f,-1.1724576f,-0.9213625f,-0.7282985f,-0.69201136f,-2.525659f,4.8947067f,2.60553f,-3.4397857f,1.3117536f,0.33508545f,-1.8025786f,-2.8215067f,1.7599157f,-0.8635038f,-2.571162f,-0.5902455f,-0.9872613f,-1.98669f,3.1255221f,-4.9858913f,-2.7639987f,-0.6900962f,-1.8790852f,-1.0224519f,-1.3781574f,0.55236316f,-2.172552f,-2.7512767f,-1.7265601f,1.8943746f,-0.2588414f,1.5891484f,-0.7711933f,-0.098038375f,-2.4717624f,1.3156297f,2.2258303f,-1.8060026f,-1.7545635f,4.8953133f,-0.5413962f,-1.9931698f,1.6137799f,-3.7009003f,2.8296735f,-3.912191f,1.6817639f,-0.27569985f,-0.6485758f,-2.949867f,2.6082757f,1.3755738f,-1.7020819f,-5.6003466f,1.582434f,2.9451172f,-5.56187f,-2.5561275f,1.3018242f,1.1547328f,5.510728f,-1.2782195f,5.7779946f,-0.931301f,1.4498637f,-2.6768503f,-0.2910896f,-1.3899759f,2.2506676f,-4.2937717f,4.231136f,-2.5069313f,0.06966999f,-1.4121199f,0.953368f,0.92574537f,1.6600273f,3.3478744f,0.15223604f,-1.9919732f,1.9680952f,3.3840718f,1.1256486f,0.61492735f,0.8654354f,1.3738631f,-2.0382822f,-0.12590848f,-1.6394019f,-2.2453442f,1.6190757f,-2.3581402f,-3.4329958f,-2.234366f,-3.707441f,-3.912847f,3.642704f,1.5339038f,-1.3969164f,-1.6134034f,-3.0981157f,1.3253703f,0.24910623f,0.8236454f,0.5578787f,3.0634036f,-0.7402464f,2.7498722f,2.2548928f,-2.3127737f,0.23927397f,-2.5749598f,-2.1405218f,-3.3773239f,-6.2080097f,2.0633802f,-0.17010441f,3.4895368f,4.1123204f,1.2391902f,-3.3203957f,2.751585f,0.509681f,1.1892169f,-0.11687591f,-0.18090472f,-2.039388f,4.3422008f,-0.95622224f,-0.8255861f,2.0710146f,-2.419053f,-1.5965565f,-2.1131487f,1.1899805f,0.022867756f,-1.5402356f,0.1373681f,1.9798831f,4.9160557f,3.318802f,-0.3342623f,0.8071877f,-1.107868f,-2.8698661f,2.7397947f,-2.7073658f,-5.4885983f,-4.4303155f,-3.1398602f,1.6299297f,-2.730113f,0.6625773f,-0.18541816f,-3.3706586f,1.4131746f,-0.35839665f,-1.6969724f,-2.302503f,1.5605311f,-0.65098685f,1.9671826f,4.1698303f,-0.07130461f,0.17275266f,-1.6045713f,-2.3342757f,-0.6181181f,-5.6871147f,5.8284993f,-0.06556915f,-2.1859937f,-0.07317382f,-0.8593435f,-0.8265488f,3.0315256f,8.522542f,-3.874742f,3.2720044f,2.0233881f,-0.81279576f,0.28068882f,-0.86393416f,-1.6399875f,0.50935334f,-5.3643937f,-0.22303873f,-5.208553f,-1.1891285f,2.421438f,-4.267103f,-2.6504202f,-3.8266504f,-3.6184044f,-0.8925348f,1.7315388f,1.953953f,2.6612518f,-0.28070956f,3.3997357f,3.0304453f,-1.9827945f,-0.66468835f,0.6030713f,-2.5073845f,1.5580411f,4.7060547f,-2.5894763f,-1.0009722f,0.08569365f,1.200547f,-2.2026458f,3.020465f,-0.31747058f,6.0896816f,2.505014f,-0.10671742f,4.991466f,-2.2990174f,2.9524221f,-2.7483337f,-3.3666844f,3.0013928f,2.198253f,-4.10437f,3.367871f,0.25654256f,-1.6060879f,-2.460554f,3.1951113f,-0.8238753f,1.3820648f,-1.7688099f,-3.8124897f,2.174838f,4.9157543f,0.0686346f,-3.8530319f,0.88130915f,-0.0114713535f,-1.4960647f,-3.950634f,1.2064466f,1.8400848f,-0.5862416f,2.5991068f,-0.569731f,-3.119478f,-0.9163152f,1.4701666f,2.2280536f,-2.283317f,-6.4914894f,-2.0973258f,-2.5000455f,-0.40264046f,-1.5524516f,-1.699117f,-4.174464f,-1.0571109f,-0.35134298f,2.5832827f,-2.311993f,-0.37894234f,3.3262868f,2.4071918f,-1.1533568f,1.6229159f,-0.2120589f,3.4841475f,-1.0508322f,0.45131338f,4.6934686f,-2.1905162f,6.6823645f,-0.80948216f,2.6328394f,1.1157724f,3.1197338f,0.94831324f,-1.6089793f,2.0246897f,5.079234f,-4.6724377f,3.2272794f,2.1137722f,-7.1369095f,-2.9471185f,2.7650087f,-3.112034f,3.637027f,-1.972207f,3.1206481f,0.033424646f,-5.8901052f,1.7662625f,1.1345341f,1.1712672f,2.079328f,1.5794041f,0.4592021f,3.0888216f,4.640731f,-0.5978096f,-0.07722959f,4.824803f,2.2372801f,1.2936043f,-0.47366565f,0.69719386f,0.13177887f,2.4898527f,-0.65387726f,3.034097f,-0.6974137f,1.8341511f,-5.5403175f,2.1835532f,-2.4835582f,0.7873132f,-3.2850816f,-1.0262933f,-1.9783666f,-0.49916163f,2.9782238f,0.412243f,4.639945f,2.8915367f,2.5608456f,-2.158268f,-2.0547063f,-2.9881802f,2.8575149f,1.1955645f,-0.9622987f,-2.0984547f,4.126f,-0.42874292f,-1.1962006f,-0.9882456f,1.7772855f,1.301986f,0.7777303f,-4.5782447f,1.3209089f,-1.9563439f,-2.0536554f,0.84963006f,2.2949085f,1.1089945f,-6.052453f,-0.5039177f,-3.5659213f,-1.5125304f,0.9760146f,-2.8967798f,1.6854335f,0.7922226f,2.459783f,-0.97604275f,-4.012042f,-1.7920868f,1.7007172f,-1.2691613f,4.604112f,2.2541156f,-4.3007126f,-0.9858217f,3.6952226f,-2.4351592f,-1.4967476f,-2.8135989f,1.1100558f,-2.644528f,-1.4781053f};
        records.add(new Quintuple<String, String, String, String, float[]>("qqqqqq", null,
                "2018-08-08", "dsafsa", feature));
        MemoryCacheImpl.<String,String, float[]>getInstance().moveToCacheRecords(records);



        ComparatorsImpl2 comparators = new ComparatorsImpl2();
        List<Pair<String, float[]>> dataFilterd =  comparators.filter(null, null, "2018-08-07", "2018-08-09");
        SearchResult result = comparators.compareSecond(feature, 0.9f, dataFilterd);

        result = result.take(10);
        System.out.println(result);

    }

    public static void createRecords(int days, int num) throws IOException {
        Random ran = new Random();
        List<String> ipcIdList = new ArrayList<String>();
        for(int i = 0; i < 100 ; i ++){
            ipcIdList.add(i + "");
        }
        File file = new File("src" + File.separator + "test" + File.separator + "java"
                + File.separator + "com" + File.separator + "hzgc" + File.separator + "compare"
                + File.separator + "worker" + File.separator + "json.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<String> list = new ArrayList<String>();
        while((line = reader.readLine()) != null){
            list.add(line.substring(line.indexOf("\"timeSlot\"") - 2 , line.length()));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        List<File> files = new ArrayList<File>();
//        File file0 = new File("metadata" + File.separator + "metadata_0");
//        files.add(file0);
//        BufferedWriter bw = new BufferedWriter(new FileWriter(file0));
        for(long i = 0L ; i < days ; i ++){
            List<Quintuple<String, String, String, String, float[]>> records = new ArrayList<>();
            String date = sdf.format(new Date(System.currentTimeMillis() - i * 24 * 60 * 60 * 1000));
            for(int j = 0 ; j < num ; j++){
                String ipcId = ipcIdList.get(ran.nextInt(100));
//                String ipcId = "1";
                String timeStamp = ",\"timeStamp\":\"2018-07-13 11:28:47\",\"date\":\"";
                String end = list.get(ran.nextInt(26));
                String data = "{\"ipcId\":\"" +ipcId+ "\"" + timeStamp + date + end;
//                System.out.println(data);
//                bw.write(ipcId + " " + date + " " + );//"\t\n"
                FaceObject obj = FaceObjectUtil.jsonToObject(data);
                String rowkey = obj.getDate() + "-" + obj.getIpcId() + UuidUtil.getUuid().substring(0, 24);
                records.add(new Quintuple<String, String, String, String, float[]>(obj.getIpcId(), null,
                        obj.getDate(), rowkey, obj.getAttribute().getFeature()));
            }
            MemoryCacheImpl.<String,String, float[]>getInstance().moveToCacheRecords(records);
        }
    }

}
