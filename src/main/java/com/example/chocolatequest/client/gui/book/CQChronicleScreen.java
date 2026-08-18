package com.example.chocolatequest.client.gui.book;

import com.example.chocolatequest.entity.boss.EntityCQRDragon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class CQChronicleScreen extends Screen {

    public static final int BOOK_WIDTH = 420;
    public static final int BOOK_HEIGHT = 260;
    public static final int TAB_WIDTH = 26;
    public static final int TAB_HEIGHT = 24;

    private final List<ChronicleData.Chapter> chapters;
    private int currentChapter = 0;
    private float openProgress = 0.0F;

    private final Map<EntityType<?>, LivingEntity> entityCache = new HashMap<>();
    private float entityRotation = 0.0F;
    private float rotationVelocity = 1.2F;
    private boolean isDraggingEntity = false;

    public CQChronicleScreen() {
        super(Component.literal("Chocolate Quest Chronicle"));
        this.chapters = ChronicleData.buildChapters();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        this.openProgress = 0.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isDraggingEntity) {
            this.entityRotation += this.rotationVelocity;
            if (this.rotationVelocity > 1.2F) {
                this.rotationVelocity *= 0.95F;
            } else if (this.rotationVelocity < 1.2F) {
                this.rotationVelocity = 1.2F;
            }
        }

        // Smooth cubic ease-out opening animation
        if (this.openProgress < 1.0F) {
            this.openProgress += (1.0F - this.openProgress) * 0.35F;
            if (1.0F - this.openProgress < 0.005F) {
                this.openProgress = 1.0F;
            }
        }

        // Tick cached living entities so animations update
        for (LivingEntity living : this.entityCache.values()) {
            living.tickCount++;
        }
    }

    private void changeChapter(int target) {
        if (target >= 0 && target < this.chapters.size() && target != this.currentChapter) {
            this.currentChapter = target;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        float smoothOpen = Mth.lerp(partialTick, this.openProgress, Math.min(1.0F, this.openProgress + 0.05F));
        float easeOpen = 1.0F - (float) Math.pow(1.0F - smoothOpen, 3);
        float scale = 0.85F + 0.15F * easeOpen;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centerX, centerY, 50.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.pose().translate(-BOOK_WIDTH / 2.0F, -BOOK_HEIGHT / 2.0F, 0.0F);

        int bookX = 0;
        int bookY = 0;

        renderTabs(guiGraphics, bookX, bookY, mouseX, mouseY);

        renderBookBase(guiGraphics, bookX, bookY);

        ChronicleData.Chapter chapter = this.chapters.get(this.currentChapter);
        renderPage(guiGraphics, chapter.leftPage, bookX + 18, bookY + 16, 178, 228, mouseX, mouseY, true);
        renderPage(guiGraphics, chapter.rightPage, bookX + 224, bookY + 16, 178, 228, mouseX, mouseY, false);

        renderNavigationControls(guiGraphics, bookX, bookY, mouseX, mouseY);
        renderHangingRibbon(guiGraphics, bookX, bookY);

        guiGraphics.pose().popPose();

        renderTooltips(guiGraphics, mouseX, mouseY, (int)(centerX - (BOOK_WIDTH / 2.0F) * scale), (int)(centerY - (BOOK_HEIGHT / 2.0F) * scale));
    }

    private void renderBookBase(GuiGraphics guiGraphics, int x, int y) {
        // Drop Shadow
        guiGraphics.fill(x - 8, y - 8, x + BOOK_WIDTH + 8, y + BOOK_HEIGHT + 8, 0x66000000);

        // Heavy Antique Leather Binding
        guiGraphics.fill(x - 6, y - 6, x + BOOK_WIDTH + 6, y + BOOK_HEIGHT + 6, 0xFF1C0D06);
        guiGraphics.fill(x - 4, y - 4, x + BOOK_WIDTH + 4, y + BOOK_HEIGHT + 4, 0xFF2D160A);
        guiGraphics.fill(x - 2, y - 2, x + BOOK_WIDTH + 2, y + BOOK_HEIGHT + 2, 0xFF3F200E);

        // Leather Stitches on Edges
        int stitchColor = 0xFF6E4327;
        for (int sx = x + 12; sx < x + BOOK_WIDTH - 12; sx += 8) {
            guiGraphics.fill(sx, y - 4, sx + 3, y - 3, stitchColor);
            guiGraphics.fill(sx, y + BOOK_HEIGHT + 3, sx + 3, y + BOOK_HEIGHT + 4, stitchColor);
        }

        // Stepped Paper Stacks (Left & Right)
        guiGraphics.fill(x + 6, y + 5, x + 10, y + BOOK_HEIGHT - 5, 0xFFBFA77A);
        guiGraphics.fill(x + 10, y + 6, x + 13, y + BOOK_HEIGHT - 6, 0xFFD2BD93);
        guiGraphics.fill(x + 13, y + 7, x + 16, y + BOOK_HEIGHT - 7, 0xFFE5D5B3);

        guiGraphics.fill(x + BOOK_WIDTH - 10, y + 5, x + BOOK_WIDTH - 6, y + BOOK_HEIGHT - 5, 0xFFBFA77A);
        guiGraphics.fill(x + BOOK_WIDTH - 13, y + 6, x + BOOK_WIDTH - 10, y + BOOK_HEIGHT - 6, 0xFFD2BD93);
        guiGraphics.fill(x + BOOK_WIDTH - 16, y + 7, x + BOOK_WIDTH - 13, y + BOOK_HEIGHT - 7, 0xFFE5D5B3);

        // Left Parchment Page Base
        guiGraphics.fill(x + 15, y + 8, x + BOOK_WIDTH / 2 - 8, y + BOOK_HEIGHT - 8, 0xFFDECB9F);
        guiGraphics.fill(x + 17, y + 10, x + BOOK_WIDTH / 2 - 10, y + BOOK_HEIGHT - 10, 0xFFF7EED8);

        // Right Parchment Page Base
        guiGraphics.fill(x + BOOK_WIDTH / 2 + 8, y + 8, x + BOOK_WIDTH - 16, y + BOOK_HEIGHT - 8, 0xFFDECB9F);
        guiGraphics.fill(x + BOOK_WIDTH / 2 + 10, y + 10, x + BOOK_WIDTH - 18, y + BOOK_HEIGHT - 10, 0xFFF7EED8);

        // Crease Shading near spine
        guiGraphics.fill(x + BOOK_WIDTH / 2 - 14, y + 10, x + BOOK_WIDTH / 2 - 10, y + BOOK_HEIGHT - 10, 0x22000000);
        guiGraphics.fill(x + BOOK_WIDTH / 2 + 10, y + 10, x + BOOK_WIDTH / 2 + 14, y + BOOK_HEIGHT - 10, 0x22000000);

        // Center Spine & Groove
        guiGraphics.fill(x + BOOK_WIDTH / 2 - 8, y + 4, x + BOOK_WIDTH / 2 + 8, y + BOOK_HEIGHT - 4, 0xFF1C0D06);
        guiGraphics.fill(x + BOOK_WIDTH / 2 - 3, y + 6, x + BOOK_WIDTH / 2 + 3, y + BOOK_HEIGHT - 6, 0xFF4A2511);

        // Gilded Center Seam Ribbon
        guiGraphics.fill(x + BOOK_WIDTH / 2 - 1, y + 8, x + BOOK_WIDTH / 2 + 1, y + BOOK_HEIGHT - 8, 0xFFD4AF37);

        // Pixel-Art Golden Clasps with Rivets on all 4 Corners
        drawMinecraftCorner(guiGraphics, x - 2, y - 2, true, true);
        drawMinecraftCorner(guiGraphics, x + BOOK_WIDTH + 2, y - 2, false, true);
        drawMinecraftCorner(guiGraphics, x - 2, y + BOOK_HEIGHT + 2, true, false);
        drawMinecraftCorner(guiGraphics, x + BOOK_WIDTH + 2, y + BOOK_HEIGHT + 2, false, false);
    }

    private void drawMinecraftCorner(GuiGraphics guiGraphics, int cx, int cy, boolean left, boolean top) {
        int goldBright = 0xFFF4D03F;
        int goldMid = 0xFFD4AC0D;
        int goldDark = 0xFF7D6608;
        int rivet = 0xFFFFFFFF;

        int dx = left ? 1 : -1;
        int dy = top ? 1 : -1;

        // Pixelated L-bracket
        guiGraphics.fill(cx, cy, cx + (dx * 16), cy + (dy * 4), goldMid);
        guiGraphics.fill(cx, cy, cx + (dx * 4), cy + (dy * 16), goldMid);
        guiGraphics.fill(cx + (dx * 1), cy + (dy * 1), cx + (dx * 15), cy + (dy * 3), goldBright);
        guiGraphics.fill(cx + (dx * 1), cy + (dy * 1), cx + (dx * 3), cy + (dy * 15), goldBright);
        guiGraphics.fill(cx + (dx * 4), cy + (dy * 4), cx + (dx * 10), cy + (dy * 10), goldDark);

        // Center Rivet Pixel
        guiGraphics.fill(cx + (dx * 5), cy + (dy * 5), cx + (dx * 7), cy + (dy * 7), rivet);
    }

    private void renderHangingRibbon(GuiGraphics guiGraphics, int bookX, int bookY) {
        int spineX = bookX + BOOK_WIDTH / 2;
        int ribbonY = bookY + BOOK_HEIGHT - 6;

        guiGraphics.fill(spineX - 4, ribbonY, spineX + 4, ribbonY + 16, 0xFF8B0000);
        guiGraphics.fill(spineX - 3, ribbonY, spineX + 3, ribbonY + 15, 0xFFB22222);

        // Gold Tassel end
        guiGraphics.fill(spineX - 4, ribbonY + 16, spineX + 4, ribbonY + 20, 0xFFD4AF37);
        guiGraphics.fill(spineX - 2, ribbonY + 20, spineX + 2, ribbonY + 24, 0xFFC8A848);
    }

    private void renderTabs(GuiGraphics guiGraphics, int bookX, int bookY, int mouseX, int mouseY) {
        ChronicleData.Category[] categories = ChronicleData.Category.values();
        int tabY = bookY + 14;

        int screenCenterX = this.width / 2;
        int screenCenterY = this.height / 2;
        int originX = screenCenterX - BOOK_WIDTH / 2;
        int originY = screenCenterY - BOOK_HEIGHT / 2;

        for (ChronicleData.Category cat : categories) {
            boolean isActive = this.chapters.get(this.currentChapter).category == cat;
            int tabX = bookX + BOOK_WIDTH;
            int relativeMouseX = mouseX - originX;
            int relativeMouseY = mouseY - originY;

            boolean isHovered = relativeMouseX >= tabX && relativeMouseX <= tabX + TAB_WIDTH + 10
                    && relativeMouseY >= tabY && relativeMouseY <= tabY + TAB_HEIGHT;

            int tabExtension = isActive ? 10 : (isHovered ? 6 : 2);

            guiGraphics.fill(tabX, tabY, tabX + TAB_WIDTH + tabExtension, tabY + TAB_HEIGHT, cat.color | 0xFF000000);

            // Tab golden border
            guiGraphics.fill(tabX + TAB_WIDTH + tabExtension - 2, tabY, tabX + TAB_WIDTH + tabExtension, tabY + TAB_HEIGHT, 0xFFD4AF37);
            guiGraphics.fill(tabX, tabY, tabX + TAB_WIDTH + tabExtension, tabY + 1, 0xFFD4AF37);
            guiGraphics.fill(tabX, tabY + TAB_HEIGHT - 1, tabX + TAB_WIDTH + tabExtension, tabY + TAB_HEIGHT, 0xFFD4AF37);

            // Category letter
            guiGraphics.drawCenteredString(this.font, "§l" + cat.letter + "§r", tabX + (TAB_WIDTH + tabExtension) / 2, tabY + 8, 0xFFFFFFFF);

            tabY += TAB_HEIGHT + 3;
        }
    }

    private void renderPage(GuiGraphics guiGraphics, ChronicleData.PageContent page, int x, int y, int w, int h, int mouseX, int mouseY, boolean isLeft) {
        if (page == null) return;

        // Inset Border
        guiGraphics.fill(x + 4, y + 4, x + w - 4, y + 5, 0xFFD6C09A);
        guiGraphics.fill(x + 4, y + h - 5, x + w - 4, y + h - 4, 0xFFD6C09A);
        guiGraphics.fill(x + 4, y + 4, x + 5, y + h - 4, 0xFFD6C09A);
        guiGraphics.fill(x + w - 5, y + 4, x + w - 4, y + h - 4, 0xFFD6C09A);

        guiGraphics.drawCenteredString(this.font, "§4§l" + page.header + "§r", x + w / 2, y + 6, 0xFF4A150A);
        if (page.subHeader != null && !page.subHeader.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, "§8§o" + page.subHeader + "§r", x + w / 2, y + 17, 0xFF6D523B);
        }

        // Ornamental Header Divider
        guiGraphics.fill(x + 16, y + 28, x + w - 16, y + 29, 0xFFB89B72);
        guiGraphics.fill(x + w / 2 - 12, y + 27, x + w / 2 + 12, y + 30, 0xFFD4AF37);

        if (page.type == ChronicleData.PageContent.PageType.BOSS_SHOWCASE && page.bossData != null) {
            renderBossShowcase(guiGraphics, page.bossData, x, y + 34, w, h - 54);
        } else if (page.type == ChronicleData.PageContent.PageType.ITEM_GRID) {
            renderItemGridPage(guiGraphics, page, x, y + 34, w, h - 54, mouseX, mouseY);
        } else if (page.type == ChronicleData.PageContent.PageType.TRADE_RECIPES) {
            renderTradeRecipesPage(guiGraphics, page, x, y + 34, w, h - 54, mouseX, mouseY);
        } else if (page.type == ChronicleData.PageContent.PageType.INDEX) {
            renderIndexPage(guiGraphics, page, x, y + 34, w, h - 54, mouseX, mouseY);
        } else {
            renderStandardTextPage(guiGraphics, page, x, y + 34, w, h - 54);
        }

        guiGraphics.fill(x + 16, y + h - 18, x + w - 16, y + h - 17, 0xFFD4C2A3);
        String pageNumStr = isLeft ? String.valueOf((this.currentChapter * 2) + 1) : String.valueOf((this.currentChapter * 2) + 2);
        guiGraphics.drawCenteredString(this.font, "§8- " + pageNumStr + " -§r", x + w / 2, y + h - 13, 0xFF7A654C);
    }

    private void renderStandardTextPage(GuiGraphics guiGraphics, ChronicleData.PageContent page, int x, int y, int w, int h) {
        int textY = y + 2;
        int maxTextY = y + h - 4;

        for (String line : page.textLines) {
            if (textY >= maxTextY) break;
            if (line.isEmpty()) {
                textY += 4;
                continue;
            }

            List<net.minecraft.util.FormattedCharSequence> wrappedLines = this.font.split(Component.literal(line), w - 14);
            for (net.minecraft.util.FormattedCharSequence seq : wrappedLines) {
                if (textY >= maxTextY) break;
                guiGraphics.drawString(this.font, seq, x + 8, textY, 0xFF2A1C0E, false);
                textY += this.font.lineHeight + 1;
            }
        }
    }

    private void renderIndexPage(GuiGraphics guiGraphics, ChronicleData.PageContent page, int x, int y, int w, int h, int mouseX, int mouseY) {
        // Render Titles
        guiGraphics.drawString(this.font, page.textLines.get(0), x + 8, y + 2, 0xFF2A1C0E, false);
        guiGraphics.drawString(this.font, page.textLines.get(1), x + 8, y + 14, 0xFF2A1C0E, false);
        guiGraphics.drawString(this.font, page.textLines.get(3), x + 8, y + 30, 0xFF2A1C0E, false);

        int itemStartY = y + 46;
        int itemHeight = this.font.lineHeight + 6;

        int originX = (this.width / 2) - BOOK_WIDTH / 2;
        int originY = (this.height / 2) - BOOK_HEIGHT / 2;
        int relMouseX = mouseX - originX;
        int relMouseY = mouseY - originY;

        // Render clean single-column index for chapters 1-7
        for (int i = 4; i < page.textLines.size(); i++) {
            String line = page.textLines.get(i);
            int itemY = itemStartY + (i - 4) * itemHeight;

            boolean isHovered = relMouseX >= x + 4 && relMouseX <= x + w - 4
                    && relMouseY >= itemY - 2 && relMouseY <= itemY + this.font.lineHeight + 1;

            if (isHovered) {
                guiGraphics.fill(x + 4, itemY - 2, x + w - 4, itemY + this.font.lineHeight + 1, 0x220094FF);
                guiGraphics.drawString(this.font, "§1§n" + line + "§r", x + 10, itemY, 0xFF0055AA, false);
            } else {
                guiGraphics.drawString(this.font, line, x + 8, itemY, 0xFF2A1C0E, false);
            }
        }
    }

    private void renderTradeRecipesPage(GuiGraphics guiGraphics, ChronicleData.PageContent page, int x, int y, int w, int h, int mouseX, int mouseY) {
        int originX = (this.width / 2) - BOOK_WIDTH / 2;
        int originY = (this.height / 2) - BOOK_HEIGHT / 2;
        int relMouseX = mouseX - originX;
        int relMouseY = mouseY - originY;

        int tradeBoxY = y + 4;
        int boxHeight = 44;

        for (ChronicleData.TradeRecipe trade : page.trades) {
            boolean isHovered = relMouseX >= x + 6 && relMouseX <= x + w - 6 && relMouseY >= tradeBoxY && relMouseY <= tradeBoxY + boxHeight;

            int borderColor = isHovered ? 0xFFD4AF37 : 0xFFDFCEAA;
            int bgColor = isHovered ? 0xFFFFFDF5 : 0xFFF9F1DF;

            // Framed recipe card
            guiGraphics.fill(x + 6, tradeBoxY, x + w - 6, tradeBoxY + boxHeight, borderColor);
            guiGraphics.fill(x + 7, tradeBoxY + 1, x + w - 7, tradeBoxY + boxHeight - 1, bgColor);

            // Centered Title
            guiGraphics.drawCenteredString(this.font, "§1§l" + trade.title + "§r", x + w / 2, tradeBoxY + 4, 0xFF1B3B6F);

            // Slots centered horizontally
            int slotY = tradeBoxY + 18;
            int centerX = x + w / 2;

            // Input Slot 1
            int in1X = centerX - 56;
            guiGraphics.fill(in1X, slotY, in1X + 18, slotY + 18, 0xFFCDB487);
            guiGraphics.renderItem(trade.input1, in1X + 1, slotY + 1);
            if (trade.input1.getCount() > 1) {
                guiGraphics.renderItemDecorations(this.font, trade.input1, in1X + 1, slotY + 1);
            }

            // Plus Sign
            guiGraphics.drawCenteredString(this.font, "§8+§r", centerX - 28, slotY + 5, 0xFF555555);

            // Input Slot 2
            int in2X = centerX - 18;
            guiGraphics.fill(in2X, slotY, in2X + 18, slotY + 18, 0xFFCDB487);
            guiGraphics.renderItem(trade.input2, in2X + 1, slotY + 1);
            if (trade.input2.getCount() > 1) {
                guiGraphics.renderItemDecorations(this.font, trade.input2, in2X + 1, slotY + 1);
            }

            // Arrow
            guiGraphics.drawCenteredString(this.font, "§4➔§r", centerX + 10, slotY + 5, 0xFF8B0000);

            // Output Slot (Gilded Frame)
            int outX = centerX + 24;
            guiGraphics.fill(outX - 1, slotY - 1, outX + 19, slotY + 19, 0xFFD4AF37);
            guiGraphics.fill(outX, slotY, outX + 18, slotY + 18, 0xFFFFF8DC);
            guiGraphics.renderItem(trade.output, outX + 1, slotY + 1);
            if (trade.output.getCount() > 1) {
                guiGraphics.renderItemDecorations(this.font, trade.output, outX + 1, slotY + 1);
            }

            tradeBoxY += boxHeight + 6;
        }
    }

    private void renderItemGridPage(GuiGraphics guiGraphics, ChronicleData.PageContent page, int x, int y, int w, int h, int mouseX, int mouseY) {
        int textY = y + 2;
        for (String line : page.textLines) {
            if (line.isEmpty()) {
                textY += 3;
                continue;
            }
            List<net.minecraft.util.FormattedCharSequence> wrappedLines = this.font.split(Component.literal(line), w - 14);
            for (net.minecraft.util.FormattedCharSequence seq : wrappedLines) {
                guiGraphics.drawString(this.font, seq, x + 8, textY, 0xFF2A1C0E, false);
                textY += this.font.lineHeight + 1;
            }
        }

        int originX = (this.width / 2) - BOOK_WIDTH / 2;
        int originY = (this.height / 2) - BOOK_HEIGHT / 2;
        int relMouseX = mouseX - originX;
        int relMouseY = mouseY - originY;

        // Render Showcase Items in elegant framed cards
        int slotY = y + h - (page.items.size() * 26) + 2;
        for (ChronicleData.ShowcaseItem item : page.items) {
            boolean isHovered = relMouseX >= x + 6 && relMouseX <= x + w - 6 && relMouseY >= slotY && relMouseY <= slotY + 23;

            int frameBorder = isHovered ? 0xFFD4AF37 : 0xFFDFCEAA;
            int frameBg = isHovered ? 0xFFFFFDF5 : 0xFFF9F1DF;

            guiGraphics.fill(x + 6, slotY, x + w - 6, slotY + 23, frameBorder);
            guiGraphics.fill(x + 7, slotY + 1, x + w - 7, slotY + 22, frameBg);
            guiGraphics.fill(x + 8, slotY + 2, x + 27, slotY + 21, 0xFFD2BD93);

            guiGraphics.renderItem(item.stack, x + 10, slotY + 3);

            guiGraphics.drawString(this.font, "§1§l" + item.title + "§r", x + 32, slotY + 3, 0xFF1B3B6F, false);
            guiGraphics.drawString(this.font, "§8" + item.description + "§r", x + 32, slotY + 12, 0xFF6D523B, false);

            slotY += 26;
        }
    }

    private void renderBossShowcase(GuiGraphics guiGraphics, ChronicleData.BossEntry boss, int x, int y, int w, int h) {
        guiGraphics.drawCenteredString(this.font, "§4§l" + boss.bossTitle + "§r", x + w / 2, y, 0xFF800000);
        guiGraphics.drawCenteredString(this.font, "§8§o" + boss.subtitle + "§r", x + w / 2, y + 11, 0xFF555555);

        // Stats Badges (Health & Difficulty)
        int badgeY = y + 24;
        guiGraphics.fill(x + 16, badgeY, x + w / 2 - 4, badgeY + 13, 0xFF8B0000);
        guiGraphics.fill(x + 17, badgeY + 1, x + w / 2 - 5, badgeY + 12, 0xFFB22222);
        guiGraphics.drawCenteredString(this.font, "§f❤ " + boss.health + "§r", x + 16 + (w / 2 - 20) / 2, badgeY + 3, 0xFFFFFFFF);

        guiGraphics.fill(x + w / 2 + 4, badgeY, x + w - 16, badgeY + 13, 0xFFB8860B);
        guiGraphics.fill(x + w / 2 + 5, badgeY + 1, x + w - 17, badgeY + 12, 0xFFDAA520);
        guiGraphics.drawCenteredString(this.font, "§f⚔ " + boss.difficulty + "§r", x + w / 2 + 4 + (w / 2 - 20) / 2, badgeY + 3, 0xFFFFFFFF);

        int pedestalY = badgeY + 80;
        guiGraphics.fill(x + 24, pedestalY - 2, x + w - 24, pedestalY + 6, 0xFF8C7355);
        guiGraphics.fill(x + 26, pedestalY, x + w - 26, pedestalY + 4, 0xFFB8A282);

        if (boss.entityType != null && Minecraft.getInstance().level != null) {
            LivingEntity entity = this.entityCache.computeIfAbsent(boss.entityType, type -> {
                if (type.create(Minecraft.getInstance().level) instanceof LivingEntity living) {
                    if (living instanceof EntityCQRDragon dragon) {
                        dragon.setFlightState(EntityCQRDragon.FLIGHT_FLYING);
                    }
                    return living;
                }
                return null;
            });

            if (entity != null) {
                renderEntityInGui(guiGraphics, x + w / 2.0F, pedestalY - 2.0F, boss.scale, this.entityRotation, entity);
            }
        }

        int dropBoxY = pedestalY + 10;
        guiGraphics.fill(x + 12, dropBoxY, x + w - 12, dropBoxY + 38, 0xFFDFCEAA);
        guiGraphics.fill(x + 13, dropBoxY + 1, x + w - 13, dropBoxY + 37, 0xFFF4EBD7);
        guiGraphics.drawString(this.font, "§4§lSignature Drops:§r", x + 18, dropBoxY + 4, 0xFF8B0000, false);

        int slotX = x + 18;
        for (ChronicleData.ShowcaseItem drop : boss.signatureDrops) {
            guiGraphics.fill(slotX, dropBoxY + 15, slotX + 20, dropBoxY + 35, 0xFFCDB487);
            guiGraphics.renderItem(drop.stack, slotX + 2, dropBoxY + 17);
            slotX += 26;
        }
    }

    private void renderEntityInGui(GuiGraphics guiGraphics, float x, float y, float scale, float rotationY, LivingEntity entity) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 100.0F);
        guiGraphics.pose().scale(scale, scale, -scale);

        guiGraphics.pose().mulPose(new Quaternionf().rotationZ((float) Math.PI));
        guiGraphics.pose().mulPose(new Quaternionf().rotationY((float) Math.toRadians(rotationY)));

        float prevYBodyRot = entity.yBodyRot;
        float prevYRot = entity.getYRot();
        float prevXRot = entity.getXRot();
        float prevYHeadRotO = entity.yHeadRotO;
        float prevYHeadRot = entity.yHeadRot;

        entity.yBodyRot = 180.0F;
        entity.setYRot(180.0F);
        entity.setXRot(0.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();

        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadow(false);
        dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, guiGraphics.pose(), guiGraphics.bufferSource(), 0xF000F0);
        guiGraphics.bufferSource().endBatch();
        dispatcher.setRenderShadow(true);

        entity.yBodyRot = prevYBodyRot;
        entity.setYRot(prevYRot);
        entity.setXRot(prevXRot);
        entity.yHeadRotO = prevYHeadRotO;
        entity.yHeadRot = prevYHeadRot;

        guiGraphics.pose().popPose();
    }

    private void renderNavigationControls(GuiGraphics guiGraphics, int bookX, int bookY, int mouseX, int mouseY) {
        int screenCenterX = this.width / 2;
        int screenCenterY = this.height / 2;
        int originX = screenCenterX - BOOK_WIDTH / 2;
        int originY = screenCenterY - BOOK_HEIGHT / 2;
        int relMouseX = mouseX - originX;
        int relMouseY = mouseY - originY;

        // Left Navigation Arrow (Inside left page area)
        if (this.currentChapter > 0) {
            int prevX = bookX + 24;
            int prevY = bookY + BOOK_HEIGHT - 28;
            boolean hoverPrev = relMouseX >= prevX && relMouseX <= prevX + 44
                    && relMouseY >= prevY && relMouseY <= prevY + 12;
            int color = hoverPrev ? 0xFF0094FF : 0xFF5D3A1A;
            guiGraphics.drawString(this.font, "◀ PREV", prevX, prevY, color, false);
        }

        // Right Navigation Arrow (Inside right page area)
        if (this.currentChapter < this.chapters.size() - 1) {
            int nextX = bookX + BOOK_WIDTH - 68;
            int nextY = bookY + BOOK_HEIGHT - 28;
            boolean hoverNext = relMouseX >= nextX && relMouseX <= nextX + 44
                    && relMouseY >= nextY && relMouseY <= nextY + 12;
            int color = hoverNext ? 0xFF0094FF : 0xFF5D3A1A;
            guiGraphics.drawString(this.font, "NEXT ▶", nextX, nextY, color, false);
        }

        // Close Button
        boolean hoverClose = relMouseX >= bookX + BOOK_WIDTH - 18 && relMouseX <= bookX + BOOK_WIDTH - 4
                && relMouseY >= bookY + 6 && relMouseY <= bookY + 18;
        int closeColor = hoverClose ? 0xFFFF3333 : 0xFF888888;
        guiGraphics.drawString(this.font, "✕", bookX + BOOK_WIDTH - 16, bookY + 8, closeColor, false);
    }

    private void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int bookScreenX, int bookScreenY) {
        int relMouseX = mouseX - bookScreenX;
        int relMouseY = mouseY - bookScreenY;

        // Check Tab Tooltips
        ChronicleData.Category[] categories = ChronicleData.Category.values();
        int tabY = 14;
        for (ChronicleData.Category cat : categories) {
            boolean isActive = this.chapters.get(this.currentChapter).category == cat;
            int tabExtension = isActive ? 10 : 2;
            if (relMouseX >= BOOK_WIDTH && relMouseX <= BOOK_WIDTH + TAB_WIDTH + tabExtension
                    && relMouseY >= tabY && relMouseY <= tabY + TAB_HEIGHT) {
                guiGraphics.renderTooltip(this.font, Component.literal("§6" + cat.label + " Chapter"), mouseX, mouseY);
                return;
            }
            tabY += TAB_HEIGHT + 3;
        }

        // Check Boss Drops, Trades & Item Grid Tooltips
        ChronicleData.Chapter chapter = this.chapters.get(this.currentChapter);
        checkPageTooltips(guiGraphics, chapter.leftPage, 18, 16, 178, 228, relMouseX, relMouseY, mouseX, mouseY);
        checkPageTooltips(guiGraphics, chapter.rightPage, 224, 16, 178, 228, relMouseX, relMouseY, mouseX, mouseY);
    }

    private void checkPageTooltips(GuiGraphics guiGraphics, ChronicleData.PageContent page, int x, int y, int w, int h, int relMouseX, int relMouseY, int mouseX, int mouseY) {
        if (page == null) return;

        // Boss Signature Drops
        if (page.type == ChronicleData.PageContent.PageType.BOSS_SHOWCASE && page.bossData != null) {
            int dropBoxY = y + 34 + 24 + 80 + 10;
            int slotX = x + 18;
            for (ChronicleData.ShowcaseItem drop : page.bossData.signatureDrops) {
                if (relMouseX >= slotX && relMouseX <= slotX + 20 && relMouseY >= dropBoxY + 15 && relMouseY <= dropBoxY + 35) {
                    guiGraphics.renderTooltip(this.font, drop.stack, mouseX, mouseY);
                    return;
                }
                slotX += 26;
            }
        }

        // Centered Trade Recipes
        if (page.type == ChronicleData.PageContent.PageType.TRADE_RECIPES && !page.trades.isEmpty()) {
            int tradeBoxY = y + 34 + 4;
            int boxHeight = 44;
            int centerX = x + w / 2;

            for (ChronicleData.TradeRecipe trade : page.trades) {
                int slotY = tradeBoxY + 18;

                // Input 1
                int in1X = centerX - 56;
                if (relMouseX >= in1X && relMouseX <= in1X + 18 && relMouseY >= slotY && relMouseY <= slotY + 18) {
                    guiGraphics.renderTooltip(this.font, trade.input1, mouseX, mouseY);
                    return;
                }
                // Input 2
                int in2X = centerX - 18;
                if (relMouseX >= in2X && relMouseX <= in2X + 18 && relMouseY >= slotY && relMouseY <= slotY + 18) {
                    guiGraphics.renderTooltip(this.font, trade.input2, mouseX, mouseY);
                    return;
                }
                // Output
                int outX = centerX + 24;
                if (relMouseX >= outX && relMouseX <= outX + 18 && relMouseY >= slotY && relMouseY <= slotY + 18) {
                    guiGraphics.renderTooltip(this.font, trade.output, mouseX, mouseY);
                    return;
                }

                tradeBoxY += boxHeight + 6;
            }
        }

        // Item Grid Cards
        if (page.type == ChronicleData.PageContent.PageType.ITEM_GRID && !page.items.isEmpty()) {
            int slotY = y + 34 + (h - 54) - (page.items.size() * 26) + 2;
            for (ChronicleData.ShowcaseItem item : page.items) {
                if (relMouseX >= x + 6 && relMouseX <= x + w - 6 && relMouseY >= slotY && relMouseY <= slotY + 23) {
                    guiGraphics.renderTooltip(this.font, item.stack, mouseX, mouseY);
                    return;
                }
                slotY += 26;
            }
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            int screenCenterX = this.width / 2;
            int screenCenterY = this.height / 2;
            int originX = screenCenterX - BOOK_WIDTH / 2;
            int originY = screenCenterY - BOOK_HEIGHT / 2;
            int relMouseX = (int) mouseX - originX;
            int relMouseY = (int) mouseY - originY;

            // If dragging over right page on a boss showcase
            ChronicleData.Chapter chapter = this.chapters.get(this.currentChapter);
            if (chapter.rightPage.type == ChronicleData.PageContent.PageType.BOSS_SHOWCASE
                    && relMouseX >= 224 && relMouseX <= 224 + 178 && relMouseY >= 16 && relMouseY <= 16 + 228) {
                this.isDraggingEntity = true;
                this.entityRotation += (float) dragX * 1.5F;
                this.rotationVelocity = (float) dragX * 1.2F;
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isDraggingEntity = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int screenCenterX = this.width / 2;
            int screenCenterY = this.height / 2;
            int originX = screenCenterX - BOOK_WIDTH / 2;
            int originY = screenCenterY - BOOK_HEIGHT / 2;
            int relMouseX = (int) mouseX - originX;
            int relMouseY = (int) mouseY - originY;

            // Close button
            if (relMouseX >= BOOK_WIDTH - 18 && relMouseX <= BOOK_WIDTH - 4 && relMouseY >= 6 && relMouseY <= 18) {
                this.onClose();
                return true;
            }

            // Left Navigation Arrow
            if (this.currentChapter > 0 && relMouseX >= 24 && relMouseX <= 68
                    && relMouseY >= BOOK_HEIGHT - 30 && relMouseY <= BOOK_HEIGHT - 14) {
                changeChapter(this.currentChapter - 1);
                return true;
            }

            // Right Navigation Arrow
            if (this.currentChapter < this.chapters.size() - 1 && relMouseX >= BOOK_WIDTH - 68 && relMouseX <= BOOK_WIDTH - 24
                    && relMouseY >= BOOK_HEIGHT - 30 && relMouseY <= BOOK_HEIGHT - 14) {
                changeChapter(this.currentChapter + 1);
                return true;
            }

            // Category Tabs
            ChronicleData.Category[] categories = ChronicleData.Category.values();
            int tabY = 14;
            for (ChronicleData.Category cat : categories) {
                boolean isActive = this.chapters.get(this.currentChapter).category == cat;
                int tabExtension = isActive ? 10 : 2;
                if (relMouseX >= BOOK_WIDTH && relMouseX <= BOOK_WIDTH + TAB_WIDTH + tabExtension
                        && relMouseY >= tabY && relMouseY <= tabY + TAB_HEIGHT) {
                    changeChapter(cat.targetChapter);
                    return true;
                }
                tabY += TAB_HEIGHT + 3;
            }

            // Clickable Table of Contents in Chapter 0
            if (this.currentChapter == 0 && relMouseX >= 22 && relMouseX <= 190) {
                int itemStartY = 16 + 34 + 46;
                int itemHeight = this.font.lineHeight + 6;
                if (relMouseY >= itemStartY) {
                    int clickedIndex = (relMouseY - itemStartY) / itemHeight;
                    if (clickedIndex >= 0 && clickedIndex < 7) {
                        if (clickedIndex == 6) {
                            // Credits
                            changeChapter(14);
                        } else if (clickedIndex == 5) {
                            // Boss Showcases
                            changeChapter(6);
                        } else {
                            changeChapter(clickedIndex + 1);
                        }
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
