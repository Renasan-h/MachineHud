package com.rsdvlp.machinehud.hud;

import com.rsdvlp.machinehud.config.ClientConfig;

import java.util.ArrayList;
import java.util.List;

public enum HudElement {

    // General
    POSITION("position", "Position", HudGroup.COMMON),

    // Create Mod
    SPEED("speed", "Speed", HudGroup.MACHINE),
    IMPACT("impact", "Stress Impact", HudGroup.MACHINE),
    STRESS("stress", "Stress", HudGroup.MACHINE),
    STATUS("status", "Status", HudGroup.MACHINE),
    THEORETICAL_SPEED("theoreticalSpeed", "Theoretical Speed", HudGroup.MACHINE),

    // Createの回転ネットワーク全体に関する情報。
    NETWORK_STRESS("networkStress", "Network Stress", HudGroup.NETWORK),
    NETWORK_CAPACITY("networkCapacity", "Network Capacity", HudGroup.NETWORK),
    NETWORK_USAGE("networkUsage", "Network Usage", HudGroup.NETWORK),
    NETWORK_SIZE("networkSize", "Network Size", HudGroup.NETWORK),
    NETWORK_STATUS("networkStatus", "Network Status", HudGroup.NETWORK),;

    // Configに保存するときに使用するID。
    private final String id;

    // 設定画面などに表示する名前。
    private final String displayName;

    // このHUD項目が所属するグループ。
    private final HudGroup hudGroup;

    HudElement(String id, String displayName, HudGroup hudGroup) {
        this.id = id;
        this.displayName = displayName;
        this.hudGroup = hudGroup;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public HudGroup getHudGroup() { return hudGroup;}

    // Config上のIDから対応するHUD項目を取得する。
    public static HudElement fromId(String id) {

        for (HudElement element : values()) {
            if (element.id.equals(id)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Configに保存された表示順を基準に、
     * 新しく追加されたHudElementも含めた完全な表示順を生成する。
     *
     * MOD更新などでHudElementが追加された場合でも、
     * 古いConfigに存在しない項目を末尾へ自動追加する。
     */
    public static List<HudElement> getOrderedElements() {

        List<HudElement> elements = new ArrayList<>();

        // まずConfigに保存されている順番を読み込む。
        for (String id : ClientConfig.DISPLAY_ORDER.get()) {

            HudElement element = fromId(id);

            // 不正なIDや重複した項目は追加しない。
            if (element != null && !elements.contains(element)) {
                elements.add(element);
            }
        }

        // HudElementには存在するが、
        // 古いConfigにはまだ存在しない新規項目を末尾へ追加する。
        for (HudElement element : values()) {

            if (!elements.contains(element)) {
                elements.add(element);
            }
        }

        return elements;
    }
}