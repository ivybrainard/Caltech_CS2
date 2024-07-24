package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.helpers.*;
import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.Reflection;
import edu.caltech.cs2.interfaces.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static edu.caltech.cs2.project04.Project04TestOrdering.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestExtension.class)
public class TrieMapTests implements ITrieMapProvider {

    private static String STRING_SOURCE = "src/edu/caltech/cs2/datastructures/TrieMap.java";
    public Counter collectorCounter = new Counter();

    public ITrieMap<Object, Iterable<Object>, Object> newITrieMap() {
        Constructor c = Reflection.getConstructor(TrieMap.class, Function.class);
        this.collectorCounter.resetCounter();
        Function<IDeque<Object>, Iterable<Object>> collector = (IDeque<Object> o) -> {
            this.collectorCounter.touchCounter();
            List<Object> k = new ArrayList<>(o.size());
            for (Object m : o) {
                k.add(m);
            }

            return k;
        };

        return Reflection.newInstance(c, collector);
    }


    @DisplayName("Style")
    @Nested
    @Tag("C")
    class StyleTests implements IStyleTests {
        @Override
        public String getSource() {
            return STRING_SOURCE;
        }

        @Override
        public Class<?> getClazz() {
            return TrieMap.class;
        }

        @Override
        public List<String> getPublicInterface() {
            return List.of("clear", "containsKey", "containsValue", "get", "getCompletions", "isPrefix", "iterator", "keys", "put", "remove", "size", "toString", "values");
        }

        @Override
        public int getMaxFields() {
            return 4;
        }

        @Override
        public List<String> methodsToBanSelf() {
            return List.of("put", "get", "remove", "keys", "values");
        }


        @Order(classSpecificTestLevel)
        @DisplayName("Does not use or import disallowed classes")
        @TestHint("Remember that you're not allowed to use anything in java.util except Iterator, Random, and HashMap!")
        @Test
        public void testForInvalidClasses() {
            List<String> regexps = List.of("java\\.util\\.(?!Iterator|Map|HashMap|Set|HashSet|Random|function.Function)", "java\\.lang\\.reflect", "java\\.io");
            Inspection.assertNoImportsOf(getSource(), regexps);
            Inspection.assertNoUsageOf(getSource(), regexps);
        }
    }

    @Nested
    @DisplayName("Runtime Complexity")
    class RuntimeComplexityTests {
        @Order(specialTestLevel)
        @Tag("C")
        @DisplayName("Test get() -- constant complexity")
        @Timeout(value = 10, unit = SECONDS)
        @Test
        public void testGetComplexity() {
            Function<Integer, ITrieMap<Object, Iterable<Object>, Object>> provide = (Integer numElements) -> {
                Random rand = new Random(34857);
                ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
                Map<Iterable<Object>, Object> o = generateRandomTestDataIterable(numElements, rand, 10, 5, 6);
                for (Map.Entry<Iterable<Object>, Object> e : o.entrySet()) {
                    impl.put(e.getKey(), e.getValue());
                }
                return impl;
            };

            BiConsumer<Integer, ITrieMap<Object, Iterable<Object>, Object>> put = (size, impl) -> {
                impl.get(List.of(1, 2, 3, 4, 5));
            };
            RuntimeInstrumentation.assertAtMost("get", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, put, 12);
        }

        @Order(specialTestLevel)
        @Tag("C")
        @DisplayName("Test put() -- constant complexity")
        @Timeout(value = 10, unit = SECONDS)
        @Test
        public void testPutComplexity() {
            Function<Integer, ITrieMap<Object, Iterable<Object>, Object>> provide = (Integer numElements) -> {
                Random rand = new Random(34857);
                ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
                Map<Iterable<Object>, Object> o = generateRandomTestDataIterable(numElements, rand, 10, 5, 6);
                for (Map.Entry<Iterable<Object>, Object> e : o.entrySet()) {
                    impl.put(e.getKey(), e.getValue());
                }
                return impl;
            };

            BiConsumer<Integer, ITrieMap<Object, Iterable<Object>, Object>> put = (size, impl) -> {
                impl.put(List.of(1, 2, 3, 4, 5), 0);
            };
            RuntimeInstrumentation.assertAtMost("put", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, put, 12);
        }

