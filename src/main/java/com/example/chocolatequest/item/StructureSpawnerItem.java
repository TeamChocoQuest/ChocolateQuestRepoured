package com.example.chocolatequest.item;

import com.example.chocolatequest.world.structure.CQStructureLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

import java.io.InputStream;
import java.util.List;

public class StructureSpawnerItem extends Item {
    private final List<ResourceLocation> structureLocations;
    private final List<String> entityReplacements;

    public StructureSpawnerItem(List<ResourceLocation> structureLocations, List<String> entityReplacements, Properties properties) {
        super(properties);
        this.structureLocations = structureLocations;
        this.entityReplacements = entityReplacements;
    }

    private static final java.util.Map<ResourceLocation, CompoundTag> CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide && context.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
            
            ResourceLocation structureLocation = structureLocations.get(serverLevel.random.nextInt(structureLocations.size()));
            String entityReplacement = null;
            if (entityReplacements != null && !entityReplacements.isEmpty()) {
                entityReplacement = entityReplacements.get(serverLevel.random.nextInt(entityReplacements.size()));
            }
            
            try {
                CompoundTag tag = CACHE.get(structureLocation);
                if (tag == null) {
                    InputStream in = null;
                    var resource = serverLevel.getServer().getResourceManager().getResource(structureLocation);
                    if (resource.isPresent()) {
                        in = resource.get().open();
                    } else {
                        String classPath = "/data/" + structureLocation.getNamespace() + "/" + structureLocation.getPath();
                        in = StructureSpawnerItem.class.getResourceAsStream(classPath);
                    }

                    if (in != null) {
                        try (InputStream inStream = in) {
                            tag = NbtIo.readCompressed(inStream, net.minecraft.nbt.NbtAccounter.unlimitedHeap());
                            CACHE.put(structureLocation, tag);
                        }
                    }
                }

                if (tag != null) {
                    CQStructureLoader loader = new CQStructureLoader();
                    if (entityReplacement != null && !entityReplacement.isEmpty()) {
                        loader.setEntityReplacement(entityReplacement);
                    }
                    loader.readFromNBT(tag);
                    
                    if (context.getPlayer() != null) {
                        context.getPlayer().sendSystemMessage(Component.literal("§f[CQR] Spawning structure: " + structureLocation.getPath() + " at " + pos.toShortString()));
                    }
                    
                    loader.placeInWorld(serverLevel, pos, null, net.minecraft.world.level.block.Rotation.NONE, false);
                    
                    if (context.getPlayer() != null) {
                        context.getPlayer().sendSystemMessage(Component.literal("§f[CQR] Successfully spawned structure!"));
                    }
                    return InteractionResult.SUCCESS;
                } else {
                    if (context.getPlayer() != null) {
                        context.getPlayer().sendSystemMessage(Component.literal("§c[CQR] Could not find structure resource: " + structureLocation));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (context.getPlayer() != null) {
                    context.getPlayer().sendSystemMessage(Component.literal("§c[CQR] Failed to load structure: " + e.getMessage()));
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
