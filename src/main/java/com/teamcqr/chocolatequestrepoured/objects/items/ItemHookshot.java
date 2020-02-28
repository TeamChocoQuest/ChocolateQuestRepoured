package com.teamcqr.chocolatequestrepoured.objects.items;

/**
 * Copyright (c) 15 Feb 2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class ItemHookshot extends ItemHookshotBase {
    public ItemHookshot() {
        super("hookshot");
    }

    @Override
    public String getTranslationKey() {
        return "description.hookshot.name";
    }

    @Override
    public double getHookRange() {
        return 20.0;
    }

    @Override
    public int getCooldown() {
        return 30;
    }
}
