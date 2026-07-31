package codecluster.problemsubmission.executor;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.HostConfig;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("containerFactoryImpl")
public class ContainerFactoryImpl implements ContainerFactory {

    private final DockerClient dockerClient;

    public ContainerFactoryImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public Container buildContainer(short programmingLanguage) {
        String dockerImage = determineImage(programmingLanguage);
        // Ensure image exists before building container
        ensureImageExists(dockerImage);
        HostConfig hostConfig = HostConfig.newHostConfig()
                .withMemory(256L * 1024 * 1024)
                .withMemorySwap(256L * 1024 * 1024)
                .withCpuCount(1L)
                .withNetworkMode("none")
                .withAutoRemove(false);

        CreateContainerResponse container = dockerClient.createContainerCmd(dockerImage)
                .withHostConfig(hostConfig)
                .withTty(true)
                .withCmd("/bin/sh")
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        // Return the Container implementation instance initialized with containerId and language
        return new DockerContainerImpl(container.getId(), programmingLanguage, dockerClient);
    }

    @Override
    public void destroyContainer(String containerId) {
        try {
            dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        } catch (Exception e) {
            // Log error
        }
    }

    private String determineImage(short programmingLanguage) {
        return switch (programmingLanguage) {
            case 1 -> "openjdk:17-alpine";
            case 2 -> "python:3.10-slim";
            case 3, 4 -> "gcc:latest";
            case 5 -> "node:18-alpine";
            default -> throw new IllegalArgumentException("Unsupported language code: " + programmingLanguage);
        };
    }

    private void ensureImageExists(String imageName) {
        try {
            // Check if image exists locally
            dockerClient.inspectImageCmd(imageName).exec();
        } catch (NotFoundException e) {
            // Image missing -> Pull from Docker Hub
            try {
                System.out.println("Pulling image: " + imageName + "...");
                dockerClient.pullImageCmd(imageName)
                        .start()
                        .awaitCompletion(5, TimeUnit.MINUTES);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while pulling image " + imageName, ex);
            }
        }
    }



}