package team.cqr.cqrepoured.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.trade.Trade;
import team.cqr.cqrepoured.entity.trade.TradeInput;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.util.CraftingHelper;

public class ContainerMerchant extends Container implements IInteractable {

	private final AbstractEntityCQR entity;
	private final InventoryMerchant merchantInventory;

	public ContainerMerchant(final int containerID, PlayerInventory playerInv, PacketBuffer data) {
		this(containerID, tryGetCQREntity(data), playerInv);
	}
	
	public static AbstractEntityCQR tryGetCQREntity(PacketBuffer data) {
		AbstractEntityCQR aecqr = null;
		if(data == null) {
			CQRMain.logger.debug("packetbuffer is empty, using proxy reference");
			aecqr = CQRMain.PROXY.getCurrentCQREntityInGUI();
		} else {
			try {
				int id = data.readInt();
				Entity ent = Minecraft.getInstance().level.getEntity(id);
				if (ent instanceof AbstractEntityCQR) {
					aecqr = (AbstractEntityCQR)ent;
				} else {
					aecqr = CQRMain.PROXY.getCurrentCQREntityInGUI();
				}
			} catch(IndexOutOfBoundsException ioobex) {
				CQRMain.logger.debug("Using proxy reference, packetbuffer is empty");
				aecqr = CQRMain.PROXY.getCurrentCQREntityInGUI();
			}
		}
		return aecqr;
	}

	public static ContainerMerchant fromNetwork(int id, PlayerInventory playerInv) {
		return new ContainerMerchant(id, null, playerInv);
	}
	
	public ContainerMerchant(final int containerID, AbstractEntityCQR entity, PlayerInventory playerInv) {
		this(CQRContainerTypes.MERCHANT.get(), containerID, entity, playerInv);
	}
	
