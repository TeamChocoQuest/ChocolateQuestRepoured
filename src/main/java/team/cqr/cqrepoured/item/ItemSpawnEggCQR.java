package team.cqr.cqrepoured.item;

import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ItemSpawnEggCQR extends Item {

	private Class<? extends AbstractEntityCQR> entityClass;
	private String entityName;
	private ItemStack mainhand;
	private ItemStack offhand;
	private Material armor;

	public ItemSpawnEggCQR(Class<? extends AbstractEntityCQR> entityClass, String entityName, ItemStack mainhand, ItemStack offhand, Material armor) {
		this.entityClass = entityClass;
		this.entityName = entityName;
		this.mainhand = mainhand;
		this.offhand = offhand;
		this.armor = armor;
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResultType onItemUse(PlayerEntity player, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
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
				entity.setPosition(blockpos.getX() + 0.5D, blockpos.getY() + d0, blockpos.getZ() + 0.5D);
				this.setEquipment(entity);
				worldIn.spawnEntity(entity);
			}
			if (!player.isCreative()) {
				player.getHeldItem(hand).shrink(1);
			}
		}
		return ActionResultType.SUCCESS;
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

			return d0 - blockpos.getY();
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocal("entity.cqr_" + this.entityName + ".name").trim() + " (" + this.mainhand.getDisplayName() + ", " + this.offhand.getDisplayName() + ", " + this.armor.name() + ")";
	}

	private void setEquipment(AbstractEntityCQR entity) {
		entity.setHeldItem(Hand.MAIN_HAND, this.mainhand.copy());
		entity.setHeldItem(Hand.OFF_HAND, this.offhand.copy());
		switch (this.armor) {
		case LEATHER:
			entity.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.LEATHER_BOOTS));
			entity.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
			entity.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
			entity.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
			break;
		case GOLD:
			entity.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.GOLDEN_BOOTS));
			entity.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
			entity.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
			entity.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.GOLDEN_HELMET));
			break;
		case CHAIN:
			entity.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
			entity.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
			entity.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
			entity.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
			break;
		case IRON:
			entity.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
			entity.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
			entity.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			entity.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
			break;
		case DIAMOND:
			entity.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.DIAMOND_BOOTS));
			entity.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
			entity.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
			entity.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.DIAMOND_HELMET));
			break;
		default:
			break;
		}
	}

	public static List<Item> getItemList(Class<? extends AbstractEntityCQR> entityClass, String entityName) {
		List<Item> itemList = new ArrayList<>();
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.WOODEN_SWORD), ItemStack.EMPTY, Material.LEATHER));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.GOLDEN_SWORD), ItemStack.EMPTY, Material.GOLD));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.STONE_SWORD), ItemStack.EMPTY, Material.CHAIN));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.IRON_SWORD), ItemStack.EMPTY, Material.IRON));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.DIAMOND_SWORD), ItemStack.EMPTY, Material.DIAMOND));
		return itemList;
	}

}
