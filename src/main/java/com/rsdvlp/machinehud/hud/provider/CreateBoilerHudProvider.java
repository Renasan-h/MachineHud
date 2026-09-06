package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudLevel;
import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.data.CreateBoilerHudData;
import com.rsdvlp.machinehud.hud.element.CreateHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;
import net.minecraft.network.chat.Component;

/**
 * Create Boiler専用のHUD情報生成Provider。
 * BoilerDataから取得した情報を、
 * MachineHUDのHudLineへ変換する。
 */
public final class CreateBoilerHudProvider implements HudProvider {

    private static final int TEXT_PRIMARY = 0xFFFFFF;

    private final CreateBoilerHudData data;

    public CreateBoilerHudProvider(
            CreateBoilerHudData data
    ) {
        this.data = data;
    }

    @Override
    public boolean supports(
            HudElement element
    ) {
        return element == CreateHudElement.BOILER_WATER;
    }

    @Override
    public HudLine createLine(
            HudElement element
    ) {

        if (element != CreateHudElement.BOILER_WATER) {
            return null;
        }

        return new HudLine(
                Component.literal("Water"),
                Component.literal(
                        Integer.toString(
                                data.waterLevel()
                        )
                ),
                1,
                TEXT_PRIMARY,
                HudLineType.LEVEL_BLOCKS,
                null,
                new HudLevel(
                        data.waterLevel(),
                        data.maxLevel()
                )
        );
    }
}