	private ContainerMerchant(ContainerType<?> type, final int containerID, AbstractEntityCQR entity, PlayerInventory playerInv) {
		super(type, containerID);
		this.entity = entity;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 139 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(playerInv, k, 139 + k * 18, 142));
		}

		this.merchantInventory = new InventoryMerchant(entity, playerInv.player);
		this.addSlot(new Slot(this.merchantInventory, 0, 141, 37));
		this.addSlot(new Slot(this.merchantInventory, 1, 167, 37));
		this.addSlot(new Slot(this.merchantInventory, 2, 193, 37));
		this.addSlot(new Slot(this.merchantInventory, 3, 219, 37));
		this.addSlot(new SlotMerchantOutput(playerInv.player, this.merchantInventory, 4, 277, 37));
	}

	@Override
	public void slotsChanged(IInventory inventoryIn) {
		this.merchantInventory.resetTradeAndSlots();
		super.slotsChanged(inventoryIn);
	}

	public void setCurrentTradeIndex(int index) {
		this.merchantInventory.setCurrentTradeIndex(index);
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		if (!this.entity.isAlive()) {
			return false;
		}
		return playerIn.distanceToSqr(this.entity) <= 64.0D;
	}
	
	
	
	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
		return !(slotIn instanceof SlotMerchantOutput);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		ItemStack oldStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack newStack = slot.getItem();
			oldStack = newStack.copy();

			if (index == 40) {
				if (!this.moveItemStackTo(newStack, 0, 36, true)) {
					return ItemStack.EMPTY;
				}

				//Correct method?
				slot.onQuickCraft(newStack, oldStack);
			} else if (index > 35) {
				if (!this.moveItemStackTo(newStack, 0, 36, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(newStack, 36, 40, false)) {
				if (index > 26) {
					if (this.moveItemStackTo(newStack, 0, 27, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (this.moveItemStackTo(newStack, 27, 36, false)) {
						return ItemStack.EMPTY;
					}
				}
			}

			if (newStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (newStack.getCount() == oldStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, newStack);
		}

		return oldStack;
	}
	
	@Override
	public void removed(PlayerEntity playerIn) {
		super.removed(playerIn);

		if (!playerIn.isAlive() || playerIn instanceof ServerPlayerEntity && ((ServerPlayerEntity) playerIn).hasDisconnected()) {
			for (int i = 0; i < 4; i++) {
				playerIn.drop(this.merchantInventory.removeItemNoUpdate(i), false);
			}
		} else {
			for (int i = 0; i < 4; i++) {
				playerIn.inventory.placeItemBackInInventory(playerIn.level, this.merchantInventory.removeItemNoUpdate(i));
			}
		}
	}

	public void updateInputsForTrade(int tradeIndex) {
		Trade trade = this.entity.getTrades().get(tradeIndex);
		if (trade != null) {
			NonNullList<TradeInput> input = trade.getInputItems();

			for (int i = 0; i < 4; i++) {
				ItemStack stack = this.merchantInventory.getItem(i);
				if (!stack.isEmpty()) {
					if (!this.moveItemStackTo(stack, 0, 36, true)) {
						return;
					}

					this.merchantInventory.setItem(i, stack);
				}
			}

			for (int i = 0; i < 4 && i < input.size(); i++) {
				this.fillSlot(i, input.get(i));
			}
		}
	}

	private void fillSlot(int slotIndex, TradeInput input) {
		if (!input.getStack().isEmpty()) {
			for (int i = 0; i < 36; i++) {
				ItemStack stack1 = this.slots.get(i).getItem();
				if (!stack1.isEmpty() && CraftingHelper.areItemStacksEqualIgnoreCount(input.getStack(), stack1, input.ignoreMeta(), input.ignoreNBT())) {
					ItemStack stack2 = this.merchantInventory.getItem(slotIndex);
					int j = stack2.isEmpty() ? 0 : stack2.getCount();
					int k = Math.min(input.getStack().getMaxStackSize() - j, stack1.getCount());
					ItemStack stack3 = stack1.copy();
					int l = j + k;
					stack1.shrink(k);
					stack3.setCount(l);
					this.merchantInventory.setItem(slotIndex, stack3);
					if (l >= input.getStack().getMaxStackSize()) {
						break;
					}
				}
			}
		}

	}

	@Override
	public void onClickButton(PlayerEntity player, int button, PacketBuffer extraData) {
		if (button < 10) {
			if (button == 0) {
				// new trade
				//player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_EDIT_TRADE_GUI_ID, player.level, this.entity.getId(), this.entity.getTrades().size(), 0);
				player.openMenu(new INamedContainerProvider() {
					
					@Override
					public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
						//TODO: Use proper container-type
						return new ContainerMerchantEditTrade(getType(), p_createMenu_1_, ContainerMerchant.this.entity, player, ContainerMerchant.this.entity.getTrades().size());
					}
					
					@Override
					public ITextComponent getDisplayName() {
						return ContainerMerchant.this.entity.getDisplayName();
					}
				});
			}
		} else if (button < 20) {
			// select
			int index = extraData.readInt();
			this.setCurrentTradeIndex(index);
			this.updateInputsForTrade(index);
		} else if (button < 30) {
			// push up
			int index = extraData.readInt();
			int newIndex = index - 1;
			this.entity.getTrades().updateTradeIndex(index, newIndex);
		} else if (button < 40) {
			// push down
			int index = extraData.readInt();
			int newIndex = index + 1;
			this.entity.getTrades().updateTradeIndex(index, newIndex);
		} else if (button < 50) {
			// delete
			int index = extraData.readInt();
			this.entity.getTrades().deleteTrade(index);
		} else if (button < 60) {
			// edit
			int index = extraData.readInt();
			//player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_EDIT_TRADE_GUI_ID, player.level, this.entity.getId(), index, 0);
			player.openMenu(new INamedContainerProvider() {
				
				@Override
				public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
					//TODO: Use proper container-type
					return new ContainerMerchantEditTrade(getType(), p_createMenu_1_, ContainerMerchant.this.entity, player, index);
				}
				
				@Override
				public ITextComponent getDisplayName() {
					return ContainerMerchant.this.entity.getDisplayName();
				}
			});
		}
	}

	public void onTradesUpdated() {
		this.merchantInventory.resetTradeAndSlots();
	}

	public AbstractEntityCQR getMerchant() {
		return this.entity;
	}

}
