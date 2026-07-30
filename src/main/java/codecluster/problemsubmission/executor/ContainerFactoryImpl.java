package codecluster.problemsubmission.executor;

import org.springframework.stereotype.Component;

@Component("containerFactoryImpl")
public class ContainerFactoryImpl implements ContainerFactory {

    @Override
    public Container buildContainer(short programmingLanguage) {
        // Dummy Implementation
        System.out.println("Container Generated");
        return new ContainerImpl();
    }
}
