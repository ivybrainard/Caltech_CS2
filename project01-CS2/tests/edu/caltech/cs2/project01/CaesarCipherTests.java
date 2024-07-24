package edu.caltech.cs2.project01;

import edu.caltech.cs2.helpers.*;
import edu.caltech.cs2.interfaces.IStyleTests;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static edu.caltech.cs2.project01.Project01TestOrdering.classSpecificTestLevel;
import static edu.caltech.cs2.project01.Project01TestOrdering.sanityTestLevel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestExtension.class)
@Tag("D")
public class CaesarCipherTests {
    String STRING_SOURCE = "src/edu/caltech/cs2/project01/CaesarCipher.java";

    @DisplayName("Style")
    @Nested
    class StyleTests implements IStyleTests {
        @Override
        public String getSource() {
            return STRING_SOURCE;
        }

        @Override
        public Class<?> getClazz() {
            return CaesarCipher.class;
        }

        @Override
        public List<String> getPublicInterface() {
            return List.of("rot",
                    "findIndexInAlphabet",
                    "rot",
                    "main");
        }

        @Override
        public int getMaxFields() {
            return 1;
        }

        @Override
        public List<String> methodsToBanSelf() {
            return List.of();
        }
    }

    @DisplayName("General Functionality")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    class SanityTests {
        @Order(sanityTestLevel)
        @DisplayName("Test findIndexInAlphabet(char)")
        @TestDescription("This test makes sure the findIndexInAlphabet method returns the right index" +
                " every letter in the alphabet.")
        @ParameterizedTest
        @ValueSource(chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'})
        public void testFindIndexInAlphabet(char c) {
            Assertions.assertEquals(c, CaesarCipher.ALPHABET[CaesarCipher.findIndexInAlphabet(c)]);
        }

        @Order(sanityTestLevel + 1)
        @DisplayName("Test findIndexInAlphabet(char) for non-alphabet characters")
        @TestDescription("This test makes sure the findIndexInAlphabet method returns -1 for invalid characters" +
                " such as lowercase letters and non-alphabet characters.")
        @TestHint("Make sure you are returning -1 for any character that is not an uppercase letter.")
        @ParameterizedTest
        @ValueSource(chars = { 'a', '+', '-', 'z' })
        public void testFindIndexInAlphabetDoesntExist(char c) {
            Assertions.assertEquals(-1, CaesarCipher.findIndexInAlphabet(c));
        }

        @Order(sanityTestLevel + 2)
        @ParameterizedTest
        @DisplayName("Test rot(char, int)")
        @TestDescription("This test makes sure the rot method works with character input.")
        @DependsOn("findIndexInAlphabet")
        @CsvSource({
                "A, 1, B",
                "A, 10, K",
                "B, 1, C",
                "C, 1, D",
                "D, 1, E",
                "E, 1, F",
                "F, 1, G",
                "G, 1, H",
                "H, 1, I",
                "I, 1, J",
                "J, 1, K",
                "K, 1, L",
                "L, 1, M",
                "M, 1, N",
                "N, 1, O",
                "O, 1, P",
                "P, 1, Q",
                "Q, 1, R",
                "R, 1, S",
                "S, 1, T",
                "T, 1, U",
                "U, 1, V",
                "V, 1, W",
                "W, 1, X",
                "X, 1, Y",
                "Y, 1, Z",
                "Z, 1, A",
                "Z, 2, B",
                "Z, 26, Z",
        })
        public void testRotCharacter(char c, int rot, char result) {
            Assertions.assertEquals(result, CaesarCipher.rot(c, rot));
        }

        @Order(sanityTestLevel + 3)
        @DisplayName("Test rot(String, int)")
        @TestDescription("This test makes sure the rot method works with String input.")
        @TestHint("Try to use your character version of rot.")
        @DependsOn("rot")
        @ParameterizedTest
        @CsvSource({
                "A, 1, B",
                "B, 1, C",
                "C, 1, D",
                "D, 1, E",
                "E, 1, F",
                "F, 1, G",
                "G, 1, H",
                "H, 1, I",
                "I, 1, J",
                "J, 1, K",
                "K, 1, L",
                "L, 1, M",
                "M, 1, N",
                "N, 1, O",
                "O, 1, P",
                "P, 1, Q",
                "Q, 1, R",
                "R, 1, S",
                "S, 1, T",
                "T, 1, U",
                "U, 1, V",
                "V, 1, W",
                "W, 1, X",
                "X, 1, Y",
                "Y, 1, Z",
                "Z, 1, A",
                "Z, 2, B",
                "Z, 26, Z",
                "ABCD, 5, FGHI"
        })
        public void testRotString(String s, int rot, String result) {
            Assertions.assertEquals(result, CaesarCipher.rot(s, rot));
        }

        @Order(sanityTestLevel + 4)
        @ParameterizedTest
        @DisplayName("Test main()")
        @TestDescription("This test runs the provided main function to ensure the functionality of the Cipher.")
        @CsvSource({
                "HELLO, HELLO, 0",
                "ATTACK, HAAHJR, 7",
                "TEAM, DOKW, 10"
        })
        public void testMain(String cipher, String expected, int amount) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            byte[] input = (cipher + "\n" + amount).getBytes();
            InputStream fakeIn = new ByteArrayInputStream(input);
            System.setIn(fakeIn);
            CaesarCipher.main(null);
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
            String output = outputStream.toString();
            System.out.println(output);
            assertEquals("Type text to encrypt: Type a number to rotate by: " + expected + "\n", output.replaceAll("\r\n", "\n"));
        }
    }
}