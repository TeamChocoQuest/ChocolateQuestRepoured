package team.cqr.cqrepoured.entity;

import java.util.Optional;
import java.util.Set;

import net.minecraft.entity.MobEntity;
import net.minecraft.item.UseAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.gun.IFireArmTwoHanded;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemRevolver;
import team.cqr.cqrepoured.item.spear.ItemSpearBase;
import team.cqr.cqrepoured.item.sword.ItemGreatSword;

public interface IAnimatableCQR extends GeoEntity {
	
	@Override
	default double getTick(Object arg0) {
		if(arg0 instanceof Entity ent) {
			return ent.tickCount;
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
						animatable.getMainHandItem().getItem() instanceof ProjectileWeaponItem || animatable.getOffhandItem().getItem() instanceof ProjectileWeaponItem
						|| animatable.getMainHandItem().getItem() instanceof ItemRevolver || animatable.getOffhandItem().getItem() instanceof ItemRevolver
						|| animatable.getMainHandItem().getItem() instanceof IFireArmTwoHanded || animatable.getOffhandItem().getItem() instanceof IFireArmTwoHanded
						|| animatable.getMainHandItem().getUseAnimation() == UseAnim.BOW || animatable.getOffhandItem().getUseAnimation() == UseAnim.BOW
						|| animatable.getMainHandItem().getUseAnimation() == UseAnim.CROSSBOW || animatable.getOffhandItem().getUseAnimation() == UseAnim.CROSSBOW
				)
				|| //Spears
				(
						animatable.getMainHandItem().getUseAnimation() == UseAnim.SPEAR || animatable.getOffhandItem().getUseAnimation() == UseAnim.SPEAR
						|| animatable.getMainHandItem().getItem() instanceof ItemSpearBase || animatable.getOffhandItem().getItem() instanceof ItemSpearBase
				)
				|| //Greatswords
				(
						animatable.getMainHandItem().getItem() instanceof ItemGreatSword || animatable.getOffhandItem().getItem() instanceof ItemGreatSword
				);
	}
	
