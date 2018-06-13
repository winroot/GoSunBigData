package com.hzgc.service.util.journal.service;

import com.hzgc.service.util.journal.dto.OperateLogDTO;

/**
 * @author liuzhikun
 * @date 2018/05/28
 */
public interface OperateLogWriterService {
    void writeLog(OperateLogDTO operateLogDTO);
}
