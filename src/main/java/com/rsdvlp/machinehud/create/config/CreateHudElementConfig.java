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
                CreateHudElement.CREATE_SPEED,
                ClientConfig.SHOW_CREATE_SPEED
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_IMPACT,
                ClientConfig.SHOW_CREATE_IMPACT
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_STRESS,
                ClientConfig.SHOW_CREATE_STRESS
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_STATUS,
                ClientConfig.SHOW_CREATE_STATUS
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_THEORETICAL_SPEED,
                ClientConfig.SHOW_CREATE_THEORETICAL_SPEED
        );

        // Create - Processing
        HudElementConfig.register(
                CreateHudElement.CREATE_PROCESSING_MODE,
                ClientConfig.SHOW_CREATE_PROCESSING_MODE
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_PROCESSING_STATE,
                ClientConfig.SHOW_CREATE_PROCESSING_STATE
        );

        // Create - Boiler
        HudElementConfig.register(
                CreateHudElement.CREATE_BOILER_SIZE,
                ClientConfig.SHOW_CREATE_BOILER_SIZE
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_BOILER_WATER,
                ClientConfig.SHOW_CREATE_BOILER_WATER
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_BOILER_HEAT,
                ClientConfig.SHOW_CREATE_BOILER_HEAT
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_BOILER_LEVEL,
                ClientConfig.SHOW_CREATE_BOILER_LEVEL
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_BOILER_OUTPUT,
                ClientConfig.SHOW_CREATE_BOILER_OUTPUT
        );

        // Create - Network
        HudElementConfig.register(
                CreateHudElement.CREATE_NETWORK_STRESS,
                ClientConfig.SHOW_CREATE_NETWORK_STRESS
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_NETWORK_CAPACITY,
                ClientConfig.SHOW_CREATE_NETWORK_CAPACITY
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_NETWORK_USAGE,
                ClientConfig.SHOW_CREATE_NETWORK_USAGE
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_NETWORK_SIZE,
                ClientConfig.SHOW_CREATE_NETWORK_SIZE
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_NETWORK_STATUS,
                ClientConfig.SHOW_CREATE_NETWORK_STATUS
        );

        // Create - Power
        HudElementConfig.register(
                CreateHudElement.CREATE_POWER_STATE,
                ClientConfig.SHOW_CREATE_POWER_STATE
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_POWER_TARGET_SPEED,
                ClientConfig.SHOW_CREATE_POWER_TARGET_SPEED
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_POWER_SPEED_MODIFIER,
                ClientConfig.SHOW_CREATE_POWER_TARGET_SPEED_MODIFIER
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_POWER_REDSTONE_SIGNAL,
                ClientConfig.SHOW_CREATE_POWER_REDSTONE_SIGNAL
        );

        // Create - Fluid
        HudElementConfig.register(
                CreateHudElement.CREATE_FLUID_INPUT_CONNECTION,
                ClientConfig.SHOW_CREATE_FLUID_INPUT_CONNECTION
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_FLUID_OUTPUT_CONNECTION,
                ClientConfig.SHOW_CREATE_FLUID_OUTPUT_CONNECTION
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_FLUID_MAX_FLOW_RATE,
                ClientConfig.SHOW_CREATE_FLUID_MAX_FLOW_RATE
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_FLUID_VALVE_STATE,
                ClientConfig.SHOW_CREATE_FLUID_VALVE_STATE
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_FLUID_FILTER,
                ClientConfig.SHOW_CREATE_FLUID_FILTER
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_FLUID_CONTENT,
                ClientConfig.SHOW_CREATE_FLUID_CONTENT
        );

        HudElementConfig.register(
                CreateHudElement.CREATE_FLUID_AMOUNT,
                ClientConfig.SHOW_CREATE_FLUID_AMOUNT
        );
    }
}
