package com.rsdvlp.machinehud.create.element;

import com.rsdvlp.machinehud.common.hud.HudGroup;
import com.rsdvlp.machinehud.common.hud.element.HudElement;

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
    CREATE_SPEED(
            "create.speed",
            "machinehud.create.unit.speed",
            HudGroup.CREATE_KINETIC
    ),
    CREATE_IMPACT(
            "impact",
            "machinehud.create.unit.stress_impact",
            HudGroup.CREATE_KINETIC
    ),
    CREATE_STRESS(
            "stress",
            "machinehud.create.unit.stress",
            HudGroup.CREATE_KINETIC
    ),
    CREATE_STATUS(
            "status",
            "machinehud.create.unit.status",
            HudGroup.CREATE_KINETIC
    ),
    CREATE_THEORETICAL_SPEED(
            "theoreticalSpeed",
            "machinehud.create.unit.theoretical_speed",
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
    CREATE_PROCESSING_MODE(
            "processingMode",
            "machinehud.create.processing.mode",
            HudGroup.CREATE_PROCESSING
    ),
    CREATE_PROCESSING_STATE(
            "processingState",
            "machinehud.create.processing.state",
            HudGroup.CREATE_PROCESSING
    ),

    /*
     * =========================
     * 動力情報
     * =========================
     */
    CREATE_POWER_STATE(
            "powerState",
            "machinehud.create.power.state",
            HudGroup.CREATE_POWER
    ),

    CREATE_POWER_TARGET_SPEED(
            "powerTargetSpeed",
            "machinehud.create.power.target_speed",
            HudGroup.CREATE_POWER
    ),

    CREATE_POWER_SPEED_MODIFIER(
            "powerSpeedModifier",
            "machinehud.create.power.speed_modifier",
            HudGroup.CREATE_POWER
    ),

    CREATE_POWER_REDSTONE_SIGNAL(
            "powerRedstoneSignal",
            "machinehud.create.power.redstone_signal",
            HudGroup.CREATE_POWER
    ),

    /*
     * =========================
     * 流体情報
     * =========================
     */
    CREATE_FLUID_INPUT_CONNECTION(
            "fluidInputDirection",
            "machinehud.create.fluid.input_direction",
            HudGroup.CREATE_FLUID
    ),

    CREATE_FLUID_OUTPUT_CONNECTION(
            "fluidOutputDirection",
            "machinehud.create.fluid.output_direction",
            HudGroup.CREATE_FLUID
    ),

    CREATE_FLUID_MAX_FLOW_RATE(
            "fluidMaxFlowRate",
            "machinehud.create.fluid.max_flow_rate",
            HudGroup.CREATE_FLUID
    ),

    CREATE_FLUID_VALVE_STATE(
            "fluidValveState",
            "machinehud.create.fluid.valve_state",
            HudGroup.CREATE_FLUID
    ),

    CREATE_FLUID_FILTER(
            "fluidFilter",
            "machinehud.create.fluid.filter",
            HudGroup.CREATE_FLUID
    ),

    CREATE_FLUID_CONTENT(
            "fluidContent",
            "machinehud.create.fluid.content",
            HudGroup.CREATE_FLUID
    ),

    CREATE_FLUID_AMOUNT(
            "fluidAmount",
            "machinehud.create.fluid.amount",
            HudGroup.CREATE_FLUID
    ),

    /*
     * =========================
     * ボイラー情報
     * =========================
     */
    CREATE_BOILER_LEVEL(
            "boilerLevel",
            "machinehud.create.boiler.level",
            HudGroup.CREATE_BOILER
    ),
    CREATE_BOILER_SIZE(
            "boilerSize",
            "machinehud.create.boiler.size",
            HudGroup.CREATE_BOILER
    ),
    CREATE_BOILER_WATER(
            "boilerWater",
            "machinehud.create.boiler.water",
            HudGroup.CREATE_BOILER
    ),
    CREATE_BOILER_HEAT(
            "boilerHeat",
            "machinehud.create.boiler.heat",
            HudGroup.CREATE_BOILER
    ),
    CREATE_BOILER_OUTPUT(
            "boilerStreamOutput",
            "machinehud.create.boiler.steam_output",
            HudGroup.CREATE_BOILER
    ),

    /*
     * =========================
     * 回転ネットワーク情報
     * =========================
     */
    CREATE_NETWORK_STRESS(
            "networkStress",
            "machinehud.create.network.stress",
            HudGroup.CREATE_NETWORK
    ),
    CREATE_NETWORK_CAPACITY(
            "networkCapacity",
            "machinehud.create.network.capacity",
            HudGroup.CREATE_NETWORK
    ),
    CREATE_NETWORK_USAGE(
            "networkUsage",
            "machinehud.create.network.usage",
            HudGroup.CREATE_NETWORK
    ),
    CREATE_NETWORK_SIZE(
            "networkSize",
            "machinehud.create.network.size",
            HudGroup.CREATE_NETWORK
    ),
    CREATE_NETWORK_STATUS(
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