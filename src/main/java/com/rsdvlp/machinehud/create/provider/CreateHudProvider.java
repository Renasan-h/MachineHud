package com.rsdvlp.machinehud.create.provider;

import com.rsdvlp.machinehud.common.hud.HudGroup;
import com.rsdvlp.machinehud.common.hud.HudLine;
import com.rsdvlp.machinehud.common.hud.HudLineType;
import com.rsdvlp.machinehud.common.hud.element.HudElement;
import com.rsdvlp.machinehud.common.hud.provider.HudProvider;
import com.rsdvlp.machinehud.create.data.CreateHudData;
import com.rsdvlp.machinehud.create.data.KineticStatus;
import com.rsdvlp.machinehud.create.data.NetworkStatus;
import com.rsdvlp.machinehud.create.element.CreateHudElement;
import net.minecraft.network.chat.Component;

/**
 * Create専用のHUD情報生成Provider。
 * Create固有のデータ取得・表示変換をRendererから分離する。
 */
public final class CreateHudProvider implements HudProvider {

    // 通常文字。
    private static final int TEXT_PRIMARY = 0xFFFFFF;

    private final CreateHudData data;

    public CreateHudProvider(CreateHudData data) {
        this.data = data;
    }

    @Override
    public boolean supports(HudElement element) {

        if (!(element instanceof CreateHudElement createElement)) {
            return false;
        }

        /*
         * 動力入力が存在しない場合は、
         * 詳細なKinetic / Network情報を表示せず、
         * STATUSだけを表示する。
         */
        if (!data.hasPowerInput()) {
            return createElement == CreateHudElement.CREATE_STATUS;
        }

        /*
         * 動力入力が存在する場合は、
         * これまで通りKinetic / Network情報を担当する。
         */
        return element.getHudGroup() == HudGroup.CREATE_KINETIC
                || element.getHudGroup() == HudGroup.CREATE_NETWORK;
    }

    @Override
    public HudLine createLine(HudElement element) {

        if (!(element instanceof CreateHudElement createHudElement)) {
            return null;
        }

        return switch (createHudElement) {
            case CREATE_SPEED -> new HudLine(
                    Component.translatable(createHudElement.getDisplayName()),
                    Component.literal(String.format("%.1f RPM", data.getSpeed())),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case CREATE_IMPACT -> new HudLine(
                    Component.translatable(createHudElement.getDisplayName()),
                    Component.literal(String.format("%.2f SU/RPM", data.getImpact())),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case CREATE_STRESS -> new HudLine(
                    Component.translatable(createHudElement.getDisplayName()),
                    Component.literal(String.format("%.1f SU", data.getStress())),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case CREATE_STATUS -> {

                KineticStatus status =
                        data.getKineticStatus();

                yield new HudLine(
                        Component.translatable(createHudElement.getDisplayName()),
                        Component.translatable(status.getStatus()),
                        1,
                        status.getColor(),
                        HudLineType.VALUE,
                        null,
                        null
                );
            }

            case CREATE_THEORETICAL_SPEED -> new HudLine(
                    Component.translatable(createHudElement.getDisplayName()),
                    Component.literal(String.format(
                            "%.1f RPM",
                            data.getTheoreticalSpeed())
                    ),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case CREATE_NETWORK_STRESS -> new HudLine(
                    Component.translatable(createHudElement.getDisplayName()),
                    Component.literal(String.format(
                            "%.1f SU",
                            data.getNetworkStress())
                    ),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case CREATE_NETWORK_CAPACITY -> new HudLine(
                    Component.translatable(createHudElement.getDisplayName()),
                    Component.literal(String.format(
                            "%.1f SU",
                            data.getNetworkCapacity()
                    )),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case CREATE_NETWORK_USAGE -> new HudLine(
                    Component.translatable(createHudElement.getDisplayName()),
                    Component.literal(String.format(
                            "%.1f%%",
                            data.getNetworkUsage()
                    )),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case CREATE_NETWORK_SIZE -> new HudLine(
                    Component.translatable(createHudElement.getDisplayName()),
                    Component.literal(Integer.toString(
                            data.getNetworkSize()
                    )),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case CREATE_NETWORK_STATUS -> {

                NetworkStatus status =
                        data.getNetworkStatus();

                yield new HudLine(
                        Component.translatable(createHudElement.getDisplayName()),
                        Component.literal(status.getStatus()),
                        1,
                        status.getColor(),
                        HudLineType.VALUE,
                        null,
                        null
                );
            }

            // 機械固有情報は専用Providerが担当する。
            case CREATE_PROCESSING_MODE,
                 CREATE_PROCESSING_STATE,
                 CREATE_BOILER_LEVEL,
                 CREATE_BOILER_SIZE,
                 CREATE_BOILER_WATER,
                 CREATE_BOILER_HEAT,
                 CREATE_BOILER_OUTPUT,
                 CREATE_POWER_STATE,
                 CREATE_POWER_TARGET_SPEED,
                 CREATE_POWER_SPEED_MODIFIER,
                 CREATE_POWER_REDSTONE_SIGNAL,
                 CREATE_FLUID_INPUT_CONNECTION,
                 CREATE_FLUID_OUTPUT_CONNECTION,
                 CREATE_FLUID_MAX_FLOW_RATE,
                 CREATE_FLUID_VALVE_STATE,
                 CREATE_FLUID_FILTER,
                 CREATE_FLUID_AMOUNT,
                 CREATE_FLUID_CONTENT -> null;
        };
    }
}
