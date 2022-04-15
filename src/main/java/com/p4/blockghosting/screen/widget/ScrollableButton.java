package com.p4.blockghosting.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ScrollableButton extends DrawableHelper implements Drawable, Element, Selectable {
    public static final Identifier WIDGETS_TEXTURE = new Identifier("textures/gui/widgets.png");
    protected int width;
    protected int height;
    public int relativeX;
    public int relativeY;
    private Text message;
    protected boolean hovered;
    public boolean active = true;
    public boolean visible = true;
    protected float alpha = 1.0f;
    private boolean focused;
    protected final PressAction onPress;
    private final ScrollMenu parent;

    public ScrollableButton(int relativeX, int relativeY, int width, int height, Text message, ScrollableButton.PressAction onPress, ScrollMenu parent) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.width = width;
        this.height = height;
        this.message = message;
        this.onPress = onPress;
        this.parent = parent;
    }

    @Environment(value=EnvType.CLIENT)
    public interface PressAction {
        void onPress(ScrollableButton var1);
    }
    public void onPress() {
        this.onPress.onPress(this);
    }

    public int getHeight() {
        return this.height;
    }

    protected int getYImage(boolean hovered) {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (hovered) {
            i = 2;
        }
        return i;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int x = relativeX + parent.scrollX;
        int y = relativeY + parent.scrollY;
        if (!this.visible) {
            return;
        }
        this.hovered = mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
        this.renderButton(matrices, mouseX, mouseY, delta, x, y);
    }

    protected MutableText getNarrationMessage() {
        return ClickableWidget.getNarrationMessage(this.getMessage());
    }

    public static MutableText getNarrationMessage(Text message) {
        return new TranslatableText("gui.narrate.button", message);
    }

    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta, int x, int y) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(matrices, x, y, 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexture(matrices, x + this.width / 2, y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        ClickableWidget.drawCenteredText(matrices, textRenderer, this.getMessage(), x + this.width / 2, y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);
    }

    protected void renderBackground(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
    }

    public void onClick(double mouseX, double mouseY) {
    }

    public void onRelease(double mouseX, double mouseY) {
    }

    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.active || !this.visible) {
            return false;
        }
        if (this.isValidClickButton(button) && (this.clicked(mouseX, mouseY))) {
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            this.onClick(mouseX, mouseY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.isValidClickButton(button)) {
            this.onRelease(mouseX, mouseY);
            return true;
        }
        return false;
    }

    protected boolean isValidClickButton(int button) {
        return button == 0;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isValidClickButton(button)) {
            this.onDrag(mouseX, mouseY, deltaX, deltaY);
            return true;
        }
        return false;
    }

    protected boolean clicked(double mouseX, double mouseY) {
        int x = relativeX + parent.scrollX;
        int y = relativeY + parent.scrollY;
        return this.active && this.visible && mouseX >= (double)x && mouseY >= (double)y && mouseX < (double)(x + this.width) && mouseY < (double)(y + this.height);
    }

    public boolean isHovered() {
        return this.hovered || this.focused;
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if (!this.active || !this.visible) {
            return false;
        }
        this.focused = !this.focused;
        this.onFocusedChanged(this.focused);
        return this.focused;
    }

    protected void onFocusedChanged(boolean newFocused) {
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        int x = relativeX + parent.scrollX;
        int y = relativeY + parent.scrollY;
        return this.active && this.visible && mouseX >= (double)x && mouseY >= (double)y && mouseX < (double)(x + this.width) && mouseY < (double)(y + this.height);
    }

    public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setMessage(Text message) {
        this.message = message;
    }

    public Text getMessage() {
        return this.message;
    }

    public boolean isFocused() {
        return this.focused;
    }

    @Override
    public boolean isNarratable() {
        return this.visible && this.active;
    }

    protected void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public Selectable.SelectionType getType() {
        if (this.focused) {
            return Selectable.SelectionType.FOCUSED;
        }
        if (this.hovered) {
            return Selectable.SelectionType.HOVERED;
        }
        return Selectable.SelectionType.NONE;
    }

    protected void appendDefaultNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, this.getNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                builder.put(NarrationPart.USAGE, new TranslatableText("narration.button.usage.focused"));
            } else {
                builder.put(NarrationPart.USAGE, new TranslatableText("narration.button.usage.hovered"));
            }
        }
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
