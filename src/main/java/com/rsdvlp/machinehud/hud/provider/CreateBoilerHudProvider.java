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
                || element == BOILER_HEAT
                || element == BOILER_OUTPUT;
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
                    Component.translatable(BOILER_LEVEL.getDisplayName()),
                    getBoilerLevelDisplay(data.boilerLevel(), data.maxLevel()),
                    1,
                    0xFF55FF55,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case BOILER_SIZE -> createLevelCompareLine(
                    Component.translatable(BOILER_SIZE.getDisplayName()),
                    data.sizeLevel()
            );

            case BOILER_WATER -> createLevelCompareLine(
                    Component.translatable(BOILER_WATER.getDisplayName()),
                    data.waterLevel()
            );

            case BOILER_HEAT -> createLevelCompareLine(
                    Component.translatable(BOILER_HEAT.getDisplayName()),
                    data.heatLevel()
            );

            case BOILER_OUTPUT -> {
                // Steam Engineが接続されていない場合は出力先が存在しないためHUDには表示しない。
                if (data.attachedEngines() <= 0) {
                    yield null;
                }

                // 水供給が不足している場合は、
                // Steam OutputではなくWater Inputを案内する。
                if (data.requiresWaterInput()) {
                    yield createWaterInputLine();
                }

                yield new HudLine(
                        Component.translatable(BOILER_OUTPUT.getDisplayName()),
                        createSteamOutputDisplay(),
                        1,
                        TEXT_PRIMARY,
                        HudLineType.VALUE,
                        null,
                        null
                );
            }

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

    /**
     * BoilerからSteam Engine経由で取り出せる出力を表示する。
     * 表示例:
     * 2,048 SU ×1
     */
    private Component createSteamOutputDisplay() {

        String capacity = String.format("%,.0f", data.stressCapacity());

        return Component.literal(capacity + " SU").append(Component.literal(" ×" + data.attachedEngines()));
    }

    /**
     * 水供給不足時に、現在のWater Inputを表示する。
     * CreateではBoiler Level 1につき
     * 10 mB/tの水供給を必要とする。
     */
    private HudLine createWaterInputLine() {

        return new HudLine(
                Component.translatable("machinehud.boiler.water_input"),
                Component.literal(
                        String.format("%.1f mB/t / 10 mB/t", data.waterSupply())
                ),
                1,
                TEXT_PRIMARY,
                HudLineType.VALUE,
                null,
                null
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