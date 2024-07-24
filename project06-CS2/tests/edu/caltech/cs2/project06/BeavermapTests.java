package edu.caltech.cs2.project06;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.caltech.cs2.datastructures.BeaverMapsGraph;
import edu.caltech.cs2.datastructures.Location;
import edu.caltech.cs2.helpers.*;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ISet;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.IsIterableContaining;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeavermapTests {
    private static String BEAVERMAP_GRAPH_SOURCE = "src/edu/caltech/cs2/datastructures/BeaverMapsGraph.java";

    private static JsonElement fromFile(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            return JsonParser.parseReader(reader);
        }
        catch (IOException e) {
            return null;
        }
    }

    @Tag("C")
    @Tag("Beavermap")
    @DisplayName("BeaverMapsGraph implements required public methods")
    @TestDescription("This test checks that you have implemented the required public methods.")
    @TestHint("Make sure all the methods are sufficiently filled out!")
    @Test
    @Order(0)
    public void testMethodsBeaverMapsGraph() {
        SortedSet<String> expected = new TreeSet<>(List.of(
                "getLocationByName", "getBuildings", "getClosestBuilding", "dfs", "dijkstra", "addVertex"
        ));
        SortedSet<String> actual = new TreeSet<>(
                Stream.of(BeaverMapsGraph.class.getDeclaredMethods())
                        .filter(Reflection.hasModifier("public"))
                        .map(x -> x.getName())
                        .collect(Collectors.toList()));
        MatcherAssert.assertThat(new ArrayList<>(actual),
                IsIterableContaining.hasItems((expected.toArray())));
    }

    @Tag("C")
    @Tag("Beavermap")
    @Order(1)
    @DisplayName("Does not use or import disallowed classes")
    @Test
    public void testForInvalidClasses() {
        List<String> graphDisallow = List.of("java\\.lang\\.reflect");
        Inspection.assertNoImportsOf(BEAVERMAP_GRAPH_SOURCE, graphDisallow);
        Inspection.assertNoUsageOf(BEAVERMAP_GRAPH_SOURCE, graphDisallow);
    }

    @Order(2)
    @DisplayName("Does not use or import disallowed classes from java.util")
    @TestDescription("This is a style test")
    @TestHint("You should not be importing any class beside java.util")
    @Test
    @Tag("C")
    @Tag("Beavermap")
    public void testForInvalidImportsJavaUtil() {
        List<String> allowed = List.of("Iterator");
        Inspection.assertNoImportsOfExcept(BEAVERMAP_GRAPH_SOURCE, "java\\.util", allowed);

        List<String> bannedUsages = List.of("java\\.util\\.(?!" + String.join("|", allowed) + ")");
        Inspection.assertNoUsageOf(BEAVERMAP_GRAPH_SOURCE, bannedUsages);
    }


    // Only use Caltech map and buildings to test for correctness
    @Tag("C")
    @Tag("Beavermap")
    @Test
    @Order(3)
    @DisplayName("Test getLocationById()")
    @TestDescription("This is a functionality test")
    @TestHint("Make sure that when you add a vertex of a location to your graph, you also add its id mapping" +
            " to the full Location in this.ids. You should maintain the invariant in your code that vertices" +
            " have the ids of locations as data, and the full locations can be accessed for every vertex by" +
            " calling get(id) on this.ids\n" +
            "If you are facing errors parsing roads, make sure you are using .getAsLong() method instead " +
            "of .getAsJsonObject() to process the road locations since the roads are given as an array of array" +
            " of longs representing location ids")
    @DependsOn({"constructor", "getLocationByID"})
    public void testGetLocationByID() {
        BeaverMapsGraph bmg = new BeaverMapsGraph(
                "data/caltech/caltech.buildings.json",
                "data/caltech/caltech.waypoints.json",
                "data/caltech/caltech.roads.json");
        JsonElement bs = fromFile("data/caltech/caltech.buildings.json");
        for (JsonElement b : bs.getAsJsonArray()) {
            Location loc = new Location(b.getAsJsonObject());
            assertNotNull(bmg.getLocationByID(loc.id), "Location id " + loc.id + " not found by id");
        }
    }

    @Tag("C")
    @Tag("Beavermap")
    @Test
    @Order(4)
    @DisplayName("Test getLocationByName()")
    @TestDescription("This is a functionality test")
    @TestHint("If you are facing errors parsing roads, make sure you are using .getAsLong() method instead " +
            "of .getAsJsonObject() to process the road locations since the roads are given as an array of array" +
            " of longs representing location ids")
    @DependsOn({"constructor", "getLocationByName"})
    public void testGetLocationByName() {
        BeaverMapsGraph bmg = new BeaverMapsGraph(
                "data/caltech/caltech.buildings.json",
                "data/caltech/caltech.waypoints.json",
                "data/caltech/caltech.roads.json");
        JsonElement bs = fromFile("data/caltech/caltech.buildings.json");
        for (JsonElement b : bs.getAsJsonArray()) {
            Location loc = new Location(b.getAsJsonObject());
            if (loc.name != null) {
                assertNotNull(bmg.getLocationByName(loc.name), "Location " + loc.name + " not found by name");
            }
        }
    }

    @Tag("C")
    @Tag("Beavermap")
    @DisplayName("Test getBuildings()")
    @TestDescription("This is a functionality test")
    @TestHint("If you are facing errors parsing roads, make sure you are using .getAsLong() method instead " +
            "of .getAsJsonObject() to process the road locations since the roads are given as an array of array" +
            " of longs representing location ids")
    @DependsOn({"constructor", "getBuildings"})
    @ParameterizedTest(name = "Test getBuildings() on {0}")
    @CsvSource({
            "caltech/caltech.buildings.json, caltech/caltech.waypoints.json, caltech/caltech.roads.json",
            "pasadena/pasadena.buildings.json, pasadena/pasadena.waypoints.json, pasadena/pasadena.roads.json",
    })
    @Order(5)
    public void testGetBuildings(String buildingsFile, String waypointsFile, String roadsFile) {
        BeaverMapsGraph bmg = new BeaverMapsGraph(
                "data/" + buildingsFile, "data/" + waypointsFile, "data/" + roadsFile);
        Set<Location> buildings = new HashSet<>();
        JsonElement bs = fromFile("data/" + buildingsFile);
        for (JsonElement b : bs.getAsJsonArray()) {
            Location loc = new Location(b.getAsJsonObject());
            buildings.add(loc);
        }
        MatcherAssert.assertThat(bmg.getBuildings(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(buildings.toArray()));
    }

    @Tag("C")
    @Tag("Beavermap")
    @DisplayName("Test getClosestBuilding()")
    @TestDescription("This is a functionality test")
    @TestHint("If you are stuck in an infinite loop, please verify that your ChainingHashDictionary contains" +
            " framework to be resizeable past 400k, either by giving it more prime capacities or by writing code" +
            " to double the length if we have exhausted all primes")
    @DependsOn({"constructor", "getLocationByID", "getClosestBuilding"})
    @ParameterizedTest(name = "Test getClosestBuilding() on {0}")
    @CsvSource({
            "caltech/caltech.buildings.json, caltech/caltech.waypoints.json, caltech/caltech.roads.json, caltech/caltech.closest_trace.json",
            "pasadena/pasadena.buildings.json, pasadena/pasadena.waypoints.json, pasadena/pasadena.roads.json, pasadena/pasadena.closest_trace.json",
    })
    @Order(6)
    public void testGetClosestLocation(String buildingsFile, String waypointsFile, String roadsFile, String traceFile) {
        BeaverMapsGraph bmg = new BeaverMapsGraph(
                "data/" + buildingsFile, "data/" + waypointsFile, "data/" + roadsFile);
        JsonElement bs = fromFile("data/" + traceFile);
        for (JsonElement b : bs.getAsJsonArray()) {
            JsonObject curr = b.getAsJsonObject();
            Location center = bmg.getLocationByID(curr.get("center").getAsLong());
            Location closestExpected = bmg.getLocationByID(curr.get("closest").getAsLong());
            Location closestActual = bmg.getClosestBuilding(center.lat, center.lon);
            assertEquals(closestExpected.lat, closestActual.lat, "Latitudes differ");
            assertEquals(closestExpected.lon, closestActual.lon, "Longitudes differ");
        }
    }

    @Tag("C")
    @Tag("Beavermap")
    @DisplayName("Test addVertex() updates BeaverMapsGraph and underlying Graph properly")
    @TestDescription("This is a functionality test")
    @TestHint("Make sure adding the vertex also updates the ids map!")
    @DependsOn({"constructor", "getLocationByID", "addVertex"})
    @Test
    @Order(7)
    public void testAddVertexBeaverMapsGraph() {
        BeaverMapsGraph bmg = new BeaverMapsGraph(
                "data/caltech/caltech.buildings.json",
                "data/caltech/caltech.waypoints.json",
                "data/caltech/caltech.roads.json");
        JsonElement bs = fromFile("data/pasadena/pasadena.buildings.json");
        for (JsonElement b : bs.getAsJsonArray()) {
            Location loc = new Location(b.getAsJsonObject());
            bmg.addVertex(loc);
            assertNotNull(bmg.getLocationByID(loc.id), "Location id " + loc.id + " not found by id");
            // Test that the vertex was actually added to the graph
            bmg.neighbors(loc.id);
        }
    }

    // Note: Pasadena map is WAY TOO LARGE to test all edges, don't try
    @Tag("C")
    @Tag("Beavermap")
    @DisplayName("Completely check all nodes and edges in BeaverMapsGraph loaded from files")
    @TestDescription("This is a functionality test")
    @DependsOn({"constructor", "getLocationByID", "vertices", "neighbors"})
    @ParameterizedTest(name = "Test nodes in file {0}")
    @CsvSource({
            "caltech/caltech.buildings.json, caltech/caltech.waypoints.json, caltech/caltech.roads.json, caltech/caltech.neighbors_trace.json"
    })
    @Order(8)
    public void testNodesEdgesInMap(String bFile, String wFile, String roadsFile, String traceFile) {
        BeaverMapsGraph bmg = new BeaverMapsGraph("data/" + bFile, "data/" + wFile, "data/" + roadsFile);

        List<Long> actualNodeIDs = new ArrayList<>();
        for (long nid : bmg.vertices()) {
            actualNodeIDs.add(nid);
        }

        JsonElement s = fromFile("data/" + traceFile);
        for (JsonElement b : s.getAsJsonArray()) {
            JsonObject curr = b.getAsJsonObject();
            long locID = curr.get("id").getAsLong();
            Location loc = bmg.getLocationByID(locID);
            assertNotNull(loc, locID + " should be in graph, but is not");
            actualNodeIDs.remove(locID);

            JsonArray neighbors = curr.get("neighbors").getAsJsonArray();
            ISet<Long> actualNeighbors = bmg.neighbors(locID);
            List<Long> missingNeighbors = new ArrayList<>();
            for (JsonElement e : neighbors) {
                long neighborID = e.getAsLong();
                if (!actualNeighbors.remove(neighborID)) {
                    missingNeighbors.add(neighborID);
                }
            }

            // Use this instead of MatcherAssert to provide better errors (though I doubt they'll be needed)
            // Should use Truth instead... but not this year.
            if (missingNeighbors.size() > 0) {
                fail(locID + " missing neighbors " + missingNeighbors);
            } else if (actualNeighbors.size() != 0) {
                fail(locID + " has extra neighbors " + actualNeighbors);
            }
        }

        assertEquals(0, actualNodeIDs.size(), "Graph has extra nodes: " + actualNodeIDs);
    }

    @Tag("B")
    @DisplayName("Test DFS radius search")
    @TestDescription("This is a functionality test")
    @TestHint("If you are stuck in an infinite loop, please verify that your ChainingHashDictionary contains" +
            " framework to be resizeable past 400k, either by giving it more prime capacities or by writing code" +
            " to double the length if we have exhausted all primes")
    @DependsOn({"constructor", "getLocationByID", "dfs"})
    @ParameterizedTest(name = "Test DFS on graph {0}")
    @CsvSource({
            "caltech/caltech.buildings.json, caltech/caltech.waypoints.json, caltech/caltech.roads.json, caltech/caltech.radius_trace.json",
            "pasadena/pasadena.buildings.json, pasadena/pasadena.waypoints.json, pasadena/pasadena.roads.json, pasadena/pasadena.radius_trace.json",
    })
    @Order(9)
    public void testDFSRadius(String buildingsFile, String waypointsFile, String roadsFile, String traceFile) {

        BeaverMapsGraph bmg = new BeaverMapsGraph(
                "data/" + buildingsFile, "data/" + waypointsFile, "data/" + roadsFile);

        JsonElement s = fromFile("data/" + traceFile);
        for (JsonElement b : s.getAsJsonArray()) {
            JsonObject curr = b.getAsJsonObject();
            long locID = curr.get("center").getAsLong();
            Location loc = bmg.getLocationByID(locID);
            double dist = curr.get("radius").getAsDouble();

            JsonArray locList = curr.get("locations").getAsJsonArray();
            Set<Long> expectedLocIDs = new HashSet<>();
            for (JsonElement e : locList) {
                expectedLocIDs.add(e.getAsLong());
            }

            ISet<Location> actualLoc = bmg.dfs(loc, dist);
            Set<Long> locIDs = new HashSet<>(actualLoc.size());
            for (Location l : actualLoc) {
                locIDs.add(l.id);
            }
            MatcherAssert.assertThat(locIDs,
                    IsIterableContainingInAnyOrder.containsInAnyOrder(expectedLocIDs.toArray()));
        }
    }

    @Tag("A")
    @DisplayName("Test buildings are ignored in dijkstra path")
    @TestDescription("This is a functionality test")
    @TestHint("Be sure that when you construct your final path, you are making it in the correct order. " +
            "The path should begin with the start vertex and end with the target vertex.")
    @DependsOn({"constructor", "getLocationByID", "dijkstra"})
    @Test
    @Order(10)
    public void testDijkstraIgnoreBuildings() {
        BeaverMapsGraph bmg = new BeaverMapsGraph(
                "data/caltech/caltech.buildings.json",
                "data/caltech/caltech.waypoints.json",
                "data/caltech/caltech.roads.json");
        JsonElement s = fromFile("data/caltech/caltech.paths_trace.json");
        for (JsonElement b : s.getAsJsonArray()) {
            JsonObject curr = b.getAsJsonObject();
            Location start = bmg.getLocationByID(curr.get("start").getAsLong());
            Location target = bmg.getLocationByID(curr.get("target").getAsLong());

            IDeque<Location> actualPath = bmg.dijkstra(start, target);
            if (actualPath == null) {
                continue;
            }
            for (Location loc : actualPath){
                if (loc.id == start.id || loc.id == target.id) {
                    continue;
                }
                ISet<Location> buildings = Reflection.getFieldValue(BeaverMapsGraph.class, "buildings", bmg);
                assertFalse(buildings.contains(loc), "Location " + loc.id + " in path is a building");
            }
        }
    }

    @Tag("A")
    @DisplayName("Test Dijkstra")
    @TestDescription("This is a functionality test")
    @TestHint("Be sure that when you construct your final path, you are making it in the correct order. " +
            "The path should begin with the start vertex and end with the target vertex.\n" +
            "Make sure you are finding the distances between adjacent locations correctly (check FAQs for more!)" +
            " and that when you travel your road, you are grabbing the locations you want.")
    @DependsOn({"constructor", "getLocationByID", "dijkstra"})
    @ParameterizedTest(name = "Test Dijkstra on graph {0}")
    @CsvSource({
            "caltech/caltech.buildings.json, caltech/caltech.waypoints.json, caltech/caltech.roads.json, caltech/caltech.paths_trace.json",
            "pasadena/pasadena.buildings.json, pasadena/pasadena.waypoints.json, pasadena/pasadena.roads.json, pasadena/pasadena.paths_trace.json",
    })
    @Order(11)
    public void testDijkstraBeaverMap(String buildingsFile, String waypointsFile, String roadsFile, String traceFile) throws FileNotFoundException {
        BeaverMapsGraph bmg = new BeaverMapsGraph(
                "data/" + buildingsFile, "data/" + waypointsFile, "data/" + roadsFile);
        JsonElement s = fromFile("data/" + traceFile);
        for (JsonElement b : s.getAsJsonArray()) {
            JsonObject curr = b.getAsJsonObject();
            Location start = bmg.getLocationByID(curr.get("start").getAsLong());
            Location target = bmg.getLocationByID(curr.get("target").getAsLong());

            // Build expected list
            JsonArray pathList = curr.get("path").getAsJsonArray();
            List<Long> expectedPathIDs = new ArrayList<>();
            for (JsonElement e : pathList) {
                expectedPathIDs.add(e.getAsLong());
            }

            IDeque<Location> actualPath = bmg.dijkstra(start, target);
            List<Long> actualPathIDs = new ArrayList<>();

            if (expectedPathIDs.size() == 0) {
                assertNull(actualPath, "Path does not exist from " + start.id + " to " + target.id + " but was found");
            }
            else {
                assertNotNull(actualPath, "Path exists from " + start.id + " to " + target.id + " but was not found");
                for (Location l : actualPath) {
                    actualPathIDs.add(l.id);
                }
                // Check that path is *exactly* equivalent
                MatcherAssert.assertThat(actualPathIDs,
                        IsIterableContainingInOrder.contains(expectedPathIDs.toArray()));
            }

        }
    }
}
