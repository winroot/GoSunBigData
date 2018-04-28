package com.hzgc.collect.expand.merge;

import com.hzgc.collect.expand.log.LogEvent;
import com.hzgc.collect.expand.util.FTPConstants;
import com.hzgc.common.util.json.JSONUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 工具类FindDiffRows(曹大报)
 * 其中包含以下三个方法：
 * <p>
 * getNotProRows：获取集合中未处理的所有行；
 * getAllDiffRows：获取集合中不同行；
 */
class FindDiffRows {

    private Logger LOG = Logger.getLogger(FindDiffRows.class);

    /**
     * 获取日志中未处理的的所有数据
     *
     * @param allRows 日志合并后的所有行
     * @return List对象  未处理数据的集合
     */
    List<String> getNotProRows(List<String> allRows) {
        List<String> notProList = new ArrayList<>();
        if (allRows == null || allRows.size() == FTPConstants.NUM_ZERO) {
            LOG.warn("The unionAllRows size is None");
        } else if (allRows.size() == FTPConstants.NUM_ONE) {
            LOG.info("The unionAllRows size is OnlyOne");
            LogEvent eventState = JSONUtil.toObject(allRows.get(FTPConstants.NUM_ZERO), LogEvent.class);
            String processState = eventState.getStatus();
            if (processState.equals("0")) {
                notProList.add(allRows.get(FTPConstants.NUM_ZERO));
            }
        } else {
            Collections.sort(allRows);
            for (int i = FTPConstants.NUM_ONE; i <= allRows.size() - FTPConstants.NUM_TWO; i++) {
                LogEvent rowEvent = JSONUtil.toObject(allRows.get(i), LogEvent.class);
                LogEvent upRowEvent = JSONUtil.toObject(allRows.get(i - FTPConstants.NUM_ONE), LogEvent.class);
                LogEvent downRowEvent = JSONUtil.toObject(allRows.get(i + FTPConstants.NUM_ONE), LogEvent.class);
                long rowCount = rowEvent.getCount();
                long upRowCount = upRowEvent.getCount();
                long downRowCount = downRowEvent.getCount();
                //将日志中一行数据的序号与其上下行的序号进行比较，
                // 存在相同则表示已处理，没有相同表示未处理
                if (rowCount != upRowCount && rowCount != downRowCount) {
                    notProList.add(allRows.get(i));
                }
            }
            //判断第一行数据是否已经处理
            LogEvent firstEventCount = JSONUtil.toObject(allRows.get(FTPConstants.NUM_ZERO), LogEvent.class);
            LogEvent secondEventCount = JSONUtil.toObject(allRows.get(FTPConstants.NUM_ONE), LogEvent.class);
            if (firstEventCount.getCount() != secondEventCount.getCount()) {
                notProList.add(allRows.get(FTPConstants.NUM_ZERO));
            }
            //判断最后一行数据是否已经处理
            LogEvent lastEventCount = JSONUtil.toObject(allRows.get(allRows.size() - FTPConstants.NUM_ONE), LogEvent.class);
            LogEvent eventCount = JSONUtil.toObject(allRows.get(allRows.size() - FTPConstants.NUM_TWO), LogEvent.class);
            if (lastEventCount.getCount() != eventCount.getCount()) {
                notProList.add(allRows.get(allRows.size() - FTPConstants.NUM_ONE));
            }
        }
        notProList.sort(new ListComparator());
        return notProList;
    }

    /**
     * 获取集合中不同行
     *
     * @param allRows 合并后日志集合
     * @return List对象       返回合并后不同行的集合
     */
    List<String> getAllDiffRows(List<String> allRows) {
        List<String> rows = new ArrayList<>();
        String row;
        if (allRows == null || allRows.size() == FTPConstants.NUM_ZERO) {
            LOG.warn("The unionAllRows size is None");
        } else if (allRows.size() == FTPConstants.NUM_ONE) {
            LOG.info("The unionAllRows size is OnlyOne");
            rows.add(allRows.get(FTPConstants.NUM_ZERO));
        } else {
            Collections.sort(allRows);
            for (int i = FTPConstants.NUM_ONE; i < allRows.size() - FTPConstants.NUM_TWO; i++) {
                row = allRows.get(i);
                if (!row.equals(allRows.get(i - FTPConstants.NUM_ONE)) && !row.equals(allRows.get(i + FTPConstants.NUM_ONE))) {
                    rows.add(row);
                }
            }
            if (!allRows.get(FTPConstants.NUM_ZERO).equals(allRows.get(FTPConstants.NUM_ONE))) {
                rows.add(allRows.get(FTPConstants.NUM_ZERO));
            }
            if (!allRows.get(allRows.size() - FTPConstants.NUM_ONE).equals(allRows.get(allRows.size() - FTPConstants.NUM_TWO))) {
                rows.add(allRows.get(allRows.size() - FTPConstants.NUM_ONE));
            }

        }
        rows.sort(new ListComparator());
        return rows;
    }


    private class ListComparator implements Comparator<String> {
        @Override
        public int compare(String row1, String row2) {
            LogEvent event1 = JSONUtil.toObject(row1, LogEvent.class);
            LogEvent event2 = JSONUtil.toObject(row2, LogEvent.class);
            return Long.compare(event1.getCount(), event2.getCount());
        }
    }
}
