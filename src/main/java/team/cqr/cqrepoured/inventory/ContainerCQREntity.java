package team.cqr.cqrepoured.inventory;

import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.item.ItemBadge;
import team.cqr.cqrepoured.item.ItemPotionHealing;
import team.cqr.cqrepoured.item.gun.ItemBullet;

import java.util.Objects;

public class ContainerCQREntity extends Container {

	private final AbstractEntityCQR entity;
	
	public static final ResourceLocation EMPTY_SLOT_MAIN_HAND = CQRMain.prefix("items/empty_slot_sword");
	public static final ResourceLocation EMPTY_SLOT_OFF_HAND = new ResourceLocation("item/empty_armor_slot_shield");
	public static final ResourceLocation EMPTY_SLOT_POTION = CQRMain.prefix("items/empty_slot_potion");
	public static final ResourceLocation EMPTY_SLOT_BADGE = CQRMain.prefix("items/empty_slot_badge");
	public static final ResourceLocation EMPTY_SLOT_ARROW = CQRMain.prefix("items/empty_slot_arrow");

	public ContainerCQREntity(final int containerID, PlayerInventory playerInv, PacketBuffer data)
	{
		this(containerID, playerInv, getEntity(playerInv, data));
	}
	
	public AbstractEntityCQR getEntity() {
		return this.entity;
	}
	
	private static AbstractEntityCQR getEntity(final PlayerInventory playerInventory, final PacketBuffer data)
	{
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
		int entityID = data.readInt();

		if(playerInventory.player.level.getEntity(entityID) != null && playerInventory.player.level.getEntity(entityID) instanceof AbstractEntityCQR)
		{
			return (AbstractEntityCQR)playerInventory.player.level.getEntity(entityID);
		}
		throw new IllegalStateException("EntityID is not correct! " + entityID);
	}

	public ContainerCQREntity(final int containerID, PlayerInventory playerInv, AbstractEntityCQR entity) {
		super(CQRContainerTypes.CQR_ENTITY_EDITOR.get(), containerID);
		this.entity = entity;
		if(entity == null) {
			return;
		}
		LazyOptional<IItemHandler> lOpCap = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		IItemHandler inventory = null;
		if(lOpCap.isPresent()) {
			inventory = lOpCap.resolve().get();
		}
		LazyOptional<CapabilityExtraItemHandler> lOpCapTwo = entity.getCapability(CapabilityExtraItemHandlerProvider.EXTRA_ITEM_HANDLER, null);
		CapabilityExtraItemHandler extraInventory = null;
		if(lOpCapTwo.isPresent()) {
			extraInventory = lOpCapTwo.resolve().get();
		} else {
			throw new NullPointerException("Extra item handler capability is NOT present!");
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 48 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(playerInv, k, 8 + k * 18, 106));
		}

		//Boots
		this.addSlot(new SlotItemHandler(inventory, 2, 107, 8) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem().canEquip(stack, EquipmentSlotType.FEET, entity);
			}

			@Override
			public boolean mayPickup(PlayerEntity playerIn) {
				ItemStack itemstack = this.getItem();
				return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.mayPickup(playerIn);
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS, PlayerContainer.EMPTY_ARMOR_SLOT_BOOTS);
			}

		});
		//Legs
		this.addSlot(new SlotItemHandler(inventory, 3, 89, 8) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem().canEquip(stack, EquipmentSlotType.LEGS, entity);
			}

			@Override
			public boolean mayPickup(PlayerEntity playerIn) {
				ItemStack itemstack = this.getItem();
				return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.mayPickup(playerIn);
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS, PlayerContainer.EMPTY_ARMOR_SLOT_LEGGINGS);
			}
		});
		this.addSlot(new SlotItemHandler(inventory, 4, 71, 8) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem().canEquip(stack, EquipmentSlotType.CHEST, entity);
			}

			@Override
			public boolean mayPickup(PlayerEntity playerIn) {
				ItemStack itemstack = this.getItem();
				return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.mayPickup(playerIn);
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS, PlayerContainer.EMPTY_ARMOR_SLOT_CHESTPLATE);
			}
		});
		//helmet
		this.addSlot(new SlotItemHandler(inventory, 5, 53, 8) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}

			@Override
			public boolean mayPickup(PlayerEntity playerIn) {
				ItemStack itemstack = this.getItem();
				return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.mayPickup(playerIn);
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS, PlayerContainer.EMPTY_ARMOR_SLOT_HELMET);
			}
		});
		//mainhand
		this.addSlot(new SlotItemHandler(inventory, 0, 71, 26) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS, EMPTY_SLOT_MAIN_HAND);
			}
		});
		//offhand
		this.addSlot(new SlotItemHandler(inventory, 1, 89, 26) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS, EMPTY_SLOT_OFF_HAND);
			}
		});
		//potion
		this.addSlot(new SlotItemHandler(extraInventory, 0, 53, 26) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() instanceof ItemPotionHealing;
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS, EMPTY_SLOT_POTION);
			}
		});
		//badge
		this.addSlot(new SlotItemHandler(extraInventory, 1, 107, 26) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return playerInv.player.isCreative() && stack.getItem() instanceof ItemBadge;
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS, EMPTY_SLOT_BADGE);
			}
		});
		//arrow
		this.addSlot(new SlotItemHandler(extraInventory, 2, 125, 26) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() instanceof ArrowItem || stack.getItem() instanceof ItemBullet;
			}

			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS, EMPTY_SLOT_ARROW);
			}
		});
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		if (!playerIn.isCreative() && this.entity.getLeader() != playerIn) {
			return false;
		}
		if (!this.entity.isAlive()) {
			return false;
		}
		return playerIn.distanceToSqr(this.entity) <= 64.0D;
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			ItemStack itemstack = itemstack1.copy();

			// custom slot priority: FEET -> LEGS -> CHEST -> HEAD (helmet) -> POTION -> BADGE -> ARROW -> OFFHAND (shield) ->
			// MAINHAND -> OFFHAND (other) -> HEAD (other)
			if (index > 35) {
				if (this.moveItemStackTo(itemstack1, 0, 36, false)) {
					return itemstack;
				}
			} else {
				if (this.moveItemStackTo(itemstack1, 36, 39, false)) {
					return itemstack;
				} else if (this.isHelmet(itemstack1) && this.moveItemStackTo(itemstack1, 39, 40, false)) {
					return itemstack;
				} else if (this.moveItemStackTo(itemstack1, 42, 45, false)) {
					return itemstack;
				} else if (itemstack1.getItem() instanceof ShieldItem && this.moveItemStackTo(itemstack1, 41, 42, false)) {
					return itemstack;
				} else if (this.moveItemStackTo(itemstack1, 40, 42, false)) {
					return itemstack;
				} else {
					if (index > 26) {
						if (this.moveItemStackTo(itemstack1, 0, 27, false)) {
							return ItemStack.EMPTY;
						}
					} else {
						if (this.moveItemStackTo(itemstack1, 27, 36, false)) {
							return ItemStack.EMPTY;
						}
					}
				}
			}
		}

		return ItemStack.EMPTY;
	}

	private boolean isHelmet(ItemStack stack) {
		return (stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlotType.HEAD) || stack.getItem().getEquipmentSlot(stack) == EquipmentSlotType.HEAD;
	}

}
