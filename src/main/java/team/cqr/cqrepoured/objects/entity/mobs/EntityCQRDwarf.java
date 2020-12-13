package team.cqr.cqrepoured.objects.entity.mobs;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.CQRConfig;

public class EntityCQRDwarf extends AbstractEntityCQR {

	public EntityCQRDwarf(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Dwarf;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.DWARVES_AND_GOLEMS;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.CLASSIC_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_VILLAGER_DEATH;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_DWARF;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.isFireDamage()) {
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public int getTextureCount() {
		return 3;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.85F;
	}

	@Override
	public float getDefaultWidth() {
		return 0.54F;
	}

	@Override
	public float getDefaultHeight() {
		return 1.235F;
	}

}
