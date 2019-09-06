package com.teamcqr.chocolatequestrepoured.dungeonprot;

import java.util.ArrayList;
import java.util.HashMap;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.intrusive.IntrusiveModificationHelper;

import com.teamcqr.chocolatequestrepoured.util.data.ArchiveManipulationUtil;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;
import com.teamcqr.chocolatequestrepoured.util.data.ObjectSerializationUtil;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Central class responsible for handling all activities related to the protection of regions
 * Intended for use on dungeons but can easily be used for other areas
 *
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 22.07.19
 */
public class ProtectionHandler {

    /* ************ */
    /* Registration */
    /* ************ */

    // Singleton setup
    private static ProtectionHandler PROTECTION_HANDLER = new ProtectionHandler();
    private ProtectionHandler() { this.activeRegions = new HashMap<>(); }
    public static ProtectionHandler getInstance() { return PROTECTION_HANDLER; }

    // Region Data
    private HashMap<Integer, ArrayList<ProtectedRegion>> activeRegions;

    /* ************* */
    /* Serialization */
    /* ************* */

    // ProtectedRegion Serialization
    @SubscribeEvent
    public void eventHandleDungeonSpawn(CQDungeonStructureGenerateEvent e) {
        // Create ProtectedRegion obj
        ProtectedRegion regionToBeRegistered = new ProtectedRegion(e.getDungeonID().toString(), e.getPos(), new BlockPos(e.getPos().getX() + e.getSize().getX(), e.getPos().getY() + e.getSize().getY(), e.getPos().getZ() + e.getSize().getZ()));
        int dimID = e.getWorld().provider.getDimension();
        // Register in Memory
        ArrayList<ProtectedRegion> updated = activeRegions.get(dimID);
        if(updated == null) updated = new ArrayList<>();
        updated.add(regionToBeRegistered);
        activeRegions.put(dimID, updated);
        // Register on Disc
        HashMap<String, byte[]> regionsToBeZipped = new HashMap<>();
        for(ProtectedRegion region : activeRegions.get(dimID)) {
            regionsToBeZipped.put( "dim_" + dimID + "\\" + region.getUUIDString(), ObjectSerializationUtil.writeSerializableToByteArray(region) );
        }
        FileIOUtil.saveToFile(FileIOUtil.getAbsoluteWorldPath() + "data\\CQR\\prot_region_defs.zip", ArchiveManipulationUtil.zip(regionsToBeZipped));
    }

    // ProtectedRegion Deserialization
    @SubscribeEvent
    public void eventHandleWorldLoad(WorldEvent.Load e) {
        HashMap<String, byte[]> protectedRegionsFromDisc = ArchiveManipulationUtil.unzip(FileIOUtil.loadFromFile(FileIOUtil.getAbsoluteWorldPath() + "prot_region_defs.zip"));
        if(protectedRegionsFromDisc != null) {
            for(String regionFileName : protectedRegionsFromDisc.keySet()) {
                ArrayList<ProtectedRegion> temp = activeRegions.get(Integer.parseInt(regionFileName.substring(4,5)));
                temp.add((ProtectedRegion)ObjectSerializationUtil.readObectFromByteArray(protectedRegionsFromDisc.get(regionFileName)));
                activeRegions.put(Integer.parseInt(regionFileName.substring(4,5)), temp);
            }
        }
    }

    /* ************** */
    /* Event Handling */
    /* ************** */

    // TODO Add checking for entity dependency death

    @SubscribeEvent
    public void eventHandleBlockBreak(BlockEvent.BreakEvent e) {

        // TODO Add checking for block dependency destruction

        // Loop through all dims present in registry
        for(int dimID : activeRegions.keySet()) {
            // Loop through all registered regions for dim
            for( ProtectedRegion region : activeRegions.get(dimID) ) {
                // Noop if global setting disabled
                if(!region.settings.get("preventBlockBreak")) {
                    // noop
                }
                // Noop if in creative mode and specific setting disabled
                else if(!region.settings.get("preventBlockBreakCreative") && e.getPlayer().isCreative()) {
                    // noop
                }
                // Noop if different dim
                else if(dimID != e.getWorld().provider.getDimension()) {
                    // noop
                }
                // Otherwise check break pos and cancel if overlapping
                else if(region.checkIfBlockPosInRegion(e.getPos())) {
                    e.setCanceled(true);
                }
            }
        }

    }

    @SubscribeEvent
    public void eventHandleBlockPlace(BlockEvent.PlaceEvent e) {

        // Loop through all dims present in registry
        for(int dimID : activeRegions.keySet()) {
            // Loop through all registered regions for dim
            for( ProtectedRegion region : activeRegions.get(dimID) ) {
                // Noop if global setting disabled
                if(!region.settings.get("preventBlockPlace")) {
                    // noop
                }
                // Noop if in creative mode and specific setting disabled
                else if(!region.settings.get("preventBlockPlaceCreative") && e.getPlayer().isCreative()) {
                    // noop
                }
                // Noop if different dim
                else if(dimID != e.getWorld().provider.getDimension()) {
                    // noop
                }
                // Otherwise check block pos and cancel if overlapping
                else if(region.checkIfBlockPosInRegion(e.getPos())) {
                    e.setCanceled(true);
                }
            }
        }

    }

    @SubscribeEvent
    public void eventHandleExplosion(ExplosionEvent e) {

        // Loop through all dims present in registry
        for(int dimID : activeRegions.keySet()) {
            // Check explosion pos against all active regions
            for( ProtectedRegion region : activeRegions.get(dimID) ) {

                // Noop if different dim
                if(dimID != e.getWorld().provider.getDimension()) {
                    // noop
                }
                // Check if TNT
                else if (IntrusiveModificationHelper.reflectGetFieldValue(e.getExplosion(), IntrusiveModificationHelper.reflectGetField( e.getExplosion(), new String[] {"exploder", "field_77283_e"} ) ) instanceof EntityTNTPrimed) {
                    // Check if TNT allowed
                    if(!region.settings.get("preventExplosionTNT")) {
                        // noop
                    }
                    // Check if outside relevant region
                    else if(!region.checkIfBlockPosInRegion( new BlockPos(e.getExplosion().getPosition().x, e.getExplosion().getPosition().y, e.getExplosion().getPosition().z))) {
                        // noop
                    }
                    // Otherwise cancel
                    else {
                        e.setCanceled(true);
                    }
                }
                // Otherwise
                else {
                    // Check if non-TNT explosions allowed
                    if(!region.settings.get("preventExplosionOther")) {
                        // noop
                    }
                    // Check if outside relevant region
                    else if(!region.checkIfBlockPosInRegion( new BlockPos(e.getExplosion().getPosition().x, e.getExplosion().getPosition().y, e.getExplosion().getPosition().z))) {
                        // noop
                    }
                    // Otherwise cancel
                    else {
                        e.setCanceled(true);
                    }
                }

            }

        }

    }

    @SubscribeEvent
    public void eventHandleNaturalSpawn(LivingSpawnEvent.CheckSpawn e) {
        // TODO Implement - previous attempt unsuccessful, will likely require subscription to a different event
    }

}
