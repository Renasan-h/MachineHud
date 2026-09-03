package com.rsdvlp.machinehud.hud.element;

import com.rsdvlp.machinehud.config.ClientConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.Map;

/**
 * HudElementとClientConfigの表示設定の対応を管理する。
 * Rendererや設定画面が、
 * SHOW_SPEEDやSHOW_STRESSなどの個別Configを
 * 直接意識しなくて済むようにする。
 */
public final class HudElementConfig {

    // HudElementのIDと、
    // 対応するBoolean Configを保存する。
    private static final Map<String, ModConfigSpec.BooleanValue> CONFIGS =
            new HashMap<>();

    static {
        /*
         * =========================
         * Common
         * =========================
         */
        register(
                CommonHudElement.POSITION,
                ClientConfig.SHOW_POSITION
        );

        /*
         * =========================
         * Create - Machine
         * =========================
         */
        register(
                CreateHudElement.SPEED,
                ClientConfig.SHOW_SPEED
        );

        register(
                CreateHudElement.IMPACT,
                ClientConfig.SHOW_IMPACT
        );

        register(
                CreateHudElement.STRESS,
                ClientConfig.SHOW_STRESS
        );

        register(
                CreateHudElement.STATUS,
                ClientConfig.SHOW_STATUS
        );

        register(
                CreateHudElement.THEORETICAL_SPEED,
                ClientConfig.SHOW_THEORETICAL_SPEED
        );

        /*
         * =========================
         * Create - Network
         * =========================
         */
        register(
                CreateHudElement.NETWORK_STRESS,
                ClientConfig.SHOW_NETWORK_STRESS
        );

        register(
                CreateHudElement.NETWORK_CAPACITY,
                ClientConfig.SHOW_NETWORK_CAPACITY
        );

        register(
                CreateHudElement.NETWORK_USAGE,
                ClientConfig.SHOW_NETWORK_USAGE
        );

        register(
                CreateHudElement.NETWORK_SIZE,
                ClientConfig.SHOW_NETWORK_SIZE
        );

        register(
                CreateHudElement.NETWORK_STATUS,
                ClientConfig.SHOW_NETWORK_STATUS
        );
    }

    private HudElementConfig() {
    }

    /**
     * HudElementとBoolean Configの対応を登録する。
     */
    private static void register(
            HudElement element,
            ModConfigSpec.BooleanValue config
    ) {

        CONFIGS.put(
                element.getId(),
                config
        );
    }

    /**
     * 指定されたHudElementに対応する
     * Boolean Configを取得する。
     * Configが登録されていない場合はnullを返す。
     */
    public static ModConfigSpec.BooleanValue getConfig(
            HudElement element
    ) {

        return CONFIGS.get(
                element.getId()
        );
    }

    /**
     * 指定されたHudElementが
     * 現在表示設定で有効になっているか確認する。
     */
    public static boolean isEnabled(
            HudElement element
    ) {

        ModConfigSpec.BooleanValue config =
                getConfig(element);

        // Configが登録されていないHudElementは、
        // 安全のため非表示として扱う。
        if (config == null) {
            return false;
        }

        return config.get();
    }
}