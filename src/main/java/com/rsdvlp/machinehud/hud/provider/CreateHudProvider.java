package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.data.CreateHudData;
import com.rsdvlp.machinehud.hud.data.KineticStatus;
import com.rsdvlp.machinehud.hud.data.NetworkStatus;
import com.rsdvlp.machinehud.hud.element.CreateHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;
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
        return element instanceof CreateHudElement;
    }

    @Override
    public HudLine createLine(HudElement element) {

        if (!(element instanceof CreateHudElement createHudElement)) {
            return null;
        }

        return switch (createHudElement) {
            case SPEED -> new HudLine(
                    Component.literal("Speed"),
                    Component.literal(String.format("%.1f RPM", data.getSpeed())),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case IMPACT -> new HudLine(
                    Component.literal("Stress Impact"),
                    Component.literal(String.format("%.2f SU/RPM", data.getImpact())),
                    1,
                    TEXT_PRIMARY,
                    HudLineType.VALUE,
                    null,
                    null
            );

            case STRESS -> new HudLine(
                    Component.literal("Stress"),
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
                        Component.literal("Status"),
                        Component.literal(status.getStatus()),
                        1,
                        status.getColor(),
                        HudLineType.VALUE,
                        null,
                        null
                );
            }

            case THEORETICAL_SPEED -> new HudLine(
                    Component.literal("Theoretical"),
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
                    Component.literal("Stress"),
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
                    Component.literal("Capacity"),
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
                    Component.literal("Usage"),
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
                    Component.literal("Network Size"),
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
                        Component.literal("Network Status"),
                        Component.literal(status.getStatus()),
                        1,
                        status.getColor(),
                        HudLineType.VALUE,
                        null,
                        null
                );
            }
        };
    }
}
