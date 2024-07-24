package edu.caltech.cs2.project02;

import edu.caltech.cs2.helpers.*;
import edu.caltech.cs2.interfaces.IStyleTests;
import edu.caltech.cs2.project02.choosers.EvilHangmanChooser;
import edu.caltech.cs2.project02.guessers.ConsoleHangmanGuesser;
import edu.caltech.cs2.project02.interfaces.IHangmanChooser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import edu.caltech.cs2.helpers.TestDescription;
import edu.caltech.cs2.helpers.TestExtension;
import edu.caltech.cs2.helpers.TestHint;


import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

import static edu.caltech.cs2.project02.Project02TestOrdering.*;
import static edu.caltech.cs2.project02.Project02TestOrdering.stressTestLevel;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestExtension.class)
@Tag("B")
public class EvilHangmanChooserTests {
    private static String EVIL_SOURCE = "src/edu/caltech/cs2/project02/choosers/EvilHangmanChooser.java";

    @DisplayName("Style")
    @Nested
    class StyleTests implements IStyleTests {
        @Override
        public String getSource() {
            return EVIL_SOURCE;
        }

        @Override
        public Class<?> getClazz() {
            return EvilHangmanChooser.class;
        }

        @Override
        public List<String> getPublicInterface() {
            return List.of("makeGuess",
                    "isGameOver",
                    "getPattern",
                    "getGuesses",
                    "getGuessesRemaining",
                    "getWord");
        }

        @Override
        public int getMaxFields() {
            return 5;
        }

        @Override
        public List<String> methodsToBanSelf() {
            return List.of();
        }

        @Order(classSpecificTestLevel)
        @DisplayName("Does not use or import disallowed classes")
        @TestHint("Remember that you're not allowed to use anything in java.util except SortedSet, TreeSet, Scanner, Map and Random! Do not import java.util.*!")
        @Test
        @Override
        public void testForInvalidClasses() {
            List<String> regexps = List.of("java.util.Iterator",
                    "java.util.function.Function",
                    "java.util.List",
                    "java.util.Arrays",
                    "java.util.Random",
                    "java\\.lang\\.reflect",
                    "java\\.io\\.(?!File|FileNotFoundException)");
            Inspection.assertNoImportsOf(getSource(), regexps);
            Inspection.assertNoUsageOf(getSource(), regexps);
        }

    }

    public void runTestGame(Class<? extends IHangmanChooser> clazz, int wordLength, int wrongAnswersAllowed, String guesses) throws FileNotFoundException {
        Constructor<? extends IHangmanChooser> constructor = Reflection.getConstructor(clazz, int.class, int.class);
        HangmanGame.playGame(
                Reflection.newInstance(constructor, wordLength, wrongAnswersAllowed),
                new ConsoleHangmanGuesser(new Scanner(String.join("\n", guesses.split(""))))
        );
    }

    @DisplayName("Basic Functionality Tests")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    class EHCFunctionalityTests {
        @Order(sanityTestLevel)
        @DisplayName("Does not use Map as a field")
        @Test
        public void testNoMapFieldEHC() {
            Reflection.assertFieldsEqualTo(EvilHangmanChooser.class, Map.class, 0);
        }

        @Order(sanityTestLevel+1)
        @DisplayName("There is no map field in EvilHangmanChooser")
        @Test
        @TestHint("You should not be storing the map as a field! Think about if the map is used in more than one method.")
        public void testNoMapField() {
            if (Reflection.getFields(EvilHangmanChooser.class).filter(Reflection.hasType(Map.class)).findAny().isPresent()) {
                fail();
            }
        }

        @Order(sanityTestLevel+2)
        @DisplayName("Expected constructor exceptions for EvilHangmanChooser")
        @Test
        public void testExceptionsViolatedInEvilConstructor() {
            Constructor c = Reflection.getConstructor(EvilHangmanChooser.class, int.class, int.class);
            assertThrows(IllegalArgumentException.class, () -> Reflection.newInstance(c, -1, 3));
            assertThrows(IllegalArgumentException.class, () -> Reflection.newInstance(c, 3, -1));
            assertThrows(IllegalStateException.class, () -> Reflection.newInstance(c, Integer.MAX_VALUE, 3));
        }

        @Order(sanityTestLevel+3)
        @DisplayName("Expected makeGuess() exceptions for characters that aren't lower case for EvilHangmanChooser")
        @Test
        @DependsOn("makeGuess")
        public void testMakeGuessLowercaseCharExceptionsInEvil() {
            Constructor c = Reflection.getConstructor(EvilHangmanChooser.class, int.class, int.class);
            EvilHangmanChooser chooser = Reflection.newInstance(c, 3, 1);
            Method m = Reflection.getMethod(EvilHangmanChooser.class, "makeGuess", char.class);
            IntStream.range(0, 20).forEach(i -> assertThrows(IllegalArgumentException.class, () -> Reflection.invoke(m, chooser, (char) ('a' - (i + 1)))));
            IntStream.range(0, 20).forEach(i -> assertThrows(IllegalArgumentException.class, () -> Reflection.invoke(m, chooser, (char) ('z' + (i + 1)))));
        }

        @Order(sanityTestLevel + 4)
        @DisplayName("Expected makeGuess() exceptions for characters that have been previously guessed for EvilHangmanChooser")
        @TestDescription("This test makes sure that the makeGuess method throws exceptions for characters that have been guessed previously")
        @DependsOn("makeGuess")
        @Test
        public void testMakeGuessRepeatedCharExceptionsInEvil() throws FileNotFoundException {
            EvilHangmanChooser chooser = new EvilHangmanChooser(5, 5);
            chooser.makeGuess('v');
            assertThrows(IllegalArgumentException.class, () -> chooser.makeGuess('v'));
        }

        @Order(sanityTestLevel + 5)
        @DisplayName("Expected makeGuess() exceptions if no guesses left for EvilHangmanChooser")
        @TestDescription("This test makes sure that the makeGuess method throws exceptions if number of guesses left is not at least one")
        @DependsOn("makeGuess")
        @Test
        public void testMakeTooManyGuessExceptionsInEvil() throws FileNotFoundException {
            EvilHangmanChooser chooser = new EvilHangmanChooser(22, 2);
            chooser.makeGuess('x');
            chooser.makeGuess('y');
            assertThrows(IllegalStateException.class, () -> chooser.makeGuess('z'));
        }
    }



