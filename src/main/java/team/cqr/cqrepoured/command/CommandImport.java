//package team.cqr.cqrepoured.command;
//
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.world.World;
//import org.apache.commons.io.FileUtils;
//import team.cqr.cqrepoured.CQRMain;
//import team.cqr.cqrepoured.config.CQRConfig;
//import team.cqr.cqrepoured.init.CQRBlocks;
//import team.cqr.cqrepoured.tileentity.TileEntityExporter;
//import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
//import team.cqr.cqrepoured.world.structure.generation.generation.DungeonGenerationManager;
//import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
//import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon.Builder;
//import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;
//import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
//import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;
//
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.stream.Stream;
//
//public class CommandImport extends CommandBase {
//
//	@Override
//	public String getName() {
//		return "cqr_import_all";
//	}
//
//	@Override
//	public String getUsage(ICommandSender sender) {
//		return "/cqr_import_all";
//	}
//
//	@Override
//	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
//		boolean importMode = CQRConfig.advanced.structureImportMode;
//		CQRConfig.advanced.structureImportMode = true;
//		try {
//			List<File> allFiles = new ArrayList<>();
//			Map<File, List<File>> dir2file = new HashMap<>();
//			try (Stream<Path> stream = Files.list(CQRMain.CQ_STRUCTURE_FILES_FOLDER.toPath())) {
//				stream.map(Path::toFile).filter(File::isDirectory).forEach(dir -> {
//					for (File f : FileUtils.listFiles(dir, new String[] { "nbt" }, true)) {
//						if (allFiles.stream().anyMatch(f1 -> f1.getName().equals(f.getName()) && f1.length() == f.length())) {
//							CQRMain.logger.info("Duplicate: {}", f);
//							continue;
//						}
//						allFiles.add(f);
//						dir2file.computeIfAbsent(dir, k -> new ArrayList<>()).add(f);
//					}
//				});
//			}
//
//			World world = sender.getEntityWorld();
//			BlockPos pos = sender.getPosition();
//			int z = 0;
//			for (Entry<File, List<File>> e : dir2file.entrySet()) {
//				int maxSizeZ = 0;
//				int x = 0;
//				for (File f : e.getValue()) {
//					if (x >= 1024) {
//						z += maxSizeZ + 10;
//						maxSizeZ = 0;
//						x = 0;
//					}
//					CQStructure structure = CQStructure.createFromFile(f);
//
//					// place structure
//					BlockPos pos1 = pos.add(x, 0, z);
//					Builder builder = new Builder(world, pos1.add(2, 0, 2), "Import-" + f.getName(), DungeonInhabitantManager.DEFAULT_DUNGEON_INHABITANT.getName());
//					structure.addAll(builder, pos1.add(2, 0, 2), Offset.NORTH_EAST);
//					GeneratableDungeon dungeon = builder.build(world);
//					DungeonGenerationManager.generateNow(world, dungeon, null, DungeonSpawnType.DUNGEON_PLACER_ITEM);
//
//					// place exporter
//					world.setBlockState(pos1, CQRBlocks.EXPORTER.getDefaultState());
//					TileEntityExporter exporter = (TileEntityExporter) world.getTileEntity(pos1);
//					String s1 = CQRMain.CQ_STRUCTURE_FILES_FOLDER.getAbsolutePath();
//					String s2 = f.getAbsolutePath();
//					String s3 = s2.substring(s1.length() + 1, s2.lastIndexOf('.'));
//					BlockPos[] unprot = structure.getUnprotectedBlockList().toArray(new BlockPos[0]);
//					exporter.setValues(s3, new BlockPos(2, 0, 2), new BlockPos(1, -1, 1).add(structure.getSize()), true, true, unprot);
//
//					maxSizeZ = Math.max(structure.getSize().getZ(), maxSizeZ);
//					x += structure.getSize().getX() + 10;
//				}
//				z += maxSizeZ + 30;
//			}
//
//			sender.sendMessage(new StringTextComponent("Imported " + allFiles.size() + " structures successfully"));
//		} catch (Exception e) {
//			CQRMain.logger.error("Failed importing structures!", e);
//			throw new CommandException("Failed importing structures: %s", e);
//		} finally {
//			CQRConfig.advanced.structureImportMode = importMode;
//		}
//	}
//
//}
