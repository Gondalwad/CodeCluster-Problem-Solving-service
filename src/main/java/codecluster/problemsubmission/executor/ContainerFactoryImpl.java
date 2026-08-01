package codecluster.problemsubmission.executor;

import codecluster.problemsubmission.exception.ProgrammingLanguageNotSupportedException;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.HostConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("containerFactoryImpl")
public class ContainerFactoryImpl implements ContainerFactory {

    private static final Logger log = LoggerFactory.getLogger(ContainerFactoryImpl.class);
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
                .withTty(false)
                .withCmd("tail", "-f", "/dev/null")
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();
        /// inspect container
        InspectContainerResponse inspect = dockerClient.inspectContainerCmd(container.getId()).exec();
        if (!Boolean.TRUE.equals(inspect.getState().getRunning())) {
            log.error("ContainerFActoryImpl : Failed to start container");
            throw new IllegalStateException(
                    "Container failed to start. Status: "
                            + inspect.getState().getStatus());
        }
        // Return the Container implementation instance initialized with containerId and language
        return new DockerContainerImpl(container.getId(), programmingLanguage, dockerClient);
    }

    @Override
    public void destroyContainer(String containerId) {
        try {
            dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String determineImage(short programmingLanguage) {
        return switch (programmingLanguage) {
            case 1,5 -> "gcc:latest";
            case 2 -> "python:3.10-slim";
            case 3 -> "eclipse-temurin:21-jdk";
            case 4 -> "node:18-alpine";
            default -> throw new ProgrammingLanguageNotSupportedException("Unsupported language code: " + programmingLanguage);
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