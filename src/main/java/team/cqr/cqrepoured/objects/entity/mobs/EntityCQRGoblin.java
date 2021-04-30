package team.cqr.cqrepoured.objects.entity.mobs;

import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityCQRGoblin extends AbstractEntityCQR {

	public EntityCQRGoblin(World worldIn) {
		super(worldIn);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_GOBLIN;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Goblin;
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.GOBLINS;
	}

	@Override
	public float getDefaultHeight() {
		return super.getDefaultHeight() * 0.75F;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.GOBLIN_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CQRSounds.GOBLIN_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.GOBLIN_HURT;
	}

}
