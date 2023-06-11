package team.cqr.cqrepoured.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.SpawnReason;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

@SuppressWarnings("deprecation")
public class ItemSpawnEggCQR extends Item {

	private Class<? extends AbstractEntityCQR> entityClass;
	private String entityName;
	private ItemStack mainhand;
	private ItemStack offhand;
	private ArmorMaterial armor;

	public ItemSpawnEggCQR(Class<? extends AbstractEntityCQR> entityClass, String entityName, ItemStack mainhand, ItemStack offhand, ArmorMaterial armor, Properties properties) {
		super(properties.stacksTo(1));
		this.entityClass = entityClass;
		this.entityName = entityName;
		this.mainhand = mainhand;
		this.offhand = offhand;
		this.armor = armor;
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		if (!context.getLevel().isClientSide) {
			AbstractEntityCQR entity = null;
			try {
				entity = this.entityClass.getConstructor(World.class).newInstance(context.getLevel());
			} catch (Exception e) {
				CQRMain.logger.error("Failed to spawn entity from preset item!", e);
			}
			if (entity != null) {
				entity.finalizeSpawn((ServerWorld) context.getLevel(), context.getLevel().getCurrentDifficultyAt(context.getClickedPos()), SpawnReason.SPAWN_EGG, null, null );
				BlockPos blockpos = context.getClickedPos().relative(context.getClickedFace());
				double d0 = this.getYOffset(context.getLevel(), blockpos);
				entity.setPos(blockpos.getX() + 0.5D, blockpos.getY() + d0, blockpos.getZ() + 0.5D);
				this.setEquipment(entity);
				context.getLevel().addFreshEntity(entity);
			}
			if (!context.getPlayer().isCreative()) {
				context.getPlayer().getItemInHand(context.getHand()).shrink(1);
			}
		}
		return ActionResultType.SUCCESS;
	}

	protected double getYOffset(World world, BlockPos blockpos) {
		AxisAlignedBB aabb = new AxisAlignedBB(blockpos).expandTowards(0.0D, -1.0D, 0.0D);
		List<VoxelShape> list = world.getBlockCollisions(null, aabb).collect(Collectors.toList());

		if (list.isEmpty()) {
			return 0.0D;
		} else {
			double d0 = aabb.minY;

			for (VoxelShape aabb1 : list) {
				d0 = Math.max(aabb1.max(Direction.Axis.Y), d0);
			}

			return d0 - blockpos.getY();
		}
	}

	@Override
	public ITextComponent getName(ItemStack stack) {
		return new TranslationTextComponent("entity.cqr_" + this.entityName + ".name").append(new StringTextComponent(" (" + this.mainhand.getDisplayName() + ", " + this.offhand.getDisplayName() + ", " + this.armor.name() + ")"));
	}

	private void setEquipment(AbstractEntityCQR entity) {
		entity.setItemInHand(Hand.MAIN_HAND, this.mainhand.copy());
		entity.setItemInHand(Hand.OFF_HAND, this.offhand.copy());
		switch (this.armor) {
		case LEATHER:
			entity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.LEATHER_BOOTS));
			entity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
			entity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
			entity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
			break;
		case GOLD:
			entity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.GOLDEN_BOOTS));
			entity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
			entity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
			entity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.GOLDEN_HELMET));
			break;
		case CHAIN:
			entity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
			entity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
			entity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
			entity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
			break;
		case IRON:
			entity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
			entity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
			entity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			entity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
			break;
		case DIAMOND:
			entity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.DIAMOND_BOOTS));
			entity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
			entity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
			entity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.DIAMOND_HELMET));
			break;
		default:
			break;
		}
	}

	public static List<Item> getItemList(Class<? extends AbstractEntityCQR> entityClass, String entityName) {
		List<Item> itemList = new ArrayList<>();
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.WOODEN_SWORD), ItemStack.EMPTY, ArmorMaterial.LEATHER, new Properties()));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.GOLDEN_SWORD), ItemStack.EMPTY, ArmorMaterial.GOLD, new Properties()));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.STONE_SWORD), ItemStack.EMPTY, ArmorMaterial.CHAIN, new Properties()));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.IRON_SWORD), ItemStack.EMPTY, ArmorMaterial.IRON, new Properties()));
		itemList.add(new ItemSpawnEggCQR(entityClass, entityName, new ItemStack(Items.DIAMOND_SWORD), ItemStack.EMPTY, ArmorMaterial.DIAMOND, new Properties()));
		return itemList;
	}

}
