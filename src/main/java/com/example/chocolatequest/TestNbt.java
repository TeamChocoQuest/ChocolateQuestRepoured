package com.example.chocolatequest;

import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.CompoundTag;
import java.io.File;
import java.io.FileInputStream;

public class TestNbt {
    public static void main(String[] args) throws Exception {
        CompoundTag tag = NbtIo.readCompressed(new FileInputStream(new File("C:\\cqrepoured-template-1.21.1\\src\\main\\resources\\data\\cqrepoured\\structure\\volcano\\rooms\\hallway\\ew\\stronghold_east_west_1.nbt")), net.minecraft.nbt.NbtAccounter.unlimitedHeap());
        System.out.println("Size X: " + tag.getCompound("size").getInt("X"));
        System.out.println("Size Y: " + tag.getCompound("size").getInt("Y"));
        System.out.println("Size Z: " + tag.getCompound("size").getInt("Z"));
    }
}
