package team.cqr.cqrepoured.command;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.IndentableStringBuilder;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

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
			throw new WrongUsageException(this.getUsage(sender));
		}

		BlockPos pos = parseBlockPos(sender, args, 0, false);
		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(sender.getEntityWorld());

		if (protectedRegionManager != null) {
			List<ProtectedRegion> protectedRegions = protectedRegionManager.getProtectedRegionsAt(pos);

			IndentableStringBuilder sb = new IndentableStringBuilder();
			sb.append("Protected Regions at (%d, %d, %d):", pos.getX(), pos.getY(), pos.getZ()).newLine();
			sb.incrementIndentation();

			if (protectedRegions.isEmpty()) {
				sb.append("NONE");
			} else {
				for (int i = 0; i < protectedRegions.size(); i++) {
					ProtectedRegion protectedRegion = protectedRegions.get(i);
					sb.append(i).append(':').newLine();
					sb.incrementIndentation();

					sb.append("Name: %s", protectedRegion.getName()).newLine();
					sb.append("UUID: %s", protectedRegion.getUuid()).newLine();

					sb.append("Entities: [").newLine();
					sb.incrementIndentation();
					for (UUID entityUUID : protectedRegion.getEntityDependencies()) {
						Entity entity = EntityUtil.getEntityByUUID(sender.getEntityWorld(), entityUUID);
						if (entity != null) {
							sb.append("%s (%d, %d, %d)", entity.getClass().getSimpleName(),
									MathHelper.ceil(entity.posX), MathHelper.ceil(entity.posY),
									MathHelper.ceil(entity.posZ)).newLine();
						} else {
							sb.append(entityUUID).newLine();
						}
					}
					sb.decrementIndentation();
					sb.append("]").newLine();

					sb.append("Blocks: [").newLine();
					sb.incrementIndentation();
					for (BlockPos blockPos : protectedRegion.getBlockDependencies()) {
						sb.append("%d, %d, %d", blockPos.getX(), blockPos.getY(), blockPos.getZ()).newLine();
					}
					sb.decrementIndentation();
					sb.append("]");
				}
			}

			sb.decrementIndentation();

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
