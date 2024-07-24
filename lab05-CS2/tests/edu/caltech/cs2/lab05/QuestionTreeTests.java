package edu.caltech.cs2.lab05;

import edu.caltech.cs2.helpers.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.*;
import java.util.Scanner;

import static edu.caltech.cs2.lab05.Lab05TestOrdering.stressTestLevel;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestExtension.class)
public class QuestionTreeTests {

    @Order(stressTestLevel)
    @CaptureSystemOutput
    @Tag("A")
    @DisplayName("Testing Play()")
    @TestHint("Make sure you recursively traverse the entire tree until you arrive at a leaf node")
    @DependsOn({"Play()"})
    @ParameterizedTest(name = "Test play on QuestionTree constructed from input {0} against expected trace:\n{1}")
    @FileSource(
            inputs = {"trace0-input.txt",
                    "trace2-input.txt",
            },
            outputFiles = {"trace0.txt",
                    "trace2.txt",
            })
    public void testPlay(String filename, String expectedOutput, CaptureSystemOutput.OutputCapture capture){
        // The file that the QuestionTree will be constructed from
        String inputFile = null;
        // The input to be read as the QuestionTree game is playing
        String input = "";
        Scanner s = null;
        try {
            s = new Scanner(new File("tests/inputs/" + filename));
        } catch (java.io.FileNotFoundException e) {
            fail("Could not open file tests/inputs/" + filename + "with a Scanner");
        }

        if (s.hasNextLine()) {
            inputFile = s.nextLine();
        }
        while (s.hasNextLine()) {
            input = input + s.nextLine() + "\n";
        }
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        try {
            s = new Scanner(new File("tests/inputs/" + inputFile));
        } catch (java.io.FileNotFoundException e) {
            fail("Could not open file tests/inputs/" + inputFile + "with a Scanner");
        }
        QuestionTree tree = new QuestionTree(s);
        tree.play();


        assertEquals(expectedOutput.replace("\r\n", "\n").strip(), capture.toString().replace("\r\n", "\n").strip());



    }
}
