package com.example.chocolatequest.entity.mob;

import com.example.chocolatequest.registry.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CQEntityHelper {

    public static void equipBasedOnRankAndRole(Mob mob, CQRanks rank, CQRoles role, RandomSource random) {
        // Clear equipment
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            mob.setItemSlot(slot, ItemStack.EMPTY);
            mob.setDropChance(slot, 0.0f);
        }

        // Equip based on Rank
        switch (rank) {
            case LEATHER:
                mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                mob.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                mob.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
                mob.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
                break;
            case CHAIN:
                mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
                mob.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                mob.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
                mob.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
                break;
            case GOLD:
                mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
                mob.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
                mob.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
                mob.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
                break;
            case IRON:
                mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                mob.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                mob.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                mob.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
                break;
            case DIAMOND:
                mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                mob.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                mob.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                mob.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
                break;
            case CQ_GEAR:
                mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                mob.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                mob.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                mob.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
                break;
        }

        // Equip based on Role
        switch (role) {
            case KNIGHT:
                if (rank == CQRanks.CQ_GEAR) {
                    mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                } else if (rank == CQRanks.DIAMOND) {
                    mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                } else {
                    mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                }
                if (random.nextFloat() < 0.4f) { // 40% chance for shield
                    mob.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(getShieldForMob(mob)));
                }
                break;
            case HEALER:
                mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.HEAL_STAFF.get()));
                break;
            case ARCHER:
                float r = random.nextFloat();
                if (r < 0.15f) {
                    mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MUSKET.get()));
                } else if (r < 0.25f) { // 10% chance
                    mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.REVOLVER.get()));
                } else {
                    mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                }
                break;
            case COMMANDER:
                if (rank == CQRanks.CQ_GEAR || rank == CQRanks.DIAMOND) {
                    mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                } else {
                    mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                }
                if (random.nextFloat() < 0.6f) { // 60% chance for shield for commanders
                    mob.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(getShieldForMob(mob)));
                }
                // Commander gets a feather on head
                mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.FEATHER));
                break;
        }
    }

    public static net.minecraft.world.item.Item getShieldForMob(Mob mob) {
        if (mob instanceof CQWalkerEntity) return ModItems.SHIELD_WALKER.get();
        if (mob instanceof CQPirateEntity) return ModItems.SHIELD_PIRATE.get();
        if (mob instanceof CQGoblinEntity) return ModItems.SHIELD_GOBLIN.get();
        if (mob instanceof CQZombieEntity) return ModItems.SHIELD_ZOMBIE.get();
        if (mob instanceof CQSkeletonEntity) return ModItems.SHIELD_SKELETON_FRIENDS.get();
        if (mob instanceof CQBoarmanEntity) return ModItems.SHIELD_BULL.get();
        if (mob instanceof CQSpecterEntity) return ModItems.SHIELD_SPECTER.get();
        if (mob instanceof CQMummyEntity) return ModItems.SHIELD_MUMMY.get();
        if (mob instanceof CQTritonEntity) return ModItems.SHIELD_TRITON.get();
        if (mob instanceof SpiderMinionEntity) return ModItems.SHIELD_SPIDER.get();
        if (mob instanceof CQGremlinEntity) return ModItems.SHIELD_WARPED.get();
        if (mob instanceof CQOrcEntity) return ModItems.SHIELD_FIRE.get();
        if (mob instanceof CQMinotaurEntity) return ModItems.SHIELD_SUN.get();
        if (mob instanceof CQDwarfEntity) return ModItems.SHIELD_RUSTED.get();
        
        return Items.SHIELD;
    }
}
