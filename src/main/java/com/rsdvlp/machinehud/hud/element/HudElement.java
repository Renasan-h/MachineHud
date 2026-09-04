package com.rsdvlp.machinehud.hud.element;

import com.rsdvlp.machinehud.hud.HudGroup;

/**
 * MachineHUDに表示できるHUD項目の共通インターフェース。
 * Createなど、
 * MODごとのHUD項目はこのインターフェースを実装する。
 */
public interface HudElement {

    /**
     * Config保存時などに使用する一意のID。
     * 例:
     * speed
     * networkStress
     * position
     */
    String getId();

    /**
     * 設定画面などで使用する表示名。
     */
    String getDisplayName();

    /**
     * このHUD項目が所属する表示グループを取得する。
     */
    HudGroup getHudGroup();
}
