package team.cqr.cqrepoured.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.PartEntity;

public interface IEntityMultiPart<T extends Entity> {

	public Level getWorld();

	public boolean hurt(PartEntity<T> dragonPart, DamageSource source, float damage);

}
