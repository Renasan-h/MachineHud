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
    }
}