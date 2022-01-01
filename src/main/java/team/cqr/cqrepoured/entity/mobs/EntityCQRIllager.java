package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTables;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;

public class EntityCQRIllager extends AbstractEntityCQR {

	public EntityCQRIllager(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Illager;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ILLAGERS;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LootTables.ENTITIES_VINDICATION_ILLAGER;
	}

	@Override
	public int getTextureCount() {
		return 2;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ILLAGER;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.VINDICATION_ILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_VINDICATION_ILLAGER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ILLAGER_DEATH;
	}

}
