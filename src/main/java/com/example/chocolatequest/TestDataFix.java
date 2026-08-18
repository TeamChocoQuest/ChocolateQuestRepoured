package com.example.chocolatequest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtOps;
import net.minecraft.SharedConstants;

public class TestDataFix {
    public static void main(String[] args) {
        SharedConstants.tryDetectVersion();
        CompoundTag tag = new CompoundTag();
        tag.putString("id", "cqrepoured:dummy");
        
        ListTag armor = new ListTag();
        CompoundTag boots = new CompoundTag();
        boots.putString("id", "minecraft:golden_boots");
        boots.putByte("Count", (byte) 1);
        armor.add(boots);
        tag.put("ArmorItems", armor);
        
        ListTag hand = new ListTag();
        CompoundTag revolver = new CompoundTag();
        revolver.putString("id", "cqrepoured:revolver");
        revolver.putByte("Count", (byte) 1);
        hand.add(revolver);
        tag.put("HandItems", hand);
        
        System.out.println("Before: " + tag);
        
        tag.putString("id", "minecraft:zombie");
        
        Dynamic<net.minecraft.nbt.Tag> dynamic = new Dynamic<>(NbtOps.INSTANCE, tag);
        dynamic = DataFixers.getDataFixer().update(
            References.ENTITY, 
            dynamic, 
            3463, 
            SharedConstants.getCurrentVersion().getDataVersion().getVersion()
        );
        CompoundTag fixed = (CompoundTag) dynamic.getValue();
        fixed.putString("id", "cqrepoured:dummy");
        System.out.println("After: " + fixed);
    }
}
