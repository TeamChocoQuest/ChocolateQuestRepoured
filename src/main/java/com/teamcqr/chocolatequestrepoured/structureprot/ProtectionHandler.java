package com.teamcqr.chocolatequestrepoured.structureprot;

import java.util.ArrayList;
import java.util.HashMap;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.util.ReflectionHelper;
import com.teamcqr.chocolatequestrepoured.util.data.ArchiveManipulationUtil;
import com.teamcqr.chocolatequestrepoured.util.data.ArrayCollectionMapManipulationUtil;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;
import com.teamcqr.chocolatequestrepoured.util.data.ObjectSerializationUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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
 *         GitHub: https://github.com/jdawg3636
 *
 * @version 05.01.20
 */
public class ProtectionHandler {

    /* *************** */
    /* Constants/Setup */
    /* *************** */

    // Singleton setup
    private static ProtectionHandler PROTECTION_HANDLER = new ProtectionHandler();

    private ProtectionHandler() {
    }

    public static ProtectionHandler getInstance() {
        return PROTECTION_HANDLER;
    }

    // Globally Exempt Blocks (Can always be broken/placed)
    private ArrayList<Block> getGlobalProtectionExemptBlockTypes() {
        ArrayList<Block> toReturn = new ArrayList<>();
        toReturn.add(ModBlocks.PHYLACTERY);
        return toReturn;
    }

    /* ************ */
    /* Registration */
    /* ************ */

    // Region Data
    private HashMap<Integer, ArrayList<ProtectedRegion>> activeRegions = new HashMap<>();

    // Register
    @SubscribeEvent
    public void eventHandleDungeonSpawn(CQDungeonStructureGenerateEvent e) {

        // Convert Settings Overrides to HashMap
        HashMap<String, Boolean> settingsOverrides = new HashMap<>();
        settingsOverrides.put("preventBlockBreak", !e.getDungeon().getAllowBlockBreaking());
        settingsOverrides.put("preventBlockPlace", !e.getDungeon().getAllowBlockPlacing());
        settingsOverrides.put("preventExplosionTNT", !e.getDungeon().getAllowExplosionTNT());
        settingsOverrides.put("preventExplosionOther", !e.getDungeon().getAllowExplosionOther());
        settingsOverrides.put("preventFireSpread", !e.getDungeon().getAllowFireSpread());
        settingsOverrides.put("preventNaturalMobSpawn", !e.getDungeon().getAllowMobSpawns());
        settingsOverrides.put("requireDependencies", !e.getDungeon().getSecurityBypassEnabled());

        // Create ProtectedRegion obj
        ProtectedRegion regionToBeRegistered = new ProtectedRegion(
                /* Dungeon UUID        */ e.getDungeonID().toString(),
                /* NW Corner           */ e.getPos(),
                /* SE Corner           */ new BlockPos(e.getPos().getX() + e.getSize().getX(), e.getPos().getY() + e.getSize().getY(), e.getPos().getZ() + e.getSize().getZ()),
                /* Entity Dependencies */ e.getBossIDs(),
                /* Block Dependencies  */ (ArrayList<BlockPos>) ArrayCollectionMapManipulationUtil.genericAddValueToArrayish(new ArrayList<BlockPos>(), e.getShieldCorePosition(), null),
                /* Settings Overrides  */ settingsOverrides
        );

        // Get Dimension ID
        int dimID = e.getWorld().provider.getDimension();

        // Register in Memory
        ArrayList<ProtectedRegion> updated = this.activeRegions.get(dimID);
        if (updated == null) {
            updated = new ArrayList<>();
        }
        updated.add(regionToBeRegistered);
        this.activeRegions.put(dimID, updated);

        // Serialize to disk
        this.serializeToDisc(dimID);

    }

    // Deregister - Called by ProtectedRegion
    public void deregister(ProtectedRegion toBeRemoved) {
        for (Integer key : this.activeRegions.keySet()) {
            for (ProtectedRegion region : this.activeRegions.get(key)) {
                if (region == toBeRemoved) {
                    this.activeRegions.remove(key);
                }
            }
        }
    }

