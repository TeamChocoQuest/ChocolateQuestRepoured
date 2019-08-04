package com.teamcqr.chocolatequestrepoured.dungeonprot;

import java.util.ArrayList;

import com.teamcqr.chocolatequestrepoured.intrusive.IntrusiveModificationHelper;
import com.teamcqr.chocolatequestrepoured.util.data.IO.ICQONReady;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Custom type for containing info about a protected region
 * Intended for use by dungeons but can theoretically be anything
 *
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 23.07.19
 */
public class ProtectedRegion implements ICQONReady {

    /*
     * Variables
     */

    // Region Data - Immutable
    private final BlockPos NWCorner;
    private final BlockPos SECorner;
    private final World world;

    // Dependencies (Things that will disable ProtectedRegion if killed/destroyed)
    private ArrayList<Entity> entityDependencies = new ArrayList<>(); // Likely a dungeon boss
    private ArrayList<Block> blockDependencies = new ArrayList<>();   // Likely a ForceFieldNexus

    // Settings
    public boolean preventBlockBreak = true;
    public boolean preventBlockBreakCreative = false;
    public boolean preventBlockPlace = true;
    public boolean preventBlockPlaceCreative = false;
    public boolean preventExplosionTNT = false;
    public boolean preventExplosionOther = true;
    public boolean preventFireSpread = true;
    public boolean preventNaturalMobSpawn = true;

    // Variable Names for Serialization
    public String[] getCQONFieldNames() {
        return IntrusiveModificationHelper.reflectGetAllFieldNames(this);
    }

    /*
     * Constructors
     */

    // Contains all available region settings
    public ProtectedRegion(BlockPos NWCorner, BlockPos SECorner, World world, ArrayList<Entity> entityDependencies, ArrayList<Block> blockDependencies, boolean preventBlockBreak, boolean preventBlockBreakCreative, boolean preventBlockPlace, boolean preventBlockPlaceCreative, boolean preventExplosionTNT, boolean preventExplosionOther, boolean preventFireSpread, boolean preventNaturalMobSpawn) {
        // Region Data
        this.NWCorner = NWCorner;
        this.SECorner = SECorner;
        this.world = world;
        // Dependencies
        this.entityDependencies.addAll(entityDependencies);
        this.blockDependencies.addAll(blockDependencies);
        // Protection Settings
        this.preventBlockBreak = preventBlockBreak;
        this.preventBlockBreakCreative = preventBlockBreakCreative;
        this.preventBlockPlace = preventBlockPlace;
        this.preventBlockPlaceCreative = preventBlockPlaceCreative;
        this.preventExplosionTNT = preventExplosionTNT;
        this.preventExplosionOther = preventExplosionOther;
        this.preventFireSpread = preventFireSpread;
        this.preventNaturalMobSpawn = preventNaturalMobSpawn;
    }

    // Contains only region data and uses default values for all dependency and protection settings
    public ProtectedRegion(BlockPos NWCorner, BlockPos SECorner, World world) {
        this.NWCorner = NWCorner;
        this.SECorner = SECorner;
        this.world = world;
    }

    /*
     * Private Field Accessors
     */

    public void addEntityDependency(Entity entity) {
        entityDependencies.add(entity);
    }

    public void addBlockDependency(Block block) {
        blockDependencies.add(block);
    }

    public ArrayList<Entity> getEntityDependencies() {
        return entityDependencies;
    }

    public ArrayList<Block> getBlockDependencies() {
        return blockDependencies;
    }

    /*
     * Util
     */

    public boolean checkIfBlockPosInRegion(BlockPos toCheck, World ofBlockPos) {

        // Check World
        if(!world.equals(ofBlockPos)) return false;

        // Check NW (min)
        if(toCheck.getX() < NWCorner.getX()) return false;
        if(toCheck.getY() < NWCorner.getY()) return false;
        if(toCheck.getZ() < NWCorner.getZ()) return false;

        // Check SE (max)
        if(toCheck.getX() > SECorner.getX()) return false;
        if(toCheck.getY() > SECorner.getY()) return false;
        if(toCheck.getZ() > SECorner.getZ()) return false;

        // Default (this means that all disqualifiers failed)
        return true;

    }

    public int getRegionVolume() {
        // Calculate deltas for x,y,z and multiply results
        return ( this.SECorner.getX() - this.NWCorner.getX() ) * ( this.SECorner.getY() - this.NWCorner.getY() ) * ( this.SECorner.getZ() - this.NWCorner.getZ() );
    }

}
