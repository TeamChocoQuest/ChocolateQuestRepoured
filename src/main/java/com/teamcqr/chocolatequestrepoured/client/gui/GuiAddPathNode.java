package com.teamcqr.chocolatequestrepoured.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.client.util.GuiHelper;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketAddPathNode;
import com.teamcqr.chocolatequestrepoured.objects.entity.pathfinding.Path;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemPathTool;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAddPathNode extends GuiScreen {

	private final EnumHand hand;
	private final int rootNodeIndex;
	private final BlockPos pos;
	private final List<GuiTextField> textFieldList = new ArrayList<>();

	private GuiButtonExt buttonConfirm;
	private GuiButtonExt buttonCancel;
	private GuiNumberTextField textFieldX;
	private GuiNumberTextField textFieldY;
	private GuiNumberTextField textFieldZ;
	private GuiNumberTextField textFieldWaitingTimeMin;
	private GuiNumberTextField textFieldWaitingTimeMax;
	private GuiNumberTextField textFieldWaitingRotation;
	private GuiNumberTextField textFieldWeight;
	private GuiNumberTextField textFieldTimeMin;
	private GuiNumberTextField textFieldTimeMax;
	private GuiCheckBox checkBoxBidirectional;
	private final IntList blacklistedPrevNodes = new IntArrayList();

	private int x;
	private int y;
	private int z;
	private int pathMapX;
	private int pathMapY;
	private int pathMapWidth;
	private int pathMapHeight;
	private int centerOffsetX;
	private int centerOffsetY;
	private boolean pathMapClicked;
	private int lastMouseX;
	private int lastMouseY;
	private float mouseOverheadX;
	private float mouseOverheadY;

	public GuiAddPathNode(EnumHand hand, int rootNode, BlockPos pos) {
		this.hand = hand;
		this.rootNodeIndex = rootNode;
		this.pos = pos;
	}

	@Override
	public void initGui() {
		this.textFieldList.clear();
		super.initGui();
		int id = 0;

		int xOffset = -43;
		int yOffset = 10 * 16 / 2 * -1; // elementCount * elementOffset / 2 * -1
		int i = 0;
		this.textFieldX = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, true, false);
		this.textFieldY = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, true, false);
		this.textFieldZ = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, true, false);
		this.textFieldWaitingTimeMin = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, false, false);
		this.textFieldWaitingTimeMax = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, false, false);
		this.textFieldWaitingRotation = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, false, false);
		this.textFieldWeight = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, false, false);
		this.textFieldTimeMin = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, false, false);
		this.textFieldTimeMax = new GuiNumberTextField(id++, this.fontRenderer, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, false, false);
		this.checkBoxBidirectional = new GuiCheckBox(id++, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, "Bidirectional", true);

		this.buttonConfirm = new GuiButtonExt(id++, this.width / 2 - 102, this.height - 36, 100, 16, "Confirm");
		this.buttonCancel = new GuiButtonExt(id++, this.width / 2 + 2, this.height - 36, 100, 16, "Cancel");

		this.textFieldX.setText(String.valueOf(this.pos.getX()));
		this.textFieldY.setText(String.valueOf(this.pos.getY()));
		this.textFieldZ.setText(String.valueOf(this.pos.getZ()));
		this.textFieldWaitingTimeMin.setText("0");
		this.textFieldWaitingTimeMax.setText("0");
		this.textFieldWaitingRotation.setText("0");
		this.textFieldWeight.setText("10");
		this.textFieldTimeMin.setText("0");
		this.textFieldTimeMax.setText("24000");

		this.buttonList.add(this.buttonConfirm);
		this.buttonList.add(this.buttonCancel);
		this.textFieldList.add(this.textFieldX);
		this.textFieldList.add(this.textFieldY);
		this.textFieldList.add(this.textFieldZ);
		this.textFieldList.add(this.textFieldWaitingTimeMin);
		this.textFieldList.add(this.textFieldWaitingTimeMax);
		this.textFieldList.add(this.textFieldWaitingRotation);
		this.textFieldList.add(this.textFieldWeight);
		this.textFieldList.add(this.textFieldTimeMin);
		this.textFieldList.add(this.textFieldTimeMax);
		this.buttonList.add(this.checkBoxBidirectional);
		this.blacklistedPrevNodes.clear();

		this.x = this.pos.getX();
		this.y = this.pos.getY();
		this.z = this.pos.getZ();
		this.pathMapX = this.width / 2 - 192;
		this.pathMapY = this.height / 2 - 80;
		this.pathMapWidth = 130;
		this.pathMapHeight = 130;
		ItemStack stack = this.mc.player.getHeldItem(this.hand);
		Path path = ItemPathTool.getPath(stack);
		if (path != null) {
			Path.PathNode rootNode = path.getNode(this.rootNodeIndex);
			if (rootNode != null) {
				this.centerOffsetX = rootNode.getPos().getX();
				this.centerOffsetY = rootNode.getPos().getZ();
			} else {
				this.centerOffsetX = this.pos.getX();
				this.centerOffsetY = this.pos.getZ();
			}
		} else {
			this.centerOffsetX = this.pos.getX();
			this.centerOffsetY = this.pos.getZ();
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		boolean textFieldFocused = false;
		for (GuiTextField textField : this.textFieldList) {
			if (textField.isFocused()) {
				textFieldFocused = true;
				break;
			}
		}
		if (textFieldFocused) {
			if (keyCode == 1) {
				for (GuiTextField textField : this.textFieldList) {
					textField.setFocused(false);
				}
			} else {
				for (GuiTextField textField : this.textFieldList) {
					textField.textboxKeyTyped(typedChar, keyCode);
				}
				if (this.textFieldX.isFocused()) {
					try {
						this.x = Integer.parseInt(this.textFieldX.getText());
					} catch (NumberFormatException e) {
						// ignore
					}
				}
				if (this.textFieldY.isFocused()) {
					try {
						this.y = Integer.parseInt(this.textFieldY.getText());
					} catch (NumberFormatException e) {
						// ignore
					}
				}
				if (this.textFieldZ.isFocused()) {
					try {
						this.z = Integer.parseInt(this.textFieldZ.getText());
					} catch (NumberFormatException e) {
						// ignore
					}
				}
			}
		} else if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			this.mc.player.closeScreen();
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();

		int deltaWheel = Mouse.getEventDWheel();
		if (deltaWheel != 0) {
			int scrollAmount = deltaWheel / 120;
			int i = new ScaledResolution(this.mc).getScaleFactor();
			int mouseX = Mouse.getX() / i;
			int mouseY = this.height - Mouse.getY() / i;
			if (mouseX >= this.textFieldX.x && mouseX <= this.textFieldX.x + this.textFieldX.width && mouseY >= this.textFieldX.y && mouseY <= this.textFieldX.y + this.textFieldX.height) {
				try {
					int number = Integer.parseInt(this.textFieldX.getText()) + scrollAmount;
					this.textFieldX.setText(String.valueOf(number));
					this.x = number;
				} catch (NumberFormatException e) {
					// ignore
				}
			}
			if (mouseX >= this.textFieldY.x && mouseX <= this.textFieldY.x + this.textFieldY.width && mouseY >= this.textFieldY.y && mouseY <= this.textFieldY.y + this.textFieldY.height) {
				try {
					int number = Integer.parseInt(this.textFieldY.getText()) + scrollAmount;
					this.textFieldY.setText(String.valueOf(number));
					this.y = number;
				} catch (NumberFormatException e) {
					// ignore
				}
			}
			if (mouseX >= this.textFieldZ.x && mouseX <= this.textFieldZ.x + this.textFieldZ.width && mouseY >= this.textFieldZ.y && mouseY <= this.textFieldZ.y + this.textFieldZ.height) {
				try {
					int number = Integer.parseInt(this.textFieldZ.getText()) + scrollAmount;
					this.textFieldZ.setText(String.valueOf(number));
					this.z = number;
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

		if (this.pathMapClicked && clickedMouseButton == 0) {
			float deltaX = (mouseX - this.lastMouseX) * 0.5F + this.mouseOverheadX;
			float deltaY = (mouseY - this.lastMouseY) * 0.5F + this.mouseOverheadY;
			this.centerOffsetX -= (int) deltaX;
			this.centerOffsetY -= (int) deltaY;
			this.mouseOverheadX = deltaX % 1.0F;
			this.mouseOverheadY = deltaY % 1.0F;
			this.lastMouseX = mouseX;
			this.lastMouseY = mouseY;
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		for (GuiTextField textField : this.textFieldList) {
			textField.mouseClicked(mouseX, mouseY, mouseButton);
		}

		int minX = this.pathMapX;
		int minY = this.pathMapY;
		int maxX = this.pathMapX + this.pathMapWidth;
		int maxY = this.pathMapY + this.pathMapHeight;
		if (mouseButton == 0 && mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY) {
			this.pathMapClicked = true;
			this.lastMouseX = mouseX;
			this.lastMouseY = mouseY;
		} else {
			this.pathMapClicked = false;
		}
		if (mouseButton == 0) {
			Path.PathNode clickedNode = this.getNodeAt(mouseX, mouseY);
			if (clickedNode != null) {
				Path path = ItemPathTool.getPath(this.mc.player.getHeldItem(this.hand));
				Path.PathNode rootNode = path.getNode(this.rootNodeIndex);
				if (rootNode != null && clickedNode.getConnectedNodes().contains(this.rootNodeIndex)) {
					if (!this.blacklistedPrevNodes.contains(clickedNode.getIndex())) {
						this.blacklistedPrevNodes.add(clickedNode.getIndex());
					} else {
						this.blacklistedPrevNodes.rem(clickedNode.getIndex());
					}
				}
			}
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		for (GuiTextField textField : this.textFieldList) {
			textField.updateCursorCounter();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "Add Path Node (Index: " + ItemPathTool.getPath(this.mc.player.getHeldItem(this.hand)).getSize() + ")", this.width / 2, 20, 16777215);

		for (GuiTextField textField : this.textFieldList) {
			textField.drawTextBox();
		}

		int xOffset = 1;
		int yOffset = 10 * 16 / 2 * -1 + 3; // elementCount * elementOffset / 2 * -1 + 3
		int i = 0;
		GuiHelper.drawString(this.fontRenderer, "Node X", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Node Y", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Node Z", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Waiting Time Min", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Waiting Time Max", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Waiting Rotation", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Weight", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Time Min", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0, false, true);
		GuiHelper.drawString(this.fontRenderer, "Time Max", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0, false, true);

		super.drawScreen(mouseX, mouseY, partialTicks);

		this.drawPathMap(this.pathMapX, this.pathMapY, this.pathMapWidth, this.pathMapHeight, this.centerOffsetX, this.centerOffsetY);

		Path.PathNode selectedNode = this.getNodeAt(mouseX, mouseY);
		if (selectedNode != null) {
			this.drawHoveringText(String.format("Index: %d, %s", selectedNode.getIndex(), selectedNode.getPos()), mouseX, mouseY);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.buttonConfirm) {
			try {
				int posX = this.textFieldX.getInt();
				int posY = this.textFieldY.getInt();
				int posZ = this.textFieldZ.getInt();
				int waitingTimeMin = this.textFieldWaitingTimeMin.getInt();
				int waitingTimeMax = this.textFieldWaitingTimeMax.getInt();
				float waitingRotation = this.textFieldWaitingRotation.getInt();
				int weight = this.textFieldWeight.getInt();
				int timeMin = this.textFieldTimeMin.getInt();
				int timeMax = this.textFieldTimeMax.getInt();
				boolean bidirectional = this.checkBoxBidirectional.isChecked();
				CQRMain.NETWORK.sendToServer(new CPacketAddPathNode(this.hand, this.rootNodeIndex, new BlockPos(posX, posY, posZ), waitingTimeMin, waitingTimeMax, waitingRotation, weight, timeMin, timeMax, bidirectional, this.blacklistedPrevNodes));
			} catch (NumberFormatException e) {
				this.mc.player.sendMessage(new TextComponentString("Invalid path node arguments!"));
			}

			this.mc.player.closeScreen();
		} else if (button == this.buttonCancel) {
			this.mc.player.closeScreen();
		} else {
			super.actionPerformed(button);
		}
	}

	@Nullable
	private Path.PathNode getNodeAt(int mouseX, int mouseY) {
		if (mouseX < this.pathMapX || mouseX > this.pathMapX + this.pathMapWidth) {
			return null;
		}
		if (mouseY < this.pathMapY || mouseY > this.pathMapY + this.pathMapHeight) {
			return null;
		}
		Path path = ItemPathTool.getPath(this.mc.player.getHeldItem(this.hand));
		Path.PathNode clickedNode = null;
		int posX = (mouseX - this.pathMapX - (this.pathMapWidth / 2)) / 2 + this.centerOffsetX;
		int posY = (mouseY - this.pathMapY - (this.pathMapHeight / 2)) / 2 + this.centerOffsetY;
		for (Path.PathNode node : path.getNodes()) {
			if (node.getPos().getX() == posX && node.getPos().getZ() == posY) {
				clickedNode = node;
				break;
			}
		}
		for (Path.PathNode node : path.getNodes()) {
			int nodeX = node.getPos().getX();
			int nodeZ = node.getPos().getZ();
			if (Math.abs(nodeX - posX) <= 1 && Math.abs(nodeZ - posY) <= 1) {
				clickedNode = node;
				break;
			}
		}
		return clickedNode;
	}

	private void drawPathMap(int x, int y, int width, int height, int centerX, int centerY) {
		width /= 2;
		height /= 2;
		int radiusX = width / 2;
		int radiusY = height / 2;
		Path path = ItemPathTool.getPath(this.mc.player.getHeldItem(this.hand));
		Path.PathNode rootNode = path.getNode(this.rootNodeIndex);

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0.0D);
		GL11.glScaled(2.0D, 2.0D, 1.0D);

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		// draw background
		GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(0.0D, height);
		GL11.glVertex2d(width, height);
		GL11.glVertex2d(width, 0.0D);
		GL11.glVertex2d(0.0D, 0.0D);
		GL11.glEnd();

		GL11.glTranslated(radiusX + 0.5D, radiusY + 0.5D, 0.0D);

		// draw node connections
		GL11.glColor4d(0.5D, 0.5D, 0.5D, 1.0D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glLineWidth(new ScaledResolution(this.mc).getScaleFactor());
		GL11.glBegin(GL11.GL_LINES);
		for (Path.PathNode node : path.getNodes()) {
			int offsetX = node.getPos().getX() - centerX;
			int offsetZ = node.getPos().getZ() - centerY;
			boolean flag = offsetX < -radiusX || offsetX > radiusX || offsetZ < -radiusY || offsetZ > radiusY;
			for (int index : node.getConnectedNodes()) {
				Path.PathNode connectedNode = path.getNode(index);
				int offsetX2 = connectedNode.getPos().getX() - centerX;
				int offsetZ2 = connectedNode.getPos().getZ() - centerY;
				boolean flag2 = offsetX2 < -radiusX || offsetX2 > radiusX || offsetZ2 < -radiusY || offsetZ2 > radiusY;
				Vec2f start = null;
				Vec2f end = null;
				if (flag) {
					start = this.calculateIntercept(offsetX2, offsetZ2, offsetX, offsetZ, -radiusX - 1, -radiusY - 1, radiusX + 1, radiusY + 1);
				} else {
					start = new Vec2f(offsetX, offsetZ);
				}
				if (flag2) {
					end = this.calculateIntercept(offsetX, offsetZ, offsetX2, offsetZ2, -radiusX - 1, -radiusY - 1, radiusX + 1, radiusY + 1);
				} else {
					end = new Vec2f(offsetX2, offsetZ2);
				}
				if (start != null && end != null) {
					GL11.glVertex2d(start.x, start.y);
					GL11.glVertex2d(end.x, end.y);
				}
			}
		}
		if (rootNode != null) {
			GL11.glColor4d(0.0D, 0.35D, 0.7D, 0.5D);
			int offsetX = this.x - centerX;
			int offsetZ = this.z - centerY;
			int offsetX2 = rootNode.getPos().getX() - centerX;
			int offsetZ2 = rootNode.getPos().getZ() - centerY;
			boolean flag = offsetX < -radiusX || offsetX > radiusX || offsetZ < -radiusY || offsetZ > radiusY;
			boolean flag2 = offsetX2 < -radiusX || offsetX2 > radiusX || offsetZ2 < -radiusY || offsetZ2 > radiusY;
			Vec2f start = null;
			Vec2f end = null;
			if (flag) {
				start = this.calculateIntercept(offsetX2, offsetZ2, offsetX, offsetZ, -radiusX - 1, -radiusY - 1, radiusX + 1, radiusY + 1);
			} else {
				start = new Vec2f(offsetX, offsetZ);
			}
			if (flag2) {
				end = this.calculateIntercept(offsetX, offsetZ, offsetX2, offsetZ2, -radiusX - 1, -radiusY - 1, radiusX + 1, radiusY + 1);
			} else {
				end = new Vec2f(offsetX2, offsetZ2);
			}
			if (start != null && end != null) {
				GL11.glVertex2d(start.x, start.y);
				GL11.glVertex2d(end.x, end.y);
			}
		}
		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		// draw nodes
		GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
		this.mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/path_map.png"));
		GL11.glBegin(GL11.GL_QUADS);
		for (Path.PathNode node : path.getNodes()) {
			int offsetX = node.getPos().getX() - centerX;
			int offsetZ = node.getPos().getZ() - centerY;
			if (offsetX < -radiusX || offsetX > radiusX || offsetZ < -radiusY || offsetZ > radiusY) {
				continue;
			}
			if (node == rootNode) {
				this.drawnRootNode(offsetX, offsetZ);
			} else if (this.blacklistedPrevNodes.contains(node.getIndex())) {
				this.drawSelectedNode(offsetX, offsetZ);
			} else {
				this.drawNode(offsetX, offsetZ);
			}
		}
		int offsetX = this.x - centerX;
		int offsetZ = this.z - centerY;
		if (offsetX >= -radiusX && offsetX <= radiusX && offsetZ >= -radiusY && offsetZ <= radiusY) {
			this.drawNewNode(offsetX, offsetZ);
		}
		GL11.glEnd();

		GL11.glPopMatrix();
	}

	@Nullable
	private Vec2f calculateIntercept(float x1, float y1, float x2, float y2, float minX, float minY, float maxX, float maxY) {
		Vec2f result = null;
		Vec2f vec = this.intersectionPoint(x1, y1, x2, y2, minX, minY, minX, maxY);
		if (vec != null && this.isInside(vec, minX, minY, maxX, maxY)) {
			result = vec;
		}
		vec = this.intersectionPoint(x1, y1, x2, y2, minX, minY, maxX, minY);
		if (vec != null && this.isInside(vec, minX, minY, maxX, maxY)) {
			result = this.getNearest(x2, y2, vec, result);
		}
		vec = this.intersectionPoint(x1, y1, x2, y2, minX, maxY, maxX, maxY);
		if (vec != null && this.isInside(vec, minX, minY, maxX, maxY)) {
			result = this.getNearest(x2, y2, vec, result);
		}
		vec = this.intersectionPoint(x1, y1, x2, y2, maxX, minY, maxX, maxY);
		if (vec != null && this.isInside(vec, minX, minY, maxX, maxY)) {
			result = this.getNearest(x2, y2, vec, result);
		}
		return result;
	}

	@Nullable
	private Vec2f intersectionPoint(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float f1 = x2 - x1;
		float f2 = y2 - y1;
		float f3 = x4 - x3;
		float f4 = y4 - y3;
		float f5 = f1 * f4 - f2 * f3;
		if (f5 == 0.0F) {
			return null;
		}
		float f6 = x1 * y2 - y1 * x2;
		float f7 = x3 * y4 - y3 * x4;
		float f8 = 1.0F / f5;
		return new Vec2f((f7 * f1 - f6 * f3) * f8, (f7 * f2 - f6 * f4) * f8);
	}

	@Nullable
	private Vec2f getNearest(float x1, float y1, @Nullable Vec2f vec1, @Nullable Vec2f vec2) {
		if (vec1 == null) {
			return vec2;
		}
		if (vec2 == null) {
			return vec1;
		}
		float f1 = vec1.x - x1;
		float f2 = vec1.y - y1;
		float f3 = f1 * f1 + f2 * f2;
		f1 = vec2.x - x1;
		f2 = vec2.y - y1;
		float f4 = f1 * f1 + f2 * f2;
		if (f3 <= f4) {
			return vec1;
		} else {
			return vec2;
		}
	}

	private boolean isInside(Vec2f vec, float minX, float minY, float maxX, float maxY) {
		return vec.x >= minX - 0.001F && vec.x <= maxX + 0.001F && vec.y >= minY - 0.001F && vec.y <= maxY + 0.001F;
	}

	private void drawNode(double x, double y) {
		GL11.glTexCoord2d(0.0D, 0.5D);
		GL11.glVertex2d(x - 1.0D, y + 1.0D);
		GL11.glTexCoord2d(0.5D, 0.5D);
		GL11.glVertex2d(x + 1.0D, y + 1.0D);
		GL11.glTexCoord2d(0.5D, 0.0D);
		GL11.glVertex2d(x + 1.0D, y - 1.0D);
		GL11.glTexCoord2d(0.0D, 0.0D);
		GL11.glVertex2d(x - 1.0D, y - 1.0D);
	}

	private void drawSelectedNode(double x, double y) {
		GL11.glTexCoord2d(0.0D, 1.0D);
		GL11.glVertex2d(x - 1.0D, y + 1.0D);
		GL11.glTexCoord2d(0.5D, 1.0D);
		GL11.glVertex2d(x + 1.0D, y + 1.0D);
		GL11.glTexCoord2d(0.5D, 0.5D);
		GL11.glVertex2d(x + 1.0D, y - 1.0D);
		GL11.glTexCoord2d(0.0D, 0.5D);
		GL11.glVertex2d(x - 1.0D, y - 1.0D);
	}

	private void drawnRootNode(double x, double y) {
		GL11.glTexCoord2d(0.5D, 0.5D);
		GL11.glVertex2d(x - 1.0D, y + 1.0D);
		GL11.glTexCoord2d(1.0D, 0.5D);
		GL11.glVertex2d(x + 1.0D, y + 1.0D);
		GL11.glTexCoord2d(1.0D, 0.0D);
		GL11.glVertex2d(x + 1.0D, y - 1.0D);
		GL11.glTexCoord2d(0.5D, 0.0D);
		GL11.glVertex2d(x - 1.0D, y - 1.0D);
	}

	private void drawNewNode(double x, double y) {
		GL11.glTexCoord2d(0.5D, 1.0D);
		GL11.glVertex2d(x - 1.0D, y + 1.0D);
		GL11.glTexCoord2d(1.0D, 1.0D);
		GL11.glVertex2d(x + 1.0D, y + 1.0D);
		GL11.glTexCoord2d(1.0D, 0.5D);
		GL11.glVertex2d(x + 1.0D, y - 1.0D);
		GL11.glTexCoord2d(0.5D, 0.5D);
		GL11.glVertex2d(x - 1.0D, y - 1.0D);
	}

}
