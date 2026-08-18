package com.example.chocolatequest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import com.example.chocolatequest.registry.ModBlockEntities;

public class TileEntityTable extends RandomizableContainerBlockEntity {

	private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
	private byte rotation = 0;

	public TileEntityTable(BlockPos pos, BlockState state) {
		super(ModBlockEntities.TABLE.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
		super.saveAdditional(pTag, provider);
		ContainerHelper.saveAllItems(pTag, this.items, provider);
		pTag.putByte("rotation", this.rotation);
	}
	
	@Override
	public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
		super.loadAdditional(pTag, provider);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(pTag, this.items, provider);
		this.rotation = pTag.getByte("rotation");
	}
	
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, provider);
		return tag;
	}

	public void setRotation(int rotation) {
		rotation = rotation % 16;
		if (rotation < 0) {
			rotation += 16;
		}

		this.rotation = (byte) rotation;
		this.setChanged();
	}

	public int getRotation() {
		return this.rotation;
	}

	public float getRotationInDegree() {
		return this.rotation * 22.5F;
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> pItemStacks) {
		this.items = pItemStacks;
	}

	@Override
	protected Component getDefaultName() {
		return Component.empty();
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return null;
	}
}
