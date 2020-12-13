package team.cqr.cqrepoured.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.factions.FactionRegistry;

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
		if (sender.getCommandSenderEntity() == null || !(sender.getCommandSenderEntity() instanceof EntityPlayerMP) || sender.getEntityWorld() == null || sender.getEntityWorld().isRemote) {
			return;
		}
		if (args.length < 2) {
			return;
		}
		int score = -1;
		try {
			score = Integer.parseInt(args[1]);
			score = MathHelper.clamp(score, -1000, 1000);
		} catch (NumberFormatException nfe) {
			sender.sendMessage(new TextComponentString("The entered reputation (" + args[1] + ") is not a number!"));
			return;
		}
		CQRFaction faction = FactionRegistry.instance().getFactionInstance(args[0]);
		if (faction != null) {
			FactionRegistry.instance().changeReputationTo((EntityPlayerMP) sender.getCommandSenderEntity(), score, faction);
			sender.sendMessage(new TextComponentString("Changed " + sender.getDisplayName().getFormattedText() + "'s reputation towards faction " + faction.getName() + " to " + score));
		} else {
			sender.sendMessage(new TextComponentString(args[0] + " is not a valid faction! Try something else"));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if (args.length <= 1) {
			List<String> list = new ArrayList<>(FactionRegistry.instance().getLoadedFactions().size());
			FactionRegistry.instance().getLoadedFactions().forEach(new Consumer<CQRFaction>() {

				@Override
				public void accept(CQRFaction t) {
					list.add(t.getName());
				}
			});
			return list;
		}
		return Collections.emptyList();
	}

}
