package com.teamcqr.chocolatequestrepoured.objects.items;

public class ItemLongshot extends ItemHookshotBase {
    public ItemLongshot() {
        super();
    }

    @Override
    String getTranslationKey() {
        return "description.longshot.name";
    }

    @Override
    double getHookRange() {
        return 40.0;
    }

    @Override
    public int getCooldown() {
        return 30;
    }
}
