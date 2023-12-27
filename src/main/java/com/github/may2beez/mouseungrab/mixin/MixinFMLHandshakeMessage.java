package com.github.may2beez.mouseungrab.mixin;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = FMLHandshakeMessage.ModList.class, remap = false, priority = Integer.MAX_VALUE)
public abstract class MixinFMLHandshakeMessage {
    @Shadow(remap = false)
    private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"), remap = false)
    private void init(List<ModContainer> modList, CallbackInfo ci) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) return;
        System.out.println("MouseUngrab: Removing all mods except FML, Forge, and MCP");
        modTags.keySet().removeIf(c -> !c.matches("FML|Forge|mcp"));
    }
}
