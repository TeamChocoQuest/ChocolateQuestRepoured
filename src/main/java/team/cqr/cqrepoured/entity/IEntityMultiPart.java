package team.cqr.cqrepoured.entity;

import net.minecraft.world.World;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.entity.PartEntity;

public interface IEntityMultiPart<T extends Entity> {

	public World getWorld();

	public boolean hurt(PartEntity<T> dragonPart, DamageSource source, float damage);

}
