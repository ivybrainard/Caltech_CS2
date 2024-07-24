package edu.caltech.cs2.project04;

import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.TestDescription;
import edu.caltech.cs2.helpers.TestExtension;
import edu.caltech.cs2.helpers.TestHint;
import edu.caltech.cs2.interfaces.IDeque;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@ExtendWith(TestExtension.class)
public class TrieAutocompleterTest {
    private static String STRING_SOURCE = "src/edu/caltech/cs2/project04/TrieMovieAutoCompleter.java";

    static {
        TrieMovieAutoCompleter.populateTitles();
    }

    @Order(1)
    @Tag("B")
    @DisplayName("General complete Tests")
    @ParameterizedTest(name = "Test complete(\"{0}\")")
    @TestHint("Make sure in your populateTitles that you are making a new IDeque every time you are adding a new suffix to the dictionary.\nIf you add the same IDeque every time for a title, they all point to the same IDeque, and they will all be modified.")
    @CsvSource({
            "mission: impossible, test_mission_impossible",
            "the avengers, test_the_avengers",
            "age of, test_age_of"
    })
    public void testComplete1(String lookup, String res_file) throws IOException {
        complete(lookup, res_file);
    }

    @Order(1)
    @Tag("B")
    @DisplayName("Edge Case complete Test")
    @ParameterizedTest(name = "Test complete(\"{0}\")")
    @TestHint("If you are not checking for duplicates, you might be adding some titles multiple times. You might consider using a HashSet in some capacity.")
    @CsvSource({
            "this, test_this",
    })
    public void testComplete2(String lookup, String res_file) throws IOException {
        complete(lookup, res_file);
    }

    void complete(String lookup, String res_file) throws IOException {
        IDeque<String> suggestions = TrieMovieAutoCompleter.complete(lookup);
        List<String> expected = new ArrayList<>();

        Scanner fr = new Scanner(new File("data/" + res_file));

        while (fr.hasNextLine()) {
            String title = fr.nextLine();
            expected.add(title);
        }

        MatcherAssert.assertThat(suggestions,
                IsIterableContainingInAnyOrder.containsInAnyOrder(expected.toArray()));

    }

    @Order(0)
    @Tag("B")
    @DisplayName("Does not use or import disallowed classes")
    @TestDescription("You may not import anything from java.util except for Sets")
    @Test
    public void testForInvalidClasses() {
        // You may not import anything from java.uil except for these classes

        List<String> allowed = List.of("Arrays", "Set", "HashSet");
        Inspection.assertNoImportsOfExcept(STRING_SOURCE,"java\\.util", allowed);
        List<String> bannedUsages = List.of("java\\.lang\\.reflect", "java\\.io",
                "java\\.util\\.(?!" + String.join("|", allowed) + ")");
        Inspection.assertNoUsageOf(STRING_SOURCE, bannedUsages);
    }
}
