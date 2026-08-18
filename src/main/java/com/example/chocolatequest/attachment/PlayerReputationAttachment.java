package com.example.chocolatequest.attachment;

import com.example.chocolatequest.faction.EDefaultFaction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerReputationAttachment implements INBTSerializable<CompoundTag> {
    private final Map<EDefaultFaction, Integer> reputations = new HashMap<>();

    public int getReputation(EDefaultFaction faction) {
        return reputations.getOrDefault(faction, 0);
    }

    public void setReputation(EDefaultFaction faction, int value) {
        reputations.put(faction, Math.max(-100, Math.min(100, value)));
    }

    public void addReputation(EDefaultFaction faction, int value) {
        setReputation(faction, getReputation(faction) + value);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<EDefaultFaction, Integer> entry : reputations.entrySet()) {
            tag.putInt(entry.getKey().name(), entry.getValue());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        reputations.clear();
        for (String key : nbt.getAllKeys()) {
            try {
                EDefaultFaction faction = EDefaultFaction.valueOf(key);
                reputations.put(faction, nbt.getInt(key));
            } catch (IllegalArgumentException e) {
            }
        }
    }
}
