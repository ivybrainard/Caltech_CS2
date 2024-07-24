package edu.caltech.cs2.project02;

import edu.caltech.cs2.helpers.*;
import edu.caltech.cs2.interfaces.IStyleTests;
import edu.caltech.cs2.project02.guessers.AIHangmanGuesser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.caltech.cs2.helpers.TestDescription;
import edu.caltech.cs2.helpers.TestExtension;
import edu.caltech.cs2.helpers.TestHint;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;


import static edu.caltech.cs2.project02.Project02TestOrdering.classSpecificTestLevel;
import static edu.caltech.cs2.project02.Project02TestOrdering.stressTestLevel;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestExtension.class)
@Tag("A")
public class GuesserTests {
    private static String GUESSER_SOURCE = "src/edu/caltech/cs2/project02/guessers/AIHangmanGuesser.java";

    @DisplayName("Style")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    class StyleTests implements IStyleTests {
        @Override
        public String getSource() {
            return GUESSER_SOURCE;
        }

        @Override
        public Class<?> getClazz() {
            return AIHangmanGuesser.class;
        }

        @Override
        public List<String> getPublicInterface() {
            return List.of("getGuess");
        }

        @Override
        public int getMaxFields() {
            return 1;
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


        @Order(classSpecificTestLevel)
        @DisplayName("Test that the dictionary is static")
        @TestHint("Remember to add the static modifier to the String containing the dictionary filepath!")
        @Test
        public void testDictionaryModifiers() {
            Field dictField = Reflection.getFieldByType(AIHangmanGuesser.class, String.class);
            Reflection.checkFieldModifiers(dictField, List.of("private", "static"));
        }
    }



    @Order(stressTestLevel)
    @DisplayName("Test getGuess Method in AIHangmanGuesser")
    @Test
    @TestDescription("Checks the getGuess method to make sure that the character with the most occurences"+
                " is chosen, tie broken by alphabetical order.")
    @DependsOn("getGuess")
    public void testGetGuess() throws FileNotFoundException {
        AIHangmanGuesser guesser = new AIHangmanGuesser();

        // test character with most occurrences is chosen
        Assertions.assertEquals('e', guesser.getGuess("---", Set.of('a')));
        Assertions.assertEquals('i', guesser.getGuess("---", Set.of('a', 'e', 'o')));
        Assertions.assertEquals('e', guesser.getGuess("sc--nc-", Set.of('s', 'n', 'c')));
        // test first character in alphabetical order is chosen
        Assertions.assertEquals('b', guesser.getGuess("-ee", Set.of('e')));
        Assertions.assertEquals('a', guesser.getGuess("-ppl-", Set.of('p', 'l')));
        // test only correct letter is chosen
        Assertions.assertEquals('g', guesser.getGuess("en-ineerin-", Set.of('e', 'n', 'i', 'r')));
        // test that only words matching the pattern are sampled (otherwise, returns 't')
        Assertions.assertEquals('u', guesser.getGuess("---s", Set.of('a', 'e', 'i', 'o', 's')));
    }

}
