package net.acomputerdog.boxle.save.world;

import net.acomputerdog.boxle.math.loc.CoordConverter;
import net.acomputerdog.boxle.math.vec.Vec3i;
import net.acomputerdog.boxle.math.vec.VecPool;
import net.acomputerdog.boxle.save.SaveManager;
import net.acomputerdog.boxle.save.world.files.Region;
import net.acomputerdog.boxle.world.World;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

//This class is more or less thread safe, but generates a lot of short-lived objects and so should be used lightly.
public class Regions {

    private static final Map<RegionLoc, Region> regionMap = new ConcurrentHashMap<>();
    private static final Map<World, Set<Region>> regionSet = new ConcurrentHashMap<>();

    public static void removeRegion(Region region) {
        getRegionList(region.getWorld()).remove(region);
        Vec3i loc = region.getLoc();
        regionMap.remove(new RegionLoc(region.getWorld(), loc.x, loc.y, loc.z));
        VecPool.free(loc);
    }

    public static Region getOrLoadRegion(World world, int x, int y, int z) {
        RegionLoc loc = new RegionLoc(world, x, y, z);
        Region region = regionMap.get(loc);
        if (region == null) {
            WorldSave save = SaveManager.getLoadedWorld(world.getName());
            region = save.getRegion(x, y, z);
            regionMap.put(loc, region);
            getRegionList(world).add(region);
        }
        return region;
    }

    private static Set<Region> getRegionList(World world) {
        Set<Region> list = regionSet.get(world);
        if (list == null) {
            regionSet.put(world, list = new ConcurrentSkipListSet<>());
        }
        return list;
    }

    public static Set<Region> getRegionsForWorld(World world) {
        return Collections.unmodifiableSet(getRegionList(world));
    }

    public static Region getOrLoadRegionChunkLoc(World world, int x, int y, int z) {
        return getOrLoadRegion(world, CoordConverter.regionLocOfChunk(x), CoordConverter.regionLocOfChunk(y), CoordConverter.regionLocOfChunk(z));
    }

    public static Region getRegionChunkLoc(World world, int x, int y, int z) {
        return getRegion(world, CoordConverter.regionLocOfChunk(x), CoordConverter.regionLocOfChunk(y), CoordConverter.regionLocOfChunk(z));

    }

    public static Region getRegion(World world, int x, int y, int z) {
        return regionMap.get(new RegionLoc(world, x, y, z));
    }

    private static class RegionLoc implements Comparable<RegionLoc> {
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


        @Override
        public int compareTo(RegionLoc o) {
            return this.hashCode() - o.hashCode();
        }
    }
}
