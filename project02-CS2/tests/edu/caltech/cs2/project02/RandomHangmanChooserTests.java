package edu.caltech.cs2.project02;

import edu.caltech.cs2.helpers.*;
import edu.caltech.cs2.interfaces.IStyleTests;
import edu.caltech.cs2.project02.choosers.RandomHangmanChooser;
import edu.caltech.cs2.project02.guessers.ConsoleHangmanGuesser;
import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import edu.caltech.cs2.helpers.TestDescription;
import edu.caltech.cs2.helpers.TestExtension;
import edu.caltech.cs2.helpers.TestHint;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;


import static edu.caltech.cs2.project02.Project02TestOrdering.*;
import static org.junit.jupiter.api.Assertions.*;





@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestExtension.class)
@Tag("C")
public class RandomHangmanChooserTests {
    private static String RANDOM_SOURCE = "src/edu/caltech/cs2/project02/choosers/RandomHangmanChooser.java";

    @DisplayName("Style")
    @Nested
    class StyleTests implements IStyleTests {
        @Override
        public String getSource() {
            return RANDOM_SOURCE;
        }

        @Override
        public Class<?> getClazz() {
            return RandomHangmanChooser.class;
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
            return 4;
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
                    "java\\.lang\\.reflect",
                    "java\\.io\\.(?!File|FileNotFoundException)");
            Inspection.assertNoImportsOf(getSource(), regexps);
            Inspection.assertNoUsageOf(getSource(), regexps);
        }

