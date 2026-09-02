package com.rsdvlp.machinehud.key;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import org.lwjgl.glfw.GLFW;

public final class ModKeyMappings {

    // MachineHUD全体の表示/非表示を切り替えるキー。
    //
    // 初期キーはEnd。
    // Minecraftの「操作設定」からユーザーが自由に変更できる。
    public static final KeyMapping TOGGLE_HUD =
            new KeyMapping(
                    "key.machinehud.toggle_hud",
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_END,
                    "key.categories.machinehud"
            );

    private ModKeyMappings() {
    }
}