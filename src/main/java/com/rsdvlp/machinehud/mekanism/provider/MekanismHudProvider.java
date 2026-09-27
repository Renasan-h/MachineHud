
package com.rsdvlp.machinehud.mekanism.provider;

import com.rsdvlp.machinehud.common.hud.HudGroup;
import com.rsdvlp.machinehud.common.hud.HudLine;
import com.rsdvlp.machinehud.common.hud.HudLineType;
import com.rsdvlp.machinehud.common.hud.element.HudElement;
import com.rsdvlp.machinehud.common.hud.provider.HudProvider;
import com.rsdvlp.machinehud.mekanism.data.MekanismChemicalHudData;
import com.rsdvlp.machinehud.mekanism.data.MekanismChemicalTankHudData;
import com.rsdvlp.machinehud.mekanism.data.MekanismFluidHudData;
import com.rsdvlp.machinehud.mekanism.data.MekanismMachineHudData;
import com.rsdvlp.machinehud.mekanism.element.MekanismHudElement;
import com.rsdvlp.machinehud.mekanism.util.MekanismEnergyFormatter;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

/**
 * Mekanismの機械データをHUD表示行に変換する。
 * データ取得はReaderが担当し、このクラスは表示内容の生成だけを担当する。
 */
public final class MekanismHudProvider implements HudProvider {

    private final MekanismMachineHudData data;
    private final MekanismChemicalHudData chemicalData;
    private final List<MekanismChemicalTankHudData> chemicalTanks;
    private final MekanismFluidHudData fluidData;

