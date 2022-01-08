package team.cqr.cqrepoured.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;

public interface IEntityMultiPart<T extends Entity> {

	World getWorld();

    boolean hurt(PartEntity<T> dragonPart, DamageSource source, float damage);
	
}
