package com.teamcqr.chocolatequestrepoured.tileentity;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.UUID;

/**
 * Copyright (c) 25.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class TileEntityForceFieldNexus extends TileEntity {

    private DungeonBase dungeonBase;
    private UUID uuid;

    public TileEntityForceFieldNexus() {
    }

    public TileEntityForceFieldNexus(DungeonBase dungeonBase) {
        this.dungeonBase = dungeonBase;
        this.uuid = dungeonBase.getDungeonID();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    public DungeonBase getDungeonBase() {
        return dungeonBase;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setDungeonBase(DungeonBase dungeonBase) {
        this.dungeonBase = dungeonBase;
    }
}