    @DisplayName("Complete Functionality Tests")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    class EHCCompleteFunctionalityTests {
        @Order(stressTestLevel)
        @DisplayName("Expected game end after revealing word for EvilHangmanChooser")
        @TestDescription("This test checks to make sure that the game ends after the word"+
                " has been revealed.")
        @DependsOn("makeGuess")
        @Test
        public void testGameEndOnRevealWordEHC() throws FileNotFoundException {
            EvilHangmanChooser chooser = new EvilHangmanChooser(8, 1);
            chooser.getWord();
            assertThrows(IllegalStateException.class, () -> chooser.makeGuess('a'));
        }

        @Order(stressTestLevel+1)
        @DisplayName("Test EvilHangmanChooser Full Game")
        @ParameterizedTest(name = "{0}")
        @FileSource(
                inputs = {
                        "{word length = 3, max wrong guesses = 10, guesses = abcdefghijklmnopqrstuvwxyz}",
                        "{word length = 3, max wrong guesses = 26, guesses = abcdefghijklmnopqrstuvwxyz}",
                        "{word length = 5, max wrong guesses = 26, guesses = abcdefghijklmnopqrstuvwxyz}",
                        "{word length = 5, max wrong guesses = 1, guesses = a}",
                        "{word length = 5, max wrong guesses = 10, guesses = abcdefghijklmnopqrstuvwxyz}",
                        "{word length = 15, max wrong guesses = 15, guesses = aeioubcdfghjklmnpqrstvwxyz}",
                        "{word length = 20, max wrong guesses = 15, guesses = aeioubcdfghjklmnpqrstvwxyz}",
                        "{word length = 8, max wrong guesses = 14, guesses = aeioubcdfghjklmnpqrstvwxyz}",
                        "{word length = 20, max wrong guesses = 1, guesses = u}",
                        "{word length = 7, max wrong guesses = 26, guesses = abcdefghijklmnopqrstuvwxyz}",
                        "{word length = 7, max wrong guesses = 5, guesses = tusor}",
                        "{word length = 7, max wrong guesses = 8, guesses = tusoraeiz}",
                        "{word length = 7, max wrong guesses = 7, guesses = ziearosut}",
                        "{word length = 4, max wrong guesses = 11, guesses = etaoinshrlud}",
                        "{word length = 12, max wrong guesses = 7, guesses = etaoinshrlud}",
                },
                outputFiles = {
                        "trace0-evil.txt",
                        "trace1-evil.txt",
                        "trace2-evil.txt",
                        "trace3-evil.txt",
                        "trace4-evil.txt",
                        "trace5-evil.txt",
                        "trace6-evil.txt",
                        "trace7-evil.txt",
                        "trace8-evil.txt",
                        "trace9-evil.txt",
                        "trace10-evil.txt",
                        "trace11-evil.txt",
                        "trace12-evil.txt",
                        "trace13-evil.txt",
                        "trace14-evil.txt",
                }
        )
        @TestDescription("This test runs the EvilHangmanChooser Game and tests the complete"+
                " functionality of it.")
        @CaptureSystemOutput
        public void testPlayGameWithEvilChooser(Map<String, String> arguments, String expectedOutput, CaptureSystemOutput.OutputCapture capture) throws FileNotFoundException {
            // Parse arguments
            int length = Integer.parseInt(arguments.get("word length"));
            int wrongAllowed = Integer.parseInt(arguments.get("max wrong guesses"));
            String guesses = arguments.get("guesses");

            // Run the actual game
            NoSuchElementException toThrow = null;
            try {
                runTestGame(EvilHangmanChooser.class, length, wrongAllowed, guesses);
            } catch (NoSuchElementException e) {
                toThrow = e;
            }
            assertEquals(expectedOutput.replace("\r\n", "\n").strip(), capture.toString().replace("\r\n", "\n").strip());
            if (toThrow != null) {
                throw toThrow;
            }
        }
    }
}
