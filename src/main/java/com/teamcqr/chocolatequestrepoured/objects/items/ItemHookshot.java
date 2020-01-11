package com.teamcqr.chocolatequestrepoured.objects.items;

public class ItemHookshot extends ItemHookshotBase {
    public ItemHookshot() {
        super();
    }

    @Override
    String getTranslationKey() {
        return "description.hookshot.name";
    }

    @Override
    double getHookRange() {
        return 20.0;
    }

    @Override
    public int getCooldown() {
        return 30;
    }
}
