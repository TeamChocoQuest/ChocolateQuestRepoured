package com.teamcqr.chocolatequestrepoured.tileentity;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.protection.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.dungeongen.protection.ProtectionHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.UUID;

/**
 * Copyright (c) 25.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class TileEntityForceFieldNexus extends TileEntity {

    private ProtectedRegion region;
    private UUID uuid;

    public TileEntityForceFieldNexus() {
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(hasData(compound)) {
            setUuid(compound.getUniqueId("dungeonUUID"));
            initUUIDRegion();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if(hasData()) {
            compound.setUniqueId("dungeonUUID",uuid);
        }
        return compound;
    }

    public void initUUIDRegion() {
        this.region = ProtectionHandler.PROTECTION_HANDLER.getProtectedRegionWithhUUID(uuid);
        if(region==null) {

        }
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ProtectedRegion getRegion() {
        return region;
    }

    private boolean hasData() {
        return uuid != null;
    }

    private boolean hasData(NBTTagCompound tagCompound) {
        return tagCompound.hasKey("dungeonUUID");
    }
}
