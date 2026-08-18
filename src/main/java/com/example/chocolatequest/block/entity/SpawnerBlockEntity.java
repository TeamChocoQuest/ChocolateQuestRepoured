package com.example.chocolatequest.block.entity;

import com.example.chocolatequest.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class SpawnerBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private int activationRange = 16;
    private int tickCounter = 0;

    public SpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPAWNER.get(), pos, state);
    }

    public SpawnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("ActivationRange", activationRange);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
        
        boolean hasItems = false;
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            if (!inventory.getStackInSlot(slot).isEmpty()) {
                hasItems = true;
                break;
            }
        }
        
        if (!hasItems) {
            net.minecraft.nbt.ListTag itemsList = null;
            if (tag.contains("inventory", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                CompoundTag invTag = tag.getCompound("inventory");
                if (invTag.contains("Items", net.minecraft.nbt.Tag.TAG_LIST)) {
                    itemsList = invTag.getList("Items", net.minecraft.nbt.Tag.TAG_COMPOUND);
                }
            } else if (tag.contains("Items", net.minecraft.nbt.Tag.TAG_LIST)) {
                itemsList = tag.getList("Items", net.minecraft.nbt.Tag.TAG_COMPOUND);
            }
            
            if (itemsList != null) {
                int spawnCount = tag.contains("SpawnCount") ? tag.getInt("SpawnCount") : -1;
                int totalItems = itemsList.size();
                int baseCount = (spawnCount > 0 && totalItems > 0) ? (spawnCount / totalItems) : 0;
                int remainder = (spawnCount > 0 && totalItems > 0) ? (spawnCount % totalItems) : 0;

                for (int i = 0; i < totalItems && i < inventory.getSlots(); i++) {
                    CompoundTag itemTag = itemsList.getCompound(i);
                    int slot = itemTag.contains("Slot") ? (itemTag.getByte("Slot") & 255) : i;
                    if (slot < 0 || slot >= inventory.getSlots()) slot = i;
                    
                    int count = baseCount > 0 ? baseCount : (itemTag.contains("Count") ? (itemTag.getByte("Count") & 255) : 1);
                    if (i < remainder) count++;
                    if (count <= 0) count = 1;

                    CompoundTag entityIn = null;
                    if (itemTag.contains("tag", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                        CompoundTag itemData = itemTag.getCompound("tag");
                        if (itemData.contains("EntityIn", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                            entityIn = itemData.getCompound("EntityIn");
                        } else if (itemData.contains("entity", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                            entityIn = itemData.getCompound("entity");
                        } else if (itemData.contains("EntityTag", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                            entityIn = itemData.getCompound("EntityTag");
                        } else if (itemData.contains("id")) {
                            entityIn = itemData;
                        }
                    } else if (itemTag.contains("EntityIn", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                        entityIn = itemTag.getCompound("EntityIn");
                    } else if (itemTag.contains("entity", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                        entityIn = itemTag.getCompound("entity");
                    } else if (itemTag.contains("id")) {
                        String rawId = itemTag.getString("id");
                        if (!rawId.equals("cqrepoured:soul_bottle") && !rawId.equals("cqrepoured:soul_bottle")) {
                            entityIn = itemTag;
                        }
                    }

                    if (entityIn != null) {
                        if (entityIn.contains("id")) {
                            String entityId = entityIn.getString("id");
                            if (entityId.startsWith("cqrepoured:")) {
                                entityId = entityId.replace("cqrepoured:", "cqrepoured:");
                            }
                            if (entityId.equals("cqrepoured:giant_spider")) {
                                entityId = "cqrepoured:shelob";
                            } else if (entityId.equals("cqrepoured:dummy") || entityId.equals("cqrepoured:cq_dummy") || entityId.equals("dummy")) {
                                entityId = "cqrepoured:cq_goblin";
                            }
                            entityIn.putString("id", entityId);
                        }
                        ItemStack soulBottle = new ItemStack(com.example.chocolatequest.registry.ModItems.SOUL_BOTTLE.get());
                        soulBottle.setCount(Math.min(count, soulBottle.getMaxStackSize()));
                        soulBottle.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(entityIn));
                        inventory.setStackInSlot(slot, soulBottle);
                    }
                }
            } else if (tag.contains("SpawnData", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                CompoundTag spawnData = tag.getCompound("SpawnData");
                CompoundTag entityTag = spawnData.contains("entity") ? spawnData.getCompound("entity") : spawnData;
                if (entityTag.contains("id")) {
                    String entityId = entityTag.getString("id");
                    if (entityId.startsWith("cqrepoured:")) {
                        entityId = entityId.replace("cqrepoured:", "cqrepoured:");
                    }
                    if (entityId.equals("cqrepoured:giant_spider")) {
                        entityId = "cqrepoured:shelob";
                    } else if (entityId.equals("cqrepoured:dummy") || entityId.equals("cqrepoured:cq_dummy") || entityId.equals("dummy")) {
                        entityId = "cqrepoured:cq_goblin";
                    }
                    entityTag.putString("id", entityId);
                    
                    ItemStack soulBottle = new ItemStack(com.example.chocolatequest.registry.ModItems.SOUL_BOTTLE.get());
                    soulBottle.set(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(entityTag));
                    inventory.setStackInSlot(0, soulBottle);
                }
            } else if (tag.contains("EntityId", net.minecraft.nbt.Tag.TAG_STRING)) {
                String entityId = tag.getString("EntityId");
                CompoundTag entityTag = new CompoundTag();
                entityTag.putString("id", entityId);
                if (tag.contains("EntityData", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                    entityTag.merge(tag.getCompound("EntityData"));
                }
                ItemStack soulBottle = new ItemStack(com.example.chocolatequest.registry.ModItems.SOUL_BOTTLE.get());
                soulBottle.set(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(entityTag));
                inventory.setStackInSlot(0, soulBottle);
            } else if (tag.contains("Entities", net.minecraft.nbt.Tag.TAG_LIST)) {
                net.minecraft.nbt.ListTag entitiesList = tag.getList("Entities", net.minecraft.nbt.Tag.TAG_COMPOUND);
                for (int i = 0; i < Math.min(entitiesList.size(), inventory.getSlots()); i++) {
                    CompoundTag entityTag = entitiesList.getCompound(i);
                    if (entityTag.contains("id")) {
                        ItemStack soulBottle = new ItemStack(com.example.chocolatequest.registry.ModItems.SOUL_BOTTLE.get());
                        soulBottle.set(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(entityTag));
                        inventory.setStackInSlot(i, soulBottle);
                    }
                }
            }
        }
        
        if (tag.contains("ActivationRange")) {
            activationRange = tag.getInt("ActivationRange");
        } else if (tag.contains("RequiredPlayerRange")) {
            activationRange = tag.getInt("RequiredPlayerRange");
        }
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpawnerBlockEntity blockEntity) {
        if (level.isClientSide) return;
        
        blockEntity.tickCounter++;
        if (blockEntity.tickCounter % 20 == 0) {
            if (blockEntity.isNonCreativePlayerInRange(blockEntity.activationRange)) {
                blockEntity.turnBackIntoEntity();
            }
        }
    }

    protected boolean isNonCreativePlayerInRange(double range) {
        if (range <= 0) return false;
        double rangeSq = range * range;
        for (Player player : level.players()) {
            if (!player.isCreative() && !player.isSpectator() && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) < rangeSq) {
                return true;
            }
        }
        return false;
    }

    public void turnBackIntoEntity() {
        if (level == null || level.isClientSide) return;
        
        java.util.List<ItemStack> itemsToSpawn = new java.util.ArrayList<>();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                itemsToSpawn.add(stack.copy());
                inventory.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        
        level.destroyBlock(worldPosition, false);
        
        if (itemsToSpawn.isEmpty()) {
            if (this instanceof BossBlockEntity) {
                spawnEntityById("cqrepoured:shelob");
                for (int g = 0; g < 3; g++) {
                    spawnEntityById("cqrepoured:cq_goblin");
                }
            }
            return;
        }
        
        for (ItemStack stack : itemsToSpawn) {
            int count = Math.max(1, stack.getCount());
            if (stack.has(DataComponents.CUSTOM_DATA)) {
                CustomData data = stack.get(DataComponents.CUSTOM_DATA);
                if (data != null) {
                    for (int c = 0; c < count; c++) {
                        CompoundTag nbt = data.copyTag();
                        spawnEntityFromNBT(nbt);
                    }
                }
            }
        }
        
        if (this instanceof BossBlockEntity) {
            for (int g = 0; g < 3; g++) {
                spawnEntityById("cqrepoured:cq_goblin");
            }
        }
    }

    protected void spawnEntityById(String entityId) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", entityId);
        spawnEntityFromNBT(tag);
    }

    protected void spawnEntityFromNBT(CompoundTag entityTag) {
        if (entityTag == null || entityTag.isEmpty() || level == null) return;
        
        CompoundTag tagToLoad = entityTag.copy();
        
        fixLegacyCQRData(tagToLoad);
        
        tagToLoad.remove("UUID");
        tagToLoad.remove("Pos");
        
        try {
            String originalId = tagToLoad.getString("id");
            int dataVersion = tagToLoad.contains("DataVersion") ? tagToLoad.getInt("DataVersion") : 1343;
            
            tagToLoad.putString("id", "minecraft:zombie");

            com.mojang.serialization.Dynamic<net.minecraft.nbt.Tag> dynamic = new com.mojang.serialization.Dynamic<>(net.minecraft.nbt.NbtOps.INSTANCE, tagToLoad);
            dynamic = net.minecraft.util.datafix.DataFixers.getDataFixer().update(
                net.minecraft.util.datafix.fixes.References.ENTITY, 
                dynamic, 
                dataVersion, 
                net.minecraft.SharedConstants.getCurrentVersion().getDataVersion().getVersion()
            );
            tagToLoad = (CompoundTag) dynamic.getValue();

            tagToLoad.putString("id", originalId);
        } catch (Exception e) {
            com.example.chocolatequest.ChocolateQuestReDone.LOGGER.error("Failed to apply DataFixer to entity NBT", e);
        }
        
        double offset = 0.5D;
        double x = worldPosition.getX() + 0.5D + (level.random.nextDouble() - level.random.nextDouble()) * offset;
        double y = worldPosition.getY();
        double z = worldPosition.getZ() + 0.5D + (level.random.nextDouble() - level.random.nextDouble()) * offset;
        
        Entity entity = EntityType.loadEntityRecursive(tagToLoad, level, (e) -> {
            e.moveTo(x, y, z, e.getYRot(), e.getXRot());
            return e;
        });
        
        if (entity != null) {
            // an id/tier, so run finalizeSpawn for those minimal definitions.
            // A player-captured Soul Bottle with saved equipment remains exact.
            if (entity instanceof net.minecraft.world.entity.Mob mob
                    && level instanceof net.minecraft.server.level.ServerLevel serverLevel
                    && hasNoSavedEquipment(entityTag)) {
                mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(worldPosition),
                        net.minecraft.world.entity.MobSpawnType.SPAWNER, null);
            }
            boolean success = level.addFreshEntity(entity);
            com.example.chocolatequest.ChocolateQuestReDone.LOGGER.info("Spawned entity: " + entity.getType().getDescriptionId() + " at " + x + " " + y + " " + z + ". Success: " + success);
        } else {
            com.example.chocolatequest.ChocolateQuestReDone.LOGGER.warn("Failed to load entity from tag: " + tagToLoad);
        }
    }

    private static boolean hasNoSavedEquipment(CompoundTag tag) {
        // Full Soul Bottle captures contain living-state data and must be
        // restored verbatim, even when the captured creature was unarmed.
        if (tag.contains("Health") || tag.contains("Attributes") || tag.contains("Brain")
                || tag.contains("UUID")) return false;
        if (tag.contains("equipment", net.minecraft.nbt.Tag.TAG_COMPOUND)
                && !tag.getCompound("equipment").isEmpty()) return false;
        if (tag.contains("ExtraInventory", net.minecraft.nbt.Tag.TAG_LIST)
                && !tag.getList("ExtraInventory", net.minecraft.nbt.Tag.TAG_COMPOUND).isEmpty()) return false;
        return !containsItem(tag, "HandItems") && !containsItem(tag, "ArmorItems");
    }

    private static boolean containsItem(CompoundTag tag, String key) {
        if (!tag.contains(key, net.minecraft.nbt.Tag.TAG_LIST)) return false;
        net.minecraft.nbt.ListTag list = tag.getList(key, net.minecraft.nbt.Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            if (!list.getCompound(i).isEmpty()) return true;
        }
        return false;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.cqrepoured.spawner");
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.worldPosition);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new com.example.chocolatequest.inventory.SpawnerMenu(containerId, playerInventory, this);
    }

    private void fixLegacyCQRData(net.minecraft.nbt.Tag tag) {
        if (tag instanceof CompoundTag compound) {
            if (compound.contains("id", net.minecraft.nbt.Tag.TAG_STRING)) {
                String id = compound.getString("id");
                if (id.startsWith("cqrepoured:") || id.startsWith("cqrepoured:")) {
                    String baseId = id.replace("cqrepoured:", "cqrepoured:");
                    net.minecraft.resources.ResourceLocation loc = net.minecraft.resources.ResourceLocation.tryParse(baseId);
                    if (loc != null && !net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.containsKey(loc)) {
                        String tryId = baseId.replace("cqrepoured:", "cqrepoured:cq_");
                        net.minecraft.resources.ResourceLocation tryLoc = net.minecraft.resources.ResourceLocation.tryParse(tryId);
                        if (tryLoc != null && net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.containsKey(tryLoc)) {
                            compound.putString("id", tryId);
                        } else {
                            compound.putString("id", baseId);
                        }
                    } else {
                        compound.putString("id", baseId);
                    }
                }
            }

            for (String key : compound.getAllKeys()) {
                net.minecraft.nbt.Tag child = compound.get(key);
                if (child instanceof net.minecraft.nbt.StringTag stringTag) {
                    String val = stringTag.getAsString();
                    if (val.startsWith("cqrepoured:")) {
                        compound.putString(key, val.replace("cqrepoured:", "cqrepoured:"));
                    }
                } else {
                    fixLegacyCQRData(child);
                }
            }
            
            fixEquipmentList(compound, "ArmorItems");
            fixEquipmentList(compound, "HandItems");
            
        } else if (tag instanceof net.minecraft.nbt.ListTag list) {
            for (int i = 0; i < list.size(); i++) {
                net.minecraft.nbt.Tag child = list.get(i);
                if (child instanceof net.minecraft.nbt.StringTag stringTag) {
                    String val = stringTag.getAsString();
                    if (val.startsWith("cqrepoured:")) {
                        list.set(i, net.minecraft.nbt.StringTag.valueOf(val.replace("cqrepoured:", "cqrepoured:")));
                    }
                } else {
                    fixLegacyCQRData(child);
                }
            }
        }
    }

    private void fixEquipmentList(CompoundTag compound, String listName) {
        if (compound.contains(listName, net.minecraft.nbt.Tag.TAG_LIST)) {
            net.minecraft.nbt.ListTag list = compound.getList(listName, net.minecraft.nbt.Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag itemTag = list.getCompound(i);
                if (itemTag.contains("id")) {
                    if (itemTag.contains("Count")) {
                        itemTag.putInt("count", itemTag.getByte("Count"));
                        itemTag.remove("Count");
                    }
                    if (!itemTag.contains("count")) {
                        itemTag.putInt("count", 1);
                    }
                    
                    CompoundTag components = itemTag.contains("components", net.minecraft.nbt.Tag.TAG_COMPOUND) 
                        ? itemTag.getCompound("components") : new CompoundTag();
                        
                    if (itemTag.contains("Damage")) {
                        int damage = itemTag.getInt("Damage");
                        if (damage > 0) {
                            components.putInt("minecraft:damage", damage);
                        }
                        itemTag.remove("Damage");
                    }
                    
                    if (itemTag.contains("tag", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                        CompoundTag oldTag = itemTag.getCompound("tag");
                        if (oldTag.contains("ench", net.minecraft.nbt.Tag.TAG_LIST)) {
                            components.put("minecraft:enchantments", oldTag.getList("ench", net.minecraft.nbt.Tag.TAG_COMPOUND));
                            oldTag.remove("ench");
                        }
                        if (!oldTag.isEmpty()) {
                            components.put("minecraft:custom_data", oldTag);
                        }
                        itemTag.remove("tag");
                    }
                    
                    if (!components.isEmpty()) {
                        itemTag.put("components", components);
                    }
                }
            }
        }
    }
}
