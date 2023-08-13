package com.example.zbsmaster;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.jupiter.api.Test;

/**
 * @Author zhangboshi01 <zhangboshi01@inspur.com>
 * @date 2023/8/1
 */
public class TestCreateTable {
    @Test
    public void testCreateDbTableByDefault(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
    }
}
