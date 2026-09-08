package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudLevel;
import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.data.CreateBoilerHudData;
import com.rsdvlp.machinehud.hud.element.CreateHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;
import net.minecraft.network.chat.Component;

import static com.rsdvlp.machinehud.hud.element.CreateHudElement.*;

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
        return element == BOILER_LEVEL
                || element == BOILER_SIZE
                || element == BOILER_WATER
                || element == BOILER_HEAT;
    }

    @Override
    public HudLine createLine(
            HudElement element
    ) {

        if (!(element instanceof CreateHudElement createElement)) {
            return null;
        }

        return switch (createElement) {

            case BOILER_LEVEL -> new HudLine(
                    Component.translatable("machinehud.boiler.level"),
                    getBoilerLevelDisplay(data.boilerLevel(), data.maxLevel()),
                    1,
                    0xFF55FF55,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case BOILER_SIZE -> createLevelCompareLine(
                    Component.translatable("create.boiler.size"),
                    data.sizeLevel()
            );

            case BOILER_WATER -> createLevelCompareLine(
                    Component.translatable("create.boiler.water"),
                    data.waterLevel()
            );

            case BOILER_HEAT -> createLevelCompareLine(
                    Component.translatable("create.boiler.heat"),
                    data.heatLevel()
            );

            default -> null;
        };
    }

    private HudLine createLevelCompareLine(
            Component label,
            int currentLevel
    ) {

        return new HudLine(
                label,
                Component.literal(String.valueOf(currentLevel)),
                1,
                TEXT_PRIMARY,
                HudLineType.LEVEL_COMPARE,
                null,
                new HudLevel(
                        currentLevel,
                        data.maxLevel(),
                        data.minLevel()
                )
        );
    }

    private HudLine createLevelCompareLine(
            String label,
            int currentLevel
    ) {

        return new HudLine(
                Component.literal(label),
                Component.literal(String.valueOf(currentLevel)),
                1,
                TEXT_PRIMARY,
                HudLineType.LEVEL_COMPARE,
                null,
                new HudLevel(
                        currentLevel,
                        data.maxLevel(),
                        data.minLevel()
                )
        );
    }

    private Component getBoilerLevelDisplay(int boilerLevel, int maxLevel) {
        if (boilerLevel == 0) {
            return Component.translatable("create.boiler.passive");
        }

        if (boilerLevel == maxLevel) {
            return Component.translatable("create.boiler.max_lvl");
        }

        return Component.literal(String.valueOf(boilerLevel));
    }
}