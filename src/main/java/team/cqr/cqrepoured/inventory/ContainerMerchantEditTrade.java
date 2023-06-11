package team.cqr.cqrepoured.inventory;

import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.trade.Trade;
import team.cqr.cqrepoured.entity.trade.TradeInput;
import team.cqr.cqrepoured.entity.trade.TraderOffer;
import team.cqr.cqrepoured.faction.EReputationState;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.network.CQRNetworkHooks;

public class ContainerMerchantEditTrade extends Container implements IInteractable {

	private final AbstractEntityCQR entity;
	private final Container tradeInventory;
	private final int tradeIndex;

	public int getTradeIndex() {
		return this.tradeIndex;
	}
	
	public ContainerMerchantEditTrade(int id, Inventory playerInv, FriendlyByteBuf buf) {
		this(CQRContainerTypes.MERCHANT_EDIT_TRADE.get(), id, ContainerMerchant.tryGetCQREntity(buf), playerInv.player, tryGetTradeIndex(buf));
	}

	private static int tryGetTradeIndex(FriendlyByteBuf data) {
		int id = data.readInt();
		return id;
	}

	public ContainerMerchantEditTrade(MenuType<?> type, final int containerID, AbstractEntityCQR entity, Player player, int tradeIndex) {
		super(type, containerID);
		this.entity = entity;
		this.tradeIndex = tradeIndex;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(player.inventory, j + i * 9 + 9, 72 + j * 18, 60 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(player.inventory, k, 72 + k * 18, 118));
		}

		this.tradeInventory = new Inventory(/* "", false, */ 5);
		this.addSlot(new Slot(this.tradeInventory, 0, 74, 12));
		this.addSlot(new Slot(this.tradeInventory, 1, 100, 12));
		this.addSlot(new Slot(this.tradeInventory, 2, 126, 12));
		this.addSlot(new Slot(this.tradeInventory, 3, 152, 12));
		this.addSlot(new Slot(this.tradeInventory, 4, 210, 12));

		Trade trade = entity.getTrades().get(tradeIndex);
		if (trade != null) {
			NonNullList<TradeInput> tradeInputs = trade.getInputItems();
			for (int i = 0; i < tradeInputs.size() && i < this.tradeInventory.getContainerSize() - 1; i++) {
				this.tradeInventory.setItem(i, tradeInputs.get(i).getStack());
			}
			this.tradeInventory.setItem(this.tradeInventory.getContainerSize() - 1, trade.getOutput());
		}
	}

	@Override
	public boolean stillValid(Player playerIn) {
		if (!playerIn.isCreative()) {
			return false;
		}
		if (!this.entity.isAlive()) {
			return false;
		}
		return playerIn.distanceToSqr(this.entity) <= 64.0D;
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			ItemStack itemstack = itemstack1.copy();

			if (index > 35) {
				if (this.moveItemStackTo(itemstack1, 0, 36, false)) {
					return itemstack;
				}
			} else {
				if (this.moveItemStackTo(itemstack1, 36, this.slots.size(), false)) {
					return itemstack;
				}
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);

		/*
		 * if (!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP) playerIn).hasDisconnected()) { for (int i = 0; i < 4; i++) { playerIn.dropItem(this.tradeInventory.removeStackFromSlot(i), false); } } else { for (int
		 * i = 0; i < 4; i++) { playerIn.inventory.placeItemBackInInventory(playerIn.world, this.tradeInventory.removeStackFromSlot(i)); } }
		 */
	}

	public ItemStack getOutput() {
		return this.tradeInventory.getItem(4);
	}

	public ItemStack[] getInput() {
		ItemStack[] input = new ItemStack[4];
		for (int i = 0; i < 4; i++) {
			input[i] = this.tradeInventory.getItem(i);
		}
		return input;
	}

	@Override
	public void onClickButton(Player player, int button, FriendlyByteBuf extraData) {
		if (player instanceof ServerPlayer) {
			ServerPlayer spe = (ServerPlayer) player;
			if (button == 0) {
				// player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, player.level, this.entity.getId(), 0, 0);
				CQRNetworkHooks.openGUI(spe, this.entity.getDisplayName(), buf -> buf.writeInt(this.entity.getId()), CQRContainerTypes.MERCHANT.get());
			} else if (button == 1) {
				int index = extraData.readInt();
				boolean[] ignoreMeta = new boolean[4];
				IntStream.range(0, ignoreMeta.length).forEach(i -> ignoreMeta[i] = extraData.readBoolean());
				boolean[] ignoreNBT = new boolean[4];
				IntStream.range(0, ignoreNBT.length).forEach(i -> ignoreNBT[i] = extraData.readBoolean());
				String reputationName = extraData.readUtf();
				String advancementName = extraData.readUtf();
				boolean stock = extraData.readBoolean();
				int restock = extraData.readInt();
				int inStock = extraData.readInt();
				int maxStock = extraData.readInt();

				TraderOffer trades = this.entity.getTrades();
				int reputation = this.getRequriedReputation(reputationName);
				ResourceLocation advancement = this.getRequiredAdvancement((ServerLevel) player.level, advancementName);
				ItemStack output = this.getOutput();
				TradeInput[] input = this.getTradeInput(this.getInput(), ignoreMeta, ignoreNBT);
				Trade trade = new Trade(trades, reputation, advancement, stock, restock, inStock, maxStock, output, input);

				this.entity.getTrades().editTrade(index, trade);
				// player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, player.level, this.entity.getId(), 0, 0);
				CQRNetworkHooks.openGUI(spe, this.entity.getDisplayName(), buf -> buf.writeInt(this.entity.getId()), CQRContainerTypes.MERCHANT.get());
			}
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
	private ResourceLocation getRequiredAdvancement(ServerLevel world, String advancement) {
		ResourceLocation requiredAdvancement = new ResourceLocation(advancement);
		if (world.getServer().getAdvancements().getAdvancement(requiredAdvancement) != null) {
			return requiredAdvancement;
		}
		return null;
	}

	public AbstractEntityCQR getEntity() {
		return this.entity;
	}

}
