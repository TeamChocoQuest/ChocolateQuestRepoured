package com.example.chocolatequest;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import com.example.chocolatequest.registry.ModItems;

public class TestId {
    public static void test() {
        SpawnEggItem egg = (SpawnEggItem) ModItems.CQ_ZOMBIE_SPAWN_EGG.get();
        ItemStack stack = new ItemStack(egg);
        String id = BuiltInRegistries.ENTITY_TYPE.getKey(egg.getType(stack)).toString();
        System.out.println("ID: " + id);
    }
}
