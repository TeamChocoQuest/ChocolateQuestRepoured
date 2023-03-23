//package team.cqr.cqrepoured.command;
//
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.command.WrongUsageException;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldType;
//import team.cqr.cqrepoured.config.CQRConfig;
//import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
//import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
//
//import javax.annotation.Nullable;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public class CommandLocateDungeon extends CommandBase {
//
//	@Override
//	public String getName() {
//		return "cqr_locate_dungeon";
//	}
//
//	@Override
//	public String getUsage(ICommandSender sender) {
//		return "/cqr_locate_dungeon x z chunkRadius [generatedDungeon] [notGeneratedDungeon] [dungeonName]";
//	}
//
//	@Override
//	public int getRequiredPermissionLevel() {
//		return 4;
//	}
//
//	@Override
//	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
//		if (args.length < 3) {
//			throw new WrongUsageException(this.getUsage(sender));
//		}
//
//		World world = sender.getEntityWorld();
//		int x = MathHelper.floor(parseDouble(sender.getCommandSenderEntity().posX, args[0], false));
//		int z = MathHelper.floor(parseDouble(sender.getCommandSenderEntity().posZ, args[1], false));
//		int chunkRadius = parseInt(args[2], 0, 0x10000);
//		boolean searchForGeneratedDungeon = args.length < 4 || parseBoolean(args[3]);
//		boolean searchForNotGeneratedDungeon = args.length < 5 || parseBoolean(args[4]);
//		String dungeonToSearchFor = args.length >= 6 ? args[5] : null;
//
//		if (!world.getWorldInfo().isMapFeaturesEnabled()) {
//			sender.sendMessage(new StringTextComponent("Structures are disabled."));
//			return;
//		}
//
//		if (world.getWorldType() == WorldType.FLAT && !CQRConfig.general.dungeonsInFlat) {
//			sender.sendMessage(new StringTextComponent("Flat world and dungeonsInFlat is disabled."));
//			return;
//		}
//
//		// TODO send warning if aw2 integration is enabled
//
//		DungeonGenInfo dungeonGenInfo = getNearestDungeon(world, x >> 4, z >> 4, chunkRadius, dungeonToSearchFor, searchForGeneratedDungeon, searchForNotGeneratedDungeon);
//		if (dungeonGenInfo != null) {
//			int dungeonX = (dungeonGenInfo.chunkX << 4) + 8;
//			int dungeonZ = (dungeonGenInfo.chunkZ << 4) + 8;
//			String s = String.format("Nearest dungeon: %s at x=%d z=%d", dungeonGenInfo.dungeonName, dungeonX, dungeonZ);
//			sender.sendMessage(new StringTextComponent(s));
//		} else {
//			sender.sendMessage(new StringTextComponent("No dungeon found."));
//		}
//	}
//
//	@Nullable
//	public static DungeonGenInfo getNearestDungeon(World world, int chunkX, int chunkZ, int chunkRadius, @Nullable String dungeonToSearchFor, boolean searchForGeneratedDungeon, boolean searchForNotGeneratedDungeon) {
//		if (!searchForGeneratedDungeon && !searchForNotGeneratedDungeon) {
//			return null;
//		}
//
//		if (!world.getWorldInfo().isMapFeaturesEnabled()) {
//			return null;
//		}
//
//		if (world.getWorldType() == WorldType.FLAT && !CQRConfig.general.dungeonsInFlat) {
//			return null;
//		}
//
//		int dim = world.provider.getDimension();
//		DungeonRegistry registry = DungeonRegistry.getInstance();
//		if (registry.getDungeons().stream().noneMatch(dungeon -> dungeon.canSpawnInDim(dim) && dungeon.getDungeonName().equals(dungeonToSearchFor))) {
//			return null;
//		}
//
//		boolean temp = CQRConfig.advanced.debugDungeonGen;
//		CQRConfig.advanced.debugDungeonGen = false;
//		try {
//			for (int r = 0; r <= chunkRadius; r++) {
//				int startX = chunkX - r;
//				int endX = chunkX + r;
//				int startZ = chunkZ - r;
//				int endZ = chunkZ + r;
//
//				for (int x = startX; x <= endX; x++) {
//					boolean flag = x != startX && x != endX;
//
//					for (int z = startZ; z <= endZ; z++) {
//						if (flag && z != startZ && z != endZ) {
//							continue;
//						}
//
//						DungeonBase dungeon = WorldDungeonGenerator.getDungeonAt(world, x, z);
//
//						if (dungeon == null || (dungeonToSearchFor != null && !dungeon.getDungeonName().equalsIgnoreCase(dungeonToSearchFor))) {
//							continue;
//						}
//
//						return new DungeonGenInfo(dungeon.getDungeonName(), x, z);
//					}
//				}
//			}
//		} finally {
//			CQRConfig.advanced.debugDungeonGen = temp;
//		}
//
//		return null;
//	}
//
//	@Override
//	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
//		if (args.length >= 1 && args.length <= 2) {
//			return getTabCompletionCoordinateXZ(args, 0, targetPos);
//		} else if (args.length == 3) {
//			return args[2].isEmpty() ? Arrays.asList("64") : Collections.emptyList();
//		} else if (args.length == 4) {
//			return getListOfStringsMatchingLastWord(args, "true", "false");
//		} else if (args.length == 5) {
//			return getListOfStringsMatchingLastWord(args, "true", "false");
//		} else if (args.length == 6) {
//			DungeonRegistry dungeonRegistry = DungeonRegistry.getInstance();
//			int dim = sender.getEntityWorld().provider.getDimension();
//			return getListOfStringsMatchingLastWord(args, dungeonRegistry.getDungeons().stream().filter(dungeon -> dungeon.canSpawnInDim(dim)).map(DungeonBase::getDungeonName).toArray(String[]::new));
//		} else {
//			return Collections.emptyList();
//		}
//	}
//
//	public static class DungeonGenInfo {
//
//		public final String dungeonName;
//		public final int chunkX;
//		public final int chunkZ;
//
//		public DungeonGenInfo(String dungeonName, int chunkX, int chunkZ) {
//			this.dungeonName = dungeonName;
//			this.chunkX = chunkX;
//			this.chunkZ = chunkZ;
//		}
//
//	}
//
//}
