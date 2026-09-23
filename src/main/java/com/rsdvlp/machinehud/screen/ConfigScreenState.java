package com.rsdvlp.machinehud.screen;

import com.rsdvlp.machinehud.common.hud.HudGroup;

import java.util.EnumSet;
import java.util.Set;

/**
 * 設定画面の表示状態を保持する。
 * ゲーム起動中は設定画面を閉じてもカテゴリの開閉状態を維持する。
 */
public final class ConfigScreenState {

    private static final Set<HudGroup> COLLAPSED_GROUPS =
            EnumSet.noneOf(HudGroup.class);

    private ConfigScreenState() {
    }

    public static boolean isCollapsed(HudGroup group) {
        return COLLAPSED_GROUPS.contains(group);
    }

    public static void toggle(HudGroup group) {
        if (!COLLAPSED_GROUPS.add(group)) {
            COLLAPSED_GROUPS.remove(group);
        }
    }
}