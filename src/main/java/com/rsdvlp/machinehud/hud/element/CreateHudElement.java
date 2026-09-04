package com.rsdvlp.machinehud.hud.element;

import com.rsdvlp.machinehud.hud.HudGroup;

/**
 * Createに関するHUD項目。
 * 機械単体の回転情報と、
 * 回転ネットワーク全体の情報を管理する。
 */
public enum CreateHudElement implements HudElement {

    /*
     * =========================
     * 機械単体情報
     * =========================
     */
    SPEED(
            "speed",
            "Speed",
            HudGroup.CREATE_KINETIC
    ),

    IMPACT(
            "impact",
            "Stress Impact",
            HudGroup.CREATE_KINETIC
    ),

    STRESS(
            "stress",
            "Stress",
            HudGroup.CREATE_KINETIC
    ),

    STATUS(
            "status",
            "Status",
            HudGroup.CREATE_KINETIC
    ),

    THEORETICAL_SPEED(
            "theoreticalSpeed",
            "Theoretical Speed",
            HudGroup.CREATE_KINETIC
    ),


    /*
     * =========================
     * 回転ネットワーク情報
     * =========================
     */
    NETWORK_STRESS(
            "networkStress",
            "Network Stress",
            HudGroup.CREATE_NETWORK
    ),

    NETWORK_CAPACITY(
            "networkCapacity",
            "Network Capacity",
            HudGroup.CREATE_NETWORK
    ),

    NETWORK_USAGE(
            "networkUsage",
            "Network Usage",
            HudGroup.CREATE_NETWORK
    ),

    NETWORK_SIZE(
            "networkSize",
            "Network Size",
            HudGroup.CREATE_NETWORK
    ),

    NETWORK_STATUS(
            "networkStatus",
            "Network Status",
            HudGroup.CREATE_NETWORK
    );

    private final String id;
    private final String displayName;
    private final HudGroup hudGroup;

    CreateHudElement(
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
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public HudGroup getHudGroup() {
        return hudGroup;
    }
}