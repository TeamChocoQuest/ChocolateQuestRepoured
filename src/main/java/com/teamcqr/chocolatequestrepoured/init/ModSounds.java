package com.teamcqr.chocolatequestrepoured.init;

import static com.teamcqr.chocolatequestrepoured.util.InjectionUtil.Null;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MODID)
public class ModSounds {

	@ObjectHolder("item.gun.shoot")
	public static final SoundEvent GUN_SHOOT = Null();
	@ObjectHolder("item.magic")
	public static final SoundEvent MAGIC = Null();
	@ObjectHolder("entity.player.classic_hurt")
	public static final SoundEvent CLASSIC_HURT = Null();
	@ObjectHolder("entity.nether_dragon.hurt")
	public static final SoundEvent NETHER_DRAGON_HURT = Null();
	@ObjectHolder("entity.nether_dragon.death")
	public static final SoundEvent NETHER_DRAGON_DEATH = Null();

	@EventBusSubscriber(modid = Reference.MODID)
	public static class SoundRegistrationHandler {

		@SubscribeEvent
		public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
			final SoundEvent[] sounds = {
					createSoundEvent("item.gun.shoot"),
					createSoundEvent("item.magic"),
					createSoundEvent("entity.player.classic_hurt"),
					createSoundEvent("entity.nether_dragon.hurt"),
					createSoundEvent("entity.nether_dragon.death")
			};

			IForgeRegistry<SoundEvent> registry = event.getRegistry();

			for (SoundEvent sound : sounds) {
				registry.register(sound);
			}
		}

		private static SoundEvent createSoundEvent(String name) {
			ResourceLocation location = new ResourceLocation(Reference.MODID, name);
			return new SoundEvent(location).setRegistryName(location);
		}

	}

}
