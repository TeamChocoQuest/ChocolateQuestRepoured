package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityForceFieldNexus;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Copyright (c) 03.07.2019
 * Developed by jdawg3636
 * GitHub: https://github.com/jdawg3636
 */
public class ProtectedRegion implements Serializable {

    // Region Data
    private BlockPos SWCorner;
    private BlockPos NECorner;
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
    public ProtectedRegion(BlockPos SWCorner, BlockPos NECorner, UUID uuid, boolean blockSurvival, boolean blockCreative, boolean blockFire, boolean blockNaturalMobSpawn, boolean blockPortalSpawn) {
        this.SWCorner = SWCorner;
        this.NECorner = NECorner;
        this.uuid = uuid;
        this.entityBossDependencies = new ArrayList<Entity>();
        this.nexusDependencies = new ArrayList<TileEntityForceFieldNexus>();
        this.blockSurvival = blockSurvival;
        this.blockCreative = blockCreative;
        this.blockFire = blockFire;
        this.blockNaturalMobSpawn = blockNaturalMobSpawn;
        this.blockPortalSpawn = blockPortalSpawn;
    }

    public ProtectedRegion(BlockPos SWCorner, BlockPos NECorner) {
        this.SWCorner = SWCorner;
        this.NECorner = NECorner;
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
    public BlockPos getSWCorner() {
        return SWCorner;
    }

    public BlockPos getNECorner() {
        return NECorner;
    }

    public UUID getUUID() {
        return uuid;
    }

    /** @return blockSurvival, blockCreative, blockFire, blockNaturalMobSpawn, blockPortalSpawn */
    public boolean[] getSettings() {
        return new boolean[]{blockSurvival, blockCreative, blockFire, blockNaturalMobSpawn, blockPortalSpawn};
    }

    // Setters & Manipulators
    public void setSWCorner(BlockPos SWCorner) {
        this.SWCorner = SWCorner;
    }

    public void setNECorner(BlockPos NECorner) {
        this.NECorner = NECorner;
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

    // Util
    public NBTTagCompound getFieldsAsNBT() {

        NBTTagCompound toReturn = new NBTTagCompound();

        toReturn.setInteger("SWX", SWCorner.getX());
        toReturn.setInteger("SWY", SWCorner.getY());
        toReturn.setInteger("SWZ", SWCorner.getZ());

        toReturn.setInteger("NEX", NECorner.getX());
        toReturn.setInteger("NEY", NECorner.getY());
        toReturn.setInteger("NEZ", NECorner.getZ());

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

}
