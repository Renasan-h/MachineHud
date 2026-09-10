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
            "create.speed",
            "machinehud.unit.speed",
            HudGroup.CREATE_KINETIC
    ),
    IMPACT(
            "impact",
            "machinehud.unit.stress_impact",
            HudGroup.CREATE_KINETIC
    ),
    STRESS(
            "stress",
            "machinehud.unit.stress",
            HudGroup.CREATE_KINETIC
    ),
    STATUS(
            "status",
            "machinehud.unit.status",
            HudGroup.CREATE_KINETIC
    ),
    THEORETICAL_SPEED(
            "theoreticalSpeed",
            "machinehud.unit.theoretical_speed",
            HudGroup.CREATE_KINETIC
    ),

    /*
     * =========================
     * 加工情報
     * =========================
     *
     * Press / Mixerなど、
     * Createの加工機械で共通して使用する情報。
     */
    PROCESSING_MODE(
            "processingMode",
            "machinehud.processing.mode",
            HudGroup.CREATE_PROCESSING
    ),
    PROCESSING_STATE(
            "processingState",
            "machinehud.processing.state",
            HudGroup.CREATE_PROCESSING
    ),
    PROCESSING_PROGRESS(
            "processingProgress",
            "machinehud.processing.progress",
            HudGroup.CREATE_PROCESSING
    ),

    /*
     * =========================
     * ボイラー情報
     * =========================
     */
    BOILER_LEVEL(
            "boilerLevel",
            "machinehud.boiler.level",
            HudGroup.CREATE_BOILER
    ),
    BOILER_SIZE(
            "boilerSize",
            "create.boiler.size",
            HudGroup.CREATE_BOILER
    ),
    BOILER_WATER(
            "boilerWater",
            "create.boiler.water",
            HudGroup.CREATE_BOILER
    ),
    BOILER_HEAT(
            "boilerHeat",
            "create.boiler.heat",
            HudGroup.CREATE_BOILER
    ),
    BOILER_OUTPUT(
            "boilerStreamOutput",
            "machinehud.boiler.steam_output",
            HudGroup.CREATE_BOILER
    ),

    /*
     * =========================
     * 回転ネットワーク情報
     * =========================
     */
    NETWORK_STRESS(
            "networkStress",
            "machinehud.create.network.stress",
            HudGroup.CREATE_NETWORK
    ),
    NETWORK_CAPACITY(
            "networkCapacity",
            "machinehud.create.network.capacity",
            HudGroup.CREATE_NETWORK
    ),
    NETWORK_USAGE(
            "networkUsage",
            "machinehud.create.network.usage",
            HudGroup.CREATE_NETWORK
    ),
    NETWORK_SIZE(
            "networkSize",
            "machinehud.create.network.size",
            HudGroup.CREATE_NETWORK
    ),
    NETWORK_STATUS(
            "networkStatus",
            "machinehud.create.network.status",
            HudGroup.CREATE_NETWORK
    ),
    ;

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