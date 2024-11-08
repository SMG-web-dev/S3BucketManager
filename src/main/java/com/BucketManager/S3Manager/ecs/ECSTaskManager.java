package com.BucketManager.S3Manager.ecs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ECSTaskManager {

    private final EcsClient ecsClient;
    private final String clusterName = "s3manager";
    private final String taskDefinition = "S3BucketsManagerDeploy:1";

    @Autowired
    public ECSTaskManager(EcsClient ecsClient) {
        this.ecsClient = ecsClient;
    }

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> futureTask;

    public void startTaskIfNeeded() {
        if (futureTask != null) {
            futureTask.cancel(true);
        }

        // Reanudar tarea
        startTask();

        // Programar la detención de la tarea después de 30 segundos de inactividad
        futureTask = scheduler.schedule(this::stopTask, 30, TimeUnit.SECONDS);
    }

    private void startTask() {
        RunTaskRequest runTaskRequest = RunTaskRequest.builder()
                .cluster(clusterName)
                .taskDefinition(taskDefinition)
                .count(1)
                .launchType(LaunchType.FARGATE)
                .build();

        ecsClient.runTask(runTaskRequest);
        System.out.println("Tarea iniciada");
    }

    private void stopTask() {
        ListTasksRequest listTasksRequest = ListTasksRequest.builder()
                .cluster(clusterName)
                .build();

        ListTasksResponse listTasksResponse = ecsClient.listTasks(listTasksRequest);
        for (String taskArn : listTasksResponse.taskArns()) {
            StopTaskRequest stopTaskRequest = StopTaskRequest.builder()
                    .cluster(clusterName)
                    .task(taskArn)
                    .reason("Inactividad")
                    .build();

            ecsClient.stopTask(stopTaskRequest);
            System.out.println("Tarea detenida por inactividad");
        }
    }
}



