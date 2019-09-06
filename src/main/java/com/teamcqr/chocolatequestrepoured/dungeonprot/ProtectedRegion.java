package com.teamcqr.chocolatequestrepoured.dungeonprot;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
public class ProtectedRegion implements Serializable {

    /*
     * Variables
     */

    // Region Data
    private String UUID;
    private transient BlockPos NWCorner;
    private transient BlockPos SECorner;

    // Dependencies (Things that will remove ProtectedRegion if killed/destroyed)
    // TODO Implement serialization for dependencies
    private transient ArrayList<Entity> entityDependencies = new ArrayList<>(); // Likely a dungeon boss
    private transient ArrayList<BlockPos> blockDependencies = new ArrayList<>();   // Likely a ForceFieldNexus

    // Settings
    public boolean preventBlockBreak = true;
    public boolean preventBlockBreakCreative = false;
    public boolean preventBlockPlace = true;
    public boolean preventBlockPlaceCreative = false;
    public boolean preventExplosionTNT = false;
    public boolean preventExplosionOther = true;
    public boolean preventFireSpread = true;
    public boolean preventNaturalMobSpawn = true;

    /*
     * Constructors
     */

    // Contains all available region settings
    public ProtectedRegion(String UUID, BlockPos NWCorner, BlockPos SECorner, ArrayList<Entity> entityDependencies, ArrayList<BlockPos> blockDependencies, boolean preventBlockBreak, boolean preventBlockBreakCreative, boolean preventBlockPlace, boolean preventBlockPlaceCreative, boolean preventExplosionTNT, boolean preventExplosionOther, boolean preventFireSpread, boolean preventNaturalMobSpawn) {
        // Region Data
        this.UUID = UUID;
        this.NWCorner = NWCorner;
        this.SECorner = SECorner;
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
    public ProtectedRegion(String UUID, BlockPos NWCorner, BlockPos SECorner, World world) {
        this.UUID = UUID;
        this.NWCorner = NWCorner;
        this.SECorner = SECorner;
    }

    /*
     * Serialization
     */

    private void writeObject(ObjectOutputStream stream) throws Exception {
        try {
            stream.defaultWriteObject();
        } catch (Exception ignored) {}
        stream.writeInt(NWCorner.getX());
        stream.writeInt(NWCorner.getY());
        stream.writeInt(NWCorner.getZ());
        stream.writeInt(SECorner.getX());
        stream.writeInt(SECorner.getY());
        stream.writeInt(SECorner.getZ());
    }

    private void readObject(ObjectInputStream stream) throws Exception {
        try {
            stream.defaultReadObject();
        } catch (Exception ignored) {}
        NWCorner = new BlockPos(stream.readInt(), stream.readInt(), stream.readInt());
        SECorner = new BlockPos(stream.readInt(), stream.readInt(), stream.readInt());
    }

    /*
     * Private Field Accessors
     */

    public String getUUIDString() {
        return UUID;
    }

    public BlockPos getNWCorner() {
        return NWCorner;
    }

    public BlockPos getSECorner() {
        return SECorner;
    }

    public void addEntityDependency(Entity entity) {
        entityDependencies.add(entity);
    }

    public ArrayList<Entity> getEntityDependencies() {
        return entityDependencies;
    }

    public void addBlockDependency(BlockPos positionOfBlock) {
        blockDependencies.add(positionOfBlock);
    }

    public ArrayList<BlockPos> getBlockDependencies() {
        return blockDependencies;
    }

    /*
     * Util
     */

    // Assumes correct world
    public boolean checkIfBlockPosInRegion(BlockPos toCheck, World ofBlockPos) {

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
