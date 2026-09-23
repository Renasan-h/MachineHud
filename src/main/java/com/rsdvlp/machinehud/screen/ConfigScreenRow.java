package com.rsdvlp.machinehud.screen;

import com.rsdvlp.machinehud.common.hud.HudGroup;
import com.rsdvlp.machinehud.common.hud.element.HudElement;

/**
 * 設定画面のスクロール領域に表示する行。
 * 見出し、HUD項目、追加設定を同じ一覧として管理する。
 */
public record ConfigScreenRow(
        Type type,
        HudGroup group,
        HudElement element
) {

    public enum Type {
        HEADER,
        HUD_ELEMENT,
        ENERGY_UNIT
    }

    // カテゴリ見出しを生成する。
    public static ConfigScreenRow header(HudGroup group) {
        return new ConfigScreenRow(Type.HEADER, group, null);
    }

    // HUD項目の設定行を生成する。
    public static ConfigScreenRow hudElement(HudElement element) {
        return new ConfigScreenRow(
                Type.HUD_ELEMENT,
                element.getHudGroup(),
                element
        );
    }

    // Mekanismのエネルギー単位設定行を生成する。
    public static ConfigScreenRow energyUnit() {
        return new ConfigScreenRow(
                Type.ENERGY_UNIT,
                HudGroup.MEKANISM_ENERGY,
                null
        );
    }
}