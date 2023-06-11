package team.cqr.cqrepoured.client.gui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.util.InputMappings;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.entity.pathfinding.CQRNPCPath;
import team.cqr.cqrepoured.network.client.packet.CPacketAddPathNode;

@OnlyIn(Dist.CLIENT)
public class GuiAddPathNode extends Screen {

	private final Hand hand;
	private final int rootNodeIndex;
	private final BlockPos pos;
	private final List<TextFieldWidget> textFieldList = new ArrayList<>();

	private Button buttonConfirm;
	private Button buttonCancel;
	private TextFieldWidget textFieldX;
	private TextFieldWidget textFieldY;
	private TextFieldWidget textFieldZ;
	private TextFieldWidget textFieldWaitingTimeMin;
	private TextFieldWidget textFieldWaitingTimeMax;
	private TextFieldWidget textFieldWaitingRotation;
	private TextFieldWidget textFieldWeight;
	private TextFieldWidget textFieldTimeMin;
	private TextFieldWidget textFieldTimeMax;
	private CheckboxButton checkBoxBidirectional;
	private final IntList blacklistedPrevNodes = new IntArrayList();

	private int x;
	@SuppressWarnings("unused")
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

	public GuiAddPathNode(Hand hand, int rootNode, BlockPos pos) {
		super(new TranslationTextComponent("gui.cqrepoured.add_path_node"));
		this.hand = hand;
		this.rootNodeIndex = rootNode;
		this.pos = pos;
	}

