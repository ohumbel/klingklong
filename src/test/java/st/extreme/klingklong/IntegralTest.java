package st.extreme.klingklong;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import st.extreme.klingklong.util.Horn;
import st.extreme.klingklong.util.Temperature;

/**
 * Test a real life situation: start up two virtual machines and let them talk to each other
 */
public class IntegralTest {

  private static final String WAITING_FOR_REMOTE_MESSAGE = "waiting for remote";
  private static final String STOP_MESSAGE = "got a message: 'STOP'";
  private static final String DONE_SOON_MESSAGE = "got a message: 'My work is done soon'";

  // paths for the class path
  private Path resourcesPath;
  private Path mainPath;
  private Path testPath;

  // output redirect files
  private Path jvm1Ouptut;
  private Path jvm2Ouptut;

  @Before
  public void setUp() throws Exception {
    // determine class path elements for the subprocesses
    URL propertiesURL = Horn.class.getResource("/klingklong.properties");
    assertNotNull(propertiesURL);
    Path propertiesPath = Paths.get(propertiesURL.toURI());
    Path parentPath = propertiesPath.getParent();
    if (parentPath.endsWith("default")) {
      // IDE case
      resourcesPath = parentPath; // <project>/bin/default
      mainPath = resourcesPath.resolveSibling("main"); // <project>/bin/main
      testPath = resourcesPath.resolveSibling("test"); // <project>/bin/test
    } else {
      // gradle case
      parentPath = parentPath.getParent().getParent();
      assertTrue("expected parentPath (" + parentPath.toString() + ") to end with /build", parentPath.endsWith("build"));
      resourcesPath = parentPath.resolve("resources/main");
      mainPath = parentPath.resolve("classes/java/main");
      testPath = parentPath.resolve("classes/java/test");
      assertTrue("expected main path (" + mainPath.toString() + ") to end with /klingklong/build/classes/java/main",
          mainPath.toString().endsWith("/klingklong/build/classes/java/main"));
    }

    // determine the output redirection paths
    jvm1Ouptut = propertiesPath.resolveSibling("JVM1Output.log");
    ProcessConfig.FIRST.setOutputPath(jvm1Ouptut);
    jvm2Ouptut = propertiesPath.resolveSibling("JVM2Output.log");
    ProcessConfig.SECOND.setOutputPath(jvm2Ouptut);
  }

  @Test
  public void testTwoJVMs() throws Exception {
    // configure and start both subprocesses
    ProcessBuilder processBuilder1 = configureProcess(ProcessConfig.FIRST);
    ProcessBuilder processBuilder2 = configureProcess(ProcessConfig.SECOND);
    Process process1 = processBuilder1.start();
    TimeUnit.SECONDS.sleep(3);
    Process process2 = processBuilder2.start();

    // wait for both processes to end normally
    assertTrue(process1.waitFor(120, TimeUnit.SECONDS));
    assertTrue(process2.waitFor(120, TimeUnit.SECONDS));

    // check output
    Set<String> messagesAtLeastOnce = new HashSet<>();
    assertOutput(jvm1Ouptut, messagesAtLeastOnce);
    assertOutput(jvm2Ouptut, messagesAtLeastOnce);
    assertTrue(messagesAtLeastOnce.contains(WAITING_FOR_REMOTE_MESSAGE));
    assertTrue(messagesAtLeastOnce.contains(STOP_MESSAGE));
    assertTrue(messagesAtLeastOnce.contains(DONE_SOON_MESSAGE));
  }

