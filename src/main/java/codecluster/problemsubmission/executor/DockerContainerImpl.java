package codecluster.problemsubmission.executor;



import codecluster.problemsubmission.dto.TestCaseResponseDto;
import codecluster.problemsubmission.enums.SubmissionStatus;
import codecluster.problemsubmission.exception.ProgrammingLanguageNotSupportedException;
import codecluster.problemsubmission.model.TestCase;
import codecluster.problemsubmission.util.CodeExecutionResult;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DockerContainerImpl implements Container {

    private static final Logger log = LoggerFactory.getLogger(DockerContainerImpl.class);

    private final String containerId;
    private final short programmingLanguage;
    private final DockerClient dockerClient;

    public DockerContainerImpl(String containerId, short programmingLanguage, DockerClient dockerClient) {
        this.containerId = containerId;
        this.programmingLanguage = programmingLanguage;
        this.dockerClient = dockerClient;
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    /**
     * This method executes manages the entire execution of code with helper methods
     * stops the execution if any error occurred and respond to user accordingly.
     * @param userCode   The code submitted by the user/student
     * @param driverCode The harness/driver code that feeds input and captures output
     * @param testCases  The list of test cases (stdin, expected output)
     * @return
     */
    @Override
    public CodeExecutionResult executeProgram(String userCode, String driverCode, List<TestCase> testCases) {
        List<TestCaseResponseDto> testCaseResults = new ArrayList<>();
        int passedCount = 0;

        try {
            /// Writes Source Code Files into Container
            String filename = getFileName(programmingLanguage);
            String fullSource = combineCode(userCode, driverCode, programmingLanguage);

            createFileInContainer(filename, fullSource);


            /// Compile the code for compiled languages
            String compileCommand = getCompileCommand(programmingLanguage, filename);
            if (compileCommand != null) {
                ExecResult compileResult = runCommandInContainer(compileCommand, null, 30); // 10s compile limit
                if (compileResult.exitCode != 0) {
                    return buildResult(SubmissionStatus.COMPILE_TIME_ERROR, 0, testCases.size(),
                            compileResult.stderr, 0, testCaseResults);
                }
            }


            /// Executes Given Test Cases
            String runCommand = getRunCommand(programmingLanguage, filename);
            long totalExecutionTime = 0;

            for (TestCase tc : testCases) {
                long startTime = System.currentTimeMillis();

                /// Pass testcase input into standard input
                ExecResult execResult = runCommandInContainer(runCommand, tc.getInput(), 5); // 5s TLE limit

                long timeTaken = System.currentTimeMillis() - startTime;
                totalExecutionTime += timeTaken;

                /// Handle Time Limit Exceeded (TLE) or Runtime Errors
                if (execResult.isTimeout) {
                    testCaseResults.add(
                            new TestCaseResponseDto(
                                    tc.getTestCaseId(),
                                    tc.getDisplayOrder(),
                                    tc.getInput(),
                                    " ",
                                    tc.getExpectedOutput(),
                                    false,
                                    SubmissionStatus.TIME_LIMIT_EXCEEDED.getCode())
                    );

                    return buildResult(SubmissionStatus.TIME_LIMIT_EXCEEDED, passedCount, testCases.size(),
                            "Time limit exceeded on test case", totalExecutionTime, testCaseResults);
                }

                if (execResult.exitCode != 0) {
                    testCaseResults.add(
                            new TestCaseResponseDto(
                                    tc.getTestCaseId(),
                                    tc.getDisplayOrder(),
                                    tc.getInput(),
                                    " ",
                                    tc.getExpectedOutput(),
                                    false,
                                    SubmissionStatus.RUNTIME_EXCEPTION.getCode())
                    );
                    return buildResult(SubmissionStatus.RUNTIME_EXCEPTION, passedCount, testCases.size(),
                            execResult.stderr, totalExecutionTime, testCaseResults);
                }

                /// Clean outputs for comparison (trim trailing whitespaces & newlines)
                String actualOutput = execResult.stdout.trim();
                String expectedOutput = tc.getExpectedOutput().trim();
                /// This mechanism has to change in future as it will not check the order of output
                boolean isPassed = actualOutput.equals(expectedOutput);

                if (isPassed) {
                    passedCount++;
                }
                ///  at last creation of testcase response dto to add into list
                new TestCaseResponseDto(
                        tc.getTestCaseId(),
                        tc.getDisplayOrder(),
                        tc.getInput(),
                        actualOutput,
                        tc.getExpectedOutput(),
                        isPassed,
                        isPassed ? SubmissionStatus.ACCEPTED.getCode() : SubmissionStatus.WRONG_ANSWER.getCode()
                );

                ///  halt the execution of rest of the testcases is testcase is not sample and failed
                if (!isPassed && !tc.getSample()){
                    break;
                }
            }

            SubmissionStatus finalStatus = (passedCount == testCases.size()) ?
                    SubmissionStatus.ACCEPTED : SubmissionStatus.WRONG_ANSWER;

            return buildResult(finalStatus, passedCount, testCases.size(), null, totalExecutionTime, testCaseResults);

        } catch (Exception e) {
            log.error("Execution failed inside container {}", containerId, e);
            ///  will be caught by GlobalExceptionHandler
            throw new RuntimeException("Something went Wrong ! Please Try Again Later ");
        }
    }


    /// HELPER METHODS: Code Formatting & Commands

    private String getFileName(short language) {
        return switch (language) {
            case 1 -> "solution.cpp";
            case 2 -> "solution.py";
            case 3 -> "Main.java";
            case 4 -> "solution.js";
            case 5 -> "solution.c";
            default -> throw new ProgrammingLanguageNotSupportedException("Unsupported language: " + language);
        };
    }

    /// method which combines the code of user and driver
    private String combineCode(String userCode, String driverCode, short language) {
        if (driverCode == null || driverCode.isBlank()) {
            return userCode;
        }
        // Our driver code will container "///UserCode" string where we are supposed insert the code of user
        return driverCode.replaceFirst("///UserCode", userCode);
    }

    ///  returns command to compile such as javac for java and g++ for cpp
    private String getCompileCommand(short language, String filename) {
        return switch (language) {
            case 1 -> "g++ -O2 " + filename + " -o solution";
            case 2, 4 -> null; // Python (2) and JavaScript (4) do not require compilation
            case 3 -> "javac " + filename;
            case 5 -> "gcc -O2 " + filename + " -o solution";
            default -> throw new ProgrammingLanguageNotSupportedException("Unsupported language ID: " + language);
        };
    }

    ///  return command which will run the code compiled by getting compile command through @getCompileCommand()
    private String getRunCommand(short language, String filename) {
        return switch (language) {
            case 1,5 -> "./solution";
            case 2 -> "python3 " + filename;
            case 3 -> "java Main";
            case 4 -> "node " + filename;
            default -> throw new ProgrammingLanguageNotSupportedException("Unsupported language ID: " + language);
        };
    }


    /// HELPER METHODS: Docker Execution Mechanics
    private void createFileInContainer(String filename, String content) {
        // Write source code directly to container filesystem using shell redirection
        String base64Content = java.util.Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
        String command = String.format("sh -c \"echo '%s' | base64 -d > %s\"", base64Content, filename);
        runCommandInContainer(command, null, 5);
    }

    /// this is where the command with our input will go
    private ExecResult runCommandInContainer(String command, String stdinInput, long timeoutSeconds) {
        // 1. Use docker-java's built-in stream handling callbacks
        ByteArrayOutputStream stdoutStream = new ByteArrayOutputStream();
        ByteArrayOutputStream stderrStream = new ByteArrayOutputStream();

        try (
                InputStream stdinStream = stdinInput != null && !stdinInput.isEmpty() ?
                        new ByteArrayInputStream(stdinInput.getBytes(StandardCharsets.UTF_8)) : null;
                ExecStartResultCallback callback = new ExecStartResultCallback(stdoutStream, stderrStream)
        ) {

            ExecCreateCmdResponse exec = dockerClient.execCreateCmd(containerId)
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .withAttachStdin(stdinStream != null)
                    .withCmd("sh", "-c", command) // TIP: Change 'command' to 'command < input.txt' if you switch to file redirection
                    .exec();

            var startCmd = dockerClient.execStartCmd(exec.getId());
            if (stdinStream != null) {
                startCmd = startCmd.withStdIn(stdinStream);
            }

            // 2. Execute and wait
            boolean completed = startCmd.exec(callback).awaitCompletion(timeoutSeconds, TimeUnit.SECONDS);

            if (!completed) {
                return new ExecResult(143, "", "Time Limit Exceeded", true);
            }

            int exitCode = dockerClient.inspectExecCmd(exec.getId()).exec().getExitCodeLong().intValue();

            return new ExecResult(
                    exitCode,
                    stdoutStream.toString(StandardCharsets.UTF_8),
                    stderrStream.toString(StandardCharsets.UTF_8),
                    false
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            log.error("Execution interrupted: {}", e.getMessage());
            return new ExecResult(1, "", "Execution Interrupted", false);
        } catch (Exception e) {
            log.error("Container execution error: {}", e.getMessage());
            return new ExecResult(1, "", e.getMessage(), false);
        }
    }

    ///  generates Code codeExecutionResult
    private CodeExecutionResult buildResult(SubmissionStatus status, int passed, int total,
                                            String errorDetails, long timeMs, List<TestCaseResponseDto> results) {
        CodeExecutionResult result = new CodeExecutionResult();
        result.setNoOfPassedTestCases(passed);
        result.setTestcases(results);
        result.setExecutionTimeInMs((int)timeMs);
        result.setCodeClusterErrorCode(status.getCode());
        result.setSuccessful(passed==total);
        result.setMessage(errorDetails);
        return result;
    }

    private record ExecResult(int exitCode, String stdout, String stderr, boolean isTimeout) {}
}