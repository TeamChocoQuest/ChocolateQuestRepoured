package com.tiviacz.chocolatequestrepoured.util.handlers;

import com.tiviacz.chocolatequestrepoured.CQRMain;
import com.tiviacz.chocolatequestrepoured.objects.entity.EntitySlimePart;
import com.tiviacz.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityHandler 
{
	public static void registerEntity()
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":slime_part"), EntitySlimePart.class, "EntityPartSlime", Reference.ENTITY_SLIME_PART_ID, CQRMain.instance, 64, 1, true);
	}
}