        @Order(specialTestLevel)
        @Tag("A")
        @DisplayName("Test remove() -- constant complexity")
        @Timeout(value = 10, unit = SECONDS)
        @Test
        public void testRemoveComplexity() {
            Function<Integer, ITrieMap<Object, Iterable<Object>, Object>> provide = (Integer numElements) -> {
                Random rand = new Random(34857);
                ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
                Map<Iterable<Object>, Object> o = generateRandomTestDataIterable(numElements, rand, 10, 5, 6);
                for (Map.Entry<Iterable<Object>, Object> e : o.entrySet()) {
                    impl.put(e.getKey(), e.getValue());
                }
                return impl;
            };

            BiConsumer<Integer, ITrieMap<Object, Iterable<Object>, Object>> put = (size, impl) -> {
                impl.get(List.of(1, 2, 3, 4, 5));
            };
            RuntimeInstrumentation.assertAtMost("remove", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, put, 12);
        }


        @Order(specialTestLevel)
        @Tag("C")
        @DisplayName("Test size() -- constant complexity")
        @Timeout(value = 10, unit = SECONDS)
        @Test
        public void testSizeComplexity() {
            Function<Integer, ITrieMap<Object, Iterable<Object>, Object>> provide = (Integer numElements) -> {
                Random rand = new Random(34857);
                Map<Iterable<Object>, Object> o = generateRandomTestDataIterable(numElements, rand);
                ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
                for (Map.Entry<Iterable<Object>, Object> e : o.entrySet()) {
                    impl.put(e.getKey(), e.getValue());
                }
                return impl;
            };

            BiConsumer<Integer, ITrieMap<Object, Iterable<Object>, Object>> size = (x, y) -> y.size();
            RuntimeInstrumentation.assertAtMost("size", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, size, 8);
        }
    }

    @DisplayName("ITrieMap Functionality")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    class ITrieMapTests implements ITrieMapProvider {
        @Tag("C")
        @Order(7)
        @DisplayName("Stress test ITrieMap isPrefix, getCompletions")
        @ParameterizedTest(name = "Test ITrieMap with seed={0} and size={1}")
        @CsvSource({"24589, 3000", "96206, 5000"})
        @DependsOn({"put", "getCompletions", "isPrefix"})
        public void stressTestITrieMapNoRemove(int seed, int size) {
            iTrieMapStressTestHelper(seed, size, false);
        }

        @Tag("C")
        @Order(6)
        @DisplayName("Smoke test ITrieMap isPrefix, getCompletions")
        @ParameterizedTest(name = "Test ITrieMap with data={0}")
        @MethodSource("iTrieMapSanityDataSource")
        @DependsOn({"put", "isPrefix", "getCompletions"})
        public void sanityTestITrieMapNoRemove(Map<Iterable<Object>, Object> base, String[] prefixesToCheck) {
            iTrieMapSanityTestHelper(base, prefixesToCheck, false);
        }

        @Tag("A")
        @Order(sanityTestLevel)
        @DisplayName("Smoke test ITrieMap isPrefix, getCompletions, with remove")
        @ParameterizedTest(name = "Test ITrieMap with data={0}")
        @MethodSource("iTrieMapSanityDataSource")
        @DependsOn({"put", "isPrefix", "getCompletions", "remove"})
        public void sanityTestITrieMapRemove(Map<Iterable<Object>, Object> base, String[] prefixesToCheck) {
            iTrieMapSanityTestHelper(base, prefixesToCheck, true);
        }

        @Tag("A")
        @Order(stressTestLevel)
        @DisplayName("Stress test ITrieMap isPrefix, getCompletions, with remove")
        @ParameterizedTest(name = "Test IDictionary  with seed={0} and size={1}")
        @CsvSource({"24589, 3000", "96206, 5000"})
        @DependsOn({"put", "isPrefix", "getCompletions", "remove"})
        public void stressTestITrieMapRemove(int seed, int size) {
            iTrieMapStressTestHelper(seed, size, true);
        }


        @Tag("C")
        @Order(3)
        @DisplayName("Test ITrieMap clear")
        @DependsOn({"get", "put", "clear", "size", "keys", "values"})
        @Test
        public void testClear() {
            int size = 100;
            Map<Iterable<Object>, Object> base = generateRandomTestDataIterable(size, new Random(1), 3, 1, 10);
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
            for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
                impl.put(e.getKey(), e.getValue());
            }
            assertEquals(impl.size(), base.size(), "Trie size is not correct");
            impl.clear();
            assertEquals(0, impl.size(), "Size after clear is not zero");
            assertTrue(impl.keys().isEmpty(), "Keys after clear is not empty");
            assertTrue(impl.values().isEmpty(), "Values after clear is not empty");
        }

