package com.rsdvlp.machinehud.hud;

/**
 * MachineHUDに表示する1行分の情報。
 *
 * VALUEの場合:
 *   label = "Speed"
 *   value = "32 RPM"
 *
 * GROUP_HEADERの場合:
 *   label = "Kinetic Stats"
 *   value = null
 *
 * @param label  項目名またはグループ名
 * @param value  項目の値。グループヘッダーではnull
 * @param indent 左側のインデント段階
 * @param color  文字色
 * @param type   行の種類
 * @param group  グループヘッダーの場合のHudGroup
 */
public record HudLine(
        String label,
        String value,
        int indent,
        int color,
        HudLineType type,
        HudGroup group
) {
}
