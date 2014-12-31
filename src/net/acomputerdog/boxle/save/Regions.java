package net.acomputerdog.boxle.save;

import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.save.world.WorldSave;
import net.acomputerdog.boxle.world.World;

import java.util.*;

public class Regions {

    private static final RegionLoc readLoc = new RegionLoc(null, 0, 0, 0);

    private static final Map<RegionLoc, Region> regionMap = new HashMap<>();
    private static final Map<World, Set<Region>> regionSet = new HashMap<>();

    public static void removeRegion(Region region) {
        //System.out.println("Removing region at: " + region.getLoc().asCoords());
        getRegionList(region.getWorld()).remove(region);
        readLoc.world = region.getWorld();
        Vec3i loc = region.getLoc();
        readLoc.x = loc.x;
        readLoc.y = loc.y;
        readLoc.z = loc.z;
        regionMap.remove(readLoc);
        VecPool.free(loc);
    }

    public static Region getOrLoadRegion(World world, int x, int y, int z) {
        readLoc.world = world;
        readLoc.x = x;
        readLoc.y = y;
        readLoc.z = z;
        Region region = regionMap.get(readLoc);
        if (region == null) {
            WorldSave save = SaveManager.getLoadedWorld(world.getName());
            region = save.getRegion(x, y, z);
            regionMap.put(readLoc.copy(), region);
            getRegionList(world).add(region);
        }
        return region;
    }

    private static Set<Region> getRegionList(World world) {
        Set<Region> list = regionSet.get(world);
        if (list == null) {
            regionSet.put(world, list = new HashSet<Region>());
        }
        return list;
    }

    public static Set<Region> getRegionsForWorld(World world) {
        return Collections.unmodifiableSet(getRegionList(world));
    }

    public static Region getOrLoadRegionChunkLoc(World world, int x, int y, int z) {
        return getOrLoadRegion(world, (int) Math.floor((float) x / (float) Region.REGION_SIZE), (int) Math.floor((float) y / (float) Region.REGION_SIZE), (int) Math.floor((float) z / (float) Region.REGION_SIZE));
    }

    public static Region getRegionChunkLoc(World world, int x, int y, int z) {
        return getRegion(world, (int) Math.floor((float) x / (float) Region.REGION_SIZE), (int) Math.floor((float) y / (float) Region.REGION_SIZE), (int) Math.floor((float) z / (float) Region.REGION_SIZE));

    }

    public static Region getRegion(World world, int x, int y, int z) {
        readLoc.world = world;
        readLoc.x = x;
        readLoc.y = y;
        readLoc.z = z;
        return regionMap.get(readLoc);
    }

    private static class RegionLoc {
        private World world;
        private int x;
        private int y;
        private int z;

        public RegionLoc(World world, int x, int y, int z) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public RegionLoc copy() {
            return new RegionLoc(world, x, y, z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RegionLoc)) return false;

            RegionLoc regionLoc = (RegionLoc) o;

            return x == regionLoc.x && y == regionLoc.y && z == regionLoc.z && world.equals(regionLoc.world);

        }

        @Override
        public int hashCode() {
            int result = world.hashCode();
            result = 31 * result + x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }
    }
}