        @Order(4)
        @Tag("C")
        @DisplayName("Test isPrefix() returns false for empty TrieMap")
        @TestDescription("This test is checking the behavior of isPrefix() on empty TrieMaps and \"empty\" keys.\n No call to isPrefix() should return true for an empty Triemap. \n If a TrieMap is nonempty, even the \"empty\" key should return true.")
        @DependsOn({"isPrefix", "put"})
        @Test
        public void testIsPrefixEmpty() {
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
            assertFalse(impl.isPrefix(new ArrayList<>()), "isPrefix([]) on empty TrieMap should return false");

            List<Object> key = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertFalse(impl.isPrefix(key), "New empty TrieMap isPrefix() returns True for non-empty key");

            impl.put(new ArrayList<>(), 0);
            assertTrue(impl.isPrefix(new ArrayList<>()), "isPrefix([]) on nonempty TrieMap should return true");
        }

        @Order(5)
        @Tag("C")
        @DisplayName("Test getCompletions() returns [] for empty TrieMap")
        @TestDescription("If a TrieMap is empty, there are no keys nor values, and thus the getCompletions) should always return an empty collection.")
        @DependsOn({"getCompletions"})
        @Test
        public void testGetCompletionsEmpty() {
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
            assertEquals(0, impl.getCompletions(new ArrayList<>()).size(), "New empty TrieMap getCompletions([]) should return []");

            List<Object> key = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

            ICollection<Object> result = impl.getCompletions(key);
            assertEquals(0, result.size(), "New empty TrieMap getCompletions() returns non-empty collection");
        }

        @Tag("C")
        @Order(1)
        @DisplayName("Test that ITrieMap clear sets root to null")
        @TestDescription("Clearing the ITrieMap means forcefully removing all the data in the ITrieMap and thus the root also needs to be null.")
        @TestHint("You have to adjust more than one field variable here!")
        @DependsOn({"put", "clear"})
        @Test
        public void testNullRootAfterClear() {
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();

            Object root = Reflection.getFieldValue(TrieMap.class, "root", impl);
            assertNull(root, "New empty TrieMap nas nonnull root node");

            List<Object> key = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
            impl.put(key, 0);

            root = Reflection.getFieldValue(TrieMap.class, "root", impl);
            assertNotNull(root, "Nonempty TrieMap has null root node");

            impl.clear();
            root = Reflection.getFieldValue(TrieMap.class, "root", impl);
            assertNull(root, "TrieMap emptied by clear() has nonnull root node");
        }

        @Order(0)
        @Tag("C")
        @DisplayName("Test get() returns null for empty TrieMap")
        @TestDescription("An empty TrieMap has no values so there is no possible value for get().")
        @DependsOn({"get", "put"})
        @Test
        public void testGetEmpty() {
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
            assertNull(impl.get(new ArrayList<>()), "get([]) on empty TrieMap should return null");

            impl.put(new ArrayList<>(), 0);
            assertEquals(0, impl.get(new ArrayList<>()), "get([]) on TrieMap with mapping {[], 0} does not return 0");
        }

        @Order(sanityTestLevel)
        @Tag("A")
        @DisplayName("Test remove() returns null for empty TrieMap")
        @TestDescription("An empty TrieMap has no values so there is nothing to remove.")
        @DependsOn({"remove", "put", "get"})
        @Test
        public void testRemoveEmpty() {
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
            assertNull(impl.remove(new ArrayList<>()), "remove([]) on empty TrieMap should return null");

            impl.put(new ArrayList<>(), 0);
            assertEquals(0, impl.get(new ArrayList<>()), "get([]) on TrieMap with mapping {[], 0} does not return 0");

            assertEquals(0, impl.remove(new ArrayList<>()), "remove([]) on TrieMap with mapping {[], 0} does not return 0");
            assertNull(impl.get(new ArrayList<>()), "get([]) on TrieMap after removal of [] is not null");
        }

