package com.rsdvlp.machinehud.mekanism.config;

import com.rsdvlp.machinehud.common.config.ClientConfig;
import com.rsdvlp.machinehud.common.hud.element.HudElementConfig;
import com.rsdvlp.machinehud.mekanism.element.MekanismHudElement;

/**
 * Mekanism固有のHudElementとClientConfigの対応を登録する。
 */
public final class MekanismHudElementConfig {

    private MekanismHudElementConfig() {
    }

    /**
     * Mekanism用HudElementのConfig対応を
     * 共通HudElementConfigへ登録する。
     */
    public static void register() {

        // Mekanism - Energy
        HudElementConfig.register(
                MekanismHudElement.MEKANISM_ENERGY,
                ClientConfig.SHOW_MEKANISM_ENERGY
        );

        HudElementConfig.register(
                MekanismHudElement.MEKANISM_ENERGY_USAGE,
                ClientConfig.SHOW_MEKANISM_ENERGY_USAGE
        );

        // Mekanism - Processing
        HudElementConfig.register(
                MekanismHudElement.MEKANISM_PROGRESS,
                ClientConfig.SHOW_MEKANISM_PROGRESS
        );

        HudElementConfig.register(
                MekanismHudElement.MEKANISM_STATUS,
                ClientConfig.SHOW_MEKANISM_STATUS
        );

        // Mekanism - Chemical
        HudElementConfig.register(
                MekanismHudElement.MEKANISM_CHEMICAL,
                ClientConfig.SHOW_MEKANISM_CHEMICAL
        );

        HudElementConfig.register(
                MekanismHudElement.MEKANISM_CHEMICAL_AMOUNT,
                ClientConfig.SHOW_MEKANISM_CHEMICAL_AMOUNT
        );

        HudElementConfig.register(
                MekanismHudElement.MEKANISM_CHEMICAL_INPUT,
                ClientConfig.SHOW_MEKANISM_CHEMICAL_INPUT
        );

        HudElementConfig.register(
                MekanismHudElement.MEKANISM_CHEMICAL_INPUT_AMOUNT,
                ClientConfig.SHOW_MEKANISM_CHEMICAL_INPUT_AMOUNT
        );

        HudElementConfig.register(
                MekanismHudElement.MEKANISM_CHEMICAL_OUTPUT,
                ClientConfig.SHOW_MEKANISM_CHEMICAL_OUTPUT
        );

        HudElementConfig.register(
                MekanismHudElement.MEKANISM_CHEMICAL_OUTPUT_AMOUNT,
                ClientConfig.SHOW_MEKANISM_CHEMICAL_OUTPUT_AMOUNT
        );

        HudElementConfig.register(
                MekanismHudElement.MEKANISM_FLUID_INPUT,
                ClientConfig.SHOW_MEKANISM_FLUID_INPUT
        );

        HudElementConfig.register(
                MekanismHudElement.MEKANISM_FLUID_INPUT_AMOUNT,
                ClientConfig.SHOW_MEKANISM_FLUID_INPUT_AMOUNT
        );
    }
}