package com.rsdvlp.machinehud.mekanism.element;

import com.rsdvlp.machinehud.common.hud.HudGroup;
import com.rsdvlp.machinehud.common.hud.element.HudElement;

/**
 * Mekanism連携で使用するHUD要素。
 * 特定の機械名に依存する項目ではなく、
 * Mekanismの複数の機械で共通利用できる情報を定義する。
 */
public enum MekanismHudElement implements HudElement {

    /**
     * 内部エネルギーの蓄積量。
     */
    MEKANISM_ENERGY(
            "mekanism_energy",
            "machinehud.mekanism.energy",
            HudGroup.MEKANISM_ENERGY
    ),

    /**
     * 機械が動作するときのエネルギー消費量。
     */
    MEKANISM_ENERGY_USAGE(
            "mekanism_energy_usage",
            "machinehud.mekanism.energy_usage",
            HudGroup.MEKANISM_ENERGY
    ),

    /**
     * 機械の処理進捗。
     */
    MEKANISM_PROGRESS(
            "mekanism_progress",
            "machinehud.mekanism.progress",
            HudGroup.MEKANISM_PROCESSING
    ),

    /**
     * 稼働中・待機中などの機械状態。
     */
    MEKANISM_STATUS(
            "mekanism_status",
            "machinehud.mekanism.status",
            HudGroup.MEKANISM_PROCESSING
    ),

    /**
     * 化学タンク内の素材名。
     */
    MEKANISM_CHEMICAL(
            "mekanism_chemical",
            "machinehud.mekanism.chemical",
            HudGroup.MEKANISM_PROCESSING
    ),

    /**
     * 化学タンクの現在量と最大容量。
     */
    MEKANISM_CHEMICAL_AMOUNT(
            "mekanism_chemical_amount",
            "machinehud.mekanism.chemical_amount",
            HudGroup.MEKANISM_PROCESSING
    ),

    // 入力：液体
    MEKANISM_FLUID_INPUT(
            "mekanism_fluid_input",
            "machinehud.mekanism.fluid_input",
            HudGroup.MEKANISM_PROCESSING
    ),

    MEKANISM_FLUID_INPUT_AMOUNT(
            "mekanism_fluid_input_amount",
            "machinehud.mekanism.fluid_input_amount",
            HudGroup.MEKANISM_PROCESSING
    ),

    // 入力：化学素材
    MEKANISM_CHEMICAL_INPUT(
            "mekanism_chemical_input",
            "machinehud.mekanism.chemical_input",
            HudGroup.MEKANISM_PROCESSING
    ),

    MEKANISM_CHEMICAL_INPUT_AMOUNT(
            "mekanism_chemical_input_amount",
            "machinehud.mekanism.chemical_input_amount",
            HudGroup.MEKANISM_PROCESSING
    ),

    // 出力：化学素材
    MEKANISM_CHEMICAL_OUTPUT(
            "mekanism_chemical_output",
            "machinehud.mekanism.chemical_output",
            HudGroup.MEKANISM_PROCESSING
    ),

    MEKANISM_CHEMICAL_OUTPUT_AMOUNT(
            "mekanism_chemical_output_amount",
            "machinehud.mekanism.chemical_output_amount",
            HudGroup.MEKANISM_PROCESSING
    ),
    ;

    private final String id;
    private final String displayName;
    private final HudGroup hudGroup;

    MekanismHudElement(
            String id,
            String displayName,
            HudGroup hudGroup
    ) {
        this.id = id;
        this.displayName = displayName;
        this.hudGroup = hudGroup;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public HudGroup getHudGroup() {
        return hudGroup;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}