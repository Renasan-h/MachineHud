
package com.rsdvlp.machinehud.mekanism.util;

import com.rsdvlp.machinehud.common.config.ClientConfig;
import com.rsdvlp.machinehud.common.config.EnergyDisplayUnit;
import mekanism.common.util.UnitDisplayUtils;

import java.util.Locale;

/**
 * Mekanismのエネルギー値をSI接頭辞付きで短縮表示する。
 */
public final class MekanismEnergyFormatter {

    private static final String[] UNITS = {
            "J", "kJ", "MJ", "GJ", "TJ", "PJ", "EJ"
    };

    private MekanismEnergyFormatter() {
    }

    /**
     * エネルギー値を読みやすい文字列に変換する。
     * 例:
     * 500     -> 500 J
     * 1500    -> 1.5 kJ
     * 2500000 -> 2.5 MJ
     */
    public static String format(long energy) {

        double value = energy;
        int unitIndex = 0;

        // 1000ごとに単位を1段階大きくする。
        while (Math.abs(value) >= 1000
                && unitIndex < UNITS.length - 1) {
            value /= 1000.0;
            unitIndex++;
        }

        // 小数第1位まで表示し、不要な末尾の .0 を除去する。
        String number = String.format(
                Locale.ROOT,
                "%.1f",
                value
        );

        if (number.endsWith(".0")) {
            number = number.substring(0, number.length() - 2);
        }

        return number + " " + UNITS[unitIndex];
    }

    /**
     * クライアント設定に応じて、JまたはFEで表示する。
     */
    public static String formatConfigured(long joules) {
        EnergyDisplayUnit unit =
                ClientConfig.MEKANISM_ENERGY_UNIT.get();

        // FE表示が選択され、Mekanism側でもFE連携が有効な場合のみ変換する。
        if (unit == EnergyDisplayUnit.FE
                && UnitDisplayUtils.EnergyUnit.FORGE_ENERGY.isEnabled()) {

            double fe = UnitDisplayUtils.EnergyUnit.FORGE_ENERGY
                    .convertToDouble(joules);

            return formatValue(fe, "FE");
        }

        // J表示が選択されている場合やFE連携が無効な場合。
        return formatValue(joules, "J");
    }

    /**
     * エネルギー消費量を1tick当たりの値として表示する。
     */
    public static String formatPerTick(long joulesPerTick) {
        return formatConfigured(joulesPerTick) + "/t";
    }

    /**
     * SI接頭辞を付けてエネルギー量を整形する。
     */
    private static String formatValue(double value, String unit) {
        String[] prefixes = {"", "k", "M", "G", "T", "P", "E"};

        int index = 0;

        while (Math.abs(value) >= 1000
                && index < prefixes.length - 1) {
            value /= 1000;
            index++;
        }

        // 小数点以下の不要な0を除去する。
        String number = String.format(
                Locale.ROOT,
                "%.2f",
                value
        ).replaceAll("\\.?0+$", "");

        return number + " " + prefixes[index] + unit;
    }
}