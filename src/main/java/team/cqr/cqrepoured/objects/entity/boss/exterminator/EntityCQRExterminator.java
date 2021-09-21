package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.objects.entity.IDontRenderFire;
import team.cqr.cqrepoured.objects.entity.IMechanical;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityCQRExterminator extends AbstractEntityCQRBoss implements IMechanical, IDontRenderFire, IEntityMultiPart, IAnimatable {

	//Entity parts
	private SubEntityExterminatorBackpack backpackEntity;
	private SubEntityExterminatorFieldEmitter emitterLeft;
	private SubEntityExterminatorFieldEmitter emitterRight;
	
	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);
	
	//Entity state handling
	//Arm state
	
	public EntityCQRExterminator(World worldIn) {
		super(worldIn);
		this.experienceValue = 100;
		
		this.backpackEntity = new SubEntityExterminatorBackpack(this, "exterminator_backpack");
		this.emitterLeft = new SubEntityExterminatorFieldEmitter(this, "emitter_left");
		this.emitterRight = new SubEntityExterminatorFieldEmitter(this, "emitter_right");
	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Exterminatior;
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ILLAGERS;
	}

	@Override
	public World getWorld() {
		return this.getEntityWorld();
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
		return super.attackEntityFrom(source, damage, true);
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		//Spin animation for tesla coils
		data.addAnimationController(new AnimationController<EntityCQRExterminator>(this, "controller_spin_coils", 0, this::predicateSpinCoils));
		
		//Stun animation
		
		//Kick, Throw and smash animation
		
		//Cannon controller (raising, lowering and shooting)
		
		//Inactive animation
		data.addAnimationController(new AnimationController<EntityCQRExterminator>(this, "controller_sitting_animation", 30, this::predicateInactive));
	}
	
	public static final String ANIM_NAME_PREFIX = "animation.exterminator.";
	
	public static final String ANIM_NAME_SPIN_COILS = ANIM_NAME_PREFIX + "spin_tesla_coils";
	private <E extends IAnimatable> PlayState predicateSpinCoils(AnimationEvent<E> event) {
		if(event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_SPIN_COILS, true));
		}
		return PlayState.CONTINUE;
	}
	
	public static final String ANIM_NAME_INACTIVE = ANIM_NAME_PREFIX + "inactive";
	private <E extends IAnimatable> PlayState predicateInactive(AnimationEvent<E> event) {
		if(super.isSitting()) {
			if(event.getController().getCurrentAnimation() == null || event.getController().isJustStarting) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_INACTIVE, true));
			}
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(null);
		return PlayState.STOP;
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
	
	//Default hitbox size
	@Override
	public float getDefaultHeight() {
		return 2.75F;
	}

	@Override
	public float getDefaultWidth() {
		return 2F;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		this.world.updateEntityWithOptionalForce(this.backpackEntity, true);
		this.world.updateEntityWithOptionalForce(this.emitterLeft, true);
		this.world.updateEntityWithOptionalForce(this.emitterRight, true);
		this.alignParts();
	}
	
	//Multipart stuff
	private void alignParts() {
		Vec3d offset = this.getLookVec().normalize().scale(-0.25D * this.getSizeVariation());
		offset = offset.add(0, 1.25D * this.getSizeVariation(), 0);
		
		this.backpackEntity.setPosition(this.posX + offset.x, this.posY + offset.y, this.posZ + offset.z);
		
		Vec3d offsetEmittersHorizontal = this.getLookVec().normalize().scale(0.5 * this.getSizeVariation());
		
		Vec3d offsetEmitters = this.getLookVec().normalize().scale(-0.4D * this.getSizeVariation());
		offsetEmitters = offsetEmitters.add(0, 2.375D * this.getSizeVariation(), 0);
		
		offsetEmittersHorizontal = VectorUtil.rotateVectorAroundY(offsetEmittersHorizontal, 90);
		this.emitterRight.setPosition(this.posX + offsetEmitters.x + offsetEmittersHorizontal.x, this.posY + offsetEmitters.y, this.posZ + offsetEmitters.z + offsetEmittersHorizontal.z);
		
		offsetEmittersHorizontal = VectorUtil.rotateVectorAroundY(offsetEmittersHorizontal, 180);
		this.emitterLeft.setPosition(this.posX + offsetEmitters.x + offsetEmittersHorizontal.x, this.posY + offsetEmitters.y, this.posZ + offsetEmitters.z + offsetEmittersHorizontal.z);
	}
	
	@Override
	public void resize(float widthScale, float heightSacle) {
		super.resize(widthScale, heightSacle);
		
		this.backpackEntity.resize(widthScale, heightSacle);
		this.emitterLeft.resize(widthScale, heightSacle);
		this.emitterRight.resize(widthScale, heightSacle);
	}
	
	@Override
	public boolean isSitting() {
		return false;
	}
	
	@Override
	public void setDead() {
		this.world.removeEntityDangerously(this.backpackEntity);
		this.world.removeEntityDangerously(this.emitterLeft);
		this.world.removeEntityDangerously(this.emitterRight);
		
		super.setDead();
	}
	
}
