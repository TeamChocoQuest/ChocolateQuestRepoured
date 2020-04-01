package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemSpawnEggCQR extends Item {

	private Class<? extends AbstractEntityCQR> entityClass;
	private String entityName;
	private ItemStack mainhand;
	private ItemStack offhand;
	private ArmorMaterial armor;

	public ItemSpawnEggCQR(Class<? extends AbstractEntityCQR> entityClass, String entityName, ItemStack mainhand, ItemStack offhand, ArmorMaterial armor) {
		this.entityClass = entityClass;
		this.entityName = entityName;
		this.mainhand = mainhand;
		this.offhand = offhand;
		this.armor = armor;
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			AbstractEntityCQR entity = null;
			try {
				entity = this.entityClass.getConstructor(World.class).newInstance(worldIn);
			} catch (Exception e) {
				CQRMain.logger.error("Failed to spawn entity from preset item!", e);
			}
			if (entity != null) {
				entity.onInitialSpawn(worldIn.getDifficultyForLocation(pos), null);
				BlockPos blockpos = pos.offset(facing);
				double d0 = this.getYOffset(worldIn, blockpos);
				entity.setPosition((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5D);
				this.setEquipment(entity);
				worldIn.spawnEntity(entity);
			}
			if (!player.isCreative()) {
				player.getHeldItem(hand).shrink(1);
			}
		}
		return EnumActionResult.SUCCESS;
	}

	protected double getYOffset(World world, BlockPos blockpos) {
		AxisAlignedBB aabb = new AxisAlignedBB(blockpos).expand(0.0D, -1.0D, 0.0D);
		List<AxisAlignedBB> list = world.getCollisionBoxes(null, aabb);

		if (list.isEmpty()) {
			return 0.0D;
		} else {
			double d0 = aabb.minY;

			for (AxisAlignedBB aabb1 : list) {
				d0 = Math.max(aabb1.maxY, d0);
			}

			return d0 - (double) blockpos.getY();
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocal("entity.cqr_" + this.entityName + ".name").trim() + " (" + this.mainhand.getDisplayName() + ", " + this.offhand.getDisplayName() + ", " + this.armor.name() + ")";
	}

	private void setEquipment(AbstractEntityCQR entity) {
		entity.setHeldItem(EnumHand.MAIN_HAND, this.mainhand.copy());
		entity.setHeldItem(EnumHand.OFF_HAND, this.offhand.copy());
		switch (this.armor) {
		case LEATHER:
			entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
			break;
		case GOLD:
			entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
			break;
		case CHAIN:
			entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
			break;
		case IRON:
			entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
			break;
		case DIAMOND:
			entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
			break;
		default:
			break;
		}
	}

	public static List<Item> getItemList(Class<? extends AbstractEntityCQR> entityClass, String entityName) {
		List<Item> itemList = new ArrayList<>();
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, ItemStack.EMPTY, ItemStack.EMPTY, ArmorMaterial.LEATHER));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.WOODEN_SWORD), ItemStack.EMPTY, ArmorMaterial.LEATHER));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.WOODEN_SWORD), new ItemStack(Items.SHIELD), ArmorMaterial.LEATHER));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.BOW), ItemStack.EMPTY, ArmorMaterial.LEATHER));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(ModItems.STAFF_HEALING), ItemStack.EMPTY, ArmorMaterial.LEATHER));

		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, ItemStack.EMPTY, ItemStack.EMPTY, ArmorMaterial.GOLD));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.GOLDEN_SWORD), ItemStack.EMPTY, ArmorMaterial.GOLD));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.GOLDEN_SWORD), new ItemStack(Items.SHIELD), ArmorMaterial.GOLD));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.BOW), ItemStack.EMPTY, ArmorMaterial.GOLD));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(ModItems.STAFF_HEALING), ItemStack.EMPTY, ArmorMaterial.GOLD));

		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, ItemStack.EMPTY, ItemStack.EMPTY, ArmorMaterial.CHAIN));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.STONE_SWORD), ItemStack.EMPTY, ArmorMaterial.CHAIN));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.STONE_SWORD), new ItemStack(Items.SHIELD), ArmorMaterial.CHAIN));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.BOW), ItemStack.EMPTY, ArmorMaterial.CHAIN));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(ModItems.STAFF_HEALING), ItemStack.EMPTY, ArmorMaterial.CHAIN));

		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, ItemStack.EMPTY, ItemStack.EMPTY, ArmorMaterial.IRON));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.IRON_SWORD), ItemStack.EMPTY, ArmorMaterial.IRON));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.IRON_SWORD), new ItemStack(Items.SHIELD), ArmorMaterial.IRON));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.BOW), ItemStack.EMPTY, ArmorMaterial.IRON));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(ModItems.STAFF_HEALING), ItemStack.EMPTY, ArmorMaterial.IRON));

		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, ItemStack.EMPTY, ItemStack.EMPTY, ArmorMaterial.DIAMOND));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.DIAMOND_SWORD), ItemStack.EMPTY, ArmorMaterial.DIAMOND));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.SHIELD), ArmorMaterial.DIAMOND));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.BOW), ItemStack.EMPTY, ArmorMaterial.DIAMOND));
		// itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(ModItems.STAFF_HEALING), ItemStack.EMPTY, ArmorMaterial.DIAMOND));
		return itemList;
	}

}
