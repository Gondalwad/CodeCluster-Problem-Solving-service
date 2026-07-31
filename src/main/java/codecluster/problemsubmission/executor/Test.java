package codecluster.problemsubmission.executor;

import codecluster.problemsubmission.dto.ExecuteCodeDto;
import codecluster.problemsubmission.dto.TestCaseResponseDto;
import codecluster.problemsubmission.model.CodeSnippet;
import codecluster.problemsubmission.model.TestCase;
import codecluster.problemsubmission.util.CodeExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Test {

    final ContainerFactory containerFactory;

    @Autowired
    public Test(@Qualifier("containerFactoryImpl") ContainerFactory containerFactory) {
        this.containerFactory = containerFactory;
    }

    /// method executing actual task of the class
    public CodeExecutionResult test(ExecuteCodeDto executeCodeDto, List<TestCase> testCases, CodeSnippet codeSnippet){
        /// Gets container for programming language id
        Container container = containerFactory.buildContainer(executeCodeDto.getProgrammingLanguageId());

        /// saves returned string whether it is error or output
        return container.executeProgram(executeCodeDto.getProgram(), codeSnippet.getDriverCode(), testCases);

    }



}