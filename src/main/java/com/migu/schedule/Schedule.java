package com.migu.schedule;


import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.TaskInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
*类名和方法不能修改
 */
public class Schedule {

    // 节点列表
    private ArrayList<Node> nodeList = new ArrayList<Node>();
    // 挂起的任务列表
    private ArrayList<Task> hangTaskList = new ArrayList<Task>();

    // 节点类
   public class Node{

        public Node(int id){
           nodeId = id;
       }

        public int nodeId;

        public ArrayList<Task> taskList = new ArrayList<Task>();

        public void clear(){
           taskList.clear();
       }

   }

    public class Task{

        public int taskId;

        public int consumption;

        public Task(int id, int con){
           taskId = id;
           consumption = con;
       }
    }

    public int init() {

       // 清除任务
        for(Node node : nodeList){
            node.clear();
        }

        // 清除节点
        nodeList.clear();

        return ReturnCodeKeys.E001;
    }

    public int registerNode(int nodeId) {

       if(nodeId <= 0){
           return ReturnCodeKeys.E004;
       }else{
           for(Node node : nodeList){
               if(node.nodeId == nodeId){
                   return ReturnCodeKeys.E005;
               }
           }

           // 注册节点
           nodeList.add(new Node(nodeId));
           return ReturnCodeKeys.E003;
       }
    }

    public int unregisterNode(int nodeId) {

       if(nodeId <= 0){
           return ReturnCodeKeys.E004;
       }else{
           Node targetNode = null;
           for(Node node : nodeList){
               if(node.nodeId == nodeId){
                   targetNode = node;
                   break;
               }
           }

           //反注册节点
           if(targetNode != null){
                nodeList.remove(targetNode);
                for(Task task : targetNode.taskList){
                    hangTaskList.add(task);
                }
                targetNode.clear();

                return ReturnCodeKeys.E006;
           }else{
               return ReturnCodeKeys.E007;
           }

       }
    }


    public int addTask(int taskId, int consumption) {

       if(taskId <= 0){
           return ReturnCodeKeys.E009;
       }else{
            for(Task task : hangTaskList){
                if(task.taskId == taskId){
                    return ReturnCodeKeys.E010;
                }
            }

           // 添加到挂起列表
            hangTaskList.add(new Task(taskId, consumption));

            return ReturnCodeKeys.E008;
       }
    }


    public int deleteTask(int taskId) {

       if(taskId <= 0){
           return  ReturnCodeKeys.E009;
       }else{

           Task targetTask = null;
           for(Task task : hangTaskList){
               if(task.taskId == taskId){
                    targetTask = task;
                    break;
               }
           }

           // 在挂起列表找到
           if(targetTask != null){
               hangTaskList.remove(targetTask);
               return ReturnCodeKeys.E011;
           }

           // 查找节点
           targetTask = null;
           for(Node node : nodeList){
               for(Task task : node.taskList){
                    if(task.taskId == taskId){
                        targetTask = task;
                        break;
                    }
               }

               //在节点找到
               if(targetTask != null){
                   node.taskList.remove(targetTask);

                   return ReturnCodeKeys.E011;
               }
           }

           // 未找到
           return ReturnCodeKeys.E012;
       }
    }


    public int scheduleTask(int threshold) {

       if(threshold <= 0){
           return ReturnCodeKeys.E002;
       }else{
           // 获取所有task
           List<TaskInfo> allTasks = new ArrayList<TaskInfo>();
           queryTaskStatus(allTasks);

           // 获取节点数量,也就意味着需要把所有task分成几组
           int nodeCount = nodeList.size();

       }

       return ReturnCodeKeys.E014;
    }


    public int queryTaskStatus(List<TaskInfo> tasks) {

        if(tasks == null){
            return ReturnCodeKeys.E016;
        }else{
            tasks.clear();

            // 查询挂起列表
            for(Task task : hangTaskList){
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setNodeId(-1);
                taskInfo.setTaskId(task.taskId);
                tasks.add(taskInfo);
            }

            // 查询节点列表
            for(Node node : nodeList){
                for(Task task : node.taskList){
                    TaskInfo taskInfo = new TaskInfo();
                    taskInfo.setNodeId(node.nodeId);
                    taskInfo.setTaskId(task.taskId);
                    tasks.add(taskInfo);
                }
            }

            // 升序
            Collections.sort(tasks, new Comparator<TaskInfo>() {
                public int compare(TaskInfo o1, TaskInfo o2) {
                    return o1.getTaskId() - o2.getTaskId();
                }
            });

            return ReturnCodeKeys.E015;
        }
    }

}
