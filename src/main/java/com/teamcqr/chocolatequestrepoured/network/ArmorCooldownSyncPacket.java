package com.teamcqr.chocolatequestrepoured.network;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilitySpecialArmor;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ArmorCooldownSyncPacket implements IMessage {

	private int[] cooldowns = new int[0];

	public ArmorCooldownSyncPacket() {

	}

	public ArmorCooldownSyncPacket(int[] cooldowns) {
		this.cooldowns = cooldowns;
	}

	public ArmorCooldownSyncPacket(EntityPlayer player, List<Capability<? extends CapabilitySpecialArmor>> capabilities) {
		this.cooldowns = new int[capabilities.size()];
		CapabilitySpecialArmor icapability;
		for (int i = 0; i < capabilities.size(); i++) {
			icapability = player.getCapability(capabilities.get(i), null);
			this.cooldowns[i] = icapability.getCooldown();
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		this.cooldowns = new int[size];
		for (int i = 0; i < size; i++) {
			this.cooldowns[i] = buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		int size = this.cooldowns.length;
		buf.writeInt(size);
		for (int i = 0; i < size; i++) {
			buf.writeInt(this.cooldowns[i]);
		}
	}

	public int[] getCooldowns() {
		return this.cooldowns;
	}

}
