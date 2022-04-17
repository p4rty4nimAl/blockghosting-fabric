package com.p4.blockghosting.mixins;

import com.p4.blockghosting.screen.BaseBlockGhostingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
//TODO: add back to mixin file - currently disabled for public builds
public class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        addDrawableChild(new ButtonWidget(
                this.width / 2 + 5,
                ((this.height + 100) / 6) - 1,
                150,
                20,
                new LiteralText("Block Ghosting"),
                click -> this.client.setScreen(new BaseBlockGhostingScreen(new OptionsScreen(null, this.client.options)))
        ));
    }
}