	@Override
	public void init() {
		this.textFieldList.clear();
		super.init();
		int id = 0;

		int xOffset = -43;
		int yOffset = 10 * 16 / 2 * -1; // elementCount * elementOffset / 2 * -1
		int i = 0;
		this.textFieldX = new TextFieldWidget(this.font, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, new TranslationTextComponent("add_path_node.text_field.x"));
		this.textFieldY = new TextFieldWidget(this.font, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, new TranslationTextComponent("add_path_node.text_field.y"));
		this.textFieldZ = new TextFieldWidget(this.font, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, new TranslationTextComponent("add_path_node.text_field.z"));
		this.textFieldWaitingTimeMin = new TextFieldWidget(this.font, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, new TranslationTextComponent("add_path_node.text_field.waiting_time_min"));
		this.textFieldWaitingTimeMax = new TextFieldWidget(this.font, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, new TranslationTextComponent("add_path_node.text_field.waiting_time_max"));
		this.textFieldWaitingRotation = new TextFieldWidget(this.font, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, new TranslationTextComponent("add_path_node.text_field.waiting_rotation"));
		this.textFieldWeight = new TextFieldWidget(this.font, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, new TranslationTextComponent("add_path_node.text_field.weight"));
		this.textFieldTimeMin = new TextFieldWidget(this.font, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, new TranslationTextComponent("add_path_node.text_field.time_min"));
		this.textFieldTimeMax = new TextFieldWidget(this.font, this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 40, 12, new TranslationTextComponent("add_path_node.text_field.time_max"));
		this.checkBoxBidirectional = this.addButton(new CheckboxButton(this.width / 2 + 1 + xOffset, this.height / 2 + 1 + yOffset + i++ * 16, 20, 20, new TranslationTextComponent("add_path_node.bidirectional"), true));

		this.buttonConfirm = this.addButton(new Button(this.width / 2 - 102, this.height - 36, 100, 16, new TranslationTextComponent("add_path_node.confirm"), (p_214271_1_) -> this.performConfirm()));
		this.buttonCancel = this.addButton(new Button(this.width / 2 + 2, this.height - 36, 100, 16, new TranslationTextComponent("add_path_node.cancel"), (p_214271_1_) -> this.minecraft.setScreen((Screen)null)));

		this.textFieldX.setValue(String.valueOf(this.pos.getX()));
		this.textFieldY.setValue(String.valueOf(this.pos.getY()));
		this.textFieldZ.setValue(String.valueOf(this.pos.getZ()));
		this.textFieldWaitingTimeMin.setValue("0");
		this.textFieldWaitingTimeMax.setValue("0");
		this.textFieldWaitingRotation.setValue("0");
		this.textFieldWeight.setValue("10");
		this.textFieldTimeMin.setValue("0");
		this.textFieldTimeMax.setValue("24000");

		this.children.add(this.textFieldX);
		this.children.add(this.textFieldY);
		this.children.add(this.textFieldZ);
		this.children.add(this.textFieldWaitingTimeMin);
		this.children.add(this.textFieldWaitingTimeMax);
		this.children.add(this.textFieldWaitingRotation);
		this.children.add(this.textFieldWeight);
		this.children.add(this.textFieldTimeMin);
		this.children.add(this.textFieldTimeMax);

		this.textFieldList.add(this.textFieldX);
		this.textFieldList.add(this.textFieldY);
		this.textFieldList.add(this.textFieldZ);
		this.textFieldList.add(this.textFieldWaitingTimeMin);
		this.textFieldList.add(this.textFieldWaitingTimeMax);
		this.textFieldList.add(this.textFieldWaitingRotation);
		this.textFieldList.add(this.textFieldWeight);
		this.textFieldList.add(this.textFieldTimeMin);
		this.textFieldList.add(this.textFieldTimeMax);
		this.blacklistedPrevNodes.clear();

		this.x = this.pos.getX();
		this.y = this.pos.getY();
		this.z = this.pos.getZ();
		this.pathMapX = this.width / 2 - 192;
		this.pathMapY = this.height / 2 - 80;
		this.pathMapWidth = 130;
		this.pathMapHeight = 130;
		ItemStack stack = this.minecraft.player.getItemInHand(this.hand);
		CQRNPCPath path = null;//ItemPathTool.getPath(stack);
		if (path != null) {
			CQRNPCPath.PathNode rootNode = path.getNode(this.rootNodeIndex);
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

	private void performConfirm() {
		try {
			int posX = Integer.parseInt(this.textFieldX.getValue());
			int posY = Integer.parseInt(this.textFieldY.getValue());
			int posZ = Integer.parseInt(this.textFieldZ.getValue());
			int waitingTimeMin = Integer.parseInt(this.textFieldWaitingTimeMin.getValue());
			int waitingTimeMax = Integer.parseInt(this.textFieldWaitingTimeMax.getValue());
			float waitingRotation = Integer.parseInt(this.textFieldWaitingRotation.getValue());
			int weight = Integer.parseInt(this.textFieldWeight.getValue());
			int timeMin = Integer.parseInt(this.textFieldTimeMin.getValue());
			int timeMax = Integer.parseInt(this.textFieldTimeMax.getValue());
			boolean bidirectional = this.checkBoxBidirectional.selected();
			CQRMain.NETWORK.sendToServer(new CPacketAddPathNode(this.hand, this.rootNodeIndex, new BlockPos(posX, posY, posZ), waitingTimeMin, waitingTimeMax, waitingRotation, weight, timeMin, timeMax, bidirectional, this.blacklistedPrevNodes));
		} catch (NumberFormatException e) {
			this.minecraft.player.sendMessage(new StringTextComponent("Invalid path node arguments!"), this.minecraft.player.getUUID());
		}
		this.minecraft.setScreen((Screen)null);
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		InputMappings.Input keyInput = InputMappings.getKey(pKeyCode, pScanCode);
		boolean textFieldFocused = false;
		for (TextFieldWidget textField : this.textFieldList) {
			if (textField.isFocused()) {
				textFieldFocused = true;
				break;
			}
		}
		if (textFieldFocused) {
			if (pKeyCode == 1) {
				for (TextFieldWidget textField : this.textFieldList) {
					textField.setFocus(false);
				}
			} else {
				for (TextFieldWidget textField : this.textFieldList) {
					textField.keyPressed(pKeyCode, pScanCode, pModifiers);
				}
				if (this.textFieldX.isFocused()) {
					try {
						this.x = Integer.parseInt(this.textFieldX.getMessage().getString());
					} catch (NumberFormatException e) {
						// ignore
					}
				}
				if (this.textFieldY.isFocused()) {
					try {
						this.y = Integer.parseInt(this.textFieldY.getMessage().getString());
					} catch (NumberFormatException e) {
						// ignore
					}
				}
				if (this.textFieldZ.isFocused()) {
					try {
						this.z = Integer.parseInt(this.textFieldZ.getMessage().getString());
					} catch (NumberFormatException e) {
						// ignore
					}
				}
			}
		} else if (pKeyCode == 1 || this.minecraft.options.keyInventory.isActiveAndMatches(keyInput)) {
			this.minecraft.setScreen((Screen)null);
		} else {
			super.keyPressed(pKeyCode, pScanCode, pModifiers);
		}
		return true;
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
		if (pDelta != 0) {
			int scrollAmount = (int) pDelta / 120;
			if (this.textFieldX.isMouseOver(pMouseX, pMouseY)) {
				try {
					int number = Integer.parseInt(this.textFieldX.getMessage().getString()) + scrollAmount;
					this.textFieldX.setValue(String.valueOf(number));
					this.x = number;
					return true;
				} catch (NumberFormatException e) {
					// ignore
				}
			}
			if (this.textFieldY.isMouseOver(pMouseX, pMouseY)) {
				try {
					int number = Integer.parseInt(this.textFieldY.getMessage().getString()) + scrollAmount;
					this.textFieldY.setValue(String.valueOf(number));
					this.y = number;
					return true;
				} catch (NumberFormatException e) {
					// ignore
				}
			}
			if (this.textFieldZ.isMouseOver(pMouseX, pMouseY)) {
				try {
					int number = Integer.parseInt(this.textFieldZ.getMessage().getString()) + scrollAmount;
					this.textFieldZ.setValue(String.valueOf(number));
					this.z = number;
					return true;
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		}
		return super.mouseScrolled(pMouseX, pMouseY, pDelta);
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

		if (this.pathMapClicked && pButton == 0) {
			double deltaX = (pMouseX - this.lastMouseX) * 0.5F + this.mouseOverheadX;
			double deltaY = (pMouseY - this.lastMouseY) * 0.5F + this.mouseOverheadY;
			this.centerOffsetX -= (int) deltaX;
			this.centerOffsetY -= (int) deltaY;
			this.mouseOverheadX = (float) deltaX % 1.0F;
			this.mouseOverheadY = (float) deltaY % 1.0F;
			this.lastMouseX = (int) pMouseX;
			this.lastMouseY = (int) pMouseY;
			return true;
		}
		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int mouseButton){
		super.mouseClicked(pMouseX, pMouseY, mouseButton);

		for (TextFieldWidget textField : this.textFieldList) {
			textField.mouseClicked(pMouseX, pMouseY, mouseButton);
		}

		int minX = this.pathMapX;
		int minY = this.pathMapY;
		int maxX = this.pathMapX + this.pathMapWidth;
		int maxY = this.pathMapY + this.pathMapHeight;
		if (mouseButton == 0 && pMouseX >= minX && pMouseX <= maxX && pMouseY >= minY && pMouseY <= maxY) {
			this.pathMapClicked = true;
			this.lastMouseX = (int) pMouseX;
			this.lastMouseY = (int) pMouseY;
		} else {
			this.pathMapClicked = false;
		}
		if (mouseButton == 0) {
			CQRNPCPath.PathNode clickedNode = this.getNodeAt((int) pMouseX, (int) pMouseY);
			if (clickedNode != null) {
				CQRNPCPath path = null;//ItemPathTool.getPath(this.minecraft.player.getItemInHand(this.hand));
				CQRNPCPath.PathNode rootNode = path.getNode(this.rootNodeIndex);
				if (rootNode != null && clickedNode.getConnectedNodes().contains(this.rootNodeIndex)) {
					if (!this.blacklistedPrevNodes.contains(clickedNode.getIndex())) {
						this.blacklistedPrevNodes.add(clickedNode.getIndex());
					} else {
						this.blacklistedPrevNodes.rem(clickedNode.getIndex());
					}
				}
			}
		}
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		for (TextFieldWidget textField : this.textFieldList) {
			textField.tick();
		}
	}

	@Override
	public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBackground(pMatrixStack);
		//drawCenteredString(pMatrixStack, this.font, "Add Path Node (Index: " + ItemPathTool.getPath(this.minecraft.player.getItemInHand(this.hand)).getSize() + ")", this.width / 2, 20, 0xFFFFFF);

		for (TextFieldWidget textField : this.textFieldList) {
			textField.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;
		}

		int xOffset = 1;
		int yOffset = 10 * 16 / 2 * -1 + 3; // elementCount * elementOffset / 2 * -1 + 3
		int i = 0;
		drawString(pMatrixStack, this.font, "Node X", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0);
		drawString(pMatrixStack, this.font, "Node Y", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0);
		drawString(pMatrixStack, this.font, "Node Z", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0);
		drawString(pMatrixStack, this.font, "Waiting Time Min", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0);
		drawString(pMatrixStack, this.font, "Waiting Time Max", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0);
		drawString(pMatrixStack, this.font, "Waiting Rotation", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0);
		drawString(pMatrixStack, this.font, "Weight", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0);
		drawString(pMatrixStack, this.font, "Time Min", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0);
		drawString(pMatrixStack, this.font, "Time Max", this.width / 2 + xOffset, this.height / 2 + yOffset + i++ * 16, 0xE0E0E0);

		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

		this.drawPathMap(this.pathMapX, this.pathMapY, this.pathMapWidth, this.pathMapHeight, this.centerOffsetX, this.centerOffsetY);
		drawString(pMatrixStack, this.font, "Help?", this.pathMapX, this.pathMapY + this.pathMapHeight + 4, 0xE0E0E0);

		CQRNPCPath.PathNode selectedNode = this.getNodeAt(pMouseX, pMouseY);
		if (selectedNode != null) {
			this.renderTooltip(pMatrixStack, new StringTextComponent(String.format("Index: %d, %s", selectedNode.getIndex(), selectedNode.getPos())), pMouseX, pMouseY);
		}

		if (pMouseX >= this.pathMapX && pMouseX <= this.pathMapX + this.font.width("Help?") && pMouseY >= this.pathMapY + this.pathMapHeight + 4 && pMouseY <= this.pathMapY + this.pathMapHeight + 12) {
			List<ITextComponent> tooltip = new ArrayList<>();
			tooltip.add(new StringTextComponent("The path map shows the current path from above and visualizes the 'new node'."));
			tooltip.add(new StringTextComponent("Also it allows you to select 'blacklisted previous nodes' for the 'new node'. That means when an entity is at one of the nodes connected with the 'new node' and comes from a 'blacklisted previous node' it won't go to the 'new node'."));
			tooltip.add(new StringTextComponent(""));
			tooltip.add(new StringTextComponent("Blue Node: New Node"));
			tooltip.add(new StringTextComponent("Black Node: Selected Node"));
			tooltip.add(new StringTextComponent("Grey Node: Normal Node"));
			tooltip.add(new StringTextComponent("Red Node: Blacklisted Previous Node"));
			this.renderComponentTooltip(pMatrixStack, tooltip, pMouseX, pMouseY);
		}

		if (GuiHelper.isMouseOver(pMouseX, pMouseY, this.textFieldWaitingTimeMin)) {
			this.renderTooltip(pMatrixStack, new StringTextComponent("When reaching this node this defines how long the entity waits at least before walking to the next node. (min: 0, max: 24000 ticks)"), pMouseX, pMouseY);
		}
		if (this.textFieldWaitingTimeMax.isMouseOver(pMouseX, pMouseY)) {
			this.renderTooltip(pMatrixStack, new StringTextComponent("When reaching this node this defines how long the entity waits at most before walking to the next node. (min: 0, max: 24000 ticks)"), pMouseX, pMouseY);
		}
		if (this.textFieldWaitingRotation.isMouseOver(pMouseX, pMouseY)) {
			this.renderTooltip(pMatrixStack, new StringTextComponent("When waiting at this node this defines where the entity should look. (min: 0, max: 360 degree)"), pMouseX, pMouseY);
		}
		if (this.textFieldWeight.isMouseOver(pMouseX, pMouseY)) {
			this.renderTooltip(pMatrixStack, new StringTextComponent("The weight that this node is selected as the next node when there are multiple options. (min: 1, max: 10000)"), pMouseX, pMouseY);
		}
		if (this.textFieldTimeMin.isMouseOver(pMouseX, pMouseY)) {
			this.renderTooltip(pMatrixStack, new StringTextComponent("The node can only be selected as the next node when the time is between 'Time Min' and 'Time Max'. (0=morning, 6000=noon, 12000=evening, 18000=midnight) (min: 0, max: 24000 ticks)"), pMouseX, pMouseY);
		}
		if (this.textFieldTimeMax.isMouseOver(pMouseX, pMouseY)) {
			this.renderTooltip(pMatrixStack, new StringTextComponent("The node can only be selected as the next node when the time is between 'Time Min' and 'Time Max'. (0=morning, 6000=noon, 12000=evening, 18000=midnight) (min: 0, max: 24000 ticks)"), pMouseX, pMouseY);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Nullable
	private CQRNPCPath.PathNode getNodeAt(int pMouseX, int pMouseY) {
		if (pMouseX < this.pathMapX || pMouseX > this.pathMapX + this.pathMapWidth) {
			return null;
		}
		if (pMouseY < this.pathMapY || pMouseY > this.pathMapY + this.pathMapHeight) {
			return null;
		}
		CQRNPCPath path = null;//ItemPathTool.getPath(this.minecraft.player.getItemInHand(this.hand));
		CQRNPCPath.PathNode clickedNode = null;
		int posX = (pMouseX - this.pathMapX - (this.pathMapWidth / 2)) / 2 + this.centerOffsetX;
		int posY = (pMouseY - this.pathMapY - (this.pathMapHeight / 2)) / 2 + this.centerOffsetY;
		for (CQRNPCPath.PathNode node : path.getNodes()) {
			if (node.getPos().getX() == posX && node.getPos().getZ() == posY) {
				clickedNode = node;
				break;
			}
		}
		for (CQRNPCPath.PathNode node : path.getNodes()) {
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
		CQRNPCPath path = null;//ItemPathTool.getPath(this.minecraft.player.getItemInHand(this.hand));
		CQRNPCPath.PathNode rootNode = path.getNode(this.rootNodeIndex);

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
		GL11.glLineWidth((float) Minecraft.getInstance().getWindow().getGuiScale());
		GL11.glBegin(GL11.GL_LINES);
		for (CQRNPCPath.PathNode node : path.getNodes()) {
			int offsetX = node.getPos().getX() - centerX;
			int offsetZ = node.getPos().getZ() - centerY;
			boolean flag = offsetX < -radiusX || offsetX > radiusX || offsetZ < -radiusY || offsetZ > radiusY;
			for (int index : node.getConnectedNodes()) {
				CQRNPCPath.PathNode connectedNode = path.getNode(index);
				int offsetX2 = connectedNode.getPos().getX() - centerX;
				int offsetZ2 = connectedNode.getPos().getZ() - centerY;
				boolean flag2 = offsetX2 < -radiusX || offsetX2 > radiusX || offsetZ2 < -radiusY || offsetZ2 > radiusY;
				Vector2f start = null;
				Vector2f end = null;
				if (flag) {
					start = this.calculateIntercept(offsetX2, offsetZ2, offsetX, offsetZ, -radiusX - 1, -radiusY - 1, radiusX + 1, radiusY + 1);
				} else {
					start = new Vector2f(offsetX, offsetZ);
				}
				if (flag2) {
					end = this.calculateIntercept(offsetX, offsetZ, offsetX2, offsetZ2, -radiusX - 1, -radiusY - 1, radiusX + 1, radiusY + 1);
				} else {
					end = new Vector2f(offsetX2, offsetZ2);
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
			Vector2f start = null;
			Vector2f end = null;
			if (flag) {
				start = this.calculateIntercept(offsetX2, offsetZ2, offsetX, offsetZ, -radiusX - 1, -radiusY - 1, radiusX + 1, radiusY + 1);
			} else {
				start = new Vector2f(offsetX, offsetZ);
			}
			if (flag2) {
				end = this.calculateIntercept(offsetX, offsetZ, offsetX2, offsetZ2, -radiusX - 1, -radiusY - 1, radiusX + 1, radiusY + 1);
			} else {
				end = new Vector2f(offsetX2, offsetZ2);
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
//		this.minecraft.getTextureManager().bindTexture(new ResourceLocation(CQRMain.MODID, "textures/gui/path_map.png"));
		GL11.glBegin(GL11.GL_QUADS);
		for (CQRNPCPath.PathNode node : path.getNodes()) {
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
	private Vector2f calculateIntercept(float x1, float y1, float x2, float y2, float minX, float minY, float maxX, float maxY) {
		Vector2f result = null;
		Vector2f vec = this.intersectionPoint(x1, y1, x2, y2, minX, minY, minX, maxY);
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
	private Vector2f intersectionPoint(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
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
		return new Vector2f((f7 * f1 - f6 * f3) * f8, (f7 * f2 - f6 * f4) * f8);
	}

	@Nullable
	private Vector2f getNearest(float x1, float y1, @Nullable Vector2f vec1, @Nullable Vector2f vec2) {
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

	private boolean isInside(Vector2f vec, float minX, float minY, float maxX, float maxY) {
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