    /* ************* */
    /* Serialization */
    /* ************* */

    // ProtectedRegion Serialization
    public void serializeToDisc(int dimID) {
        // Register on Disc
        HashMap<String, byte[]> regionsToBeZipped = new HashMap<>();
        for (ProtectedRegion region : this.activeRegions.get(dimID)) {
            regionsToBeZipped.put("dim_" + dimID + "\\" + region.getUUIDString(), ObjectSerializationUtil.writeSerializableToByteArray(region));
        }
        FileIOUtil.saveToFile(FileIOUtil.getAbsoluteWorldPath() + "data\\CQR\\prot_region_defs.zip", ArchiveManipulationUtil.zip(regionsToBeZipped));
    }

    // ProtectedRegion Deserialization
    @SubscribeEvent
    public void eventHandleWorldLoad(WorldEvent.Load e) {
        HashMap<String, byte[]> protectedRegionsFromDisc = ArchiveManipulationUtil.unzip(FileIOUtil.loadFromFile(FileIOUtil.getAbsoluteWorldPath() + "data\\CQR\\prot_region_defs.zip"));
        if (protectedRegionsFromDisc != null) {
            for (String regionFileName : protectedRegionsFromDisc.keySet()) {

                // Filter Dimension ID from filename (hacky hotfix to support negative dimension ids)
                String dimIDAsString = "";
                int dimIDAsStringCursorPos = 4;
                while (regionFileName.charAt(dimIDAsStringCursorPos) != '\\') {
                    dimIDAsString += regionFileName.substring(dimIDAsStringCursorPos, dimIDAsStringCursorPos + 1);
                    dimIDAsStringCursorPos++;
                }

                ArrayList<ProtectedRegion> temp = this.activeRegions.get(Integer.parseInt(dimIDAsString));
                if (e.getWorld().provider.getDimension() != Integer.parseInt(dimIDAsString)) {
                    continue;
                }
                System.out.println("DIM: " + dimIDAsString);
                if (temp == null) {
                    temp = new ArrayList<>();
                }
                temp.add((ProtectedRegion) ObjectSerializationUtil.readObjectFromByteArray(protectedRegionsFromDisc.get(regionFileName)));
                this.activeRegions.put(Integer.parseInt(dimIDAsString), temp);
            }
        }
    }

    /* ************** */
    /* Event Handling */
    /* ************** */

