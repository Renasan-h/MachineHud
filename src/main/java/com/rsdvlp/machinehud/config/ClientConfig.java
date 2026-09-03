package com.rsdvlp.machinehud.config;

import com.rsdvlp.machinehud.hud.element.CreateHudElement;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

public class ClientConfig {

    // Config全体を組み立てるBuilder。
    private static final ModConfigSpec.Builder BUILDER =
            new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHOW_SPEED =
            BUILDER
                    .comment("Show Create rotation speed.")
                    .define("showSpeed", true);

    public static final ModConfigSpec.BooleanValue SHOW_IMPACT =
            BUILDER
                    .comment("Show Create stress impact coefficient.")
                    .define("showImpact", true);

    public static final ModConfigSpec.BooleanValue SHOW_STRESS =
            BUILDER
                    .comment("Show current Create stress usage.")
                    .define("showStress", true);

    // Createの機械の動作状態をHUDへ表示するかどうか。
    public static final ModConfigSpec.BooleanValue SHOW_STATUS =
            BUILDER
                    .comment("Show Create machine status.")
                    .define("showStatus", true);

    public static final ModConfigSpec.BooleanValue SHOW_THEORETICAL_SPEED =
            BUILDER
                    .comment("Show theoretical Create rotation speed.")
                    .define("showTheoreticalSpeed", false);

    public static final ModConfigSpec.BooleanValue SHOW_POSITION =
            BUILDER
                    .comment("Show target block position.")
                    .define("showPosition", false);

    // Createの回転ネットワーク全体のStressを表示するかどうか。
    public static final ModConfigSpec.BooleanValue SHOW_NETWORK_STRESS =
            BUILDER
                    .comment("Show Create network stress.")
                    .define("showNetworkStress", true);


    // Createの回転ネットワーク全体のCapacityを表示するかどうか。
    public static final ModConfigSpec.BooleanValue SHOW_NETWORK_CAPACITY =
            BUILDER
                    .comment("Show Create network stress capacity.")
                    .define("showNetworkCapacity", true);


    // 現在ネットワークのCapacityを何%使用しているか表示する。
    public static final ModConfigSpec.BooleanValue SHOW_NETWORK_USAGE =
            BUILDER
                    .comment("Show Create network stress usage percentage.")
                    .define("showNetworkUsage", true);


    // Createが保持している回転ネットワークサイズを表示する。
    public static final ModConfigSpec.BooleanValue SHOW_NETWORK_SIZE =
            BUILDER
                    .comment("Show Create kinetic network size.")
                    .define("showNetworkSize", false);

    // Createが保持している回転ネットワークステータスを表示する
    public static final ModConfigSpec.BooleanValue SHOW_NETWORK_STATUS =
            BUILDER
                    .comment("Show Create kinetic network Status.")
                    .define("showNetworkStatus", false);


    /*
     * HUDの表示順。
     *
     * HudElementのIDをStringとして保存する。
     *
     * 例:
     * ["blockName", "modName", "speed", "impact", "stress", "state", "position"]
     */
    public static final ModConfigSpec.ConfigValue<List<? extends String>> DISPLAY_ORDER =
            BUILDER
                    .comment("Order of HUD elements.")
                    .defineList(
                            "displayOrder",

                            // Configファイルが存在しない場合に使用する初期順序。
                            Arrays.stream(CreateHudElement.values())
                                    .map(CreateHudElement::getId)
                                    .toList(),

                            // Configに書かれた値が有効なHUD IDか確認する。
                            // 不正な文字列が入っていた場合に、
                            // HUD描画処理で問題が起きるのを防ぐ。
                            value -> {
                                if (!(value instanceof String id)) {
                                    return false;
                                }

                                return Arrays.stream(CreateHudElement.values())
                                        .anyMatch(element ->
                                                element.getId().equals(id)
                                        );
                            }
                    );

    // BuilderからNeoForgeが使用するConfig仕様を完成させる。
    public static final ModConfigSpec SPEC = BUILDER.build();
}