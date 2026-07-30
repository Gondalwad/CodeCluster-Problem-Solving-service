package codecluster.problemsubmission.executor;

import org.springframework.stereotype.Component;

@Component
public interface ContainerFactory {
    Container buildContainer(short programmingLanguage);
}