    @SubscribeEvent
    public void eventHandleEntityDeath(LivingDeathEvent e) {
        // Loop through all dims present in registry
        for (int dimID : this.activeRegions.keySet()) {
            // Loop through all registered regions for dim
            for (ProtectedRegion region : this.activeRegions.get(dimID)) {
                // Loop through all UUIDs of Entity Dependencies
                for (String depUUID : region.getEntityDependenciesAsUUIDs()) {
                    // Check if UUIDs Equal
                    if (depUUID.equals(e.getEntity().getUniqueID().toString())) {
                        region.removeEntityDependency(depUUID);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void eventHandleBlockBreak(BlockEvent.BreakEvent e) {

        // Loop through all dims present in registry
        for (int dimID : this.activeRegions.keySet()) {
            ArrayList<ProtectedRegion> toRemoveFromRegistry = new ArrayList<>();
            // Loop through all registered regions for dim
            for (ProtectedRegion region : this.activeRegions.get(dimID)) {

                // Check if block is a dependency
                boolean isBlockDependency = false;
                for (BlockPos pos : region.getBlockDependencies()) {
                    if (e.getPos().equals(pos)) {
                        isBlockDependency = true;
                        region.removeBlockDependency(pos);
                    }
                }

                // Check if already handled
                if (isBlockDependency) {
                    // noop
                }
                // Noop if global setting disabled
                else if (!region.settings.get("preventBlockBreak")) {
                    // noop
                }
                // Noop if in creative mode and specific setting disabled
                else if (!region.settings.get("preventBlockBreakCreative") && e.getPlayer().isCreative()) {
                    // noop
                }
                // Noop if different dim
                else if (dimID != e.getWorld().provider.getDimension()) {
                    // noop
                }
                // Otherwise check break pos and cancel if overlapping
                else if (region.checkIfBlockPosInRegion(e.getPos())) {
                    if (e.getState().getBlock() != Blocks.MOB_SPAWNER) {
                        e.setCanceled(true);
                    }
                }
            }
            // Remove flagged regions
            ArrayList<ProtectedRegion> updated = this.activeRegions.get(dimID);
            for (ProtectedRegion pr : toRemoveFromRegistry) {
                updated.remove(pr);
            }
            this.activeRegions.put(dimID, updated);
        }

    }

    @SubscribeEvent
    public void eventHandleBlockPlace(BlockEvent.PlaceEvent e) {
        // Loop through all dims present in registry
        for (int dimID : this.activeRegions.keySet()) {
            // Loop through all registered regions for dim
            for (ProtectedRegion region : this.activeRegions.get(dimID)) {

                if (!region.settings.get("preventBlockPlace")) {
                    // noop
                }
                // Noop if in creative mode and preventBlockPlaceCreative disabled
                else if (!region.settings.get("preventBlockPlaceCreative") && e.getPlayer().isCreative()) {
                    // noop
                }
                // Noop if different dim
                else if (dimID != e.getWorld().provider.getDimension()) {
                    // noop
                }
                // Noop if block globally exempt
                else if (this.getGlobalProtectionExemptBlockTypes().contains(e.getState().getBlock())) {
                    // noop
                }
                // Otherwise check block pos and cancel if overlapping
                else if (region.checkIfBlockPosInRegion(e.getPos())) {
                    if (e.getPlacedBlock().getBlock() != Blocks.TORCH) {
                        e.setCanceled(true);
                    }
                }

            }
        }

    }

    @SubscribeEvent
    public void eventHandleExplosion(ExplosionEvent e) {

        // Loop through all dims present in registry
        for (int dimID : this.activeRegions.keySet()) {
            // Check explosion pos against all active regions
            for (ProtectedRegion region : this.activeRegions.get(dimID)) {

                // Noop if different dim
                if (dimID != e.getWorld().provider.getDimension()) {
                    // noop
                }
                // Check if TNT
                else if (ReflectionHelper.reflectGetFieldValue(e.getExplosion(), ReflectionHelper.reflectGetField(e.getExplosion(), new String[] { "exploder", "field_77283_e" })) instanceof EntityTNTPrimed) {
                    // Check if TNT allowed
                    if (!region.settings.get("preventExplosionTNT")) {
                        // noop
                    }
                    // Check if outside relevant region
                    else if (!region.checkIfBlockPosInRegion(new BlockPos(e.getExplosion().getPosition().x, e.getExplosion().getPosition().y, e.getExplosion().getPosition().z))) {
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
                    if (!region.settings.get("preventExplosionOther")) {
                        // noop
                    }
                    // Check if outside relevant region
                    else if (!region.checkIfBlockPosInRegion(new BlockPos(e.getExplosion().getPosition().x, e.getExplosion().getPosition().y, e.getExplosion().getPosition().z))) {
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
    public void eventHandleNaturalSpawn(LivingSpawnEvent.SpecialSpawn e) {

        // Loop through all dims present in registry
        for (int dimID : this.activeRegions.keySet()) {
            // Loop through all registered regions for dim
            for (ProtectedRegion region : this.activeRegions.get(dimID)) {
                // Noop if setting disabled
                if (!region.settings.get("preventNaturalMobSpawn")) {
                    // noop
                }
                // Noop if different dim
                else if (dimID != e.getWorld().provider.getDimension()) {
                    // noop
                }
                // Otherwise check block pos and cancel if overlapping
                else if (region.checkIfBlockPosInRegion(new BlockPos(e.getX(), e.getY(), e.getZ()))) {
                    e.setCanceled(true);
                }
            }
        }

    }

}
