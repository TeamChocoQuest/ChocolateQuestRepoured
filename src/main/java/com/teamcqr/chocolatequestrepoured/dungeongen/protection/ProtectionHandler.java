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

/**
 * Copyright (c) 29.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ProtectionHandler {

    public static ProtectionHandler PROTECTION_HANDLER;

    private HashMap<ChunkPos,ProtectedRegion> regions;
    private HashMap<ChunkPos,ProtectedRegion> existingRegions;

    public ProtectionHandler() {
        this.regions = new HashMap<>();
        this.existingRegions = new HashMap<>();
    }

    public void check(BlockEvent.BreakEvent e) {
        Iterator<Map.Entry<ChunkPos,ProtectedRegion>> it = regions.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<ChunkPos,ProtectedRegion> item = it.next();
            item.getValue().checkBreakEvent(e);
        }
    }

    public void checkSpawn(LivingSpawnEvent.CheckSpawn e) {
        Iterator<Map.Entry<ChunkPos,ProtectedRegion>> it = regions.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<ChunkPos,ProtectedRegion> item = it.next();
            item.getValue().checkSpawnEvent(e);
        }
    }
    public void checkPortalSpawning(BlockEvent.PortalSpawnEvent e) {
        Iterator<Map.Entry<ChunkPos,ProtectedRegion>> it = regions.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<ChunkPos,ProtectedRegion> item = it.next();
            item.getValue().checkPortalEvent(e);
        }
    }

    public static void init() {
        PROTECTION_HANDLER = new ProtectionHandler();
    }

    public void loadData(World world) {
        NBTTagCompound tag = CQDataUtil.loadFile("cq_chunk_data.nbt",world);

        for(String key:tag.getKeySet()) {
            String[] array = key.split("_");
            int x = Integer.parseInt(array[0]);
            int z = Integer.parseInt(array[1]);

            ChunkPos pos = new ChunkPos(x,z);

            ProtectedRegion region = new ProtectedRegion(tag.getCompoundTag(key));

            existingRegions.put(pos,region);
        }
    }

    public void saveData(World world) {
        NBTTagCompound tag = new NBTTagCompound();

        for(ChunkPos key:existingRegions.keySet()) {
            NBTTagCompound data = existingRegions.get(key).save();

            tag.setTag(key.x+"_"+key.z,data);
        }

        CQDataUtil.saveFile(tag,"cq_chunk_data.nbt",world);
    }

    private ProtectedRegion region;

    public void handleLoad(ChunkEvent.Load e) {
        if(e.getChunk().isLoaded()) {
            if((region = getRegionForChunkPos(e.getChunk().getPos()))!=null) {
                regions.put(e.getChunk().getPos(),region);
                initForceFieldNexus(e.getWorld(),region.getNexus());
            }
        }
    }

    public void handleUnload(ChunkEvent.Unload e) {
        if(!e.getChunk().isLoaded()) {
            if(regions.containsKey(e.getChunk().getPos())) {
                regions.remove(e.getChunk().getPos());
            }
        }
    }

    public void addExistingRegion(ChunkPos pos,ProtectedRegion region) {
        existingRegions.put(pos,region);
    }

    private ChunkPos enter;

    public void handleChunkEnter(EntityEvent.EnteringChunk e) {
        if(e.getEntity() instanceof EntityPlayer) {
            if(regions.containsKey(enter = new ChunkPos(e.getNewChunkX(),e.getNewChunkZ()))) {
                MinecraftForge.EVENT_BUS.post(new CQProtectedRegionEnterEvent(regions.get(enter),enter,(EntityPlayer)e.getEntity()));
            }
        }
    }

    public ProtectedRegion getProtectedRegionWithUUID(UUID uuid) {
        Iterator<Map.Entry<ChunkPos,ProtectedRegion>> it = regions.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<ChunkPos,ProtectedRegion> item = it.next();
            if(item.getValue().getDungeonUUID().equals(uuid)) {
                return item.getValue();
            }
        }

        return null;
    }

    public void initForceFieldNexus(World world, BlockPos pos) {
        if(world != null && pos != null && pos.getY() > 0 && pos.getY() < 256 && world.getBlockState(pos) != null && net.minecraft.block.Block.isEqualTo(world.getBlockState(pos).getBlock(), ModBlocks.FORCE_FIELD_NEXUS)) {
            TileEntityForceFieldNexus tile = (TileEntityForceFieldNexus)world.getTileEntity(pos);
            tile.initUUIDRegion();
        }
    }

    public ProtectedRegion getRegionForChunkPos(ChunkPos pos) {
        for(ProtectedRegion region:existingRegions.values()) {
            if(region.getChunksInRegion().contains(pos)) {
                return region;
            }
        }

        return null;
    }
}
