package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import java.util.*;

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
 *
 * Copyright (c) 03.07.2019
 * Expanded by jdawg3636
 * GitHub: https://github.com/jdawg3636
 */
public class ProtectionHandler {

    // Singleton instance
    public static ProtectionHandler PROTECTION_HANDLER;

    // Region Data
    private ArrayList<ProtectedRegion> activeRegions;

    // Constructor
    public ProtectionHandler() {
        this.activeRegions = new ArrayList<>();
    }

    // Accessors
    public void registerRegion(ProtectedRegion region) {
        activeRegions.add(region);
    }

    public ProtectedRegion getProtectedRegionFromUUID(UUID uuidToFind) {

        // Search
        for( ProtectedRegion protectedRegion : activeRegions ) {
            if(protectedRegion.getUUID() == uuidToFind) return protectedRegion;
        }

        // Default
        return null;

    }

    /*
    // Serialization
    public void loadRegionDataFromFile(World world) {
        NBTTagCompound tag = CQDataUtil.loadFile("cq_chunk_data.nbt",world);

        tag.

        for(String key:tag.getKeySet()) {
            String[] array = key.split("_");
            int x = Integer.parseInt(array[0]);
            int z = Integer.parseInt(array[1]);

            ChunkPos pos = new ChunkPos(x,z);

            ProtectedRegion region = new ProtectedRegion(tag.getCompoundTag(key));

            allRegions.put(pos,region);
        }
    }

    public void saveRegionDataToFile(World world) {

        // Temp obj
        NBTTagCompound tag = new NBTTagCompound();

        // Loop through nbt data in allRegions var and add to temp obj
        for(ChunkPos key: allRegions.keySet()) {
            NBTTagCompound data = allRegions.get(key).serializeToNBT();

            tag.setTag(key.x+"_"+key.z,data);
        }

        // Save temp obj to file
        CQDataUtil.saveFile(tag,"cq_chunk_data.nbt",world);
    }
    */

    // Event Handlers
    @SubscribeEvent
    public void eventHandleBlockBreak(BlockEvent.BreakEvent e) {

        // Check break pos against all regions and cancel if overlapping
        for( ProtectedRegion region : activeRegions ) {
            if(region.checkIfBlockPosInRegion( e.getPos() ))
                e.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void eventHandleNaturalSpawn(LivingSpawnEvent.CheckSpawn e) {

        // Check spawn pos against all regions and cancel if overlapping
        for( ProtectedRegion region : activeRegions ) {
            if(region.checkIfBlockPosInRegion( e. ))
                e.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void eventHandlePortalSpawn(BlockEvent.PortalSpawnEvent e) {
        Iterator<Map.Entry<ChunkPos,ProtectedRegion>> it = activeRegions.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<ChunkPos,ProtectedRegion> item = it.next();
            item.getValue().checkPortalEvent(e);
        }
    }

    @SubscribeEvent
    public void eventHandleChunkLoad(ChunkEvent.Load e) {

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

    @SubscribeEvent
    public void eventHandleChunkUnload(ChunkEvent.Unload e) {
        // Remove relevant chunks from activeRegions variable under applicable circumstances
        if(!e.getChunk().isLoaded()) {
            if(activeRegions.containsKey(e.getChunk().getPos())) {
                activeRegions.remove(e.getChunk().getPos());
            }
        }
    }

    @SubscribeEvent
    public void eventHandleEntityEnterChunk(EntityEvent.EnteringChunk e) {

        ChunkPos enter;

        if(e.getEntity() instanceof EntityPlayer) {
            if(activeRegions.containsKey(enter = new ChunkPos(e.getNewChunkX(),e.getNewChunkZ()))) {
                MinecraftForge.EVENT_BUS.post(new CQProtectedRegionEnterEvent(activeRegions.get(enter),enter,(EntityPlayer)e.getEntity()));
            }
        }
    }
}
