package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;

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
        for(Iterator<ProtectedRegion> it = regions.iterator();it.hasNext();) {
            it.next().checkBreakEvent(e);
        }
    }

    public void checkSpawn(LivingSpawnEvent.CheckSpawn e) {
        for(Iterator<ProtectedRegion> it = regions.iterator();it.hasNext();) {
            it.next().checkSpawnEvent(e);
        }
    }
    public void checkPortalSpawning(BlockEvent.PortalSpawnEvent e) {
    	for (ProtectedRegion r: regions) {
            r.checkPortalEvent(e);
        }
    }

    public static void init() {
        PROTECTION_HANDLER = new ProtectionHandler();
    }

    public void save(ChunkDataEvent.Save e) {

        for(Iterator<ProtectedRegion> it = regions.iterator();it.hasNext();) {
            ProtectedRegion region = it.next();
            if(e.getWorld().getChunkFromBlockCoords(region.getMin())==e.getChunk()) {
                NBTTagList list = new NBTTagList();
                list.appendTag(region.save());
                e.getData().setTag("protectedRegions",list);
                return;
            }
        }
    }

    public void checkUnload(ChunkEvent.Unload e) {
        for(Iterator<ProtectedRegion> it = regions.iterator();it.hasNext();) {
            ProtectedRegion region = it.next();
            if(e.getWorld().getChunkFromBlockCoords(region.getMin())==e.getChunk()) {
                removeRegion(region);
                return;
            }
        }
    }

    public void load(ChunkDataEvent.Load e) {
        if(e.getData().hasKey("protectedRegions")) {
            NBTTagList list = e.getData().getTagList("protectedRegions", Constants.NBT.TAG_COMPOUND);
            for(int i = 0;i<list.tagCount();i++) {
                addRegion(new ProtectedRegion(list.getCompoundTagAt(i)));
            }
        }
    }
}
