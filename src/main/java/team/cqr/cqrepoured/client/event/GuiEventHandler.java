package team.cqr.cqrepoured.client.event;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.gui.GuiDungeonMapTool;
import team.cqr.cqrepoured.util.datafixer.StructureUpper;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;

@EventBusSubscriber(modid = CQRMain.MODID, value = Side.CLIENT)
public class GuiEventHandler {

	static boolean migrating = false;
	
	@SubscribeEvent
	public static void onInitGuiEvent(GuiScreenEvent.InitGuiEvent.Post event) {
		if (!(event.getGui() instanceof GuiConfig)) {
			return;
		}

		GuiConfig gui = (GuiConfig) event.getGui();
		if (!gui.modID.equals(CQRMain.MODID) || !(gui.parentScreen instanceof GuiModList)) {
			return;
		}

		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaled = new ScaledResolution(mc);

		GuiButton buttonReloadDungeons = new GuiButton(0, scaled.getScaledWidth() - 102, 2, 100, 20, "Reload Dungeons") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				DungeonRegistry.getInstance().loadDungeonFiles();
				return true;
			}

			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
				super.drawButton(mc, mouseX, mouseY, partialTicks);
				if (this.isMouseOver()) {
					gui.drawToolTip(Arrays.asList("Reloads all dungeon files located in config/CQR/dungeons."), mouseX, mouseY);
				}
			}
		};
		buttonReloadDungeons.enabled = mc.world == null || mc.isGamePaused();
		event.getButtonList().add(buttonReloadDungeons);

		GuiButton buttonMapTool = new GuiButton(1, 2, 2, 100, 20, "Map Tool") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				mc.displayGuiScreen(new GuiDungeonMapTool(gui));
				return true;
			}
		};
		buttonMapTool.enabled = mc.world == null;
		event.getButtonList().add(buttonMapTool);
		
		GuiButton buttonMigrateDungeons = new GuiButton(2, 2 + 10 + 100, 2, 150, 20, "Migrate Structure Files") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				try {
					migrating = true;
					
					List<File> allFiles = new ArrayList<>();
					Map<File, List<File>> dir2file = new HashMap<>();
					try (Stream<Path> stream = Files.list(CQRMain.CQ_STRUCTURE_FILES_FOLDER.toPath())) {
						stream.map(Path::toFile).filter(File::isDirectory).forEach(dir -> {
							for (File f : FileUtils.listFiles(dir, new String[] { "nbt" }, true)) {
								if (allFiles.stream().anyMatch(f1 -> f1.getName().equals(f.getName()) && f1.length() == f.length())) {
									CQRMain.logger.info("Duplicate: {}", f);
									continue;
								}
								allFiles.add(f);
								dir2file.computeIfAbsent(dir, k -> new ArrayList<>()).add(f);
							}
						});
					}
					
					for (Entry<File, List<File>> e : dir2file.entrySet()) {
						for(File sf : e.getValue()) {
							CQStructure struct = CQStructure.createFromFile(sf);
							NBTTagCompound migratedNBT = StructureUpper.createMigratableNBT(struct);
							
							String dirOld = sf.getAbsolutePath();
							String dirNew = dirOld.substring(CQRMain.CQ_STRUCTURE_FILES_FOLDER.getAbsolutePath().length(), dirOld.length());
							dirNew = dirNew.substring(0, dirNew.length() - sf.getName().length());
							File outputFolder = new File(CQRMain.CQ_MIGRATED_STRUCTURE_FILES_FOLDER, dirNew);
							
							File migratedFile = new File(outputFolder, sf.getName());
						
							try {
								if (migratedFile.isDirectory()) {
									throw new FileNotFoundException();
								}
								if (!migratedFile.exists()) {
									migratedFile.getParentFile().mkdirs();
									migratedFile.createNewFile();
								}
								try (OutputStream outputStream = new FileOutputStream(migratedFile)) {
									CompressedStreamTools.writeCompressed(migratedNBT, outputStream);
								}
								continue;
							} catch (Exception e2) {
								CQRMain.logger.error(String.format("Failed to write structure to file %s", migratedFile.getName()), e);
							}
							return true;
						}
					}
					
				} catch (Exception ex) {
					CQRMain.logger.error("Failed migrating structures!", ex);
				}
				finally {
					migrating = false;
				}
				
				return true;
			}
		};
		buttonMigrateDungeons.enabled = mc.world == null && !migrating;
		event.getButtonList().add(buttonMigrateDungeons);
	}

}
