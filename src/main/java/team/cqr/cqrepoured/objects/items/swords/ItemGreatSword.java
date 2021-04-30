package team.cqr.cqrepoured.objects.items.swords;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemGreatSword extends ItemCQRWeapon {

	private final float specialAttackDamage;
	private final int specialAttackCooldown;

	public ItemGreatSword(ToolMaterial material, float damage, int cooldown) {
		super(material, CQRConfig.materials.toolMaterials.greatSwordAttackDamageBonus, CQRConfig.materials.toolMaterials.greatSwordAttackSpeedBonus);
		this.specialAttackDamage = damage;
		this.specialAttackCooldown = cooldown;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		ItemUtil.attackTarget(stack, player, entity, false, 0.0F, 1.0F, true, 2.0F, 0.0F, 1.25D, 0.25D, 0.5F);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.great_sword.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		float range = 3F;
		double mx = entityLiving.posX - range;
		double my = entityLiving.posY - range;
		double mz = entityLiving.posZ - range;
		double max = entityLiving.posX + range;
		double may = entityLiving.posY + range;
		double maz = entityLiving.posZ + range;

		AxisAlignedBB bb = new AxisAlignedBB(mx, my, mz, max, may, maz);

		List<EntityLiving> entitiesInAABB = worldIn.getEntitiesWithinAABB(EntityLiving.class, bb);

		for (int i = 0; i < entitiesInAABB.size(); i++) {
			EntityLiving entityInAABB = entitiesInAABB.get(i);

			if (this.getMaxItemUseDuration(stack) - timeLeft <= 30) {
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), this.specialAttackDamage);
			}

			if (this.getMaxItemUseDuration(stack) - timeLeft > 30 && this.getMaxItemUseDuration(stack) - timeLeft <= 60) {
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), this.specialAttackDamage * 3);
			}

			if (this.getMaxItemUseDuration(stack) - timeLeft > 60) {
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), this.specialAttackDamage * 4);
			}
		}

		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;

			float x = (float) -Math.sin(Math.toRadians(player.rotationYaw));
			float z = (float) Math.cos(Math.toRadians(player.rotationYaw));
			float y = (float) -Math.sin(Math.toRadians(player.rotationPitch));
			x *= (1.0F - Math.abs(y));
			z *= (1.0F - Math.abs(y));

			if (player.onGround && this.getMaxItemUseDuration(stack) - timeLeft > 40) {
				player.posY += 0.1D;
				player.motionY += 0.35D;
			}

			player.getCooldownTracker().setCooldown(stack.getItem(), this.specialAttackCooldown);
			player.swingArm(EnumHand.MAIN_HAND);
			worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
			worldIn.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, player.posX + x, player.posY + y + 1.5D, player.posZ + z, 0D, 0D, 0D);
			stack.damageItem(1, player);
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

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
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote && entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityIn;

			if (player.getHeldItemMainhand() == stack && !player.getHeldItemOffhand().isEmpty()) {
				ItemStack stack1 = player.getHeldItemOffhand();
				player.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);

				if (!player.inventory.addItemStackToInventory(stack1)) {
					player.entityDropItem(stack1, 0.0F);
				}
			}
		}
	}

}
