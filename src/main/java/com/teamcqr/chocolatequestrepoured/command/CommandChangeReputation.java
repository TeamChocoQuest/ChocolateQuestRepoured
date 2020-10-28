package com.teamcqr.chocolatequestrepoured.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class CommandChangeReputation extends CommandBase {

	@Override
	public String getName() {
		return "cqr_set_reputation";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/cqr_set_reputation FACTION REPUTATION";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender.getCommandSenderEntity() == null || !(sender.getCommandSenderEntity() instanceof EntityPlayerMP)) {
			return;
		}
		if(args.length < 3) {
			return;
		}
		int score = -1;
		try {
			score = Integer.parseInt(args[2]);
			score = MathHelper.clamp(score, -1000, 1000);
		} catch(NumberFormatException nfe) {
			return;
		}
		CQRFaction faction = FactionRegistry.instance().getFactionInstance(args[1]);
		if(faction != null) {
			FactionRegistry.instance().setReputation(sender.getCommandSenderEntity().getPersistentID(), score, faction);
		}
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if(args.length == 1) {
			List<String> list = new ArrayList<>(FactionRegistry.instance().getLoadedFactions().size());
			FactionRegistry.instance().getLoadedFactions().forEach(new Consumer<CQRFaction>() {

				@Override
				public void accept(CQRFaction t) {
					list.add(t.getName());
				}});
			return list;
		}
		return Collections.emptyList();
	}

}
