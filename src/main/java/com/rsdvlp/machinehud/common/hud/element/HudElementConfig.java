package com.rsdvlp.machinehud.common.hud.element;

import com.rsdvlp.machinehud.common.config.ClientConfig;
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
    }

    private HudElementConfig() {
    }

    /**
     * HudElementとBoolean Configの対応を登録する。
     * common自身の設定だけでなく、CreateやMekanismなどのMOD固有設定も
     * 各側の初期化処理から登録できるようにする。
     */
    public static void register(
            HudElement element,
            ModConfigSpec.BooleanValue config
    ) {
        CONFIGS.put(element.getId(), config);
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

        ModConfigSpec.BooleanValue config = getConfig(element);

        // Configが登録されていないHudElementは、
        // 安全のため非表示として扱う。
        if (config == null) {
            return false;
        }

        return config.get();
    }
}