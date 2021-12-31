package team.cqr.cqrepoured.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import team.cqr.cqrepoured.faction.EReputationState;
import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;

public class CommandChangeReputation extends CommandBase {

	@Override
	public String getName() {
		return "cqr_set_reputation";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/cqr_set_reputation faction reputation";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException(this.getUsage(sender));
		}
		Faction faction = FactionRegistry.instance(sender.getEntityWorld()).getFactionInstance(args[0]);
		if (faction == null) {
			throw new CommandException("Faction '" + args[0] + "' cannot be found!");
		}
		int score = parseInt(args[1], EReputationStateRough.ENEMY.getLowBound(), EReputationStateRough.ALLY.getHighBound());
		FactionRegistry.instance(sender.getEntityWorld()).changeReputationTo((EntityPlayerMP) sender.getCommandSenderEntity(), score, faction);
		sender.sendMessage(new TextComponentString(
				"Changed " + sender.getDisplayName().getFormattedText() + "'s reputation towards faction " + faction.getName() + " to " + score));
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if (args.length == 1) {
			return FactionRegistry.instance(sender.getEntityWorld()).getLoadedFactions().stream().map(Faction::getName).collect(Collectors.toList());
		} else if (args.length == 2) {
			return Arrays.stream(EReputationState.values()).mapToInt(EReputationState::getValue).mapToObj(Integer::toString).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

}
