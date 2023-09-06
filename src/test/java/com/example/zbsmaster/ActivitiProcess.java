package com.example.zbsmaster;

import com.example.zbsmaster.entity.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangboshi01 <zhangboshi01@inspur.com>
 * @date 2023/8/7
 */
public class ActivitiProcess {
    @Test
    public void testDeployment(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/Process.bpmn20.xml")
                .addClasspathResource("processes/Process.png")
                .name("审批流程")
                .deploy();
        System.out.println("流程部署ID："+deploy.getId());//17501
        System.out.println("流程部署名称："+deploy.getName());//审批流程
    }
    /**
     * 启动流程的时候设置流程变量
     */
    @Test
    public void testStartProcessAndSetVariablesAboutGroup() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、流程定义的Key
        String key = "Process";
        // 4、流程变量的map
        Map<String, Object> variables = new HashMap<>();
        // 8、设定任务的负责人
        variables.put("node1", "员工张");
        variables.put("node2", "经理张");
        variables.put("node3","总裁张");
        // 9、启动流程
        runtimeService.startProcessInstanceByKey(key, variables);
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
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("Process").list();
        System.out.println("当前个人待执行任务：");
        for (Task task :
                taskList) {
            System.out.println("流程实例id:"+task.getProcessDefinitionId());
            System.out.println("任务id:"+task.getId());
            System.out.println("任务负责人"+task.getAssignee());
            System.out.println("任务名称："+task.getName());
        }
    }
    /**
     * 执行任务
     */
    @Test
    public void completTaskByVariablesAboutGroup() {
        completeTask("员工张");
        completeTask("经理张");
        completeTask("总裁张");
    }
    private void completeTask(String assignee){
        String key="Process";
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee).list();
        for (Task task :
                taskList) {
            taskService.complete(task.getId());
            System.out.println("任务ID:"+task.getId()+"-----任务负责人"+task.getAssignee()+"----任务已完成");
        }

    }
}
