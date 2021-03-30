package team.cqr.cqrepoured.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import team.cqr.cqrepoured.integration.IntegrationInformation;
import team.cqr.cqrepoured.structuregen.DungeonRegistry;
import team.cqr.cqrepoured.structuregen.WorldDungeonGenerator;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VanillaStructureHelper;

public class CommandLocateDungeon extends CommandBase {

	@Override
	public String getName() {
		return "cqr_locate_dungeon";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/cqr_locate_dungeon x z chunkRadius [generatedDungeon] [notGeneratedDungeon] [dungeonName]";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 4) {
			throw new WrongUsageException("Not enough arguments!");
		}

		World world = sender.getEntityWorld();
		int x = MathHelper.floor(parseDouble(sender.getCommandSenderEntity().posX, args[0], false));
		int z = MathHelper.floor(parseDouble(sender.getCommandSenderEntity().posZ, args[1], false));
		int chunkRadius = parseInt(args[2], 0, 0x10000);
		boolean searchForGeneratedDungeon = args.length < 4 || parseBoolean(args[3]);
		boolean searchForNotGeneratedDungeon = args.length < 5 || parseBoolean(args[4]);
		String dungeonToSearchFor = args.length >= 6 ? args[5] : null;

		if (!world.getWorldInfo().isMapFeaturesEnabled()) {
			sender.sendMessage(new TextComponentString("Structures are disabled."));
			return;
		}

		if (world.getWorldType() == WorldType.FLAT && !CQRConfig.general.dungeonsInFlat) {
			sender.sendMessage(new TextComponentString("Flat world and dungeonsInFlat is disabled."));
			return;
		}

		// TODO send warning if aw2 integration is enabled

