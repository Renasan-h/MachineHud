package com.rsdvlp.machinehud.hud;

/**
 * HUD上で視覚表示するレベル情報。
 * current:
 * 現在値。
 * max:
 * 表示上の最大値。
 * min:
 * 必要に応じて使用する最低基準値。
 */
public record HudLevel(
        double current,
        double max,
        double min
) {
    /**
     * 最低値を必要としない通常のLevel。
     */
    public HudLevel(
            double current,
            double max
    ) {
        this(current, max, 0);
    }

    /**
     * 0.0 ～ 1.0 の割合を取得する。
     */
    public double ratio() {

        if (max <= 0) {
            return 0;
        }

        return Math.clamp(current / max, 0, 1);
    }
}