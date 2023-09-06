package com.example.zbsmaster;

import com.example.zbsmaster.entity.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangboshi01 <zhangboshi01@inspur.com>
 * @date 2023/8/3
 */
public class ActivitiDemo1 {
    /**
     * 部署流程定义
     */
    @Test
    public void testDeployment(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/Leave1.bpmn20.xml")
                .addClasspathResource("processes/Leave1.png")
                .name("请假流程")
                .deploy();
        System.out.println("流程部署ID："+deploy.getId());//12501
        System.out.println("流程部署名称："+deploy.getName());
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
        String key = "Leave1";
        // 4、流程变量的map
        Map<String, Object> variables = new HashMap<>();
        // 5、设置流程变量
        Evection evection = new Evection();
        // 6、设置请假日期
        evection.setDay(5d);
        // 7、把流程变量的pojo放入map
        variables.put("evection", evection);
        // 8、设定任务的负责人
        variables.put("employee", "小八");
        variables.put("Manager", "老李");
        // 9、启动流程
        runtimeService.startProcessInstanceByKey(key, variables);
    }

    /**
     * 查询组任务
     */
    @Test
    public void findGroupTaskList() {
        // 1、流程定义的Key
        String key = "Leave1";
        // 2、任务候选人
        String candidateUser = "组长1";
        // 3、获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 4、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 5、查询组任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(key)
                // 根据候选人查询任务
                .taskCandidateUser(candidateUser)
                .list();
        for (Task task : taskList) {
            System.out.println("============查询组任务============");
            System.out.println("流程实例ID=" + task.getProcessInstanceId());
            System.out.println("任务id=" + task.getId());//17502
            System.out.println("任务名称：" + task.getName());
            System.out.println("任务负责人=" + task.getAssignee());
        }
    }
    /**
     * 候选人绑定任务
     */
    @Test
    public void claimTask() {
        // 1、获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3、当前任务的id
        String taskId = "15002";
        // 4、任务候选人
        String candidateUser = "组长1";
        // 5、查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(candidateUser)
                .singleResult();
        if (task != null) {
            // 6、绑定任务
            taskService.claim(taskId, candidateUser);
            System.out.println("taskId-" + taskId + "-用户-" + candidateUser + "-绑定任务完成");
        }
    }
    /**
     * 候选人归还任务
     */
    @Test
    public void testAssigneeToGroupTask() {
        // 1、获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3、当前任务的id
        String taskId = "17502";
        // 4、任务负责人
        String assignee = "组长1";
        // 5、根据key 和负责人来查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if (task != null) {
            // 6、归还任务 ,就是把负责人设置为空
            taskService.setAssignee(taskId, null);
            System.out.println("taskId-" + taskId + "-用户-" + assignee + "-归还任务完成");
        }
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
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("Leave1").list();
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
        // 测试组任务时，只执行这一行代码
//        completeTask("小八");
//        completeTask("组长1");
        completeTask("老李");
    }
    private void completeTask(String assignee){
        String key="Leave1";
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();
        System.out.println(task+"--------------------------------");
        if(task!=null){
            taskService.complete(task.getId());
            System.out.println("任务ID:"+task.getId()+"-----任务负责人"+task.getAssignee()+"----任务已完成");
        }
    }
}
