
package com.rsdvlp.machinehud.mekanism.provider;

import com.rsdvlp.machinehud.common.hud.HudGroup;
import com.rsdvlp.machinehud.common.hud.HudLine;
import com.rsdvlp.machinehud.common.hud.HudLineType;
import com.rsdvlp.machinehud.common.hud.element.HudElement;
import com.rsdvlp.machinehud.common.hud.provider.HudProvider;
import com.rsdvlp.machinehud.mekanism.data.MekanismMachineHudData;
import com.rsdvlp.machinehud.mekanism.element.MekanismHudElement;
import com.rsdvlp.machinehud.mekanism.util.MekanismEnergyFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

/**
 * Mekanismの機械データをHUD表示行に変換する。
 * データ取得はReaderが担当し、このクラスは表示内容の生成だけを担当する。
 */
public final class MekanismHudProvider implements HudProvider {

    private final MekanismMachineHudData data;

    public MekanismHudProvider(MekanismMachineHudData data) {
        this.data = data;
    }

    @Override
    public boolean supports(HudElement element) {
        return element instanceof MekanismHudElement;
    }

    @Override
    public HudLine createLine(HudElement element) {

        if (!(element instanceof MekanismHudElement mekanismElement)) {
            return null;
        }

        return switch (mekanismElement) {
            case MEKANISM_ENERGY -> createEnergyLine(mekanismElement);
            case MEKANISM_ENERGY_USAGE -> createEnergyUsageLine(mekanismElement);
            case MEKANISM_PROGRESS -> createProgressLine(mekanismElement);
            case MEKANISM_STATUS -> createStatusLine(mekanismElement);
        };
    }

    /**
     * 現在の蓄積エネルギーと最大容量を表示する。
     */
    private HudLine createEnergyLine(
            MekanismHudElement element
    ) {
        return createValueLine(
                element,
                Component.literal(
                        MekanismEnergyFormatter.formatConfigured(data.storedEnergy())
                                + " / "
                                + MekanismEnergyFormatter.formatConfigured(data.maxEnergy())
                ),
                HudGroup.MEKANISM_ENERGY,
                ChatFormatting.WHITE.getColor()
        );
    }

    /**
     * 稼働中のエネルギー消費量を表示する。
     */
    private HudLine createEnergyUsageLine(
            MekanismHudElement element
    ) {
        return createValueLine(
                element,
                Component.literal(
                        MekanismEnergyFormatter.formatPerTick(data.energyUsage())
                ),
                HudGroup.MEKANISM_ENERGY,
                ChatFormatting.WHITE.getColor()
        );
    }

    /**
     * 加工進捗を百分率で表示する。
     */
    private HudLine createProgressLine(
            MekanismHudElement element
    ) {

        double percentage = data.maxProgress() > 0
                ? 100.0 * data.progress() / data.maxProgress()
                : 0.0;

        // 不正な進捗値によって0～100%を超えないようにする。
        percentage = Math.clamp(percentage, 0.0, 100.0);

        return createValueLine(
                element,
                Component.literal(
                        String.format(
                                java.util.Locale.ROOT,
                                "%.1f%%",
                                percentage
                        )
                ),
                HudGroup.MEKANISM_PROCESSING,
                ChatFormatting.WHITE.getColor()
        );
    }

    /**
     * 機械の動作状態を表示する。
     */
    private HudLine createStatusLine(
            MekanismHudElement element
    ) {

        String translationKey = switch (data.state()) {
            case RUNNING -> "machinehud.mekanism.status.running";
            case IDLE -> "machinehud.mekanism.status.idle";
            case OUTPUT_BLOCKED -> "machinehud.mekanism.status.output_blocked";
        };

        int color = switch (data.state()) {
            case RUNNING -> ChatFormatting.GREEN.getColor();
            case IDLE -> ChatFormatting.GRAY.getColor();
            case OUTPUT_BLOCKED -> ChatFormatting.RED.getColor();
        };

        return createValueLine(
                element,
                Component.translatable(translationKey),
                HudGroup.MEKANISM_PROCESSING,
                color
        );
    }

    /**
     * 通常のラベル・値形式のHudLineを生成する。
     */
    private HudLine createValueLine(
            MekanismHudElement element,
            Component value,
            HudGroup group,
            int color
    ) {
        return new HudLine(
                Component.translatable(element.getDisplayName()),
                value,
                0,
                color,
                HudLineType.VALUE,
                group,
                null
        );
    }
}