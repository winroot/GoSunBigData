package com.hzgc.compare.worker.compare.task;

import com.hzgc.compare.SearchResult;
import com.hzgc.compare.worker.conf.Config;

public abstract class CompareTask implements Runnable{
    boolean isEnd = false;
    SearchResult searchResult;
    int hbaseReadMax = 500;
    int resultDefaultCount = 20;
    protected Config conf;


    public abstract SearchResult compare();

    @Override
    public void run() {
        try{
            searchResult = compare();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            isEnd = true;
        }
    }
}