    public MekanismHudProvider(
            MekanismMachineHudData data,
            MekanismChemicalHudData chemicalData,
            List<MekanismChemicalTankHudData> chemicalTanks,
            MekanismFluidHudData fluidData
    ) {
        this.data = data;
        this.chemicalData = chemicalData;
        this.chemicalTanks = List.copyOf(chemicalTanks);
        this.fluidData = fluidData;
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
            case MEKANISM_CHEMICAL -> createChemicalLine(mekanismElement);
            case MEKANISM_CHEMICAL_AMOUNT -> createChemicalAmountLine(mekanismElement);
            case MEKANISM_CHEMICAL_INPUT -> createTankChemicalLine(mekanismElement, "input");
            case MEKANISM_CHEMICAL_INPUT_AMOUNT -> createTankAmountLine(mekanismElement, "input");
            case MEKANISM_CHEMICAL_OUTPUT -> createTankChemicalLine(mekanismElement, "output");
            case MEKANISM_CHEMICAL_OUTPUT_AMOUNT -> createTankAmountLine(mekanismElement, "output");
            case MEKANISM_FLUID_INPUT -> createFluidInputLine(mekanismElement);
            case MEKANISM_FLUID_INPUT_AMOUNT -> createFluidAmountLine(mekanismElement);
            case MEKANISM_CHEMICAL_LEFT -> createTankChemicalLine(mekanismElement, "left");
            case MEKANISM_CHEMICAL_LEFT_AMOUNT -> createTankAmountLine(mekanismElement, "left");
            case MEKANISM_CHEMICAL_RIGHT -> createTankChemicalLine(mekanismElement, "right");
            case MEKANISM_CHEMICAL_RIGHT_AMOUNT -> createTankAmountLine(mekanismElement, "right");
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

    /**
     * 化学素材名を表示する。
     */
    private HudLine createChemicalLine(
            MekanismHudElement element
    ) {
        if (chemicalData == null || chemicalData.capacity() <= 0) {
            return null;
        }

        Component chemicalName;

        if (chemicalData.chemicalId().isEmpty()) {
            chemicalName = Component.translatable(
                    "machinehud.mekanism.chemical.empty"
            );
        } else {
            ResourceLocation id =
                    ResourceLocation.tryParse(chemicalData.chemicalId());

            if (id == null) {
                return null;
            }

            Chemical chemical =
                    MekanismAPI.CHEMICAL_REGISTRY.get(id);

            if (chemical == null) {
                return null;
            }

            chemicalName = chemical.getTextComponent();
        }

        return createValueLine(
                element,
                chemicalName,
                HudGroup.MEKANISM_PROCESSING,
                ChatFormatting.WHITE.getColor()
        );
    }

    /**
     * 化学タンクの現在量と最大容量を表示する。
     */
    private HudLine createChemicalAmountLine(
            MekanismHudElement element
    ) {
        if (chemicalData == null || chemicalData.capacity() <= 0) {
            return null;
        }

        Component amount = Component.literal(
                String.format(
                        java.util.Locale.ROOT,
                        "%,d / %,d mB",
                        chemicalData.amount(),
                        chemicalData.capacity()
                )
        );

        return createValueLine(
                element,
                amount,
                HudGroup.MEKANISM_PROCESSING,
                ChatFormatting.WHITE.getColor()
        );
    }

    /**
     * 識別子から対象の化学タンクを取得する。
     */
    private MekanismChemicalTankHudData findTank(String tankId) {
        for (MekanismChemicalTankHudData tank : chemicalTanks) {
            if (tank.tankId().equals(tankId)) {
                return tank;
            }
        }

        return null;
    }

    /**
     * 入力または出力タンクの化学素材名を表示する。
     */
    private HudLine createTankChemicalLine(
            MekanismHudElement element,
            String tankId
    ) {
        MekanismChemicalTankHudData tank = findTank(tankId);

        if (tank == null || tank.capacity() <= 0) {
            return null;
        }

        Component chemicalName;

        if (tank.chemicalId().isEmpty()) {
            chemicalName = Component.translatable(
                    "machinehud.mekanism.chemical.empty"
            );
        } else {
            ResourceLocation id =
                    ResourceLocation.tryParse(tank.chemicalId());

            if (id == null) {
                return null;
            }

            Chemical chemical =
                    MekanismAPI.CHEMICAL_REGISTRY.get(id);

            if (chemical == null) {
                return null;
            }

            chemicalName = chemical.getTextComponent();
        }

        return createValueLine(
                element,
                chemicalName,
                HudGroup.MEKANISM_PROCESSING,
                ChatFormatting.WHITE.getColor()
        );
    }

    /**
     * 入力または出力タンクの貯蔵量を表示する。
     */
    private HudLine createTankAmountLine(
            MekanismHudElement element,
            String tankId
    ) {
        MekanismChemicalTankHudData tank = findTank(tankId);

        if (tank == null || tank.capacity() <= 0) {
            return null;
        }

        Component amount = Component.literal(
                String.format(
                        java.util.Locale.ROOT,
                        "%,d / %,d mB",
                        tank.amount(),
                        tank.capacity()
                )
        );

        return createValueLine(
                element,
                amount,
                HudGroup.MEKANISM_PROCESSING,
                ChatFormatting.WHITE.getColor()
        );
    }

    /**
     * 入力液体の名前を表示する。
     */
    private HudLine createFluidInputLine(
            MekanismHudElement element
    ) {
        if (fluidData == null) {
            return null;
        }

        Component fluidName;

        if (fluidData.fluidId().isEmpty()) {
            fluidName = Component.translatable(
                    "machinehud.mekanism.fluid.empty"
            );
        } else {
            ResourceLocation id =
                    ResourceLocation.tryParse(fluidData.fluidId());

            if (id == null) {
                return null;
            }

            var fluid = BuiltInRegistries.FLUID.get(id);

            if (fluid == null) {
                return null;
            }

            // FluidStackの表示名を使い、
            // Minecraftや他MODの翻訳にも対応する。
            fluidName = new FluidStack(fluid, 1).getHoverName();
        }

        return createValueLine(
                element,
                fluidName,
                HudGroup.MEKANISM_PROCESSING,
                ChatFormatting.WHITE.getColor()
        );
    }

    /**
     * 入力液体タンクの現在量と最大容量を表示する。
     */
    private HudLine createFluidAmountLine(
            MekanismHudElement element
    ) {
        if (fluidData == null) {
            return null;
        }

        Component amount = Component.literal(
                String.format(
                        java.util.Locale.ROOT,
                        "%,d / %,d mB",
                        fluidData.amount(),
                        fluidData.capacity()
                )
        );

        return createValueLine(
                element,
                amount,
                HudGroup.MEKANISM_PROCESSING,
                ChatFormatting.WHITE.getColor()
        );
    }
}