		DungeonGenInfo dungeonGenInfo = getNearestDungeon(world, x >> 4, z >> 4, chunkRadius, dungeonToSearchFor, searchForGeneratedDungeon, searchForNotGeneratedDungeon);
		if (dungeonGenInfo != null) {
			int dungeonX = (dungeonGenInfo.chunkX << 4) + 8;
			int dungeonZ = (dungeonGenInfo.chunkZ << 4) + 8;
			String s = String.format("Nearest dungeon: %s at x=%d z=%d", dungeonGenInfo.dungeonName, dungeonX, dungeonZ);
			sender.sendMessage(new TextComponentString(s));
		} else {
			sender.sendMessage(new TextComponentString("No dungeon found."));
		}
	}

	@Nullable
	public static DungeonGenInfo getNearestDungeon(World world, int chunkX, int chunkZ, int chunkRadius, @Nullable String dungeonToSearchFor, boolean searchForGeneratedDungeon, boolean searchForNotGeneratedDungeon) {
		if (!searchForGeneratedDungeon && !searchForNotGeneratedDungeon) {
			return null;
		}

		int dim = world.provider.getDimension();
		if (dungeonToSearchFor != null && DungeonRegistry.getInstance().getDungeons().stream().noneMatch(dungeon -> dungeon.canSpawnInDim(dim) && dungeon.getDungeonName().equals(dungeonToSearchFor))) {
			return null;
		}
		
		int spawnX = DungeonGenUtils.getSpawnX(world) >> 4;
		int spawnZ = DungeonGenUtils.getSpawnZ(world) >> 4;
		Random rand = new Random();
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		if (!world.getWorldInfo().isMapFeaturesEnabled()) {
			return null;
		}

		if (world.getWorldType() == WorldType.FLAT && !CQRConfig.general.dungeonsInFlat) {
			return null;
		}

		for (int r = 0; r <= chunkRadius; r++) {
			int startX = chunkX - r;
			int endX = chunkX + r;
			int startZ = chunkZ - r;
			int endZ = chunkZ + r;

			for (int x = startX; x <= endX; x++) {
				boolean flag = x != startX && x != endX;

				for (int z = startZ; z <= endZ; z++) {
					if (flag && z != startZ && z != endZ) {
						continue;
					}

					if (DungeonGenUtils.isInWallRange(world, x, z)) {
						continue;
					}

					int dungeonSeparation = CQRConfig.general.dungeonSeparation;

					if (CQRConfig.wall.enabled && z < -CQRConfig.wall.distance && CQRConfig.general.moreDungeonsBehindWall) {
						dungeonSeparation = MathHelper.ceil((double) dungeonSeparation / CQRConfig.general.densityBehindWallFactor);
					}

					if ((x - spawnX) % dungeonSeparation != 0 || (z - spawnZ) % dungeonSeparation != 0) {
						continue;
					}

					if (!DungeonGenUtils.isFarAwayEnoughFromSpawn(world, x, z)) {
						continue;
					}

					if (!DungeonGenUtils.isFarAwayEnoughFromLocationSpecifics(world, x, z, dungeonSeparation)) {
						continue;
					}

					boolean isChunkGenerated = world.isChunkGeneratedAt(x, z);

					if (searchForGeneratedDungeon && !searchForNotGeneratedDungeon && !isChunkGenerated) {
						continue;
					}

					if (!searchForGeneratedDungeon && searchForNotGeneratedDungeon && isChunkGenerated) {
						continue;
					}

					mutablePos.setPos((x << 4) + 8, 64, (z << 4) + 8);

					if (CQRConfig.advanced.generationRespectOtherStructures) {
						if (VanillaStructureHelper.isStructureInRange(world, mutablePos, MathHelper.ceil(CQRConfig.advanced.generationMinDistanceToOtherStructure / 16.0D))) {
							continue;
						}

						if (IntegrationInformation.isAW2StructureAlreadyThere(mutablePos.getX(), 64, mutablePos.getZ(), world)) {
							continue;
						}
					}

					rand.setSeed(WorldDungeonGenerator.getSeed(world, (x << 4) + 8, (z << 4) + 8));

					if (!DungeonGenUtils.percentageRandom(CQRConfig.general.overallDungeonChance, rand)) {
						continue;
					}

					CQRWeightedRandom<DungeonBase> possibleDungeons = DungeonRegistry.getInstance().getDungeonsForPos(world, mutablePos);
					DungeonBase dungeon = possibleDungeons.next(rand);

					if (dungeon == null || !DungeonGenUtils.percentageRandom(dungeon.getChance(), rand)) {
						continue;
					}

					if (dungeonToSearchFor != null && !dungeon.getDungeonName().equals(dungeonToSearchFor)) {
						continue;
					}

					return new DungeonGenInfo(dungeon.getDungeonName(), x, z);
				}
			}
		}

		return null;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if (args.length >= 1 && args.length <= 2) {
			return getTabCompletionCoordinateXZ(args, 0, targetPos);
		} else if (args.length == 3) {
			return args[2].isEmpty() ? Arrays.asList("64") : Collections.emptyList();
		} else if (args.length == 4) {
			return getListOfStringsMatchingLastWord(args, "true", "false");
		} else if (args.length == 5) {
			return getListOfStringsMatchingLastWord(args, "true", "false");
		} else if (args.length == 6) {
			DungeonRegistry dungeonRegistry = DungeonRegistry.getInstance();
			int dim = sender.getEntityWorld().provider.getDimension();
			return getListOfStringsMatchingLastWord(args, dungeonRegistry.getDungeons().stream().filter(dungeon -> dungeon.canSpawnInDim(dim)).map(DungeonBase::getDungeonName).toArray(String[]::new));
		} else {
			return Collections.emptyList();
		}
	}

	public static class DungeonGenInfo {

		public final String dungeonName;
		public final int chunkX;
		public final int chunkZ;

		public DungeonGenInfo(String dungeonName, int chunkX, int chunkZ) {
			this.dungeonName = dungeonName;
			this.chunkX = chunkX;
			this.chunkZ = chunkZ;
		}

	}

}