        @Order(classSpecificTestLevel)
        @DisplayName("Chosen word and random are the only final fields")
        @Test
        public void testChosenWordFinal() {
            Reflection.assertFieldsEqualTo(RandomHangmanChooser.class, "final", 2);
            Reflection.assertFieldsEqualTo(RandomHangmanChooser.class, "final", Random.class, 1);
            Reflection.assertFieldsEqualTo(RandomHangmanChooser.class, "final", String.class, 1);

        }
    }


    @DisplayName("Basic Functionality Tests")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    class RHCFunctionalityTests {
        @Order(sanityTestLevel)
        @DisplayName("Random is a static field")
        @Test
        public void testRandomStatic() {
            Field rand = Reflection.getFieldByType(RandomHangmanChooser.class, Random.class);
            Reflection.checkFieldModifiers(rand, List.of("private", "static"));
        }


        @Order(sanityTestLevel + 1)
        @DisplayName("Does not use Map as a field")
        @Test
        public void testNoMapFieldRHC() {
            Reflection.assertFieldsEqualTo(RandomHangmanChooser.class, Map.class, 0);
        }


        @Order(sanityTestLevel + 2)
        @DisplayName("Expected constructor exceptions for RandomHangmanChooser")
        @Test
        public void testExceptionsViolatedInRandomConstructor() {
            Constructor c = Reflection.getConstructor(RandomHangmanChooser.class, int.class, int.class);
            assertThrows(IllegalArgumentException.class, () -> Reflection.newInstance(c, -1, 3));
            assertThrows(IllegalArgumentException.class, () -> Reflection.newInstance(c, 3, -1));
            assertThrows(IllegalStateException.class, () -> Reflection.newInstance(c, Integer.MAX_VALUE, 3));
        }

        @Order(sanityTestLevel + 3)
        @DisplayName("Expected makeGuess() exceptions for characters that aren't lower case for RandomHangmanChooser")
        @TestDescription("This test makes sure that the makeGuess method throws exceptions for non-lowercase characters")
        @DependsOn("makeGuess")
        @Test
        public void testMakeGuessLowercaseCharExceptionsInRandom() {
            Constructor c = Reflection.getConstructor(RandomHangmanChooser.class, int.class, int.class);
            RandomHangmanChooser chooser = Reflection.newInstance(c, 3, 1);
            Method m = Reflection.getMethod(RandomHangmanChooser.class, "makeGuess", char.class);
            IntStream.range(0, 20).forEach(i -> assertThrows(IllegalArgumentException.class, () -> Reflection.invoke(m, chooser, (char) ('a' - (i + 1)))));
            IntStream.range(0, 20).forEach(i -> assertThrows(IllegalArgumentException.class, () -> Reflection.invoke(m, chooser, (char) ('z' + (i + 1)))));
        }

        @Order(sanityTestLevel + 4)
        @DisplayName("Expected makeGuess() exceptions for characters that have been previously guessed for RandomHangmanChooser")
        @TestDescription("This test makes sure that the makeGuess method throws exceptions for characters that have been guessed previously")
        @DependsOn("makeGuess")
        @Test
        public void testMakeGuessRepeatedCharExceptionsInRandom() throws FileNotFoundException {
            RandomHangmanChooser chooser = new RandomHangmanChooser(5, 5);
            chooser.makeGuess('v');
            assertThrows(IllegalArgumentException.class, () -> chooser.makeGuess('v'));
        }

        @Order(sanityTestLevel + 5)
        @DisplayName("Expected makeGuess() exceptions if no guesses left for RandomHangmanChooser")
        @TestDescription("This test makes sure that the makeGuess method throws exceptions if number of guesses left is not at least one")
        @DependsOn("makeGuess")
        @Test
        public void testMakeTooManyGuessExceptionsInRandom() throws FileNotFoundException {
            RandomHangmanChooser chooser = new RandomHangmanChooser(22, 2);
            chooser.makeGuess('x');
            chooser.makeGuess('y');
            assertThrows(IllegalStateException.class, () -> chooser.makeGuess('z'));
        }
    }
    @DisplayName("Complete Functionality Tests")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    class RHCCompleteFunctionalityTests {

        public void runTestGame(Class<? extends IHangmanChooser> clazz, int wordLength, int wrongAnswersAllowed, String guesses) throws FileNotFoundException {
            Constructor<? extends IHangmanChooser> constructor = Reflection.getConstructor(clazz, int.class, int.class);
            HangmanGame.playGame(
                    Reflection.newInstance(constructor, wordLength, wrongAnswersAllowed),
                    new ConsoleHangmanGuesser(new Scanner(String.join("\n", guesses.split(""))))
            );
        }

        @Order(stressTestLevel)
        @DisplayName("Expected game end after revealing word for RandomHangmanChooser")
        @Test
        @TestDescription("This test checks to make sure that the game ends after the word"+
                " has been revealed.")
        @DependsOn("makeGuess")
        public void testGameEndOnRevealWordRHC() throws FileNotFoundException {
            RandomHangmanChooser chooser = new RandomHangmanChooser(8, 1);
            chooser.getWord();
            assertThrows(IllegalStateException.class, () -> chooser.makeGuess('a'));
        }


        @Order(stressTestLevel + 1)
        @DisplayName("Test RandomHangmanChooser Full Game")
        @ParameterizedTest(name = "{0}")
        @FileSource(
                inputs = {
                        "{seed = 1337, word length = 3, max wrong guesses = 26, guesses = abcdefghijklmnopqrstuvwxyz}",
                        "{seed = 1337, word length = 3, max wrong guesses = 10, guesses = abcdefghijklmnopqrstuvwxyz}",
                        "{seed = 2, word length = 3, max wrong guesses = 10, guesses = abcdefghijklmnopqrstuvwxyz}",
                        "{seed = 44, word length = 7, max wrong guesses = 6, guesses = debats}",
                        "{seed = 6, word length = 20, max wrong guesses = 16, guesses = aeioubcdfghjklmnpqrstvwxyz}",
                        "{seed = 19, word length = 14, max wrong guesses = 1, guesses = aeiou}",
                        "{seed = 19, word length = 14, max wrong guesses = 26, guesses = abcdefghijklmnopqrstuvwxyz}",
                        "{seed = 239, word length = 9, max wrong guesses = 7, guesses = gsnreou}",
                        "{seed = 77, word length = 21, max wrong guesses = 4, guesses = iqzyx}",
                        "{seed = 1288, word length = 7, max wrong guesses = 1, guesses = negator}",
                        "{seed = 1972, word length = 5, max wrong guesses = 8, guesses = computer}",
                        "{seed = 1972, word length = 19, max wrong guesses = 3, guesses = xvcounterz}",
                        "{seed = 2019, word length = 8, max wrong guesses = 26, guesses = abcdefghijklmnopqrstuvwxyz}",
                        "{seed = 2019, word length = 8, max wrong guesses = 7, guesses = redfqin}",
                        "{seed = 2019, word length = 8, max wrong guesses = 2, guesses = redfqin}",
                },
                outputFiles = {
                        "trace0.txt",
                        "trace1.txt",
                        "trace2.txt",
                        "trace3.txt",
                        "trace4.txt",
                        "trace5.txt",
                        "trace6.txt",
                        "trace7.txt",
                        "trace8.txt",
                        "trace9.txt",
                        "trace10.txt",
                        "trace11.txt",
                        "trace12.txt",
                        "trace13.txt",
                        "trace14.txt",
                }
        )
        @TestDescription("This test runs the RandomHangmanChooser Game and tests the complete"+
                " functionality of it.")
        @CaptureSystemOutput

        public void testPlayGameWithRandomChooser(Map<String, String> arguments, String expectedOutput, CaptureSystemOutput.OutputCapture capture) throws FileNotFoundException {
            // Parse arguments
            int seed = Integer.parseInt(arguments.get("seed"));
            int length = Integer.parseInt(arguments.get("word length"));
            int wrongAllowed = Integer.parseInt(arguments.get("max wrong guesses"));
            String guesses = arguments.get("guesses");

            // Set Random field to correct seed
            Field rand = Reflection.getFieldByType(RandomHangmanChooser.class, Random.class);
            Reflection.<Random>getFieldValue(RandomHangmanChooser.class, rand.getName(), null).setSeed(seed);

            // Run the actual game
            runTestGame(RandomHangmanChooser.class, length, wrongAllowed, guesses);

            assertEquals(expectedOutput.replace("\r\n", "\n").strip(), capture.toString().replace("\r\n", "\n").strip());
        }
    }
}
