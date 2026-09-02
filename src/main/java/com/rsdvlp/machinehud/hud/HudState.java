package com.rsdvlp.machinehud.hud;

public final class HudState {

    // MachineHUD全体を表示するかどうか。
    //
    // true  = 表示
    // false = 非表示
    private static boolean enabled = true;

    private HudState() {
    }

    // 現在HUDが有効か取得する。
    public static boolean isEnabled() {
        return enabled;
    }

    // HUDの表示状態を反転する。
    //
    // true  → false
    // false → true
    public static void toggle() {
        enabled = !enabled;
    }
}