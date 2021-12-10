package team.cqr.cqrepoured.item.spear;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.item.sword.ItemCQRWeapon;
import team.cqr.cqrepoured.util.ItemUtil;

/**
 * Copyright (c) 20.12.2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ItemSpearBase extends ItemCQRWeapon {

	private static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("95dd73a8-c715-42f9-8f6d-abf5e40fa3cd");
	private static final float SPECIAL_REACH_MULTIPLIER = 1.5F;
	private final double reachDistanceBonus;

	public ItemSpearBase(ToolMaterial material) {
		super(material, CQRConfig.materials.toolMaterials.spearAttackDamageBonus, CQRConfig.materials.toolMaterials.spearAttackSpeedBonus);
		this.reachDistanceBonus = CQRConfig.materials.toolMaterials.spearReachDistanceBonus;
	}

	public double getReach() {
		return this.reachDistanceBonus;
	}

	public double getReachExtended() {
		return this.reachDistanceBonus * SPECIAL_REACH_MULTIPLIER;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		ItemUtil.attackTarget(stack, player, entity, false, 0.0F, 1.0F, true, 1.0F, 0.0F, 0.25D, 0.25D, 0.2F);
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(REACH_DISTANCE_MODIFIER, "Weapon modifier", this.reachDistanceBonus, 0));
		}

		return multimap;
	}

	// Makes the right click a "charge attack" action
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;

			if (!worldIn.isRemote) {
				Vec3d vec1 = player.getPositionEyes(1.0F);
				Vec3d vec2 = player.getLookVec();
				double reachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
				float charge = Math.min((float) player.getItemInUseMaxCount() / (float) 40, 1.0F);

				for (EntityLivingBase entity : this.getEntities(worldIn, EntityLivingBase.class, player, vec1, vec2, reachDistance, null)) {
					// TODO apply enchantments
					entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (1.0F + this.getAttackDamage()) * charge);
				}

				Vec3d vec3 = vec1.add(new Vec3d(0.0D, -0.5D, 0.0D).rotatePitch((float) Math.toRadians(-player.rotationPitch)))
						.add(new Vec3d(-0.4D, 0.0D, 0.0D).rotateYaw((float) Math.toRadians(-player.rotationYaw)));
				for (double d = reachDistance; d >= 0.0D; d--) {
					Vec3d vec4 = vec3.add(vec2.scale(d));
					((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, vec4.x, vec4.y, vec4.z, 1, 0.05D, 0.05D, 0.05D, 0.0D);
				}

				player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F,
						1.0F);
				player.getCooldownTracker().setCooldown(stack.getItem(), 200);
			} else {
				player.swingArm(EnumHand.MAIN_HAND);
			}
		}
	}

	private <T extends Entity> List<T> getEntities(World world, Class<T> entityClass, @Nullable T toIgnore, Vec3d vec1, Vec3d vec2, double range,
			@Nullable Predicate<T> predicate) {
		List<T> list = new ArrayList<>();
		Vec3d vec3 = vec1.add(vec2.normalize().scale(range));
		RayTraceResult rayTraceResult1 = world.rayTraceBlocks(vec1, vec3, false, true, false);
		Vec3d vec4 = rayTraceResult1 != null ? rayTraceResult1.hitVec : vec3;
		AxisAlignedBB aabb1 = new AxisAlignedBB(vec1.x, vec1.y, vec1.z, vec4.x, vec4.y, vec4.z);

		for (T entity : world.getEntitiesWithinAABB(entityClass, aabb1, predicate)) {
			if (entity == toIgnore) {
				continue;
			}

			AxisAlignedBB aabb2 = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
			RayTraceResult rayTraceResult2 = aabb2.calculateIntercept(vec1, vec4);

			if (rayTraceResult2 != null) {
				list.add(entity);
			}
		}

		return list;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.spear_diamond.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

}
