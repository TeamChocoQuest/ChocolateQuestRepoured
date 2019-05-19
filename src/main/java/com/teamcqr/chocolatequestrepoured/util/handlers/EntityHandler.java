package com.teamcqr.chocolatequestrepoured.util.handlers;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntitySlimePart;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRZombie;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileCannonBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityHandler 
{
	public static void registerEntity()
	{
		registerModEntity(":slime_part", EntitySlimePart.class, "EntityPartSlime", Reference.ENTITY_SLIME_PART);
		registerModEntity(":projectile_earth_quake", ProjectileEarthQuake.class, "ProjectileEarthQuake", Reference.PROJECTILE_EARTH_QUAKE);
		registerModEntity(":projectile_bullet", ProjectileBullet.class, "ProjectileBullet", Reference.PROJECTILE_BULLET);
		registerModEntity(":projectile_spider_ball", ProjectileSpiderBall.class, "ProjectileSpiderBall", Reference.PROJECTILE_SPIDER_BALL);
		registerModEntity(":projectile_cannon_ball", ProjectileCannonBall.class, "ProjectileCannonBall", Reference.PROJECTILE_CANNON_BALL);
		registerModEntity("projectile_vampiric_spell", ProjectileVampiricSpell.class, "ProjectileVampiricSpell", Reference.PROJECTILE_VAMPIRIC_SPELL);
		registerModEntity(":projectile_poison_spell", ProjectilePoisonSpell.class, "ProjectilePoisonSpell", Reference.PROJECTILE_POISON_SPELL);

		registerModEntity(":mob_cqrzombie", EntityCQRZombie.class, "EntityCQRZombie", Reference.ENTITY_CQRZOMBIE, 3289650, 000000);

	}
	
	public static void registerModEntity(String path, Class<? extends Entity> entityClass, String entityName, int id)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + path), entityClass, entityName, id, CQRMain.INSTANCE, 64, 1, true);
	}

	public static void registerModEntity(String path, Class<? extends Entity> entityClass, String entityName, int id, int color1, int color2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + path), entityClass, entityName, id, CQRMain.INSTANCE, 64, 1, true, color1, color2);
	}
}