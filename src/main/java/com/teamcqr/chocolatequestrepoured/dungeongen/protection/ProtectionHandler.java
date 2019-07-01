package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.API.events.CQProtectedRegionEnterEvent;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityForceFieldNexus;
import com.teamcqr.chocolatequestrepoured.util.CQDataUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Copyright (c) 29.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/*
 * Primary logic class for Dungeon Protection system
 * Prevents block break/place, natural entity spawning, etc.
 */
public class ProtectionHandler {

    /* VARIABLES */

    // Singleton instance
    public static ProtectionHandler PROTECTION_HANDLER;

    // instance vars
    private HashMap<ChunkPos,ProtectedRegion> activeRegions;
    private HashMap<ChunkPos,ProtectedRegion> allRegions;

    /* INIT */

    // Initialize singleton
    public static void init() { PROTECTION_HANDLER = new ProtectionHandler(); }

    // Constructor - initialize vars
    public ProtectionHandler() {
        this.activeRegions = new HashMap<>();
        this.allRegions = new HashMap<>();
    }

    /* EVENT HANDLERS */

    // Handle forge block break event
    @SubscribeEvent
    public void handleBlockBreak(BlockEvent.BreakEvent e) {

        for( Map.Entry<ChunkPos,ProtectedRegion> item : activeRegions.entrySet() ) {
            item.getValue().checkBreakEvent(e);
        }

    }

    // Handle forge LivingSpawnEvent
    @SubscribeEvent
    public void checkSpawn(LivingSpawnEvent.CheckSpawn e) {
        Iterator<Map.Entry<ChunkPos,ProtectedRegion>> it = activeRegions.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<ChunkPos,ProtectedRegion> item = it.next();
            item.getValue().checkSpawnEvent(e);
        }
    }

    // Handle portal spawning
    @SubscribeEvent
    public void handlePortalSpawn(BlockEvent.PortalSpawnEvent e) {
        Iterator<Map.Entry<ChunkPos,ProtectedRegion>> it = activeRegions.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<ChunkPos,ProtectedRegion> item = it.next();
            item.getValue().checkPortalEvent(e);
        }
    }

    // Handle chunk load event
    @SubscribeEvent
    public void handleLoad(ChunkEvent.Load e) {

        // Temp obj
        ProtectedRegion region;

        // If chunk contains protected region, add to activeRegions var
        if(e.getChunk().isLoaded()) {
            if((region = getRegionForChunkPos(e.getChunk().getPos()))!=null) {
                activeRegions.put(e.getChunk().getPos(),region);
                initForceFieldNexus(e.getWorld(),region.getNexus());
            }
        }
    }
    
    // Handle chunk unload event
    @SubscribeEvent
    public void handleUnload(ChunkEvent.Unload e) {
        // Remove relevant chunks from activeRegions variable under applicable circumstances
        if(!e.getChunk().isLoaded()) {
            if(activeRegions.containsKey(e.getChunk().getPos())) {
                activeRegions.remove(e.getChunk().getPos());
            }
        }
    }

    // Handle EnteringChunk event
    @SubscribeEvent
    public void handleChunkEnter(EntityEvent.EnteringChunk e) {

        ChunkPos enter;

        if(e.getEntity() instanceof EntityPlayer) {
            if(activeRegions.containsKey(enter = new ChunkPos(e.getNewChunkX(),e.getNewChunkZ()))) {
                MinecraftForge.EVENT_BUS.post(new CQProtectedRegionEnterEvent(activeRegions.get(enter),enter,(EntityPlayer)e.getEntity()));
            }
        }
    }

    /* UTILITY */

    // Util - add region to allRegions variable
    public void addRegion(ChunkPos pos, ProtectedRegion region) {
        allRegions.put(pos,region);
}

    // Util - return UUID given region
    public ProtectedRegion getProtectedRegionWithUUID(UUID uuid) {
        Iterator<Map.Entry<ChunkPos,ProtectedRegion>> it = activeRegions.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<ChunkPos,ProtectedRegion> item = it.next();
            if(item.getValue().getDungeonUUID().equals(uuid)) {
                return item.getValue();
            }
        }

        return null;
    }

    // Util - convert ChunkPos to ProtectedRegion
    public ProtectedRegion getRegionForChunkPos(ChunkPos pos) {

        for(ProtectedRegion region: allRegions.values()) {
            if(region.getChunksInRegion().contains(pos)) {
                return region;
            }
        }

        return null;

    }

    // Util - call the initUUIDRegion method on a force field nexus at a given location in a given world
    public void initForceFieldNexus(World world, BlockPos pos) {
        if(world != null && pos != null && pos.getY() > 0 && pos.getY() < 256 && world.getBlockState(pos) != null && net.minecraft.block.Block.isEqualTo(world.getBlockState(pos).getBlock(), ModBlocks.FORCE_FIELD_NEXUS)) {
            TileEntityForceFieldNexus tile = (TileEntityForceFieldNexus)world.getTileEntity(pos);
            tile.initUUIDRegion();
        }
    }

    // Util - extract x/z data from nbt file for given world and store into allRegions variable
    public void loadData(World world) {
        NBTTagCompound tag = CQDataUtil.loadFile("cq_chunk_data.nbt",world);

        for(String key:tag.getKeySet()) {
            String[] array = key.split("_");
            int x = Integer.parseInt(array[0]);
            int z = Integer.parseInt(array[1]);

            ChunkPos pos = new ChunkPos(x,z);

            ProtectedRegion region = new ProtectedRegion(tag.getCompoundTag(key));

            allRegions.put(pos,region);
        }
    }

    // Util - save data contained in allRegions variable to nbt file
    public void saveData(World world) {

        // Temp obj
        NBTTagCompound tag = new NBTTagCompound();

        // Loop through nbt data in allRegions var and add to temp obj
        for(ChunkPos key: allRegions.keySet()) {
            NBTTagCompound data = allRegions.get(key).save();

            tag.setTag(key.x+"_"+key.z,data);
        }

        // Save temp obj to file
        CQDataUtil.saveFile(tag,"cq_chunk_data.nbt",world);
    }
}
