package com.teamcqr.chocolatequestrepoured.util.handlers;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntitySlimePart;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityHandler 
{
	public static void registerEntity()
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":slime_part"), EntitySlimePart.class, "EntityPartSlime", Reference.ENTITY_SLIME_PART_ID, CQRMain.INSTANCE, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":projectile_earth_quake"), ProjectileEarthQuake.class, "ProjectileEarthQuake", Reference.PROJECTILE_EARTH_QUAKE, CQRMain.INSTANCE, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":projectile_bullet"), ProjectileBullet.class, "ProjectileBullet", Reference.PROJECTILE_BULLET, CQRMain.INSTANCE, 64, 1, true);
	}
}