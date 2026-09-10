package com.rsdvlp.machinehud.hud;

/**
 * HUD上に表示する行の種類。
 * 行の種類によって、文字色やインデントだけでなく
 * 将来的にアイコンなどの描画方法も変更できるようにする。
 */
public enum HudLineType {

    /**
     * 通常の値表示。
     * 例:
     * Speed        32 RPM
     * Stress       1,024 SU
     */
    VALUE,

    /**
     * HUDグループのヘッダー。
     * 例:
     * [Kinetic Stats]
     * [Network]
     */
    GROUP_HEADER,

    /**
     * 連続量を表すバー。
     * Fluid / Energy / Stressなどで使用する。
     * 例:
     * 8000 / 10000 mB
     * ||||||||||
     */
    LEVEL_BAR,

    /**
     * 段階を表すブロック表示。
     * Boiler Heat Levelなどで使用する。
     * 例:
     * Level 3
     * ■■■□□
     */
    LEVEL_BLOCKS,

    /**
     * min / current / max の3要素を比較するレベル表示。
     * Create Boilerのゴーグル表示のように、
     * 各要素の相対関係をバーとして可視化する。
     */
    LEVEL_COMPARE,

    PROGRESS,
}