        public ITrieMap<Object, Iterable<Object>, Object> newITrieMap() {
            return TrieMapTests.this.newITrieMap();
        }
    }
    @DisplayName("IDictionary Functionality")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    class IDictionaryTests implements ITrieMapProvider {
        @Override
        public ITrieMap<Object, Iterable<Object>, Object> newITrieMap() {
            return TrieMapTests.this.newITrieMap();
        }

        @Tag("C")
        @Order(sanityTestLevel)
        @DisplayName("Smoke test all IDictionary methods")
        @ParameterizedTest(name = "Test IDictionary interface on {0}")
        @MethodSource("iDictionarySanityDataSource")
        @DependsOn({"isEmpty", "containsKey", "get", "put", "size", "keys", "iterator", "values"})
        public void sanityTestIDictionaryNoRemove(Map<Object, Object> base) {
            iDictionarySanityTestHelper(base, false);
        }

        @Tag("A")
        @Order(0)
        @DisplayName("Smoke test all IDictionary methods, with remove")
        @ParameterizedTest(name = "Test IDictionary interface on {0}")
        @MethodSource("iDictionarySanityDataSource")
        @DependsOn({"isEmpty", "containsKey", "get", "put", "size", "keys", "iterator", "values", "remove"})
        public void sanityTestIDictionaryRemove(Map<Object, Object> base) {
            iDictionarySanityTestHelper(base, true);
        }

        @Tag("C")
        @Order(stressTestLevel)
        @DisplayName("Stress test all IDictionary methods")
        @ParameterizedTest(name = "Test IDictionary interface with seed={0} and size={1}")
        @CsvSource({ "24589, 3000", "96206, 5000" })
        @DependsOn({"isEmpty", "containsKey", "get", "put", "size", "keys", "iterator", "values"})
        public void stressTestIDictionaryNoRemove(int seed, int size) {
            iDictionaryStressTestHelper(seed, size, false);
        }

        @Tag("A")
        @Order(1)
        @DisplayName("Stress test all IDictionary methods, with remove")
        @ParameterizedTest(name = "Test IDictionary interface with seed={0} and size={1}")
        @DependsOn({"isEmpty", "containsKey", "get", "put", "size", "keys", "iterator", "values", "remove"})
        @CsvSource({ "24589, 3000", "96206, 5000" })
        public void stressTestIDictionaryRemove(int seed, int size) {
            iDictionaryStressTestHelper(seed, size, true);
        }
    }

    @DisplayName("Smoke Tests for Tasks 2 and 3")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    class Task2And3Smoke {
        @Tag("C")
        @Order(0)
        @DisplayName("put, size, and clear are consistent")
        @DependsOn({"put", "size", "clear"})
        @Test
        public void smokeTask1() {
            TrieMap<Object, Iterable<Object>, Object> map = (TrieMap<Object, Iterable<Object>, Object>) newITrieMap();
            assertEquals(0, map.size(), "size should return 0 after construction");
            map.put(List.of(0), 0);
            assertEquals(1, map.size(), "size should return 1 after putting 1 item");
            map.put(List.of(0), 0);
            assertEquals(1, map.size(), "size should return 1 after putting the same item twice");
            map.put(List.of(1), 0);
            assertEquals(2, map.size(), "size should return 2 after putting two unique items");
            map.clear();
            assertEquals(0, map.size(), "size should return 0 after clearing map");
        }

        @Tag("C")
        @Order(1)
        @DisplayName("get matches put")
        @DependsOn({"get", "put"})
        @Test
        public void smokeTask2_0() {
            TrieMap<Object, Iterable<Object>, Object> map = (TrieMap<Object, Iterable<Object>, Object>) newITrieMap();
            assertEquals(null, map.get(List.of(0)), "get should return null for non-existent key");
            map.put(List.of(0), 0);
            assertEquals(0, map.get(List.of(0)), "get should return correct item for existing key");
            map.put(List.of(0), 1);
            assertEquals(1, map.get(List.of(0)), "get should return correct item for replaced key");
            map.put(List.of(0, 1), 0);
            assertEquals(0, map.get(List.of(0, 1)), "get should return correct item for existing key");
            map.put(List.of(1, 1), 3);
            assertEquals(3, map.get(List.of(1, 1)), "get should return correct item for existing key");
            assertEquals(null, map.get(List.of(1)), "get should return null for a non-existent prefix of an existing key");
        }

        @Tag("C")
        @Order(2)
        @DisplayName("containsKey matches put")
        @DependsOn({"containsKey", "put"})
        @Test
        public void smokeTask2_1() {
            TrieMap<Object, Iterable<Object>, Object> map = (TrieMap<Object, Iterable<Object>, Object>) newITrieMap();
            assertFalse(map.containsKey(List.of(0)), "containsKey should return false for non-existent key");
            map.put(List.of(0), 0);
            assertTrue(map.containsKey(List.of(0)), "containsKey should return true for existing key");
            map.put(List.of(0), 1);
            assertTrue(map.containsKey(List.of(0)), "containsKey should return true for replaced key");
            map.put(List.of(0, 1), 0);
            assertTrue(map.containsKey(List.of(0, 1)), "containsKey should return true for existing key");
            map.put(List.of(1, 1), 0);
            assertTrue(map.containsKey(List.of(1, 1)), "containsKey should return true for existing key");
            assertFalse(map.containsKey(List.of(1)), "containsKey should return false for a non-existent prefix of an existing key");
        }
        @Tag("C")
        @Order(3)
        @DisplayName("isPrefix matches put")
        @DependsOn({"isPrefix", "put"})
        @Test
        public void smokeTask2_2() {
            TrieMap<Object, Iterable<Object>, Object> map = (TrieMap<Object, Iterable<Object>, Object>) newITrieMap();
            assertFalse(map.isPrefix(List.of(0, 0, 0)), "isPrefix should return false for non-existent key");
            map.put(List.of(0, 0, 0), 0);
            assertTrue(map.isPrefix(List.of()), "isPrefix should return true for the empty prefix of an existing key");
            assertTrue(map.isPrefix(List.of(0)), "isPrefix should return true for the empty prefix of an existing key");
            assertTrue(map.isPrefix(List.of(0, 0)), "isPrefix should return true for a prefix of an existing key");
            assertTrue(map.isPrefix(List.of(0, 0, 0)), "isPrefix should return true a prefix of an existing key");
            assertFalse(map.isPrefix(List.of(0, 0, 0, 0)), "isPrefix should return false for a non-existent key longer than all keys");
        }
    }

    @DisplayName("Implementation")
    @Nested
    class ImplementationTests {

        @Order(specialTestLevel)
        @Tag("C")
        @DisplayName("Test that collector function is used in keys")
        @TestDescription("The keys function must be returning a collection of keys of type K, and must not be separated into constituent elements of type A.")
        @TestHint("The collector exists to put together a collection of elements of type A into one element of type K. \n Use \"this.collector.apply\" to use the collector when finding all the keys!")
        @Test
        @DependsOn({"put", "keys"})
        public void testCollectorUsage() {
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
            Map<Iterable<Object>, Object> base = generateRandomTestDataIterable(2000, new Random(69), 2, 1, 10);
            for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
                impl.put(e.getKey(), e.getValue());
            }
            impl.keys();
            // Random test data may repeat keys, so we use the base's size
            assertEquals(base.size(), TrieMapTests.this.collectorCounter.touches, "Collector was not called once for each key.");
        }

        @Tag("C")
        @Order(classSpecificTestLevel)
        @DisplayName("Check for excessive node allocation in put")
        @TestDescription("This test checks that put creates no more nodes than the size of the input key.")
        @TestHint("You should not add more nodes than the size of the input key: check the diagram of a Trie provided on the guide!")
        @Test
        public void testForExcessiveNodeAllocationPut() {
            NewNode.TrieMap_NUM_CALLS = 0;
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
            Map<Iterable<Object>, Object> base = generateRandomTestDataIterable(2000, new Random(69), 2, 1, 10);
            for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
                int before = NewNode.TrieMap_NUM_CALLS;
                impl.put(e.getKey(), e.getValue());
                int after = NewNode.TrieMap_NUM_CALLS;
                int count = 0;
                for (Object o : e.getKey()) {
                    count++;
                }
                assertTrue(before + count + 1 >= after, "Each put() should not create more nodes than the size of the input key");
            }
        }

        @Tag("C")
        @Order(classSpecificTestLevel)
        @DisplayName("Check for excessive node allocation in get")
        @TestDescription("This test checks that get does not create any new nodes. \n get should only return existing values, and should never create any new nodes.")
        @Test
        public void testForExcessiveNodeAllocationGet() {
            NewNode.TrieMap_NUM_CALLS = 0;
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
            Map<Iterable<Object>, Object> base = generateRandomTestDataIterable(2000, new Random(69), 2, 1, 10);
            for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
                impl.put(e.getKey(), e.getValue());
                int before = NewNode.TrieMap_NUM_CALLS;
                impl.get(e.getKey());
                int after = NewNode.TrieMap_NUM_CALLS;
                assertEquals(after, before, "get() should not allocate any new nodes");
            }
        }

        @Tag("A")
        @Order(classSpecificTestLevel)
        @DisplayName("Check for excessive node allocation in remove")
        @TestDescription("Remove should not be creating any new nodes.")
        @Test
        @DependsOn({"put", "remove"})
        public void testForExcessiveNodeAllocationRemove() {
            NewNode.TrieMap_NUM_CALLS = 0;
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
            Map<Iterable<Object>, Object> base = generateRandomTestDataIterable(2000, new Random(69), 2, 1, 10);
            for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
                impl.put(e.getKey(), e.getValue());
            }

            for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
                int before = NewNode.TrieMap_NUM_CALLS;
                impl.remove(e.getKey());
                int after = NewNode.TrieMap_NUM_CALLS;
                assertEquals(after, before, "remove() should not allocate any new nodes");
            }
        }
        public void verifyNonlazyRemoveHelper(Object trieNode) {
            Map<Object, Object> c = Reflection.getFieldValue(trieNode.getClass(), "pointers", trieNode);
            if (c.isEmpty()) {
                Object v = Reflection.getFieldValue(trieNode.getClass(), "value", trieNode);
                assertNotNull(v, "Leaf node of TrieMap has null value and should have been pruned");
            }
            for (Object v : c.values()) {
                verifyNonlazyRemoveHelper(v);
            }
        }

        @Tag("A")
        @Order(specialTestLevel)
        @DisplayName("Test that remove is not a lazy implementation")
        @ParameterizedTest(name = "Verify nonlazy remove for seed={0}, size={1}")
        @CsvSource({"24589, 3000", "96206, 5000"})
        public void verifyNonlazyRemove(int seed, int size) {
            Random rand = new Random(seed);
            int maxKeyLength = 10;
            Map<Iterable<Object>, Object> base = generateRandomTestDataIterable(size, rand, 2, 1, maxKeyLength);
            int longestKeyLength = base.keySet().stream().mapToInt(i -> (int) i.spliterator().getExactSizeIfKnown()).max()
                    .getAsInt();
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();

            // Populate ITrieMap, tracking the longest keys to remove the all current leaves,
            // guaranteeing that lazy deletion will leave hanging leaves.
            List<Iterable<Object>> longKeys = new ArrayList<>();
            for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
                impl.put(e.getKey(), e.getValue());
                if (e.getKey().spliterator().getExactSizeIfKnown() >= longestKeyLength) {
                    longKeys.add(e.getKey());
                }
            }

            for (Iterable<Object> k : longKeys) {
                impl.remove(k);
                assertFalse(impl.containsKey(k), "Removed key still present in map.");
            }

            // Explore ITrieMap to completion, and verify that all leaves have values.
            Object root = Reflection.getFieldValue(TrieMap.class, "root", impl);
            verifyNonlazyRemoveHelper(root);
        }


        @Tag("A")
        @Order(sanityTestLevel)
        @DisplayName("Test that emptying a TrieMap with remove() sets the root node to null")
        @TestDescription("This test checks that after removing all TrieNodes in the TrieMap, the root is null too.")
        @TestHint("Make sure your algorithm for remove resets the root to null if needed.")
        @Test
        @DependsOn({"put", "remove"})
        public void verifyNullRootAfterEmpty() {
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();

            Object root = Reflection.getFieldValue(TrieMap.class, "root", impl);
            assertNull(root, "New empty TrieMap nas nonnull root node");

            List<Object> key = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
            impl.put(key, 0);

            root = Reflection.getFieldValue(TrieMap.class, "root", impl);
            assertNotNull(root, "Nonempty TrieMap has null root node");

            impl.remove(key);
            root = Reflection.getFieldValue(TrieMap.class, "root", impl);
            assertNull(root, "TrieMap emptied by remove() has nonnull root node");
        }


        @Order(classSpecificTestLevel)
        @DisplayName("Does not attempt to get around constructor counts")
        @Test
        public void testForAvoidCounters() {
            List<String> regexps = List.of("NUM_CALLS");
            Inspection.assertNoUsageOf(STRING_SOURCE, regexps);
        }
    }

    private static class Counter {
        public int touches;
        public Object data;

        public Counter() {
            this(0);
        }

        public Counter(Object data) {
            this.touches = 0;
            this.data = data;
        }

        public void resetCounter() {
            this.touches = 0;
        }

        public void touchCounter() {
            this.touches++;
        }

        @Override
        public int hashCode() {
            this.touchCounter();
            return this.data.hashCode();
        }

        @Override
        // Equals does not count as a "touch"
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            } else if (!(o instanceof Counter)) {
                return false;
            }
            Counter c = (Counter) o;
            if (this.data == null || c.data == null) {
                return (this.data == null && c.data == null);
            }
            return this.data.equals(c.data);
        }

        @Override
        public String toString() {
            return this.data == null ? "null" : this.data.toString();
        }
    }
}
