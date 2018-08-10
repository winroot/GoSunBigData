package com.hzgc.compare.client;

import com.hzgc.common.rpc.client.RpcClient;
import com.hzgc.common.rpc.client.result.AllReturn;
import com.hzgc.common.rpc.util.Constant;
import com.hzgc.compare.worker.Service;
import com.hzgc.compare.worker.common.CompareParam;
import com.hzgc.compare.worker.common.SearchResult;

import java.util.List;
import java.util.Map;

public class CompareClient {
    private Service service;

    public void createService(String serverAddress){
        Constant constant = new Constant("/compare", "worker");
        RpcClient rpcClient = new RpcClient(serverAddress, constant);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service = rpcClient.createAll(Service.class);
    }

    public SearchResult retrievalOnePerson(CompareParam paramt){
        AllReturn<SearchResult> searchRes = service.retrievalOnePerson(paramt);
        List<SearchResult> list = searchRes.getResult();
        SearchResult result = list.get(0);
        for(int i = 1 ; i < list.size(); i ++){
            result.merge(list.get(i));
        }
        return result;
    }

    public SearchResult retrievalSamePerson(CompareParam paramt){
        AllReturn<SearchResult> searchRes = service.retrievalSamePerson(paramt);
        List<SearchResult> list = searchRes.getResult();
        SearchResult result = list.get(0);
        for(int i = 1 ; i < list.size(); i ++){
            result.merge(list.get(i));
        }
        return result;
    }

    public Map<String, SearchResult> retrievalNotSamePerson(CompareParam paramt){
        AllReturn<Map<String, SearchResult>> searchRes = service.retrievalNotSamePerson(paramt);
        List<Map<String, SearchResult>> list = searchRes.getResult();
        Map<String, SearchResult> result = list.get(0);
        for(int i = 1 ; i < list.size(); i ++){
            Map<String, SearchResult> res = list.get(i);
            for(Map.Entry<String, SearchResult> entry : result.entrySet()){
                entry.getValue().merge(res.get(entry.getKey()));
            }
        }
        return result;
    }
}
