package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudGroup;
import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.HudLineType;
import com.rsdvlp.machinehud.hud.data.CreatePowerHudData;
import com.rsdvlp.machinehud.hud.data.CreateProcessingHudData;
import com.rsdvlp.machinehud.hud.element.CreateHudElement;
import com.rsdvlp.machinehud.hud.element.HudElement;
import com.simibubi.create.content.kinetics.transmission.ClutchBlockEntity;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Createの動力系ブロック専用HUD Provider。
 * <p>
 * 動力源・伝達・制御など、
 * MachineHudGoggleの主目的となる
 * 動力ネットワーク構築支援情報を提供する。
 */
public final class CreatePowerHudProvider implements HudProvider {

    private final CreatePowerHudData data;

    public CreatePowerHudProvider(
            BlockEntity data
    ) {
        if (data instanceof ClutchBlockEntity clutch) {
            this.data = CreatePowerHudData.create(clutch);
        } else if (data instanceof GearshiftBlockEntity gearshift) {
            this.data = CreatePowerHudData.create(gearshift);
        } else {
            this.data = null;
        }
    }

    @Override
    public boolean supports(
            HudElement element
    ) {
        return element instanceof CreateHudElement
                && element.getHudGroup() == HudGroup.CREATE_POWER;
    }

    @Override
    public HudLine createLine(
            HudElement element
    ) {

        if (!(element instanceof CreateHudElement createElement)) {
            return null;
        }

        return switch (createElement) {

            case POWER_STATE -> createStateLine();

            default -> null;
        };
    }

    /**
     * 動力系ブロックの現在状態を表示する。
     */
    private HudLine createStateLine() {

        Component value = switch (data.state()) {

            case CONNECTED -> Component.translatable(
                    "machinehud.power.state.connected"
            );
            case DISCONNECTED -> Component.translatable(
                    "machinehud.power.state.disconnected"
            );
            case NORMAL -> Component.translatable(
                    "machinehud.power.state.normal"
            );
            case REVERSED -> Component.translatable(
                    "machinehud.power.state.reversed"
            );
        };

        int color = switch (data.state()) {

            case CONNECTED -> ChatFormatting.GREEN.getColor();
            case DISCONNECTED -> ChatFormatting.RED.getColor();
            case NORMAL,
                 REVERSED -> ChatFormatting.WHITE.getColor();
        };

        return new HudLine(
                Component.translatable(
                        CreateHudElement.POWER_STATE.getDisplayName()
                ),
                value,
                0,
                color,
                HudLineType.VALUE,
                HudGroup.CREATE_POWER,
                null
        );
    }
}