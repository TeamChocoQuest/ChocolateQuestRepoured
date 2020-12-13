package team.cqr.cqrepoured.command;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.structureprot.ProtectedRegionManager;

public class CommandGetProtectedRegion extends CommandBase {

	@Override
	public String getName() {
		return "cqr_get_protected_region";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/cqr_get_protected_region x y z";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 3) {
			throw new WrongUsageException("Not enough arguments!");
		}

		BlockPos pos = parseBlockPos(sender, args, 0, false);
		ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(sender.getEntityWorld());

		if (protectedRegionManager != null) {
			List<ProtectedRegion> protectedRegions = protectedRegionManager.getProtectedRegionsAt(pos);

			StringBuilder sb = new StringBuilder("Protected Regions at (");
			sb.append(pos.getX());
			sb.append(',');
			sb.append(' ');
			sb.append(pos.getY());
			sb.append(',');
			sb.append(' ');
			sb.append(pos.getZ());
			sb.append(')');
			sb.append(':');
			sb.append('\n');
			for (int i = 0; i < protectedRegions.size(); i++) {
				ProtectedRegion protectedRegion = protectedRegions.get(i);
				sb.append(' ');
				sb.append(i);
				sb.append(':');
				sb.append(' ');
				sb.append(protectedRegion.getName());
				sb.append(' ');
				sb.append(protectedRegion.getUuid());
				if (i < protectedRegions.size() - 1) {
					sb.append('\n');
				}
			}
			sender.sendMessage(new TextComponentString(sb.toString()));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if (args.length >= 1 && args.length <= 3) {
			return getTabCompletionCoordinate(args, 0, targetPos);
		} else {
			return Collections.emptyList();
		}
	}

}
