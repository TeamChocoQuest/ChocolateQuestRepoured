package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ChunkCoordComparator;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;

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
        System.out.println("dddddd");
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
            if(e.getChunk().getPos().equals(new ChunkPos(region.getMax()))) {
                NBTTagList list = new NBTTagList();
                list.appendTag(region.save());
                e.getData().setTag("protectedRegions",list);
                break;
            }
        }
    }

    public void checkUnload(ChunkEvent.Unload e) {
        for(Iterator<ProtectedRegion> it = regions.iterator();it.hasNext();) {
            ProtectedRegion region = it.next();
            if(e.getChunk().getPos().equals(new ChunkPos(region.getMax()))) {
                it.remove();
                System.out.println("unload");
                System.out.println(regions.size());
                break;
            }
        }
    }

    public void load(ChunkDataEvent.Load e) {
        if(e.getData().getTagList("protectedRegions", Constants.NBT.TAG_COMPOUND) != null) {
            NBTTagList list = e.getData().getTagList("protectedRegions", Constants.NBT.TAG_COMPOUND);
            for(int i = 0;i<list.tagCount();i++) {
                addRegion(new ProtectedRegion(list.getCompoundTagAt(i)));
            }
        }
    }
}
