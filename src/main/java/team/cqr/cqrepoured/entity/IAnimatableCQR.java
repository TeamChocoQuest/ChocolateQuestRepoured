package team.cqr.cqrepoured.entity;

import java.util.Optional;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import team.cqr.cqrepoured.item.gun.IFireArmTwoHanded;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemRevolver;
import team.cqr.cqrepoured.item.sword.ItemGreatSword;

public interface IAnimatableCQR extends IAnimatable, IAnimationTickable {
	
	@Override
	default int tickTimer() {
		if(this instanceof Entity) {
			return ((Entity)this).tickCount;
		}
		return 0;
	}

	public Set<String> getAlwaysPlayingAnimations();
	
	public default boolean isTwoHandedAnimationRunning() {
		return this.isSpellCasting() || this.isSpinToWinActive() || this.isWieldingTwoHandedWeapon();
	}

	public default boolean isWieldingTwoHandedWeapon() {
		return 
				//Bow and crossbows
				(
						this.getMainHandItem().getItem() instanceof ShootableItem || this.getOffhandItem().getItem() instanceof ShootableItem
						|| this.getMainHandItem().getItem() instanceof IFireArmTwoHanded || this.getOffhandItem().getItem() instanceof IFireArmTwoHanded
						|| this.getMainHandItem().getUseAnimation() == UseAction.BOW || this.getOffhandItem().getUseAnimation() == UseAction.BOW
						|| this.getMainHandItem().getUseAnimation() == UseAction.CROSSBOW || this.getOffhandItem().getUseAnimation() == UseAction.CROSSBOW
				)
				|| //Spears
				(
						this.getMainHandItem().getUseAnimation() == UseAction.SPEAR || this.getOffhandItem().getUseAnimation() == UseAction.SPEAR
				)
				|| //Greatswords
				(
						this.getMainHandItem().getItem() instanceof ItemGreatSword || this.getOffhandItem().getItem() instanceof ItemGreatSword
				);
	}

	@Override
	public default void registerControllers(AnimationData data) {
		//Always playing
		if(this.getAlwaysPlayingAnimations() != null) {
			for(String animName : this.getAlwaysPlayingAnimations()) {
				data.addAnimationController(new AnimationController<>(this, "controller_always_playing_" + animName, 0, (e) -> PlayState.CONTINUE));
			}
		}
		// Idle
		data.addAnimationController(new AnimationController<>(this, "controller_idle", 0, this::predicateIdle));
		// Body pose
		data.addAnimationController(new AnimationController<>(this, "controller_body_pose", 20, this::predicateBodyPose));
		// Walking
		data.addAnimationController(new AnimationController<>(this, "controller_walk", 10, this::predicateWalking));
		// Arms
		data.addAnimationController(new AnimationController<>(this, "controller_left_hand_pose", 10, this::predicateLeftArmPose));
		data.addAnimationController(new AnimationController<>(this, "controller_right_hand_pose", 10, this::predicateRightArmPose));
		data.addAnimationController(new AnimationController<>(this, "controller_left_hand", 0, this::predicateLeftArmSwing));
		data.addAnimationController(new AnimationController<>(this, "controller_right_hand", 0, this::predicateRightArmSwing));
		// TwoHanded
		data.addAnimationController(new AnimationController<>(this, "controller_twohanded_pose", 10, this::predicateTwoHandedPose));
		data.addAnimationController(new AnimationController<>(this, "controller_twohanded", 10, this::predicateTwoHandedSwing));
	}

	public String ANIM_NAME_PREFIX = "animation.biped.";

	public String ANIM_NAME_IDLE = ANIM_NAME_PREFIX + "idle";

