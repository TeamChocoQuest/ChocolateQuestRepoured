package com.example.chocolatequest.client.gui;

import com.example.chocolatequest.client.gui.book.CQChronicleScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuideBookScreenHelper {

    public static void openBook() {
        Minecraft.getInstance().setScreen(new CQChronicleScreen());
    }
}
