package edu.caltech.cs2.project01;

import edu.caltech.cs2.helpers.DependsOn;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import edu.caltech.cs2.helpers.TestDescription;
import edu.caltech.cs2.helpers.TestExtension;
import edu.caltech.cs2.helpers.TestHint;

import java.io.*;
import java.util.stream.Stream;

import static edu.caltech.cs2.project01.Project01TestOrdering.sanityTestLevel;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestExtension.class)
public class SubstitutionCipherSolverTests {

    static Stream<Arguments> ciphertextArgumentsProvider() {
        return Stream.of(Arguments.of(
                "ILBNUPQCNYBNBGIYGDYDZNZUPGIBGQZIBPGPTUNOYTPUQNPGZXNICZUIBPGBGACPVCZEEBGVXPILBGDYNBVGPTACPVCZ" +
                        "ENZGDDZIZNIPCZVYOYDBNUQNNZGDBEASYEYGITQGDZEYGIZSDZIZNICQUIQCYNZGDZSVPCBILENRBZZNYCBY" +
                        "NPTSZXNZGDACPFYUINOYOBSSVCZDYPGUPCCYUIGYNNZGDYTTBUBYGUJPTPQCACPVCZENILBNUPQCNYQNYNFZ" +
                        "RZZNZGBEASYEYGIZIBPGSZGVQZVYXQIOYDPGPIYKAYUIZGJACBPCFZRZACPVCZEEBGVYKAYCBYGUY",
                "THISCOURSEISINTENDEDASACONTINUATIONOFCSWEFOCUSONABSTRACTIONINPROGRAMMINGBOTHINDESIGNOFPROGRA" +
                        "MSANDDATASTORAGEWEDISCUSSANDIMPLEMENTFUNDAMENTALDATASTRUCTURESANDALGORITHMSVIAASERIE" +
                        "SOFLABSANDPROJECTSWEWILLGRADEONCORRECTNESSANDEFFICIENCYOFOURPROGRAMSTHISCOURSEUSESJA" +
                        "VAASANIMPLEMENTATIONLANGUAGEBUTWEDONOTEXPECTANYPRIORJAVAPROGRAMMINGEXPERIENCE"
                ),
                Arguments.of(
                        "BIKQIHIEHYIERKQIVUGKIXDKFKIDGUEAXIAKEREATFTEAIHIARILKVUGEUIDKFSYGDQMVDKGLIGUDVAIXETIDKG" +
                                "LKAFUZVGYGKPHAEWGXIREAKQILETTEUXIRIULIHAETEKIKQIJIUIAFYBIYRFAIFUXDILVAIKQISYIDD" +
                                "GUJDERYGSIAKPKEEVADIYWIDFUXEVAHEDKIAGKPXEEAXFGUFUXIDKFSYGDQKQGDLEUDKGKVKGEUREAK" +
                                "QIVUGKIXDKFKIDERFTIAGLF",
                        "WETHEPEOPLEOFTHEUNITEDSTATESINORDERTOFORMAMOREPERFECTUNIONESTABLISHJUSTICEINSUREDOMESTI" +
                                "CTRANQUILITYPROVIDEFORTHECOMMONDEFENCEPROMOTETHEGENERALWELFAREANDSECURETHEBLESS" +
                                "INGSOFLIBERTYTOOURSELVESANDOURPOSTERITYDOORDAINANDESTABLISHTHISCONSTITUTIONFORT" +
                                "HEUNITEDSTATESOFAMERICA"
                ),
                Arguments.of(
                        "SOWFBRKAWFCZFSBSCSBQITBKOWLBFXTBKOWLSOXSOXFZWWIBICFWUQLRXINOCIJLWJFQUNWXLFBSZXFBTXAANTQ" +
                                "IFBFSFQUFCZFSBSCSBIMWHWLNKAXBISWGSTOXLXTSWLUQLXJBUUWLWISTBKOWLSWGSTOXLXTSWLBSJB" +
                                "UUWLFULQRTXWFXLTBKOWLBISOXSSOWTBKOWLXAKOXZWSBFIQSFBRKANSOWXAKOXZWSFOBUSWJBSBFTQ" +
                                "RKAWSWANECRZAWJ",
                        "THESIMPLESUBSTITUTIONCIPHERISACIPHERTHATHASBEENINUSEFORMANYHUNDREDSOFYEARSITBASICALLYCO" +
                                "NSISTSOFSUBSTITUTINGEVERYPLAINTEXTCHARACTERFORADIFFERENTCIPHERTEXTCHARACTERITDI" +
                                "FFERSFROMCAESARCIPHERINTHATTHECIPHERALPHABETISNOTSIMPLYTHEALPHABETSHIFTEDITISCO" +
                                "MPLETELYJUMBLED"
                ));
    }
    @Order(sanityTestLevel)
    @Tag("B")
    @TestDescription("This test uses the SubstitutionCipher and the provided Substitution Cipher solver " +
            "to encode and then decode that text.")
    @DependsOn({"SubstitutionCipher constructors", "SubstitutionCipher.getSolution()", "SubstitutionCipher.getScore()"})
    @ParameterizedTest(name = "Test with ciphertext = {0}")
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
    @DisplayName("Test main()")
    @MethodSource("ciphertextArgumentsProvider")
    public void testSubstitutionCipherMain(String ciphertext, String expected) throws FileNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        byte[] input = ciphertext.getBytes();
        InputStream fakeIn = new ByteArrayInputStream(input);
        System.setIn(fakeIn);
        SubstitutionCipherSolver.main(null);
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        String output = outputStream.toString();
        System.out.print(output);
        assertEquals("Type a sentence to decrypt: " + expected + "\n", output.replaceAll("\r\n", "\n"));
    }
}
