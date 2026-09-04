package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.data.CreateHudData;
import com.rsdvlp.machinehud.hud.data.KineticStatus;
import com.rsdvlp.machinehud.hud.data.NetworkStatus;
import com.rsdvlp.machinehud.hud.element.CreateHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;

/**
 * Create専用のHUD情報生成Provider。
 * Create固有のデータ取得・表示変換をRendererから分離する。
 */
public final class CreateHudProvider implements HudProvider {
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
                    "Speed",
                    String.format("%.1f RPM", data.getSpeed()),
                    1,
                    0xFFFFFF,
                    HudLineType.VALUE,
                    null
            );

            case IMPACT -> new HudLine(
                    "Stress Impact",
                    String.format("%.2f SU/RPM", data.getImpact()),
                    1,
                    0xFFFFFF,
                    HudLineType.VALUE,
                    null
            );

            case STRESS -> new HudLine(
                    "Stress",
                    String.format("%.1f SU", data.getStress()),
                    1,
                    0xFFFFFF,
                    HudLineType.VALUE,
                    null
            );

            case STATUS -> {

                KineticStatus status =
                        data.getKineticStatus();

                yield new HudLine(
                        "Status",
                        status.getStatus(),
                        1,
                        status.getColor(),
                        HudLineType.VALUE,
                        null
                );
            }

            case THEORETICAL_SPEED -> new HudLine(
                    "Theoretical",
                    String.format(
                            "%.1f RPM",
                            data.getTheoreticalSpeed()
                    ),
                    1,
                    0xFFFFFF,
                    HudLineType.VALUE,
                    null
            );

            case NETWORK_STRESS -> new HudLine(
                    "Stress",
                    String.format(
                            "%.1f SU",
                            data.getNetworkStress()
                    ),
                    1,
                    0xFFFFFF,
                    HudLineType.VALUE,
                    null
            );

            case NETWORK_CAPACITY -> new HudLine(
                    "Capacity",
                    String.format(
                            "%.1f SU",
                            data.getNetworkCapacity()
                    ),
                    1,
                    0xFFFFFF,
                    HudLineType.VALUE,
                    null
            );

            case NETWORK_USAGE -> new HudLine(
                    "Usage",
                    String.format(
                            "%.1f%%",
                            data.getNetworkUsage()
                    ),
                    1,
                    0xFFFFFF,
                    HudLineType.VALUE,
                    null
            );

            case NETWORK_SIZE -> new HudLine(
                    "Network Size",
                    Integer.toString(
                            data.getNetworkSize()
                    ),
                    1,
                    0xFFFFFF,
                    HudLineType.VALUE,
                    null
            );

            case NETWORK_STATUS -> {

                NetworkStatus status =
                        data.getNetworkStatus();

                yield new HudLine(
                        "Network Status",
                        status.getStatus(),
                        1,
                        status.getColor(),
                        HudLineType.VALUE,
                        null
                );
            }
        };
    }
}
