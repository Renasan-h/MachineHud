package com.rsdvlp.machinehud.hud;

/**
 * HUD上に表示する行の種類。
 *
 * 行の種類によって、文字色やインデントだけでなく
 * 将来的にアイコンなどの描画方法も変更できるようにする。
 */
public enum HudLineType {

    // SpeedやStressなどの通常情報。
    VALUE,

    // Kinetic StatsやNetworkなどのグループ見出し。
    GROUP_HEADER
}