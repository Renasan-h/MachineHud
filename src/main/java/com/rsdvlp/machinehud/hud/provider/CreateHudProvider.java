package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudGroup;
import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.data.CreateHudData;
import com.rsdvlp.machinehud.hud.data.KineticStatus;
import com.rsdvlp.machinehud.hud.data.NetworkStatus;
import com.rsdvlp.machinehud.hud.element.CreateHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;
import net.minecraft.network.chat.Component;

import static com.rsdvlp.machinehud.hud.element.CreateHudElement.*;

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

        if (!(element instanceof CreateHudElement)) {
            return false;
        }

        /*
         * Create共通Providerは、Kinetic情報とNetwork情報のみ担当する。
         * ProcessingやBoilerなどの機械固有情報は、それぞれ専用Providerへ任せる。
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
            case SPEED -> new HudLine(
                    Component.translatable(SPEED.getDisplayName()),
                    Component.literal(String.format("%.1f RPM", data.getSpeed())),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case IMPACT -> new HudLine(
                    Component.translatable(IMPACT.getDisplayName()),
                    Component.literal(String.format("%.2f SU/RPM", data.getImpact())),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case STRESS -> new HudLine(
                    Component.translatable(STRESS.getDisplayName()),
                    Component.literal(String.format("%.1f SU", data.getStress())),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case STATUS -> {

                KineticStatus status =
                        data.getKineticStatus();

                yield new HudLine(
                        Component.translatable(STATUS.getDisplayName()),
                        Component.translatable(status.getStatus()),
                        1,
                        status.getColor(),
                        HudLineType.VALUE,
                        null,
                        null
                );
            }

            case THEORETICAL_SPEED -> new HudLine(
                    Component.translatable(THEORETICAL_SPEED.getDisplayName()),
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

            case NETWORK_STRESS -> new HudLine(
                    Component.translatable(NETWORK_STRESS.getDisplayName()),
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

            case NETWORK_CAPACITY -> new HudLine(
                    Component.translatable(NETWORK_CAPACITY.getDisplayName()),
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

            case NETWORK_USAGE -> new HudLine(
                    Component.translatable(NETWORK_USAGE.getDisplayName()),
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

            case NETWORK_SIZE -> new HudLine(
                    Component.translatable(NETWORK_SIZE.getDisplayName()),
                    Component.literal(Integer.toString(
                            data.getNetworkSize()
                    )),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case NETWORK_STATUS -> {

                NetworkStatus status =
                        data.getNetworkStatus();

                yield new HudLine(
                        Component.translatable(NETWORK_STATUS.getDisplayName()),
                        Component.literal(status.getStatus()),
                        1,
                        status.getColor(),
                        HudLineType.VALUE,
                        null,
                        null
                );
            }

            // 機械固有情報は専用Providerが担当する。
            case PROCESSING_MODE,
                 PROCESSING_STATE,
                 BOILER_LEVEL,
                 BOILER_SIZE,
                 BOILER_WATER,
                 BOILER_HEAT,
                 BOILER_OUTPUT -> null;
        };
    }
}
