package com.teamcqr.chocolatequestrepoured.util.handlers;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsHandler 
{
	public static SoundEvent GUN_SHOOT;
	public static SoundEvent MAGIC;
	public static SoundEvent CLASSIC_HURT;
	public static SoundEvent NETHER_DRAGON_HURT;
	public static SoundEvent NETHER_DRAGON_DEATH;
	
	public static void registerSounds()
	{
		GUN_SHOOT = registerSound("item.gun.shoot");
		MAGIC = registerSound("item.magic");
		CLASSIC_HURT = registerSound("entity.player.classic_hurt");
		NETHER_DRAGON_HURT = registerSound("entity.nether_dragon.hurt");
		NETHER_DRAGON_DEATH = registerSound("entity.nether_dragon.death");
		
	}
	
	private static SoundEvent registerSound(String name)
	{
		ResourceLocation location = new ResourceLocation(Reference.MODID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}