package com.teamcqr.chocolatequestrepoured.objects.items.guns;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.init.ModSounds;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMusketKnife extends ItemSword {

	public ItemMusketKnife(ToolMaterial material) {
		super(material);
		setMaxDamage(300);
		setMaxStackSize(1);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		replaceModifier(modifiers, SharedMonsterAttributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, -0.8F);
		return modifiers;
	}

	protected void replaceModifier(Multimap<String, AttributeModifier> modifierMultimap, IAttribute attribute, UUID id,
			double value) {
		Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute.getName());
		Optional<AttributeModifier> modifierOptional = modifiers.stream()
				.filter(attributeModifier -> attributeModifier.getID().equals(id)).findFirst();

		if (modifierOptional.isPresent()) {
			AttributeModifier modifier = modifierOptional.get();
			modifiers.remove(modifier);
			modifiers.add(new AttributeModifier(modifier.getID(), modifier.getName(), modifier.getAmount() + value,
					modifier.getOperation()));
		}
	}

	/*
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.NONE;
	}
	*/

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.BLUE + "7.5 " + I18n.format("description.bullet_damage.name"));
		tooltip.add(TextFormatting.RED + "-60 " + I18n.format("description.fire_rate.name"));
		tooltip.add(TextFormatting.RED + "-10" + "% " + I18n.format("description.accuracy.name"));
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.gun.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		boolean flag = !findAmmo(playerIn).isEmpty();

		if (!playerIn.capabilities.isCreativeMode && !flag && getBulletStack(stack, playerIn) == ItemStack.EMPTY) {
			if (flag) {
				shoot(stack, worldIn, playerIn);
			}
			return flag ? new ActionResult(EnumActionResult.PASS, stack)
					: new ActionResult(EnumActionResult.FAIL, stack);
		}

		else {
			shoot(stack, worldIn, playerIn);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
	}

	public void shoot(ItemStack stack, World worldIn, EntityPlayer player) {
		boolean flag = player.capabilities.isCreativeMode;
		ItemStack itemstack = findAmmo(player);

		if (!itemstack.isEmpty() || flag) {
			if (!worldIn.isRemote) {
				if (flag && itemstack.isEmpty()) {
					ProjectileBullet bulletE = new ProjectileBullet(worldIn, player, 1);
					bulletE.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F, 2F);
					player.getCooldownTracker().setCooldown(player.getHeldItem(player.getActiveHand()).getItem(), 30);
					worldIn.spawnEntity(bulletE);
				} else {
					ProjectileBullet bulletE = new ProjectileBullet(worldIn, player, getBulletType(itemstack));
					bulletE.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F, 2F);
					player.getCooldownTracker().setCooldown(player.getHeldItem(player.getActiveHand()).getItem(), 30);
					worldIn.spawnEntity(bulletE);
					stack.damageItem(1, player);
				}
			}

			worldIn.playSound(player.posX, player.posY, player.posZ, ModSounds.GUN_SHOOT, SoundCategory.MASTER,
					1.0F, 1.0F, false);
			player.rotationPitch -= worldIn.rand.nextFloat() * 10;

			if (!flag) {
				itemstack.shrink(1);

				if (itemstack.isEmpty()) {
					player.inventory.deleteStack(itemstack);
				}
			}
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		if (!worldIn.isRemote) {
			if (entityIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityIn;

				if (player.getHeldItemMainhand() == stack)
				{
					if (!player.getHeldItemOffhand().isEmpty()) {
						if (!player.inventory.addItemStackToInventory(player.getHeldItemOffhand())) {
							player.entityDropItem(player.getHeldItemOffhand(), 0F);
						}

						if (!player.capabilities.isCreativeMode) {
							player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
						}
					}
				}
			}
		}
	}

	/*
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			boolean flag = player.capabilities.isCreativeMode;
			ItemStack itemstack = findAmmo(player);

			if (!itemstack.isEmpty() || flag) {
				if (!worldIn.isRemote) {
					if (flag && itemstack.isEmpty()) {
						ProjectileBullet bulletE = new ProjectileBullet(worldIn, player, 1);
						bulletE.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F, 2F);
						player.getCooldownTracker().setCooldown(player.getHeldItem(player.getActiveHand()).getItem(),
								30);
						worldIn.spawnEntity(bulletE);
					} else {
						ProjectileBullet bulletE = new ProjectileBullet(worldIn, player, getBulletType(itemstack));
						bulletE.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F, 2F);
						player.getCooldownTracker().setCooldown(player.getHeldItem(player.getActiveHand()).getItem(),
								30);
						worldIn.spawnEntity(bulletE);
						stack.damageItem(1, player);
					}
				}

				worldIn.playSound(player.posX, player.posY, player.posZ, SoundsHandler.GUN_SHOOT, SoundCategory.MASTER,
						1.0F, 1.0F, false);
				entityLiving.rotationPitch -= worldIn.rand.nextFloat() * 10;

				if (!flag) {
					itemstack.shrink(1);

					if (itemstack.isEmpty()) {
						player.inventory.deleteStack(itemstack);
					}
				}
			}
		}
	}
	*/

	protected boolean isBullet(ItemStack stack) {
		return stack.getItem() instanceof ItemBullet;
	}

	protected ItemStack findAmmo(EntityPlayer player) {
		if (isBullet(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		} else if (isBullet(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		} else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (isBullet(itemstack)) {
					return itemstack;
				}
			}

			return ItemStack.EMPTY;
		}
	}

	protected ItemStack getBulletStack(ItemStack stack, EntityPlayer player) {
		if (stack.getItem() == ModItems.BULLET_IRON) {
			return new ItemStack(ModItems.BULLET_IRON);
		}

		if (stack.getItem() == ModItems.BULLET_GOLD) {
			return new ItemStack(ModItems.BULLET_GOLD);
		}

		if (stack.getItem() == ModItems.BULLET_DIAMOND) {
			return new ItemStack(ModItems.BULLET_DIAMOND);
		}

		if (stack.getItem() == ModItems.BULLET_FIRE) {
			return new ItemStack(ModItems.BULLET_FIRE);
		} else {
			System.out.println("IT'S A BUG!!!! IF YOU SEE THIS REPORT IT TO MOD'S AUTHOR");
			return ItemStack.EMPTY; // #SHOULD NEVER HAPPEN
		}
	}

	protected int getBulletType(ItemStack stack) {
		if (stack.getItem() == ModItems.BULLET_IRON) {
			return 1;
		}

		if (stack.getItem() == ModItems.BULLET_GOLD) {
			return 2;
		}

		if (stack.getItem() == ModItems.BULLET_DIAMOND) {
			return 3;
		}

		if (stack.getItem() == ModItems.BULLET_FIRE) {
			return 4;
		}

		else {
			System.out.println("IT'S A BUG!!!! IF YOU SEE THIS REPORT IT TO MOD'S AUTHOR");
			return 0; // #SHOULD NEVER HAPPEN
		}
	}

}
