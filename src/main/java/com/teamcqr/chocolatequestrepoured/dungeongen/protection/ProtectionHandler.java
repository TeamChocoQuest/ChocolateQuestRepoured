package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import java.util.*;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.API.events.CQProtectedRegionEnterEvent;
import com.teamcqr.chocolatequestrepoured.CQRMain;
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
 * Written 12.07.2019 by jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * Original (Old) Version Copyright (c) 29.04.2019 MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ProtectionHandler {

    // Singleton setup
    private static ProtectionHandler PROTECTION_HANDLER = new ProtectionHandler();
    private ProtectionHandler() { this.activeRegions = new ArrayList<>(); }
    public static ProtectionHandler getInstance() { return PROTECTION_HANDLER; }

    // Region Data
    private ArrayList<ProtectedRegion> activeRegions;

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

    // Event Handlers
    @SubscribeEvent
    public void eventHandleDungeonSpawn(CQDungeonStructureGenerateEvent e) {

        // Debug
        System.out.println(e.getPos());
        System.out.println(e.getSize());

        // Temp
        activeRegions.add(new ProtectedRegion(e.getPos(), new BlockPos(e.getPos().getX() + e.getSize().getX(), e.getPos().getY() + e.getSize().getY(), e.getPos().getZ() + e.getSize().getZ()), e.getWorld()));

    }

    @SubscribeEvent
    public void eventHandleBlockBreak(BlockEvent.BreakEvent e) {

        // Check break pos against all regions and cancel if overlapping
        for( ProtectedRegion region : activeRegions ) {
            if(region.checkIfBlockPosInRegion( e.getPos(), e.getWorld() )) e.setCanceled(true);
        }

        //for testing only
        //if(e.getPos().getX() > 0) e.setCanceled(true);

    }

    @SubscribeEvent
    public void eventHandleNaturalSpawn(LivingSpawnEvent.CheckSpawn e) {

        // Check spawn pos against all regions and cancel if overlapping
        for( ProtectedRegion region : activeRegions ) {
            if(region.checkIfBlockPosInRegion( new BlockPos(e.getX(), e.getY(), e.getZ()), e.getWorld() )) {
                // todo - fix this. checkspawn event not cancelable.
                // e..setCanceled(true);
            }
        }

    }
/* //TODO implement
    @SubscribeEvent
    public void eventHandlePortalSpawn(BlockEvent.PortalSpawnEvent e) {
        Iterator<Map.Entry<ChunkPos,ProtectedRegion>> it = activeRegions.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<ChunkPos,ProtectedRegion> item = it.next();
            item.getValue().checkPortalEvent(e);
        }
    }
    */

    @SubscribeEvent
    public void eventHandleEntityEnterChunk(EntityEvent.EnteringChunk e) {
        //TODO implement
    }
}
