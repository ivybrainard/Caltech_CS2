package edu.caltech.cs2.datastructures;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.ISet;

import java.io.FileReader;
import java.io.IOException;


public class BeaverMapsGraph extends Graph<Long, Double> {
    private IDictionary<Long, Location> ids;
    private ISet<Location> buildings;

    public BeaverMapsGraph() {
        super();
        this.buildings = new ChainingHashSet<>();
        this.ids = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    /**
     * Reads in buildings, waypoinnts, and roads file into this graph.
     * Populates the ids, buildings, vertices, and edges of the graph
     * @param buildingsFileName the buildings filename
     * @param waypointsFileName the waypoints filename
     * @param roadsFileName the roads filename
     */
    public BeaverMapsGraph(String buildingsFileName, String waypointsFileName, String roadsFileName) {
        this();
        JsonElement bs = fromFile(buildingsFileName);
        for (JsonElement b : bs.getAsJsonArray()) {
            Location loc = new Location(b.getAsJsonObject());
            addVertex(loc.id);
            ids.put(loc.id, loc);
            this.buildings.add(loc);
        }

        JsonElement ws = fromFile(waypointsFileName);
        for (JsonElement b : ws.getAsJsonArray()) {
            Location loc = new Location(b.getAsJsonObject());
            addVertex(loc.id);
            this.ids.put(loc.id, loc);

        }



        JsonElement rd = fromFile(roadsFileName);
        for (JsonElement b : rd.getAsJsonArray()) {
            JsonArray x = b.getAsJsonArray();
            for(int i = 0; i < x.size()-1; i++) {
                long y = x.get(i).getAsLong();
                long z = x.get(i+1).getAsLong();
                addUndirectedEdge(y, z, ids.get(y).getDistance(ids.get(z)));
            }
        }


        // TODO (student): Write This
    }

    /**
     * Returns a deque of all the locations with the name locName.
     * @param locName the name of the locations to return
     * @return a deque of all location with the name locName
     */
    public IDeque<Location> getLocationByName(String locName) {
        IDeque<Location> list = new ArrayDeque<>();
        for(Location loc : ids.values()) {
            if(locName == loc.name) {
                list.add(loc);
            }
        }
        // TODO (student): Write This
        return list;
    }

    /**
     * Returns the Location object corresponding to the provided id
     * @param id the id of the object to return
     * @return the location identified by id
     */
    public Location getLocationByID(long id) {

        return ids.get(id);
    }

    /**
     * Adds the provided location to this map.
     * @param n the location to add
     * @return true if n is a new location and false otherwise
     */
    public boolean addVertex(Location n) {
        addVertex(n.id);
        ids.put(n.id, n);
        return true;
    }

    /**
     * Returns the closest building to the location (lat, lon)
     * @param lat the latitude of the location to search near
     * @param lon the longitute of the location to search near
     * @return the building closest to (lat, lon)
     */
    public Location getClosestBuilding(double lat, double lon) {
        Location location = null;
        double refernce = Double.MAX_VALUE;
        for(Location loc : buildings) {
            if(refernce > loc.getDistance(lat, lon)) {
                location = loc;
                refernce = loc.getDistance(lat, lon);
            }
        }
        // TODO (student): Write This
        return location;
    }

    /**
     * Returns a set of locations which are reachable along a path that goes no further than `threshold` feet from start
     * @param start the location to search around
     * @param threshold the number of feet in the search radius
     * @return all locations within the provided `threshold` feet from start
     */
    public ISet<Location> dfs(Location start, double threshold) {
        ISet<Location> list = new ChainingHashSet<>();
        ArrayDeque<Long> location = new ArrayDeque<>();
        Location curr = start;
        location.add(start.id);
        while(!location.isEmpty()) {
            list.add(curr);
            for(Long l : this.neighbors(curr.id)) {
                if(!list.contains(getLocationByID(l)) && getLocationByID(l).getDistance(start) <= threshold) {
                    list.add(getLocationByID(l));
                    location.addFront(l);
                }
            }

            if(!location.isEmpty()) {
                curr = getLocationByID(location.removeFront());
            }
        }

        return list;
    }

    /**
     * Returns a list of Locations corresponding to
     * buildings in the current map.
     * @return a list of all building locations
     */
    public ISet<Location> getBuildings() {
        return this.buildings;
    }

    /**
     * Returns a shortest path (i.e., a deque of vertices) between the start
     * and target locations (including the start and target locations).
     * @param start the location to start the path from
     * @param target the location to end the path at
     * @return a shortest path between start and target
     */
    public IDeque<Location> dijkstra(Location start, Location target) {
        // TODO (student): Write This
        return null;
    }

    /**
     * Returns a JsonElement corresponding to the data in the file
     * with the filename filename
     * @param filename the name of the file to return the data from
     * @return the JSON data from filename
     */
    private static JsonElement fromFile(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            return JsonParser.parseReader(reader);
        } catch (IOException e) {
            return null;
        }
    }
}
