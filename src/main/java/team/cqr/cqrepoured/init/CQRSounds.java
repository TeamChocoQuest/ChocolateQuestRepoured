package team.cqr.cqrepoured.init;

import static team.cqr.cqrepoured.util.InjectionUtil.Null;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import team.cqr.cqrepoured.util.Reference;

@ObjectHolder(Reference.MODID)
public class CQRSounds {

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

	@ObjectHolder("entity.gremlin.ambient")
	public static final SoundEvent GREMLIN_AMBIENT = Null();
	@ObjectHolder("entity.gremlin.hurt")
	public static final SoundEvent GREMLIN_HURT = Null();
	@ObjectHolder("entity.gremlin.death")
	public static final SoundEvent GREMLIN_DEATH = Null();
	
	// Source for goblin sounds: https://opengameart.org/content/goblins-sound-pack
	@ObjectHolder("entity.goblin.ambient")
	public static final SoundEvent GOBLIN_AMBIENT = Null();
	@ObjectHolder("entity.goblin.hurt")
	public static final SoundEvent GOBLIN_HURT = Null();
	@ObjectHolder("entity.goblin.death")
	public static final SoundEvent GOBLIN_DEATH = Null();
	
	@ObjectHolder("entity.pirate.ambient")
	public static final SoundEvent PIRATE_AMBIENT = Null();
	@ObjectHolder("entity.pirate.hurt")
	public static final SoundEvent PIRATE_HURT = Null();
	@ObjectHolder("entity.pirate.death")
	public static final SoundEvent PIRATE_DEATH = Null();

	@ObjectHolder("entity.bubble.bubble")
	public static final SoundEvent BUBBLE_BUBBLE = Null();

	@EventBusSubscriber(modid = Reference.MODID)
	public static class SoundRegistrationHandler {

		@SubscribeEvent
		public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
			final SoundEvent[] sounds = {
					createSoundEvent("item.gun.shoot"),
					createSoundEvent("item.magic"),
					createSoundEvent("entity.player.classic_hurt"),
					createSoundEvent("entity.goblin.ambient"),
					createSoundEvent("entity.goblin.hurt"),
					createSoundEvent("entity.goblin.death"),
					createSoundEvent("entity.gremlin.ambient"),
					createSoundEvent("entity.gremlin.hurt"),
					createSoundEvent("entity.gremlin.death"),
					createSoundEvent("entity.nether_dragon.hurt"),
					createSoundEvent("entity.nether_dragon.death"),
					createSoundEvent("entity.pirate.ambient"),
					createSoundEvent("entity.pirate.hurt"),
					createSoundEvent("entity.pirate.death"),
					createSoundEvent("entity.bubble.bubble") };

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
