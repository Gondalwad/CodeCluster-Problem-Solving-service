package codecluster.problemsubmission.executor;

import codecluster.problemsubmission.model.TestCase;
import codecluster.problemsubmission.util.CodeExecutionResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("containerImpl")
public class ContainerImpl implements Container{

    @Override
    public CodeExecutionResult executeProgram(List<TestCase> testCases) {
        // Dummy Implementation
        System.out.println("Programm Executed With TestCases");
        return null;
    }
}
