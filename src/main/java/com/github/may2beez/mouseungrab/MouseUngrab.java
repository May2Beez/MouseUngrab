package com.github.may2beez.mouseungrab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MouseHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Mod(modid = "mouseungrab", useMetadata=true)
public class MouseUngrab {
    private final KeyBinding keyBinding = new KeyBinding("Ungrab Mouse", Keyboard.KEY_K, "Mouse Ungrab");
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientRegistry.registerKeyBinding(keyBinding);
    }

    @SubscribeEvent
    public void onInput(InputEvent.KeyInputEvent event) {
        if (keyBinding.isPressed()) {
            if (Mouse.isGrabbed()) {
                ungrabMouse();
            } else {
                regrabMouse();
            }
        }
    }

    public static boolean isUngrabbed = false;
    private static MouseHelper oldMouseHelper;
    private static boolean doesGameWantUngrabbed;

    public static void ungrabMouse() {
        Minecraft m = Minecraft.getMinecraft();
        if (isUngrabbed) return;
        m.gameSettings.pauseOnLostFocus = false;
        if (oldMouseHelper == null) oldMouseHelper = m.mouseHelper;
        doesGameWantUngrabbed = !Mouse.isGrabbed();
        oldMouseHelper.ungrabMouseCursor();
        m.inGameHasFocus = true;
        m.mouseHelper = new MouseHelper() {
            @Override
            public void mouseXYChange() {
            }
            @Override
            public void grabMouseCursor() {
                doesGameWantUngrabbed = false;
            }
            @Override
            public void ungrabMouseCursor() {
                doesGameWantUngrabbed = true;
            }
        };
        isUngrabbed = true;
    }

    /**
     * This function performs all the steps required to regrab the mouse.
     */
    public static void regrabMouse() {
        if (!isUngrabbed) return;

        Minecraft m = Minecraft.getMinecraft();
        m.mouseHelper = oldMouseHelper;
        if (!doesGameWantUngrabbed) m.mouseHelper.grabMouseCursor();
        oldMouseHelper = null;
        isUngrabbed = false;
    }
}
