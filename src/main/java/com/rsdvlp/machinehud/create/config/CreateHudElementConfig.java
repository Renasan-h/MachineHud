package com.rsdvlp.machinehud.create.config;

import com.rsdvlp.machinehud.common.config.ClientConfig;
import com.rsdvlp.machinehud.common.hud.element.HudElementConfig;
import com.rsdvlp.machinehud.create.element.CreateHudElement;

/**
 * Create固有のHudElementとClientConfigの対応を登録する。
 */
public class CreateHudElementConfig {

    private CreateHudElementConfig(){}

    /**
     * Create用HudElementのConfig対応を
     * 共通HudElementConfigへ登録する。
     */
    public static void register(){
        // Create - Machine
        HudElementConfig.register(
                CreateHudElement.SPEED,
                ClientConfig.SHOW_SPEED
        );

        HudElementConfig.register(
                CreateHudElement.IMPACT,
                ClientConfig.SHOW_IMPACT
        );

        HudElementConfig.register(
                CreateHudElement.STRESS,
                ClientConfig.SHOW_STRESS
        );

        HudElementConfig.register(
                CreateHudElement.STATUS,
                ClientConfig.SHOW_STATUS
        );

        HudElementConfig.register(
                CreateHudElement.THEORETICAL_SPEED,
                ClientConfig.SHOW_THEORETICAL_SPEED
        );

        // Create - Processing
        HudElementConfig.register(
                CreateHudElement.PROCESSING_MODE,
                ClientConfig.SHOW_PROCESSING_MODE
        );

        HudElementConfig.register(
                CreateHudElement.PROCESSING_STATE,
                ClientConfig.SHOW_PROCESSING_STATE
        );

        // Create - Boiler
        HudElementConfig.register(
                CreateHudElement.BOILER_SIZE,
                ClientConfig.SHOW_BOILER_SIZE
        );

        HudElementConfig.register(
                CreateHudElement.BOILER_WATER,
                ClientConfig.SHOW_BOILER_WATER
        );

        HudElementConfig.register(
                CreateHudElement.BOILER_HEAT,
                ClientConfig.SHOW_BOILER_HEAT
        );

        HudElementConfig.register(
                CreateHudElement.BOILER_LEVEL,
                ClientConfig.SHOW_BOILER_LEVEL
        );

        HudElementConfig.register(
                CreateHudElement.BOILER_OUTPUT,
                ClientConfig.SHOW_BOILER_OUTPUT
        );

        // Create - Network
        HudElementConfig.register(
                CreateHudElement.NETWORK_STRESS,
                ClientConfig.SHOW_NETWORK_STRESS
        );

        HudElementConfig.register(
                CreateHudElement.NETWORK_CAPACITY,
                ClientConfig.SHOW_NETWORK_CAPACITY
        );

        HudElementConfig.register(
                CreateHudElement.NETWORK_USAGE,
                ClientConfig.SHOW_NETWORK_USAGE
        );

        HudElementConfig.register(
                CreateHudElement.NETWORK_SIZE,
                ClientConfig.SHOW_NETWORK_SIZE
        );

        HudElementConfig.register(
                CreateHudElement.NETWORK_STATUS,
                ClientConfig.SHOW_NETWORK_STATUS
        );

        // Create - Power
        HudElementConfig.register(
                CreateHudElement.POWER_STATE,
                ClientConfig.SHOW_POWER_STATE
        );

        HudElementConfig.register(
                CreateHudElement.POWER_TARGET_SPEED,
                ClientConfig.SHOW_POWER_TARGET_SPEED
        );

        HudElementConfig.register(
                CreateHudElement.POWER_SPEED_MODIFIER,
                ClientConfig.SHOW_POWER_TARGET_SPEED_MODIFIER
        );

        HudElementConfig.register(
                CreateHudElement.POWER_REDSTONE_SIGNAL,
                ClientConfig.SHOW_POWER_REDSTONE_SIGNAL
        );

        // Create - Fluid
        HudElementConfig.register(
                CreateHudElement.FLUID_INPUT_CONNECTION,
                ClientConfig.SHOW_FLUID_INPUT_CONNECTION
        );

        HudElementConfig.register(
                CreateHudElement.FLUID_OUTPUT_CONNECTION,
                ClientConfig.SHOW_FLUID_OUTPUT_CONNECTION
        );

        HudElementConfig.register(
                CreateHudElement.FLUID_MAX_FLOW_RATE,
                ClientConfig.SHOW_FLUID_MAX_FLOW_RATE
        );

        HudElementConfig.register(
                CreateHudElement.FLUID_VALVE_STATE,
                ClientConfig.SHOW_FLUID_VALVE_STATE
        );

        HudElementConfig.register(
                CreateHudElement.FLUID_FILTER,
                ClientConfig.SHOW_FLUID_FILTER
        );

        HudElementConfig.register(
                CreateHudElement.FLUID_CONTENT,
                ClientConfig.SHOW_FLUID_CONTENT
        );

        HudElementConfig.register(
                CreateHudElement.FLUID_AMOUNT,
                ClientConfig.SHOW_FLUID_AMOUNT
        );
    }
}
