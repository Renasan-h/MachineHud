package com.rsdvlp.machinehud.common.config;

import com.rsdvlp.machinehud.common.hud.element.HudElement;
import com.rsdvlp.machinehud.common.hud.element.HudElements;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class ClientConfig {

    // Config全体を組み立てるBuilder。
    private static final ModConfigSpec.Builder BUILDER =
            new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_SPEED =
            BUILDER
                    .comment("Show Create rotation speed.")
                    .define("showSpeed", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_IMPACT =
            BUILDER
                    .comment("Show Create stress impact coefficient.")
                    .define("showImpact", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_STRESS =
            BUILDER
                    .comment("Show current Create stress usage.")
                    .define("showStress", true);

    // Createの機械の動作状態をHUDへ表示するかどうか。
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_STATUS =
            BUILDER
                    .comment("Show Create machine status.")
                    .define("showStatus", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_THEORETICAL_SPEED =
            BUILDER
                    .comment("Show theoretical Create rotation speed.")
                    .define("showTheoreticalSpeed", false);

    // Createの加工モードをHUDへ表示するかどうか。
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_PROCESSING_MODE =
            BUILDER
                    .comment("Show Create processing mode.")
                    .define("showProcessingMode", true);

    // Createの加工状態をHUDへ表示するかどうか。
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_PROCESSING_STATE =
            BUILDER
                    .comment("Show Create processing state.")
                    .define("showProcessingState", true);

    // Create BoilerのWater LevelをHUDへ表示するかどうか。
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_BOILER_WATER =
            BUILDER
                    .comment("Show Create boiler water level.")
                    .define("showBoilerWater", true);
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_BOILER_SIZE =
            BUILDER
                    .comment("Show Create boiler size level.")
                    .define("showBoilerSize", true);
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_BOILER_HEAT =
            BUILDER
                    .comment("Show Create boiler heat level.")
                    .define("showBoilerHeat", true);
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_BOILER_LEVEL =
            BUILDER
                    .comment("Show Create boiler level.")
                    .define("showBoilerLevel", true);
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_BOILER_OUTPUT =
            BUILDER
                    .comment("Show Create boiler steam output.")
                    .define("showBoilerSteamOutput", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_POSITION =
            BUILDER
                    .comment("Show target block position.")
                    .define("showPosition", false);

    // Createの回転ネットワーク全体のStressを表示するかどうか。
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_NETWORK_STRESS =
            BUILDER
                    .comment("Show Create network stress.")
                    .define("showNetworkStress", true);


    // Createの回転ネットワーク全体のCapacityを表示するかどうか。
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_NETWORK_CAPACITY =
            BUILDER
                    .comment("Show Create network stress capacity.")
                    .define("showNetworkCapacity", true);


    // 現在ネットワークのCapacityを何%使用しているか表示する。
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_NETWORK_USAGE =
            BUILDER
                    .comment("Show Create network stress usage percentage.")
                    .define("showNetworkUsage", true);


    // Createが保持している回転ネットワークサイズを表示する。
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_NETWORK_SIZE =
            BUILDER
                    .comment("Show Create kinetic network size.")
                    .define("showNetworkSize", false);

    // Createが保持している回転ネットワークステータスを表示する
    public static final ModConfigSpec.BooleanValue SHOW_CREATE_NETWORK_STATUS =
            BUILDER
                    .comment("Show Create kinetic network Status.")
                    .define("showNetworkStatus", false);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_POWER_STATE =
            BUILDER
                    .comment("Show power control state")
                    .define("showPowerState", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_POWER_TARGET_SPEED =
            BUILDER
                    .comment("Show power target speed")
                    .define("showPowerTargetSpeed", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_POWER_TARGET_SPEED_MODIFIER =
            BUILDER
                    .comment("Show power target speed modifier")
                    .define("showPowerTargetSpeedModifier", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_POWER_REDSTONE_SIGNAL =
            BUILDER
                    .comment("Show power redstone signal")
                    .define("showPowerRedstoneSignal", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_FLUID_INPUT_CONNECTION =
            BUILDER
                    .comment("Show Create fluid input direction.")
                    .define("showFluidInputDirection", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_FLUID_OUTPUT_CONNECTION =
            BUILDER
                    .comment("Show Create fluid output direction.")
                    .define("showFluidOutputDirection", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_FLUID_VALVE_STATE =
            BUILDER
                    .comment("Show Create fluid valve state.")
                    .define("showFluidValveState", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_FLUID_FILTER =
            BUILDER
                    .comment("Show Create fluid filter.")
                    .define("showFluidFilter", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_FLUID_CONTENT =
            BUILDER
                    .comment("Show Create fluid content.")
                    .define("showFluidContent", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_FLUID_MAX_FLOW_RATE =
            BUILDER
                    .comment("Show Create fluid Max Flow Rate.")
                    .define("showFluidMaxFlowRate", true);

    public static final ModConfigSpec.BooleanValue SHOW_CREATE_FLUID_AMOUNT =
            BUILDER
                    .comment("Show Create fluid amount.")
                    .define("showFluidAmount", true);

    // Mekanism - Energy
    // Mekanismの内部エネルギー量を表示する。
    public static final ModConfigSpec.BooleanValue SHOW_MEKANISM_ENERGY =
            BUILDER
                    .comment("Show Mekanism stored energy.")
                    .define("showMekanismEnergy", true);

    // Mekanismのエネルギー消費量を表示する。
    public static final ModConfigSpec.BooleanValue SHOW_MEKANISM_ENERGY_USAGE =
            BUILDER
                    .comment("Show Mekanism energy usage.")
                    .define("showMekanismEnergyUsage", true);

    // Mekanism - Processing
    // Mekanismの処理進捗を表示する。
    public static final ModConfigSpec.BooleanValue SHOW_MEKANISM_PROGRESS =
            BUILDER
                    .comment("Show Mekanism processing progress.")
                    .define("showMekanismProgress", true);

    // Mekanismの機械の動作状態を表示する。
    public static final ModConfigSpec.BooleanValue SHOW_MEKANISM_STATUS =
            BUILDER
                    .comment("Show Mekanism machine status.")
                    .define("showMekanismStatus", true);

    // Mekanismのエネルギー表示単位
    public static final ModConfigSpec.EnumValue<EnergyDisplayUnit> MEKANISM_ENERGY_UNIT =
            BUILDER
                    .comment("Mekanism energy display unit: J or FE")
                    .defineEnum(
                            "mekanismEnergyUnit",
                            EnergyDisplayUnit.J
                    );

    /*
     * HUDの表示順。
     * HudElementのIDをStringとして保存する。
     * 例:
     * ["blockName", "modName", "speed", "impact", "stress", "state", "position"]
     */
    public static final ModConfigSpec.ConfigValue<List<? extends String>> DISPLAY_ORDER =
            BUILDER
                    .comment("Order of HUD elements.")
                    .defineList(
                            "displayOrder",

                            // Configファイルが存在しない場合に使用する初期順序。
                            HudElements.getAll().stream()
                                    .map(HudElement::getId)
                                    .toList(),

                            // Config画面などから新しい要素を追加するときに使用する初期値。
                            // Validatorを必ず通過する既存HudElement IDを使用する。
                            () -> HudElements.getAll()
                                    .getFirst()
                                    .getId(),

                            // Configに書かれた値が有効なHUD IDか確認する。
                            // 不正な文字列が入っていた場合に、
                            // HUD描画処理で問題が起きるのを防ぐ。
                            value -> {
                                if (!(value instanceof String id)) {
                                    return false;
                                }

                                return HudElements.fromId(id) != null;
                            }
                    );

    // BuilderからNeoForgeが使用するConfig仕様を完成させる。
    public static final ModConfigSpec SPEC = BUILDER.build();
}