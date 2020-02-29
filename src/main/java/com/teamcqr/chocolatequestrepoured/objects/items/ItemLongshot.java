package com.teamcqr.chocolatequestrepoured.objects.items;

/**
 * Copyright (c) 15 Feb 2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class ItemLongshot extends ItemHookshotBase {
    public ItemLongshot() {
        super("longshot");
    }

    @Override
    public String getTranslationKey() {
        return "description.longshot.name";
    }

    @Override
    public double getHookRange() {
        return 30.0;
    }

    @Override
    public int getCooldown() {
        return 30;
    }
}
