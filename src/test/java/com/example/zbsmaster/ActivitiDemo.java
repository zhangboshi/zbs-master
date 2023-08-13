package com.example.zbsmaster;

import com.example.zbsmaster.entity.Evection;
import com.sun.glass.ui.Window;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangboshi01 <zhangboshi01@inspur.com>
 * @date 2023/8/1
 */
public class ActivitiDemo {
    /**
     * 部署流程定义
     */
    @Test
    public void testDeployment(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/Leave.bpmn20.xml")
                .addClasspathResource("processes/Leave.png")
                .name("请假流程")
                .deploy();
        System.out.println("流程部署ID："+deploy.getId());
        System.out.println("流程部署名称："+deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Level");
        System.out.println("流程定义id："+processInstance.getProcessDefinitionId());
        System.out.println("流程实例id："+processInstance.getId());
        System.out.println("当前活动id："+processInstance.getActivityId());
    }
    /**
     * 任务查询
     * 查询当前个人待执行任务
     */
    @Test
    public void testFindPersonalTaskList(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //根据流程key和任务负责人查询任务
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("Level").list();
        for (Task task :
                taskList) {
            System.out.println("流程实例id:"+task.getProcessDefinitionId());
            System.out.println("任务id:"+task.getId());
            System.out.println("任务负责人"+task.getAssignee());
            System.out.println("任务名称："+task.getName());
        }
    }
    /**
     * 任务处理
     */
    @Test
    public void testCompleteTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("Level").list();
        for (Task task :
                taskList) {
            taskService.complete(task.getId());
        }
    }
    /**
     * 流程查询
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> definitionList = processDefinitionQuery.processDefinitionKey("Level")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        for (ProcessDefinition definition :
                definitionList) {
            System.out.println("流程定义id="+definition.getId());
            System.out.println("流程定义name="+definition.getName());
            System.out.println("流程定义key="+definition.getKey());
            System.out.println("流程定义version="+definition.getVersion());
            System.out.println("流程部署ID="+definition.getDeploymentId());
        }

    }
    /**
     * 流程查询
     * 查询流程实例
     */
    @Test
    public void queryProcessInstance(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("Level")
                .list();
        for (ProcessInstance processInstance : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id："
                    + processInstance.getProcessInstanceId());
            System.out.println("所属流程定义id："
                    + processInstance.getProcessDefinitionId());
            System.out.println("是否执行完成：" + processInstance.isEnded());
            System.out.println("是否暂停：" + processInstance.isSuspended());
            System.out.println("当前活动标识：" + processInstance.getActivityId());
            System.out.println("业务关键字：" + processInstance.getBusinessKey());
        }
    }
    /**
     * 流程历史信息查询
     */
    @Test
    public void findHistoryInfo(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();
        query.processInstanceId("2501");
        query.orderByHistoricActivityInstanceStartTime().asc();
        List<HistoricActivityInstance> list = query.list();
        System.out.println(list.size());
        for (HistoricActivityInstance hi : list) {
            System.out.println("============="+hi.getActivityId()+" START=============");
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("============="+hi.getActivityId()+" END=============");
        }
    }
    /**
     * 流程资源下载
     */
    @Test
    public void downBpmnFile() throws IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Level")
                .singleResult();
        String deploymentId = processDefinition.getDeploymentId();
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, processDefinition.getDiagramResourceName());
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName());
        File file_png = new File("d:/Level.png");
        File file_bpmn = new File("d:/Level.bpmn20.xml");
        FileOutputStream bpmnOut = new FileOutputStream(file_bpmn);
        FileOutputStream pngOut = new FileOutputStream(file_png);
        IOUtils.copy(pngInput, pngOut);
        IOUtils.copy(bpmnInput, bpmnOut);
        pngOut.close();
        bpmnOut.close();
        pngInput.close();
        bpmnInput.close();
    }
    /**
     * business key
     * 添加业务key
     */
    @Test
    public void addBusinessKey()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Level", "BusinessNO");
        System.out.println(processInstance.getBusinessKey());
    }
    /**
     * 启动流程的时候设置流程变量
     */
    @Test
    public void testStartProcessAndSetVariables(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        Evection evection = new Evection();
        evection.setDay(2d);
        variables.put("evection",evection);
        variables.put("employee","张三");
        variables.put("TeamLeader","里斯");
        variables.put("Manager","五一");
        runtimeService.startProcessInstanceByKey("Level",variables);
    }
    /**
     * 完成个人任务
     */
    @Test
    public void completeTaskByVariables(){
        completeTask("张三");
        completeTask("里斯");
        completeTask("五一");
    }
    private void completeTask(String assignee){
        String key="Level";
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();
        System.out.println(task+"--------------------------------");
        if(task!=null){
            taskService.complete(task.getId());
            System.out.println(task.getId()+"-----任务负责人"+task.getAssignee()+"----任务已完成");
        }
    }
}