  private ProcessBuilder configureProcess(ProcessConfig processConfig) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.redirectOutput(processConfig.getOutputPath().toFile());
    processBuilder.redirectErrorStream(true);
    String temperatureProperty = "-D".concat(Horn.TEMPERATURE_PROPERTY_NAME).concat("=").concat(Temperature.HOT.name());
    String classpath = resourcesPath.toString().concat(File.pathSeparator) //
        .concat(mainPath.toString()).concat(File.pathSeparator) //
        .concat(testPath.toString());
    processBuilder.command("java", "-Dfile.encoding=UTF-8", temperatureProperty, "-classpath", classpath, processConfig.getClassName());
    return processBuilder;
  }

  // TODO: handle the current case
  private void assertOutput(Path outputPath, Set<String> messagesAtLeastOnce) throws Exception {
    String line;
    List<String> lines = Files.readAllLines(outputPath);
    assertTrue("expected at least 11 lines, but only got " + lines.size() + ":\n" + lines, lines.size() >= 11);

    int index = 0;
    line = lines.get(index);
    assertTrue(line.contains("starting worker on JVM"));

    index++;
    line = lines.get(index);
    assertTrue(line.contains("creating kling") || line.contains("creating klong"));
    index++;
    line = lines.get(index);
    assertTrue(line.contains("endpoint is connecting ..."));

    index++;
    line = lines.get(index);
    if (line.contains(WAITING_FOR_REMOTE_MESSAGE) && line.endsWith("to accept a connection")) {
      messagesAtLeastOnce.add(WAITING_FOR_REMOTE_MESSAGE);
      index++;
    }

    line = lines.get(index);
    assertTrue(line.contains("endpoint is now connected"));

    index++;
    line = lines.get(index);
    assertTrue(line.contains("got a message: 'I am doing some work (0)'"));

    index++;
    line = lines.get(index);
    assertTrue(line.contains("got a message: 'I am doing some work (1)'"));

    index++;
    line = lines.get(index);
    assertTrue(line.contains("got a message: 'I am doing some work (2)'"));

    index++;
    line = lines.get(index);
    assertTrue(line.contains("got a message: 'I am doing some work (3)'"));

    // possible output 1 afer work:
    // [klingklong] got a message: 'My work is done soon'
    // [klingklong] got a message: 'STOP'
    // [klingklong] receiver got STOP signal
    // [klingklong] receiver thread is terminating now
    // [klingklong] endpoint is closing
    // [klingklong] sender thread is terminating now
    // [klingklong] endpoint is now closed

    // possible output 2 after work:
    // [klingklong] endpoint is closing
    // [klingklong] sender thread is terminating now
    // [klingklong] receiver got STOP signal
    // [klingklong] receiver thread is terminating now
    // [klingklong] endpoint is now closed

    // equired messages are:
    // - endpoint is closing
    // - receiver got STOP signal
    // - sender thread is terminating now
    // - receiver thread is terminating now
    // but the sequence cannot be predicted
    // and at the last position:
    // - endpoint is now closed

    boolean closing = false;
    boolean senderTerminates = false;
    boolean receiverTerminates = false;
    boolean gotStopSignal = false;

    while (index < lines.size() - 2) {
      index++;
      line = lines.get(index);
      if (line.contains(DONE_SOON_MESSAGE)) {
        messagesAtLeastOnce.add(DONE_SOON_MESSAGE);
      }
      if (line.contains(STOP_MESSAGE)) {
        messagesAtLeastOnce.add(STOP_MESSAGE);
      }
      if (line.contains("endpoint is closing")) {
        closing = true;
      }
      if (line.contains("receiver got STOP signal")) {
        gotStopSignal = true;
      }
      if (line.contains("thread is terminating now")) {
        if (line.contains("sender")) {
          senderTerminates = true;
        }
        if (line.contains("receiver")) {
          receiverTerminates = true;
        }
      }
    }

    assertTrue("no closing message found", closing);
    assertTrue("receiver got no STOP signal", gotStopSignal);
    assertTrue("no sender terminating message found", senderTerminates);
    assertTrue("no receiver terminating message found", receiverTerminates);

    // this always should be the last one
    index++;
    line = lines.get(index);
    assertTrue(line.endsWith("endpoint is now closed"));
  }

  private enum ProcessConfig {
    FIRST("st.extreme.klingklong.demo.JVM1"), SECOND("st.extreme.klingklong.demo.JVM2");

    private final String className;
    private Path outputPath;

    ProcessConfig(String className) {
      this.className = className;
    }

    void setOutputPath(Path outputPath) {
      this.outputPath = outputPath;
    }

    Path getOutputPath() {
      return outputPath;
    }

    String getClassName() {
      return className;
    }
  }
}
