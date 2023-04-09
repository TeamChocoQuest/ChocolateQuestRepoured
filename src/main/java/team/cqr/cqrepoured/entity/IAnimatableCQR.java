package team.cqr.cqrepoured.entity;

import java.util.Optional;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.gun.IFireArmTwoHanded;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemRevolver;
import team.cqr.cqrepoured.item.spear.ItemSpearBase;
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
	
	public default <E extends AbstractEntityCQR & IAnimatableCQR> boolean isTwoHandedAnimationRunning(E animatable) {
		return animatable.isSpellCasting() || animatable.isSpinToWinActive() || animatable.isWieldingTwoHandedWeapon(animatable);
	}

	public default <E extends AbstractEntityCQR & IAnimatableCQR> boolean isWieldingTwoHandedWeapon(E animatable) {
		return 
				//Bow and crossbows
				(
						animatable.getMainHandItem().getItem() instanceof ShootableItem || animatable.getOffhandItem().getItem() instanceof ShootableItem
						|| animatable.getMainHandItem().getItem() instanceof ItemRevolver || animatable.getOffhandItem().getItem() instanceof ItemRevolver
						|| animatable.getMainHandItem().getItem() instanceof IFireArmTwoHanded || animatable.getOffhandItem().getItem() instanceof IFireArmTwoHanded
						|| animatable.getMainHandItem().getUseAnimation() == UseAction.BOW || animatable.getOffhandItem().getUseAnimation() == UseAction.BOW
						|| animatable.getMainHandItem().getUseAnimation() == UseAction.CROSSBOW || animatable.getOffhandItem().getUseAnimation() == UseAction.CROSSBOW
				)
				|| //Spears
				(
						animatable.getMainHandItem().getUseAnimation() == UseAction.SPEAR || animatable.getOffhandItem().getUseAnimation() == UseAction.SPEAR
						|| animatable.getMainHandItem().getItem() instanceof ItemSpearBase || animatable.getOffhandItem().getItem() instanceof ItemSpearBase
				)
				|| //Greatswords
				(
						animatable.getMainHandItem().getItem() instanceof ItemGreatSword || animatable.getOffhandItem().getItem() instanceof ItemGreatSword
				);
	}
	
	public default <E extends AbstractEntityCQR & IAnimatableCQR> void registerControllers(E animatable, AnimationData data) {
		//Always playing
		Set<String> alwaysPlaying = animatable.getAlwaysPlayingAnimations();
		if(alwaysPlaying != null && alwaysPlaying.size() > 0) {
			for(String animName : animatable.getAlwaysPlayingAnimations()) {
				data.addAnimationController(new AnimationController<>(animatable, "controller_always_playing_" + animName, 0, (e) -> {
					if (e.getController().getCurrentAnimation() == null) {
						e.getController().setAnimation(new AnimationBuilder().loop(animName));
					}
					return PlayState.CONTINUE;
				}));
			}
		}
		// Idle
		data.addAnimationController(new AnimationController<>(animatable, "controller_idle", 0, this::predicateIdle));
		// Body pose
		data.addAnimationController(new AnimationController<>(animatable, "controller_body_pose", 20, this::predicateBodyPose));
		// Walking
		data.addAnimationController(new AnimationController<>(animatable, "controller_walk", 10, this::predicateWalking));
		// Arms
		data.addAnimationController(new AnimationController<>(animatable, "controller_left_hand_pose", 10, this::predicateLeftArmPose));
		data.addAnimationController(new AnimationController<>(animatable, "controller_right_hand_pose", 10, this::predicateRightArmPose));
		data.addAnimationController(new AnimationController<>(animatable, "controller_left_hand", 0, this::predicateLeftArmSwing));
		data.addAnimationController(new AnimationController<>(animatable, "controller_right_hand", 0, this::predicateRightArmSwing));
		// TwoHanded
		data.addAnimationController(new AnimationController<>(animatable, "controller_twohanded_pose", 10, this::predicateTwoHandedPose));
		data.addAnimationController(new AnimationController<>(animatable, "controller_twohanded", 10, this::predicateTwoHandedSwing));
	}

	public String ANIM_NAME_PREFIX = "animation.biped.";

	public String ANIM_NAME_IDLE = ANIM_NAME_PREFIX + "idle";

	default <E extends AbstractEntityCQR & IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_IDLE));
		}
		return PlayState.CONTINUE;
	}

	public String ANIM_NAME_SITTING = ANIM_NAME_PREFIX + "sit";
	public String ANIM_NAME_SNEAKING = ANIM_NAME_PREFIX + "body.sneak";

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateBodyPose(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		if (animatable.isTwoHandedAnimationRunning(animatable)) {

		} else if (animatable.isPassenger() || animatable.isSitting()) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_SITTING));
			return PlayState.CONTINUE;
		} else if (animatable.isCrouching()) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_SNEAKING));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	public String ANIM_NAME_BLOCKING_LEFT = ANIM_NAME_PREFIX + "arms.left.block";
	public String ANIM_NAME_BLOCKING_RIGHT = ANIM_NAME_PREFIX + "arms.right.block";

	public default <E extends AbstractEntityCQR & IAnimatableCQR> Hand getLeftHand(E animatable) {
		return animatable.isLeftHanded() ? Hand.MAIN_HAND : Hand.OFF_HAND;
	}

	public default <E extends AbstractEntityCQR & IAnimatableCQR> Hand getRightHand(E animatable) {
		return !animatable.isLeftHanded() ? Hand.MAIN_HAND : Hand.OFF_HAND;
	}

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateRightArmSwing(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		return animatable.predicateHandSwing(animatable.getRightHand(animatable), false, event);
	}

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateLeftArmSwing(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		return animatable.predicateHandSwing(animatable.getLeftHand(animatable), true, event);
	}

	public String ANIM_NAME_SWING_NORMAL_LEFT = ANIM_NAME_PREFIX + "arms.left.item-use";
	public String ANIM_NAME_SWING_NORMAL_RIGHT = ANIM_NAME_PREFIX + "arms.right.item-use";
	
	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateHandSwing(Hand hand, boolean leftHand, AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		if (!animatable.isTwoHandedAnimationRunning(animatable)) {
			if (animatable.isSwinging(hand, event)) {
				ItemStack handItemStack = animatable.getItemInHand(hand);
				/*if(event.getController().getAnimationState() != AnimationState.Running) {
					event.getController().clearAnimationCache();
					event.getController().markNeedsReload();
				}*/
				if (!handItemStack.isEmpty()) {
					if (handItemStack.getItem().getUseAnimation(handItemStack) == UseAction.EAT || handItemStack.getItem().getUseAnimation(handItemStack) == UseAction.DRINK) {
						// Eating/Drinking animation
					} else {
						// Normal swinging
						event.getController().markNeedsReload();
						event.getController().setAnimation(new AnimationBuilder().playOnce(leftHand ? ANIM_NAME_SWING_NORMAL_LEFT : ANIM_NAME_SWING_NORMAL_RIGHT));
					}
					return PlayState.CONTINUE;
				} else {
					event.getController().setAnimation(new AnimationBuilder().playOnce(leftHand ? ANIM_NAME_SWING_NORMAL_LEFT : ANIM_NAME_SWING_NORMAL_RIGHT));
					return PlayState.CONTINUE;
				}
			} else {
				//event.getController().setAnimation(null);
				//event.getController().clearAnimationCache();
			}
			if(event.getController().getCurrentAnimation() == null && !event.getController().getAnimationState().equals(AnimationState.Stopped)) {
				event.getController().clearAnimationCache();
				event.getController().markNeedsReload();
				return PlayState.STOP;
			} else if(event.getController().getAnimationState().equals(AnimationState.Stopped) && event.getController().getCurrentAnimation() != null) {
				event.getController().clearAnimationCache();
				event.getController().markNeedsReload();
				return PlayState.STOP;
			}
		}
		return PlayState.CONTINUE;
	}

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateRightArmPose(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		return animatable.predicateHandPose(animatable.getRightHand(animatable), false, event);
	}

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateLeftArmPose(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		return animatable.predicateHandPose(animatable.getLeftHand(animatable), true, event);
	}

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateHandPose(Hand hand, boolean leftHand, AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		ItemStack handItemStack = animatable.getItemInHand(hand);
		if (!handItemStack.isEmpty() && !animatable.isTwoHandedAnimationRunning(animatable)) {
			Item handItem = handItemStack.getItem();
			if (animatable.isBlocking() && (handItem instanceof ShieldItem || handItem.getUseAnimation(handItemStack) == UseAction.BLOCK)) {
				event.getController().setAnimation(new AnimationBuilder().loop(leftHand ? ANIM_NAME_BLOCKING_LEFT : ANIM_NAME_BLOCKING_RIGHT));
			} else {
				// If the item is a small gun play the correct animation
				// if(handItem instanceof IRangedWeapon) {
				//	 event.getController().setAnimation(new AnimationBuilder().addAnimation(leftHand ? ANIM_NAME_FIREARM_SMALL_POSE_LEFT : ANIM_NAME_FIREARM_SMALL_POSE_RIGHT, true));
				// }
			}
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	public String ANIM_NAME_SPELLCASTING = ANIM_NAME_PREFIX + "arms.cast-spell";

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateTwoHandedPose(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		if (animatable.isTwoHandedAnimationRunning(animatable)) {
			if (animatable.isSpellCasting()) {
				event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_SPELLCASTING));
				return PlayState.CONTINUE;
			} else {
				// First: Check for firearm, spear and greatsword in either hand
				// Main hand has priority
				Optional<PlayState> resultState = performTwoHandedLogicPerHand(animatable.getMainHandItem(), animatable.isLeftHanded(), event);
				if (!resultState.isPresent()) {
					resultState = performTwoHandedLogicPerHand(animatable.getOffhandItem(), !animatable.isLeftHanded(), event);
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

	default <E extends AbstractEntityCQR & IAnimatableCQR> Optional<PlayState> performTwoHandedLogicPerHand(ItemStack itemStack, boolean leftHanded, AnimationEvent<E> event) {
		if (itemStack.isEmpty()) {
			return Optional.empty();
		}
		Item item = itemStack.getItem();
		// If item instanceof ItemGreatsword => Greatsword animation
		if(item instanceof ItemGreatSword) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_GREATSWORD_POSE));
			return Optional.of(PlayState.CONTINUE);
		}
		else if(item instanceof ItemRevolver && !(item instanceof ItemMusket)) {
			event.getController().setAnimation(new AnimationBuilder().loop(leftHanded ? ANIM_NAME_FIREARM_SMALL_POSE_LEFT: ANIM_NAME_FIREARM_SMALL_POSE_RIGHT));
			return Optional.of(PlayState.CONTINUE);
		}
		// If item instanceof Spear => spear animation
		else if (item.getUseAnimation(itemStack) == UseAction.SPEAR || item instanceof ItemSpearBase) {
			// Yes this is for tridents but we can use it anyway
			// Spear
			event.getController().setAnimation(new AnimationBuilder().loop(leftHanded ? ANIM_NAME_SPEAR_POSE_LEFT : ANIM_NAME_SPEAR_POSE_RIGHT));
			return Optional.of(PlayState.CONTINUE);
		}
		// If item instanceof Firearm/Bow/Crossbow => firearm animation
		else if ((item instanceof IFireArmTwoHanded) || item.getUseAnimation(itemStack) == UseAction.BOW || item.getUseAnimation(itemStack) == UseAction.CROSSBOW) {
			// Firearm
			event.getController().setAnimation(new AnimationBuilder().loop(leftHanded ? ANIM_NAME_FIREARM_POSE_LEFT : ANIM_NAME_FIREARM_POSE_RIGHT));
			return Optional.of(PlayState.CONTINUE);
		}
		return Optional.empty();
	}

	public String ANIM_NAME_GREATSWORD_SWING = ANIM_NAME_PREFIX + "arms.attack-greatsword";
	public String ANIM_NAME_SPEAR_SWING = ANIM_NAME_PREFIX + "arms.attack-spear";

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateTwoHandedSwing(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		if (animatable.isTwoHandedAnimationRunning(animatable) && animatable.isSwinging(event)) {
			// Check for greatsword & spear and play their animations
			if (animatable.getMainHandItem().getItem().getUseAnimation(animatable.getMainHandItem()) == UseAction.SPEAR || animatable.getOffhandItem().getItem().getUseAnimation(animatable.getOffhandItem()) == UseAction.SPEAR) {
				// Spear use animation
				event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_NAME_SPEAR_SWING));
			}
			// If either hand item is greatsword => greatsword animation
			else if((animatable.getMainHandItem().getItem() instanceof ItemGreatSword) || (animatable.getOffhandItem().getItem() instanceof ItemGreatSword)) {
				event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_NAME_GREATSWORD_SWING));
			}
		}
		return PlayState.STOP;
	}
	
	public String ANIM_NAME_WALKING = ANIM_NAME_PREFIX + "legs.walk";
	
	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateWalking(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_WALKING));
			event.getController().setAnimationSpeed(animatable.getAttributeValue(Attributes.MOVEMENT_SPEED) * 6.0);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	public <E extends AbstractEntityCQR & IAnimatableCQR> boolean isSwinging(Hand hand, AnimationEvent<E> event);
	public default <E extends AbstractEntityCQR & IAnimatableCQR> boolean isSwinging(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		if(animatable instanceof MobEntity) {
			return ((MobEntity)animatable).swinging;
		}
		return animatable.isSwinging(Hand.MAIN_HAND, event);
	}

	public boolean isSpinToWinActive();

	public boolean isSpellCasting();
	
}