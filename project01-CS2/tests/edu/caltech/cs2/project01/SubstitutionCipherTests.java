package edu.caltech.cs2.project01;

import edu.caltech.cs2.helpers.*;
import edu.caltech.cs2.interfaces.IStyleTests;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static edu.caltech.cs2.project01.Project01TestOrdering.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestExtension.class)
@Tag("B")
public class SubstitutionCipherTests {
    private static String PANGRAM = "SPHINXOFBLACKQUARTZJUDGEMYVOW";
    public static Map<Character, Character> genIdentityMap() {
        return Map.ofEntries(Map.entry('A', 'A'),
                Map.entry('B', 'B'),
                Map.entry('C', 'C'),
                Map.entry('D', 'D'),
                Map.entry('E', 'E'),
                Map.entry('F', 'F'),
                Map.entry('G', 'G'),
                Map.entry('H', 'H'),
                Map.entry('I', 'I'),
                Map.entry('J', 'J'),
                Map.entry('K', 'K'),
                Map.entry('L', 'L'),
                Map.entry('M', 'M'),
                Map.entry('N', 'N'),
                Map.entry('O', 'O'),
                Map.entry('P', 'P'),
                Map.entry('Q', 'Q'),
                Map.entry('R', 'R'),
                Map.entry('S', 'S'),
                Map.entry('T', 'T'),
                Map.entry('U', 'U'),
                Map.entry('V', 'V'),
                Map.entry('W', 'W'),
                Map.entry('X', 'X'),
                Map.entry('Y', 'Y'),
                Map.entry('Z', 'Z'));
    }

    public static void swapSubstitutionCipherRandom(int seed) {
        // Replace the static random in the class with new seeded rand
        Field rand = Reflection.getFieldByType(SubstitutionCipher.class, Random.class);
        Reflection.<Random>getFieldValue(SubstitutionCipher.class, rand.getName(), null).setSeed(seed);
    }

    String STRING_SOURCE = "src/edu/caltech/cs2/project01/SubstitutionCipher.java";

    @DisplayName("Style")
    @Nested
    class StyleTests implements IStyleTests {
        @DisplayName("Random is a static field")
        @TestDescription("This test checks that your Random object is private and static.")
        @TestHint("We provide this object to you. Do not modify this.")
        @Test
        @Order(classSpecificTestLevel)
        public void testRandomStatic() {
            Field rand = Reflection.getFieldByType(SubstitutionCipher.class, Random.class);
            Reflection.checkFieldModifiers(rand, List.of("private", "static"));
        }
        @Override
        public String getSource() {
            return STRING_SOURCE;
        }

        @Override
        public Class<?> getClazz() {
            return SubstitutionCipher.class;
        }

        @Override
        public List<String> getPublicInterface() {
            return List.of("getCipherText",
                    "getPlainText",
                    "randomSwap",
                    "getScore",
                    "getSolution");
        }

        @Override
        public int getMaxFields() {
            return 3;
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

        static Stream<Arguments> cipherTextKeyProvider() {
            return Stream.of(Arguments.of("", new HashMap<Character, Character>(), "", 0.0),
                    Arguments.of("AAAAAAAAAAAAAAAAA", Map.ofEntries(Map.entry('A', 'A')), "AAAAAAAAAAAAAAAAA", -81.19073589942083),
                    Arguments.of("AAAAA", Map.ofEntries(Map.entry('A', 'B')), "BBBBB", -14.11038824167156),
                    Arguments.of("ABABAB", Map.ofEntries(Map.entry('A', 'B'),
                            Map.entry('B', 'A')), "BABABA", -16.570212114989623),
                    Arguments.of("HELP", Map.ofEntries(Map.entry('H', 'S'),
                            Map.entry('E', 'A'), Map.entry('L', 'F'), Map.entry('P', 'E')), "SAFE", -4.218018222026446));
        }
        @Order(sanityTestLevel)
        @DisplayName("Test first constructor and getters")
        @TestDescription("This test tests checks that the SubstitutionCipher object is set up correctly.")
        @ParameterizedTest(name = "ciphertext = {0}, key = {1}")
        @MethodSource("cipherTextKeyProvider")
        public void testConstructorAndGetters(String ciphertext, Map<Character, Character> key, String plaintext, double score) {
            SubstitutionCipher sc = new SubstitutionCipher(ciphertext, key);
            Assertions.assertEquals(ciphertext, sc.getCipherText(), "Returned ciphertext incorrect");
            Assertions.assertEquals(plaintext, sc.getPlainText(), "Returned plaintext incorrect");
        }

        @Order(sanityTestLevel + 1)
        @DisplayName("Test getScore")
        @TestDescription("This test checks the functionality of the getScore method.")
        @TestHint("If your tests are running too slow, make sure you only call getPlainText once.")
        @DependsOn({"getPlainText"})
        @ParameterizedTest(name = "ciphertext = {0}, key = {1}, score = {3}")
        @MethodSource("cipherTextKeyProvider")
        public void testGetScore(String ciphertext, Map<Character, Character> key, String plaintext, double score) throws FileNotFoundException {
            SubstitutionCipher sc = new SubstitutionCipher(ciphertext, key);
            QuadGramLikelihoods qgl = new QuadGramLikelihoods();
            Assertions.assertEquals(score, sc.getScore(qgl), 1e-3);
        }

        @Order(sanityTestLevel + 3)
        @DisplayName("Test constructor for random cipher")
        @TestDescription("This test checks that the constructor for a random cipher works as intended.")
        @TestHint("Make sure you are calling randomSwap 10,000 times.")
        @DependsOn({"constructors", "fields", "randomSwap"})
        @ParameterizedTest(name = "seed = {0}")
        @CsvSource({"520, GILOXPFCUBSDAHJSZWRKJYEQNTVFM",
                "203, OZLDCIJMEHKARUTKGVFBTYWSNQXJP",
                "342, SZVHPAMLINRECFGRYWOUGKTDXQJMB",
                "426, NVOKFHEATPBWJZSBRIXCSQYLDUGEM",
                "99, CSREMNBVJZTHKOPTGIQLPDAFWXYBU"
        })
        public void testRandomSubstitutionCipherConstructor(int seed, String expectedPlainText) {
            swapSubstitutionCipherRandom(seed);
            SubstitutionCipher sc = new SubstitutionCipher(PANGRAM);
            Assertions.assertEquals(expectedPlainText, sc.getPlainText(), "Incorrect plaintext after random initialization");
        }
    }

