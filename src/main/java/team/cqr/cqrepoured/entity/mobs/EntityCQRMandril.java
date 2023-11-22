package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;

public class EntityCQRMandril extends AbstractEntityCQR {

	private double prevTailAnimationProgress;
	private double tailAnimationProgress;

	public EntityCQRMandril(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(9, new EntityAILeapAtTarget(this, 0.6F));
	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Mandril;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.BEASTS;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.84F;
	}

	@Override
	public float getDefaultWidth() {
		return 0.6F;
	}

	@Override
	public float getDefaultHeight() {
		return 1.9F;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.world.isRemote) {
			this.prevTailAnimationProgress = this.tailAnimationProgress;
			this.tailAnimationProgress += 1.0D + this.limbSwingAmount * 4.0D;
		}
	}

	public double getTailAnimationProgress(float partialTick) {
		return this.prevTailAnimationProgress + (this.tailAnimationProgress - this.prevTailAnimationProgress) * partialTick;
	}

}
