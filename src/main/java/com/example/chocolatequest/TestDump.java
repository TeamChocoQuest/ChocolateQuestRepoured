package com.example.chocolatequest;
import net.minecraft.core.FrontAndTop;
public class TestDump {
    public static void main(String[] args) {
        for(FrontAndTop f : FrontAndTop.values()) {
            System.out.println(f);
        }
    }
}
