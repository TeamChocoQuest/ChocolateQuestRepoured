package team.cqr.cqrepoured.client.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.commons.lang3.Validate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.util.datafixer.StructureUpper;
import team.cqr.cqrepoured.util.tool.Progress;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;

public class GuiMigrateStructures extends GuiScreen {

	private final GuiScreen parent;
	private final List<GuiTextField> textFieldList = new ArrayList<>();
	private GuiTextField textFieldSrc;
	private GuiTextField textFieldDest;
	private GuiButton buttonExit;
	private GuiButton buttonCancel;
	private GuiButton buttonMigrateStructures;
	private boolean canExit = true;
	private Optional<MigrateStructuresTask> task = Optional.empty();

	public GuiMigrateStructures(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	public void initGui() {
		super.initGui();
		int id = 0;

		this.textFieldSrc = new GuiTextField(id++, this.fontRenderer, this.width / 2 - 200, 50, 400, 20);
		this.textFieldSrc.setMaxStringLength(256);
		this.textFieldSrc.setText(CQRMain.CQ_STRUCTURE_FILES_FOLDER.getPath());
		this.textFieldDest = new GuiTextField(id++, this.fontRenderer, this.width / 2 - 200, 100, 400, 20);
		this.textFieldDest.setMaxStringLength(256);
		this.textFieldDest.setText(CQRMain.CQ_MIGRATED_STRUCTURE_FILES_FOLDER.getPath());

		this.buttonExit = new GuiButton(id++, 5, 5, 20, 20, "X") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				if (!GuiMigrateStructures.this.canExit) {
					return false;
				}
				GuiMigrateStructures.this.mc.displayGuiScreen(GuiMigrateStructures.this.parent);
				return true;
			}
		};
		this.buttonCancel = new GuiButton(id++, this.width / 2 - 102, this.height - 24, 100, 20, "Cancel") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}
				GuiMigrateStructures.this.task.ifPresent(MigrateStructuresTask::cancel);
				return true;
			}
		};
		this.buttonCancel.enabled = false;
		this.buttonMigrateStructures = new GuiButton(id++, this.width / 2 + 2, this.height - 24, 100, 20, "Migrate Structures") {
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (!super.mousePressed(mc, mouseX, mouseY)) {
					return false;
				}

				Path src = Paths.get(GuiMigrateStructures.this.textFieldSrc.getText());
				Path dest = Paths.get(GuiMigrateStructures.this.textFieldDest.getText());

				GuiMigrateStructures.this.task = Optional.of(new MigrateStructuresTask(src, dest));
				GuiMigrateStructures.this.canExit = false;
				GuiMigrateStructures.this.buttonExit.enabled = false;
				GuiMigrateStructures.this.buttonCancel.enabled = true;
				GuiMigrateStructures.this.buttonMigrateStructures.enabled = false;

				GuiMigrateStructures.this.task.ifPresent(task -> task.run().handleAsync((v, t) -> {
					GuiMigrateStructures.this.canExit = true;
					GuiMigrateStructures.this.buttonExit.enabled = true;
					GuiMigrateStructures.this.buttonCancel.enabled = false;
					GuiMigrateStructures.this.buttonMigrateStructures.enabled = true;
					if (t != null) {
						if (t instanceof Exception) {
							CQRMain.logger.error("Failed migrating structures", t);
						} else {
							mc.crashed(new CrashReport("Failed migrating structures", t));
						}
					}
					return null;
				}));

				return true;
			}
		};

		this.textFieldList.clear();
		this.textFieldList.add(this.textFieldSrc);
		this.textFieldList.add(this.textFieldDest);
		this.buttonList.add(this.buttonExit);
		this.buttonList.add(this.buttonCancel);
		this.buttonList.add(this.buttonMigrateStructures);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		Optional<GuiTextField> focusedTextField = this.textFieldList.stream().filter(GuiTextField::isFocused).findFirst();
		if (focusedTextField.isPresent()) {
			if (keyCode == 1) {
				focusedTextField.get().setFocused(false);
			} else {
				focusedTextField.get().textboxKeyTyped(typedChar, keyCode);
			}
		} else if (keyCode == 1) {
			if (this.canExit) {
				this.mc.displayGuiScreen(this.parent);
			}
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.textFieldList.forEach(tf -> tf.mouseClicked(mouseX, mouseY, mouseButton));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.textFieldList.forEach(GuiTextField::updateCursorCounter);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.textFieldList.forEach(GuiTextField::drawTextBox);
		GuiHelper.drawString(this.fontRenderer, "Source Directory", this.width / 2 - 200, 40, 0xF0F0F0, false, false);
		GuiHelper.drawString(this.fontRenderer, "Destination Directory", this.width / 2 - 200, 90, 0xF0F0F0, false, false);
		this.task.ifPresent(task -> {
			GuiHelper.drawString(this.fontRenderer, task.getProgress().toString(), this.width / 2 + 120, this.height - 18, 0xF0F0F0, false, false);
		});
	}

	private static class MigrateStructuresTask {

		private final Path src;
		private final Path dest;
		private final Progress progress = new Progress(1);
		private volatile boolean cancelled;

		public MigrateStructuresTask(Path src, Path dest) {
			Validate.isTrue(Files.isDirectory(src));
			Validate.isTrue(!Files.exists(dest) || Files.isDirectory(dest));
			this.src = src;
			this.dest = dest;
		}

		public CompletableFuture<Void> run() {
			return CompletableFuture.runAsync(() -> {
				try {
					if (!Files.exists(src)) {
						throw new IllegalArgumentException();
					}

					Path[] srcFiles;
					UnaryOperator<Path> destFactory;

					if (Files.isDirectory(src)) {
						if (Files.isRegularFile(dest)) {
							throw new IllegalArgumentException();
						}

						srcFiles = Files.find(src, Integer.MAX_VALUE, (p, a) -> p.getFileName().toString().endsWith(".nbt")).toArray(Path[]::new);
						destFactory = p -> dest.resolve(src.relativize(p));
					} else {
						if (!src.getFileName().toString().endsWith(".nbt")) {
							throw new IllegalArgumentException();
						}

						srcFiles = Stream.of(src).toArray(Path[]::new);
						if (Files.isDirectory(dest)) {
							destFactory = p -> dest.resolve(p.getFileName());
						} else {
							destFactory = p -> dest;
						}
					}

					for (int i = 0; i < srcFiles.length; i++) {
						if (this.cancelled) {
							return;
						}

						Path file = srcFiles[i];
						migrate(file, destFactory.apply(file));

						this.progress.setProgress((double) i / srcFiles.length);
					}

					this.progress.finishStage();
				} catch (IOException e) {
					this.progress.setErrored();
					throw new RuntimeException(e);
				}
			});
		}

		private static void migrate(Path src, Path dest) throws IOException {
			CQStructure structure = CQStructure.createFromFile(src.toFile());
			NBTTagCompound migratedNBT = StructureUpper.createMigratableNBT(structure);
			Files.createDirectories(dest.getParent());
			try (OutputStream out = Files.newOutputStream(dest)) {
				CompressedStreamTools.writeCompressed(migratedNBT, out);
			}
		}

		public Progress getProgress() {
			return this.progress;
		}

		public void cancel() {
			this.progress.setCancelled();
			this.cancelled = true;
		}

	}

}