    @DisplayName("Stress Tests for randomSwap")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    class StressTests {
        @Order(stressTestLevel)
        @DisplayName("Test randomSwap")
        @TestDescription("This test stress tests the randomSwap method.")
        @TestHint("Check for a couple of things:\n" + "" +
                "- If one or two letters are wrong, make sure that when handling " +
                "the case of two generated characters being the same, you only regenerate " +
                "the second character.\n" +
                "- Make sure you are calling randomSwap on the correct cipher - \"this\" will " +
                "call randomSwap on the current cipher and not the input cipher.\n" +
                "- When a replacement occurs, be sure to reset your counter.\n" +
                "- The key map is immutable. If you need to modify the keys or values, create " +
                "a copy of the hashmap with the same keys and values.\n" +
                "- Do not use keySet(). The keys of a hashmap are not ordered so the keySet() " +
                "method returns a nondeterministic ordering of the keys, which will fail " +
                "our tests. Consider using ALPHABET from CaesarCipher.")
        @DependsOn({"constructor", "fields", "getPlainText()", "randomSwap()"})
        @ParameterizedTest(name = "seed = {0}")
        @ValueSource(ints = {148, 327, 608, 610, 911})
        public void testRandomSwap(int seed) throws InvocationTargetException, IllegalAccessException, IOException {
            String filename = "tests/data/trace_seed_" + seed + ".txt";
            List<String> expectedOutput = Files.readAllLines(Paths.get(filename));
            int numSwaps = 5000;
            String ciphertext = "SPHINXOFBLACKQUARTZJUDGEMYVOW";

            swapSubstitutionCipherRandom(seed);
            Map<Character, Character> identityMap = genIdentityMap();

            SubstitutionCipher sc = new SubstitutionCipher(ciphertext, identityMap);
            for (int i = 0; i < numSwaps; i ++) {
                String expected = expectedOutput.get(i);
                sc = sc.randomSwap();
                Assertions.assertEquals(expected, sc.getPlainText(), "Incorrect plaintext after random swap");
            }
        }

        @Order(stressTestLevel + 1)
        @DisplayName("Test randomSwap with shorter cipher text")
        @TestDescription("This test stress tests the randomSwap method.")
        @TestHint("Check for a couple of things:\n" + "" +
                "- If one or two letters are wrong, make sure that when handling " +
                "the case of two generated characters being the same, you only regenerate " +
                "the second character.\n" +
                "- Make sure you are calling randomSwap on the correct cipher - \"this\" will " +
                "call randomSwap on the current cipher and not the input cipher.\n" +
                "- When a replacement occurs, be sure to reset your counter.\n" +
                "- The key map is immutable. If you need to modify the keys or values, create " +
                "a copy of the hashmap with the same keys and values.\n" +
                "- Do not use keySet(). The keys of a hashmap are not ordered so the keySet() " +
                "method returns a nondeterministic ordering of the keys, which will fail " +
                "our tests. Consider using ALPHABET from CaesarCipher.")
        @DependsOn({"constructor", "fields", "getPlainText()", "randomSwap()"})
        @ParameterizedTest(name = "seed = {0}")
        @ValueSource(ints = {148, 327, 608, 610, 911})
        public void testRandomSwapShort(int seed) throws InvocationTargetException, IllegalAccessException, IOException {
            String filename = "tests/data/trace2_seed_" + seed + ".txt";
            List<String> expectedOutput = Files.readAllLines(Paths.get(filename));
            int numSwaps = 10000;
            String ciphertext = "BLANK";

            swapSubstitutionCipherRandom(seed);

            SubstitutionCipher sc = new SubstitutionCipher(ciphertext);
            for (int i = 0; i < numSwaps; i ++) {
                String expected = expectedOutput.get(i);
                sc = sc.randomSwap();
                Assertions.assertEquals(expected, sc.getPlainText(), "Incorrect plaintext after random swap");
            }
        }
    }
}
