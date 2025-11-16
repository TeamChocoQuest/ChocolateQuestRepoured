package team.cqr.cqrepoured.command;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonGenerationManager;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon.Builder;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

public class CommandImport extends CommandBase {

	@Override
	public String getName() {
		return "cqr_import_all";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/cqr_import_all";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			World world = sender.getEntityWorld();
			BlockPos pos = sender.getPosition().add(2, 0, 2);

			Map<Path, List<Path>> fileMap = Files.find(CQRMain.CQ_STRUCTURE_FILES_FOLDER.toPath(), Integer.MAX_VALUE, (p, a) -> a.isRegularFile() && p.getFileName().toString().endsWith(".nbt"))
					.collect(Collectors.groupingBy(Path::getParent, LinkedHashMap::new, Collectors.toList()));

			boolean importMode = CQRConfig.advanced.structureImportMode;
			AtomicInteger importModeModifications = new AtomicInteger();
			AtomicInteger z = new AtomicInteger();
			fileMap.values().forEach(files -> {
				AtomicInteger maxSizeZ = new AtomicInteger();
				AtomicInteger x = new AtomicInteger();

				files.forEach(file -> {
					CQStructure structure = CQStructure.createFromFile(file.toFile());
					if (x.get() != 0 && x.get() + structure.getSize().getX() > 1024) {
						z.getAndAdd(maxSizeZ.get() + 10);
						maxSizeZ.set(0);
						x.set(0);
					}
					BlockPos structurePos = pos.add(x.get(), 0, z.get());

					// place structure
					DungeonGenerationManager.generate(world, () -> {
						synchronized (importModeModifications) {
							importModeModifications.getAndIncrement();
							CQRConfig.advanced.structureImportMode = true;
						}
						try {
							Builder builder = new Builder(world, structurePos.add(2, 0, 2), "Import-" + file.getFileName().toString(), DungeonInhabitantManager.DEFAULT_DUNGEON_INHABITANT.getName());
							structure.addAll(builder, structurePos.add(2, 0, 2), Offset.NORTH_EAST);
							return builder.build(world);
						} finally {
							synchronized (importModeModifications) {
								if (importModeModifications.decrementAndGet() == 0) {
									CQRConfig.advanced.structureImportMode = importMode;
								}
							}
						}
					}, null, DungeonSpawnType.DUNGEON_PLACER_ITEM);

					// place exporter
					world.setBlockState(structurePos, CQRBlocks.EXPORTER.getDefaultState());
					TileEntityExporter exporter = (TileEntityExporter) world.getTileEntity(structurePos);
					String structureName = StringUtils.removeEnd(CQRMain.CQ_STRUCTURE_FILES_FOLDER.toPath().relativize(file).toString(), ".nbt");
					BlockPos[] unprotectedBlocks = structure.getUnprotectedBlockList().toArray(new BlockPos[0]);
					exporter.setValues(structureName, new BlockPos(2, 0, 2), new BlockPos(1, -1, 1).add(structure.getSize()), true, true, unprotectedBlocks);

					if (structure.getSize().getZ() > maxSizeZ.get()) {
						maxSizeZ.set(structure.getSize().getZ());
					}
					x.getAndAdd(structure.getSize().getX() + 10);
				});

				z.getAndAdd(maxSizeZ.get() + 30);
			});

			sender.sendMessage(new TextComponentString("Imported " + fileMap.values().stream().flatMap(List::stream).count() + " structures successfully"));
		} catch (Exception e) {
			CQRMain.logger.error("Failed importing structures!", e);
			throw new CommandException("Failed importing structures: %s", e);
		}
	}

}