	default <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_IDLE, true));
		}
		return PlayState.CONTINUE;
	}

	public String ANIM_NAME_SITTING = ANIM_NAME_PREFIX + "sit";
	public String ANIM_NAME_SNEAKING = ANIM_NAME_PREFIX + "body.sneak";

	default <E extends IAnimatable> PlayState predicateBodyPose(AnimationEvent<E> event) {
		if (this.isTwoHandedAnimationRunning()) {

		} else if (this.isPassenger() || this.isSitting()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_SITTING, true));
			return PlayState.CONTINUE;
		} else if (this.isCrouching()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_SNEAKING, true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	public String ANIM_NAME_BLOCKING_LEFT = ANIM_NAME_PREFIX + "arms.left.block";
	public String ANIM_NAME_BLOCKING_RIGHT = ANIM_NAME_PREFIX + "arms.right.block";

	public default Hand getLeftHand() {
		return this.isLeftHanded() ? Hand.MAIN_HAND : Hand.OFF_HAND;
	}

	public default Hand getRightHand() {
		return !this.isLeftHanded() ? Hand.MAIN_HAND : Hand.OFF_HAND;
	}

	default <E extends IAnimatable> PlayState predicateRightArmSwing(AnimationEvent<E> event) {
		return this.predicateHandSwing(this.getRightHand(), false, event);
	}

	default <E extends IAnimatable> PlayState predicateLeftArmSwing(AnimationEvent<E> event) {
		return this.predicateHandSwing(this.getLeftHand(), true, event);
	}

	default <E extends IAnimatable> PlayState predicateHandSwing(Hand hand, boolean leftHand, AnimationEvent<E> event) {
		if (this.isSwinging() && !this.isTwoHandedAnimationRunning()) {
			ItemStack handItemStack = this.getItemInHand(hand);
			if (!handItemStack.isEmpty()) {
				if (handItemStack.getItem().getUseAnimation(handItemStack) == UseAction.EAT || handItemStack.getItem().getUseAnimation(handItemStack) == UseAction.DRINK) {
					// Eating/Drinking animation
				} else {
					// Normal swinging
				}
				return PlayState.CONTINUE;
			}
		}
		return PlayState.STOP;
	}

	default <E extends IAnimatable> PlayState predicateRightArmPose(AnimationEvent<E> event) {
		return this.predicateHandPose(this.getRightHand(), false, event);
	}

	default <E extends IAnimatable> PlayState predicateLeftArmPose(AnimationEvent<E> event) {
		return this.predicateHandPose(this.getLeftHand(), true, event);
	}

	default <E extends IAnimatable> PlayState predicateHandPose(Hand hand, boolean leftHand, AnimationEvent<E> event) {
		ItemStack handItemStack = this.getItemInHand(hand);
		if (!handItemStack.isEmpty() && !this.isTwoHandedAnimationRunning()) {
			Item handItem = handItemStack.getItem();
			if (this.isBlocking() && (handItem instanceof ShieldItem || handItem.getUseAnimation(handItemStack) == UseAction.BLOCK)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation(leftHand ? ANIM_NAME_BLOCKING_LEFT : ANIM_NAME_BLOCKING_RIGHT, true));
			} else {
				// If the item is a small gun play the correct animation
			}
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	public String ANIM_NAME_SPELLCASTING = ANIM_NAME_PREFIX + "arms.cast-spell";

	default <E extends IAnimatable> PlayState predicateTwoHandedPose(AnimationEvent<E> event) {
		if (this.isTwoHandedAnimationRunning()) {
			if (this.isSpellCasting()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_SPELLCASTING, true));
				return PlayState.CONTINUE;
			} else {
				// First: Check for firearm, spear and greatsword in either hand
				// Main hand has priority
				Optional<PlayState> resultState = performTwoHandedLogicPerHand(this.getMainHandItem(), this.isLeftHanded(), event);
				if (!resultState.isPresent()) {
					resultState = performTwoHandedLogicPerHand(this.getOffhandItem(), !this.isLeftHanded(), event);
				}
				if (resultState.isPresent()) {
					return resultState.get();
				}
			}
		}
		return PlayState.STOP;
	}

	public String ANIM_NAME_SPEAR_POSE_LEFT = ANIM_NAME_PREFIX + "arms.left.spear";
	public String ANIM_NAME_SPEAR_POSE_RIGHT = ANIM_NAME_PREFIX + "arms.right.spear";

	public String ANIM_NAME_FIREARM_POSE_LEFT = ANIM_NAME_PREFIX + "arms.left.firearm";
	public String ANIM_NAME_FIREARM_POSE_RIGHT = ANIM_NAME_PREFIX + "arms.right.firearm";
	
	public String ANIM_NAME_FIREARM_SMALL_POSE_LEFT = ANIM_NAME_PREFIX + "arms.left.firearm-small";
	public String ANIM_NAME_FIREARM_SMALL_POSE_RIGHT = ANIM_NAME_PREFIX + "arms.right.firearm-small";

	public String ANIM_NAME_GREATSWORD_POSE = ANIM_NAME_PREFIX + "arms.greatsword";

	default <E extends IAnimatable> Optional<PlayState> performTwoHandedLogicPerHand(ItemStack itemStack, boolean leftHanded, AnimationEvent<E> event) {
		if (itemStack.isEmpty()) {
			return Optional.empty();
		}
		Item item = itemStack.getItem();
		// If item instanceof ItemGreatsword => Greatsword animation
		if(item instanceof ItemGreatSword) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_GREATSWORD_POSE, true));
			return Optional.of(PlayState.CONTINUE);
		}
		else if(item instanceof ItemRevolver && !(item instanceof ItemMusket)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(leftHanded ? ANIM_NAME_FIREARM_SMALL_POSE_LEFT: ANIM_NAME_FIREARM_SMALL_POSE_RIGHT, true));
			return Optional.of(PlayState.CONTINUE);
		}
		// If item instanceof Firearm/Bow/Crossbow => firearm animation
		else if ((item instanceof IFireArmTwoHanded) || item.getUseAnimation(itemStack) == UseAction.BOW || item.getUseAnimation(itemStack) == UseAction.CROSSBOW) {
			// Firearm
			event.getController().setAnimation(new AnimationBuilder().addAnimation(leftHanded ? ANIM_NAME_FIREARM_POSE_LEFT : ANIM_NAME_FIREARM_POSE_RIGHT, true));
			return Optional.of(PlayState.CONTINUE);
		}
		// If item instanceof Spear => spear animation
		else if (item.getUseAnimation(itemStack) == UseAction.SPEAR) {
			// Yes this is for tridents but we can use it anyway
			// Spear
			event.getController().setAnimation(new AnimationBuilder().addAnimation(leftHanded ? ANIM_NAME_SPEAR_POSE_LEFT : ANIM_NAME_SPEAR_POSE_RIGHT, true));
			return Optional.of(PlayState.CONTINUE);
		}
		return Optional.empty();
	}

	public String ANIM_NAME_GREATSWORD_SWING = ANIM_NAME_PREFIX + "arms.attack-greatsword";
	public String ANIM_NAME_SPEAR_SWING = ANIM_NAME_PREFIX + "arms.attack-spear";

	default <E extends IAnimatable> PlayState predicateTwoHandedSwing(AnimationEvent<E> event) {
		if (this.isTwoHandedAnimationRunning() && this.isSwinging()) {
			// Check for greatsword & spear and play their animations
			if (this.getMainHandItem().getItem().getUseAnimation(this.getMainHandItem()) == UseAction.SPEAR || this.getOffhandItem().getItem().getUseAnimation(this.getOffhandItem()) == UseAction.SPEAR) {
				// Spear use animation
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_SPEAR_SWING, false));
			}
			// If either hand item is greatsword => greatsword animation
			else if((this.getMainHandItem().getItem() instanceof ItemGreatSword) || (this.getOffhandItem().getItem() instanceof ItemGreatSword)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_GREATSWORD_SWING, false));
			}
		}
		return PlayState.STOP;
	}
	
	public String ANIM_NAME_WALKING = ANIM_NAME_PREFIX + "legs.walk";
	
	default <E extends IAnimatable> PlayState predicateWalking(AnimationEvent<E> event) {
		if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_WALKING, true));
			if(this instanceof LivingEntity) {
				LivingEntity entity = (LivingEntity) this;
				event.getController().setAnimationSpeed(entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * 6.0);
			} else {
				event.getController().setAnimationSpeed(this.isSprinting() ? 2.0D : 1.0D);
			}
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	//Access to entity stuff
	public boolean isSitting();
	
	public boolean isLeftHanded();
	
	public boolean isSwinging();
	
	public boolean isCrouching();

	public boolean isPassenger();

	public boolean isBlocking();

	public boolean isSpinToWinActive();

	public boolean isSpellCasting();
	
	public ItemStack getItemInHand(Hand hand);

	public ItemStack getOffhandItem();

	public ItemStack getMainHandItem();	
	
	public boolean isSprinting();

}
