package com.BucketManager.S3Manager.ecs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

import java.util.List;

@Service
public class ECSTaskManager {

    private final EcsClient ecsClient;
    private final String clusterName = "s3manager"; // Nombre del cluster ECS
    private final String taskDefinition = "S3BucketsManagerDeploy"; // Nombre de la definición de la tarea

    @Autowired
    public ECSTaskManager(EcsClient ecsClient) {
        this.ecsClient = ecsClient;
    }

    // Inicia la tarea si es necesario
    public void startTaskIfNeeded() {
        // Verificar si la tarea ya está en ejecución antes de iniciarla
        if (!isTaskRunning()) {
            startTask();
        } else {
            System.out.println("La tarea ya está en ejecución.");
        }
    }

    // Método para iniciar la tarea
    private void startTask() {
        NetworkConfiguration networkConfiguration = NetworkConfiguration.builder()
                .awsvpcConfiguration(AwsVpcConfiguration.builder()
                        .subnets("subnet-060e0f49739e88983") // Reemplaza con tu subnet
                        .securityGroups("sg-0119cb2788ac854b6\n") // Reemplaza con tu Security Group
                        .assignPublicIp(AssignPublicIp.ENABLED) // Habilitar IP pública
                        .build())
                .build();

        // Crear la solicitud de ejecución de tarea
        RunTaskRequest runTaskRequest = RunTaskRequest.builder()
                .cluster(clusterName)
                .taskDefinition(taskDefinition)
                .count(1)
                .launchType(LaunchType.EC2)
                .networkConfiguration(networkConfiguration)
                .build();

        // Ejecutar la tarea
        try {
            ecsClient.runTask(runTaskRequest);
            System.out.println("Tarea iniciada correctamente.");
        } catch (EcsException e) {
            System.out.println("Error al iniciar la tarea: " + e.getMessage());
        }
    }

    // Detiene la tarea si está en ejecución
    public void stopTask() {
        List<String> taskArns = getRunningTasks();

        // Solo detener tareas si están en ejecución
        if (taskArns.isEmpty()) {
            System.out.println("No hay tareas en ejecución para detener.");
            return;
        }

        for (String taskArn : taskArns) {
            StopTaskRequest stopTaskRequest = StopTaskRequest.builder()
                    .cluster(clusterName)
                    .task(taskArn)
                    .reason("Inactividad")
                    .build();

            try {
                ecsClient.stopTask(stopTaskRequest);
                System.out.println("Tarea detenida correctamente.");
            } catch (EcsException e) {
                System.out.println("Error al detener la tarea: " + e.getMessage());
            }
        }
    }

    // Método para verificar si la tarea está en ejecución
    private boolean isTaskRunning() {
        List<String> taskArns = getRunningTasks();
        return !taskArns.isEmpty();
    }

    // Método para obtener las tareas en ejecución en el cluster
    private List<String> getRunningTasks() {
        ListTasksRequest listTasksRequest = ListTasksRequest.builder()
                .cluster(clusterName)
                .build();

        ListTasksResponse listTasksResponse = ecsClient.listTasks(listTasksRequest);
        return listTasksResponse.taskArns();
    }
}
