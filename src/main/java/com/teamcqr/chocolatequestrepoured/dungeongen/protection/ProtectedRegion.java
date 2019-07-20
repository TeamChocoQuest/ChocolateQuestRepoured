package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityForceFieldNexus;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Written 12.07.2019 by jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * Original (Old) Version Copyright (c) 29.04.2019 MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ProtectedRegion {

    // Region Data
    private BlockPos NWCorner;
    private BlockPos SECorner;
    private World world;
    private UUID uuid;

    // Dependencies (Things that will disable ProtectedRegion if destroyed)
    private ArrayList<Entity> entityBossDependencies;
    private ArrayList<TileEntityForceFieldNexus> nexusDependencies;

    // Settings
    private boolean blockSurvival;
    private boolean blockCreative;
    private boolean blockFire;
    private boolean blockNaturalMobSpawn;
    private boolean blockPortalSpawn;

    // Constructors
    public ProtectedRegion(BlockPos NWCorner, BlockPos SECorner, World world, UUID uuid, boolean blockSurvival, boolean blockCreative, boolean blockFire, boolean blockNaturalMobSpawn, boolean blockPortalSpawn) {
        this.NWCorner = NWCorner;
        this.SECorner = SECorner;
        this.world = world;
        this.uuid = uuid;
        this.entityBossDependencies = new ArrayList<Entity>();
        this.nexusDependencies = new ArrayList<TileEntityForceFieldNexus>();
        this.blockSurvival = blockSurvival;
        this.blockCreative = blockCreative;
        this.blockFire = blockFire;
        this.blockNaturalMobSpawn = blockNaturalMobSpawn;
        this.blockPortalSpawn = blockPortalSpawn;
    }

    public ProtectedRegion(BlockPos NWCorner, BlockPos SECorner, World world) {
        this.NWCorner = NWCorner;
        this.SECorner = SECorner;
        this.world = world;
        this.uuid = UUID.randomUUID();
        this.entityBossDependencies = new ArrayList<Entity>();
        this.nexusDependencies = new ArrayList<TileEntityForceFieldNexus>();
        this.blockSurvival = true;
        this.blockCreative = false;
        this.blockFire = true;
        this.blockNaturalMobSpawn = true;
        this.blockPortalSpawn = true;
    }

    // Getters
    public BlockPos getNWCorner() {
        return NWCorner;
    }

    public BlockPos getSECorner() {
        return SECorner;
    }

    public World getWorld() {
        return world;
    }

    public UUID getUUID() {
        return uuid;
    }

    /** @return blockSurvival, blockCreative, blockFire, blockNaturalMobSpawn, blockPortalSpawn */
    public boolean[] getSettings() {
        return new boolean[]{blockSurvival, blockCreative, blockFire, blockNaturalMobSpawn, blockPortalSpawn};
    }

    // Setters & Manipulators
    public void setNWCorner(BlockPos NWCorner) {
        this.NWCorner = NWCorner;
    }

    public void setSECorner(BlockPos SECorner) {
        this.SECorner = SECorner;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void addEntityBossDependency(Entity entityBossDependency) {
        this.entityBossDependencies.add(entityBossDependency);
    }

    public void clearEntityBossDependencies() {
        this.entityBossDependencies.clear();
    }

    public void addNexusDependency(TileEntityForceFieldNexus nexusDependency) {
        this.nexusDependencies.add(nexusDependency);
    }

    public void clearNexusDependencies() {
        this.nexusDependencies.clear();
    }

    public void setBlockSurvival(boolean blockSurvival) {
        this.blockSurvival = blockSurvival;
    }

    public void setBlockCreative(boolean blockCreative) {
        this.blockCreative = blockCreative;
    }

    public void setBlockFire(boolean blockFire) {
        this.blockFire = blockFire;
    }

    public void setBlockNaturalMobSpawn(boolean blockNaturalMobSpawn) {
        this.blockNaturalMobSpawn = blockNaturalMobSpawn;
    }

    public void setBlockPortalSpawn(boolean blockPortalSpawn) {
        this.blockPortalSpawn = blockPortalSpawn;
    }

    /*
    // Util
    public NBTTagCompound getFieldsAsNBT() {

        NBTTagCompound toReturn = new NBTTagCompound();

        toReturn.setInteger("SWX", NWCorner.getX());
        toReturn.setInteger("SWY", NWCorner.getY());
        toReturn.setInteger("SWZ", NWCorner.getZ());

        toReturn.setInteger("NEX", SECorner.getX());
        toReturn.setInteger("NEY", SECorner.getY());
        toReturn.setInteger("NEZ", SECorner.getZ());

        toReturn.setUniqueId("UUID", uuid);

        try {
            toReturn.setString("EntityBossDependencies", new OutputStream().writeObject(this));
        } catch (Exception e) {
            System.out.println("[CQR ERROR] EXCEPTION THROWN WHILST SERIALIZING ProtectedRegion:");
            e.printStackTrace();
        }

        ByteStre
        return writeObject();
    }
    */

    public boolean checkIfBlockPosInRegion(BlockPos toCheck, World ofBlockPos) {

        // Check World
        if(toCheck.getX() < NWCorner.getX()) return false;

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

}
