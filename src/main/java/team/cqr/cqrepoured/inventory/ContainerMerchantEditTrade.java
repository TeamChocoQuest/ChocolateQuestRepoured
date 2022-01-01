package team.cqr.cqrepoured.inventory;

import java.util.stream.IntStream;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.trade.Trade;
import team.cqr.cqrepoured.entity.trade.TradeInput;
import team.cqr.cqrepoured.entity.trade.TraderOffer;
import team.cqr.cqrepoured.faction.EReputationState;
import team.cqr.cqrepoured.util.GuiHandler;

public class ContainerMerchantEditTrade extends Container implements IInteractable {

	private final AbstractEntityCQR entity;
	private final IInventory tradeInventory;

	public ContainerMerchantEditTrade(AbstractEntityCQR entity, PlayerEntity player, int tradeIndex) {
		this.entity = entity;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 72 + j * 18, 60 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(player.inventory, k, 72 + k * 18, 118));
		}

		this.tradeInventory = new Inventory("", false, 5);
		this.addSlotToContainer(new Slot(this.tradeInventory, 0, 74, 12));
		this.addSlotToContainer(new Slot(this.tradeInventory, 1, 100, 12));
		this.addSlotToContainer(new Slot(this.tradeInventory, 2, 126, 12));
		this.addSlotToContainer(new Slot(this.tradeInventory, 3, 152, 12));
		this.addSlotToContainer(new Slot(this.tradeInventory, 4, 210, 12));

		Trade trade = entity.getTrades().get(tradeIndex);
		if (trade != null) {
			NonNullList<TradeInput> tradeInputs = trade.getInputItems();
			for (int i = 0; i < tradeInputs.size() && i < this.tradeInventory.getSizeInventory() - 1; i++) {
				this.tradeInventory.setInventorySlotContents(i, tradeInputs.get(i).getStack());
			}
			this.tradeInventory.setInventorySlotContents(this.tradeInventory.getSizeInventory() - 1, trade.getOutput());
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		if (!playerIn.isCreative()) {
			return false;
		}
		if (this.entity.isDead) {
			return false;
		}
		return playerIn.getDistanceSq(this.entity) <= 64.0D;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			ItemStack itemstack = itemstack1.copy();

			if (index > 35) {
				if (this.mergeItemStack(itemstack1, 0, 36, false)) {
					return itemstack;
				}
			} else {
				if (this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), false)) {
					return itemstack;
				}
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);

		/*
		 * if (!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP) playerIn).hasDisconnected())
		 * { for (int i = 0; i < 4; i++) {
		 * playerIn.dropItem(this.tradeInventory.removeStackFromSlot(i), false); } } else { for (int
		 * i = 0; i < 4; i++) { playerIn.inventory.placeItemBackInInventory(playerIn.world,
		 * this.tradeInventory.removeStackFromSlot(i)); } }
		 */
	}

	public ItemStack getOutput() {
		return this.tradeInventory.getStackInSlot(4);
	}

	public ItemStack[] getInput() {
		ItemStack[] input = new ItemStack[4];
		for (int i = 0; i < 4; i++) {
			input[i] = this.tradeInventory.getStackInSlot(i);
		}
		return input;
	}

	@Override
	public void onClickButton(PlayerEntity player, int button, ByteBuf extraData) {
		if (button == 0) {
			player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, player.world, this.entity.getEntityId(), 0, 0);
		} else if (button == 1) {
			int index = extraData.readInt();
			boolean[] ignoreMeta = new boolean[4];
			IntStream.range(0, ignoreMeta.length).forEach(i -> ignoreMeta[i] = extraData.readBoolean());
			boolean[] ignoreNBT = new boolean[4];
			IntStream.range(0, ignoreNBT.length).forEach(i -> ignoreNBT[i] = extraData.readBoolean());
			String reputationName = ByteBufUtils.readUTF8String(extraData);
			String advancementName = ByteBufUtils.readUTF8String(extraData);
			boolean stock = extraData.readBoolean();
			int restock = extraData.readInt();
			int inStock = extraData.readInt();
			int maxStock = extraData.readInt();

			TraderOffer trades = this.entity.getTrades();
			int reputation = this.getRequriedReputation(reputationName);
			ResourceLocation advancement = this.getRequiredAdvancement((ServerWorld) player.world, advancementName);
			ItemStack output = this.getOutput();
			TradeInput[] input = this.getTradeInput(this.getInput(), ignoreMeta, ignoreNBT);
			Trade trade = new Trade(trades, reputation, advancement, stock, restock, inStock, maxStock, output, input);

			this.entity.getTrades().editTrade(index, trade);
			player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, player.world, this.entity.getEntityId(), 0, 0);
		}
	}

	private TradeInput[] getTradeInput(ItemStack[] stacks, boolean[] ignoreMeta, boolean[] ignoreNBT) {
		TradeInput[] input = new TradeInput[stacks.length];
		for (int i = 0; i < input.length; i++) {
			input[i] = new TradeInput(stacks[i], i < ignoreMeta.length && ignoreMeta[i], i < ignoreNBT.length && ignoreNBT[i]);
		}
		return input;
	}

	private int getRequriedReputation(String reputation) {
		try {
			EReputationState reputationState = EReputationState.valueOf(reputation.toUpperCase());
			if (reputationState != null) {
				return reputationState.getValue();
			}
		} catch (Exception e) {
			// ignore
		}
		return Integer.MIN_VALUE;
	}

	@Nullable
	private ResourceLocation getRequiredAdvancement(ServerWorld world, String advancement) {
		ResourceLocation requiredAdvancement = new ResourceLocation(advancement);
		if (world.getAdvancementManager().getAdvancement(requiredAdvancement) != null) {
			return requiredAdvancement;
		}
		return null;
	}

}