	public default <E extends AbstractEntityCQR & IAnimatableCQR> void registerControllers(E animatable, ControllerRegistrar data) {
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

	public static String ANIM_NAME_PREFIX = "animation.biped.";

	public static String ANIM_NAME_IDLE = ANIM_NAME_PREFIX + "idle";

	default <E extends AbstractEntityCQR & GeoEntity> PlayState predicateIdle(AnimationEvent<E> event) {
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_IDLE));
		}
		return PlayState.CONTINUE;
	}

	public static String ANIM_NAME_SITTING = ANIM_NAME_PREFIX + "sit";
	public static String ANIM_NAME_SNEAKING = ANIM_NAME_PREFIX + "body.sneak";

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

	public static String ANIM_NAME_BLOCKING_LEFT = ANIM_NAME_PREFIX + "arms.left.block";
	public static String ANIM_NAME_BLOCKING_RIGHT = ANIM_NAME_PREFIX + "arms.right.block";

	public default <E extends AbstractEntityCQR & IAnimatableCQR> InteractionHand getLeftHand(E animatable) {
		return animatable.isLeftHanded() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	public default <E extends AbstractEntityCQR & IAnimatableCQR> InteractionHand getRightHand(E animatable) {
		return !animatable.isLeftHanded() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateRightArmSwing(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		return animatable.predicateHandSwing(animatable.getRightHand(animatable), false, event);
	}

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateLeftArmSwing(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		return animatable.predicateHandSwing(animatable.getLeftHand(animatable), true, event);
	}

	public static String ANIM_NAME_SWING_NORMAL_LEFT = ANIM_NAME_PREFIX + "arms.left.item-use";
	public static String ANIM_NAME_SWING_NORMAL_RIGHT = ANIM_NAME_PREFIX + "arms.right.item-use";
	
	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateHandSwing(InteractionHand hand, boolean leftHand, AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		if (!animatable.isTwoHandedAnimationRunning(animatable)) {
			if (animatable.isSwinging(hand, event)) {
				ItemStack handItemStack = animatable.getItemInHand(hand);
				/*if(event.getController().getAnimationState() != AnimationState.Running) {
					event.getController().clearAnimationCache();
					event.getController().markNeedsReload();
				}*/
				if (!handItemStack.isEmpty()) {
					if (handItemStack.getItem().getUseAnimation(handItemStack) == UseAnim.EAT || handItemStack.getItem().getUseAnimation(handItemStack) == UseAnim.DRINK) {
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

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateHandPose(InteractionHand hand, boolean leftHand, AnimationEvent<E> event) {
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

	public static String ANIM_NAME_SPELLCASTING = ANIM_NAME_PREFIX + "arms.cast-spell";
	public static String ANIM_NAME_WALKING_ARMS = ANIM_NAME_PREFIX + "arms.walk";

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
		} else if(cqrIsWalking(event)) {
			//event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_WALKING_ARMS));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	public static String ANIM_NAME_SPEAR_POSE_LEFT = ANIM_NAME_PREFIX + "arms.left.spear";
	public static String ANIM_NAME_SPEAR_POSE_RIGHT = ANIM_NAME_PREFIX + "arms.right.spear";

	public static String ANIM_NAME_FIREARM_POSE_LEFT = ANIM_NAME_PREFIX + "arms.left.firearm";
	public static String ANIM_NAME_FIREARM_POSE_RIGHT = ANIM_NAME_PREFIX + "arms.right.firearm";
	
	public static String ANIM_NAME_FIREARM_SMALL_POSE_LEFT = ANIM_NAME_PREFIX + "arms.left.firearm-small";
	public static String ANIM_NAME_FIREARM_SMALL_POSE_RIGHT = ANIM_NAME_PREFIX + "arms.right.firearm-small";

	public static String ANIM_NAME_GREATSWORD_POSE = ANIM_NAME_PREFIX + "arms.greatsword";

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
		else if (item.getUseAnimation(itemStack) == UseAnim.SPEAR || item instanceof ItemSpearBase) {
			// Yes this is for tridents but we can use it anyway
			// Spear
			event.getController().setAnimation(new AnimationBuilder().loop(leftHanded ? ANIM_NAME_SPEAR_POSE_LEFT : ANIM_NAME_SPEAR_POSE_RIGHT));
			return Optional.of(PlayState.CONTINUE);
		}
		// If item instanceof Firearm/Bow/Crossbow => firearm animation
		else if ((item instanceof IFireArmTwoHanded) || item.getUseAnimation(itemStack) == UseAnim.BOW || item.getUseAnimation(itemStack) == UseAnim.CROSSBOW) {
			// Firearm
			event.getController().setAnimation(new AnimationBuilder().loop(leftHanded ? ANIM_NAME_FIREARM_POSE_LEFT : ANIM_NAME_FIREARM_POSE_RIGHT));
			return Optional.of(PlayState.CONTINUE);
		}
		return Optional.empty();
	}

	public static String ANIM_NAME_GREATSWORD_SWING = ANIM_NAME_PREFIX + "arms.attack-greatsword";
	public static String ANIM_NAME_SPEAR_SWING = ANIM_NAME_PREFIX + "arms.attack-spear";

	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateTwoHandedSwing(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		if (animatable.isTwoHandedAnimationRunning(animatable) && animatable.isSwinging(event)) {
			// Check for greatsword & spear and play their animations
			if (animatable.getMainHandItem().getItem().getUseAnimation(animatable.getMainHandItem()) == UseAnim.SPEAR || animatable.getOffhandItem().getItem().getUseAnimation(animatable.getOffhandItem()) == UseAnim.SPEAR) {
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
	
	public static String ANIM_NAME_WALKING = ANIM_NAME_PREFIX + "legs.walk";
	
	public static <E extends MobEntity & IAnimatable> boolean cqrIsWalking(AnimationEvent<E> event) {
		return (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F));
	}
	
	default <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateWalking(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_WALKING));
			event.getController().setAnimationSpeed(animatable.getAttributeValue(Attributes.MOVEMENT_SPEED) * 6.0);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	public <E extends AbstractEntityCQR & IAnimatableCQR> boolean isSwinging(InteractionHand hand, AnimationEvent<E> event);
	public default <E extends AbstractEntityCQR & IAnimatableCQR> boolean isSwinging(AnimationEvent<E> event) {
		E animatable = event.getAnimatable();
		if(animatable instanceof Mob) {
			return ((Mob)animatable).swinging;
		}
		return animatable.isSwinging(InteractionHand.MAIN_HAND, event);
	}

	public boolean isSpinToWinActive();

	public boolean isSpellCasting();
	
}