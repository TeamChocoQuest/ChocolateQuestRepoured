package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, ChocolateQuestReDone.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GUN_SHOOT = registerSoundEvent("gunshoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC = registerSoundEvent("magic");
    public static final DeferredHolder<SoundEvent, SoundEvent> BELL_USE = registerSoundEvent("bell_use");
    public static final DeferredHolder<SoundEvent, SoundEvent> REVOLVER_SHOOT = registerSoundEvent("revolver_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSKET_SHOOT = registerSoundEvent("musket_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> WIND = registerSoundEvent("wind");

    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLIN_AMBIENT = registerSoundEvent("goblin_ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLIN_DEATH = registerSoundEvent("goblin_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLIN_HURT = registerSoundEvent("goblin_hurt");

    public static final DeferredHolder<SoundEvent, SoundEvent> GREMLIN_LAUGHTSMALLMONSTER = registerSoundEvent("gremlin_laughtsmallmonster");
    public static final DeferredHolder<SoundEvent, SoundEvent> GREMLIN_SMALLMONSTERDEAD = registerSoundEvent("gremlin_smallmonsterdead");
    public static final DeferredHolder<SoundEvent, SoundEvent> GREMLIN_SMALLMONSTERHURT = registerSoundEvent("gremlin_smallmonsterhurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GREMLIN_SMALLMONSTERLAUGHT = registerSoundEvent("gremlin_smallmonsterlaught");
    public static final DeferredHolder<SoundEvent, SoundEvent> GREMLIN_SMALLMONSTERSPEAK = registerSoundEvent("gremlin_smallmonsterspeak");

    public static final DeferredHolder<SoundEvent, SoundEvent> OGRE_AMBIENT = registerSoundEvent("ogre_ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> OGRE_DEATH = registerSoundEvent("ogre_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> OGRE_HURT = registerSoundEvent("ogre_hurt");

    public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_AMBIENT = registerSoundEvent("pirate_ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_DEATH = registerSoundEvent("pirate_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_HURT = registerSoundEvent("pirate_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIRATE_PIRATECAPTAIN = registerSoundEvent("pirate_piratecaptain");

    public static final DeferredHolder<SoundEvent, SoundEvent> WALKER_AMBIENT = registerSoundEvent("walker_ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WALKER_DEATH = registerSoundEvent("walker_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WALKER_HURT = registerSoundEvent("walker_hurt");

    public static void register(net.neoforged.bus.api.IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, name)));
    }
}
