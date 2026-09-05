package com.rsdvlp.machinehud.hud;

import net.minecraft.network.chat.Component;

/**
 * MachineHUDに表示する1行分の情報。
 * VALUEの場合:
 *   label = Component.literal("Speed")
 *   value = Component.literal("32 RPM")
 * GROUP_HEADERの場合:
 *   label = Component.literal("Kinetic Stats")
 *   value = null
 * LEVEL_BAR / LEVEL_BLOCKSの場合:
 *   levelに数値情報を保持し、
 *   Renderer側で視覚表示する。
 *
 * @param label  項目名またはグループ名
 * @param value  項目の値。グループヘッダーではnull
 * @param indent 左側のインデント段階
 * @param color  基本文字色
 * @param type   行の種類
 * @param group  グループ情報
 * @param level  レベル表示用データ。通常行ではnull
 */
public record HudLine(
        Component label,
        Component value,
        int indent,
        int color,
        HudLineType type,
        HudGroup group,
        HudLevel level
) {

}
