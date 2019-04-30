package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;

import java.util.ArrayList;

/**
 * Copyright (c) 29.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ProtectionHandler {

    public static ProtectionHandler PROTECTION_HANDLER;

    private ArrayList<ProtectedRegion> regions;

    public ProtectionHandler() {
        this.regions = new ArrayList<>();
    }

    public void addRegion(ProtectedRegion region) {
        regions.listIterator(regions.size()).add(region);
    }

    public void removeRegion(ProtectedRegion region) {
        regions.listIterator(regions.indexOf(region)).remove();
    }

    public void check(BlockEvent.BreakEvent e) {
        for (ProtectedRegion r: regions) {
            r.checkBreakEvent(e);
        }
    }

    public void checkSpawn(LivingSpawnEvent.CheckSpawn e) {
        for (ProtectedRegion r: regions) {
            r.checkSpawnEvent(e);
        }
    }

    public static void init() {
        PROTECTION_HANDLER = new ProtectionHandler();
    }

    public synchronized void save(ChunkDataEvent.Save e) {
        for(ProtectedRegion r:regions) {
            if(e.getWorld().getChunkFromBlockCoords(r.getMin())==e.getChunk()) {
                NBTTagList list = new NBTTagList();
                list.appendTag(r.save());
                e.getData().setTag("protectedRegions",list);
            }
        }
    }

    public synchronized void load(ChunkDataEvent.Load e) {
        if(e.getData().hasKey("protectedRegions")) {
            NBTTagList list = e.getData().getTagList("protectedRegions", Constants.NBT.TAG_COMPOUND);
            for(int i = 0;i<list.tagCount();i++) {
                addRegion(new ProtectedRegion(list.getCompoundTagAt(i)));
            }
        }
    }
}
