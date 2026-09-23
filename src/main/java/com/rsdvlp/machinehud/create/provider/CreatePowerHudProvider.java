package com.rsdvlp.machinehud.create.provider;

import com.rsdvlp.machinehud.common.hud.HudGroup;
import com.rsdvlp.machinehud.common.hud.HudLine;
import com.rsdvlp.machinehud.common.hud.HudLineType;
import com.rsdvlp.machinehud.create.data.CreatePowerHudData;
import com.rsdvlp.machinehud.create.element.CreateHudElement;
import com.rsdvlp.machinehud.common.hud.element.HudElement;
import com.rsdvlp.machinehud.common.hud.provider.HudProvider;
import com.simibubi.create.content.kinetics.chainDrive.ChainGearshiftBlockEntity;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity;
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
        } else if (data instanceof SpeedControllerBlockEntity speedController) {
            this.data = CreatePowerHudData.create(speedController);
        } else if (data instanceof CreativeMotorBlockEntity motor) {
            this.data = CreatePowerHudData.create(motor);
        } else if (data instanceof ChainGearshiftBlockEntity chainGearshift) {
            this.data = CreatePowerHudData.create(chainGearshift);
        } else {
            this.data = null;
        }
    }

    @Override
    public boolean supports(
            HudElement element
    ) {

        if (!(element instanceof CreateHudElement createElement)) {
            return false;
        }

        if (data == null) {
            return false;
        }

        return switch (data.type()) {

            case CLUTCH,
                 GEARSHIFT -> createElement == CreateHudElement.CREATE_POWER_STATE;
            case SPEED_CONTROLLER,
                 CREATIVE_MOTOR -> createElement == CreateHudElement.CREATE_POWER_TARGET_SPEED;
            case ADJUSTABLE_CHAIN_GEARSHIFT -> createElement == CreateHudElement.CREATE_POWER_SPEED_MODIFIER
            || createElement == CreateHudElement.CREATE_POWER_REDSTONE_SIGNAL;
        };
    }

    @Override
    public HudLine createLine(
            HudElement element
    ) {

        if (!(element instanceof CreateHudElement createElement)) {
            return null;
        }

        return switch (createElement) {
            case CREATE_POWER_STATE -> createStateLine(createElement);
            case CREATE_POWER_TARGET_SPEED -> createTargetSpeedLine(createElement);
            case CREATE_POWER_SPEED_MODIFIER -> createSpeedModifierLine(createElement);
            case CREATE_POWER_REDSTONE_SIGNAL -> createRedstoneSignalLine(createElement);
            default -> null;
        };
    }

    /**
     * 動力系ブロックの現在状態を表示する。
     */
    private HudLine createStateLine(
            CreateHudElement element
    ) {

        Component value = switch (data.state()) {
            case CONNECTED -> Component.translatable(
                    "machinehud.create.power.state.connected"
            );
            case DISCONNECTED -> Component.translatable(
                    "machinehud.create.power.state.disconnected"
            );
            case NORMAL -> Component.translatable(
                    "machinehud.create.power.state.normal"
            );
            case REVERSED -> Component.translatable(
                    "machinehud.create.power.state.reversed"
            );
            // POWER_STATEを使用しない機械
            case NONE -> Component.empty();
        };

        int color = switch (data.state()) {

            case CONNECTED -> ChatFormatting.GREEN.getColor();
            case DISCONNECTED -> ChatFormatting.RED.getColor();
            case NORMAL,
                 REVERSED,
                 NONE -> ChatFormatting.WHITE.getColor();
        };

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                value,
                0,
                color,
                HudLineType.VALUE,
                HudGroup.CREATE_POWER,
                null
        );
    }

    private HudLine createTargetSpeedLine(
            CreateHudElement element
    ) {

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                Component.literal(
                        String.format(
                                "%.1f RPM",
                                data.targetSpeed()
                        )
                ),
                0,
                ChatFormatting.WHITE.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_POWER,
                null
        );
    }

    private HudLine createSpeedModifierLine(
            CreateHudElement element
    ) {

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                Component.literal(
                        String.format(
                                "×%.2f",
                                data.speedModifier()
                        )
                ),
                0,
                ChatFormatting.WHITE.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_POWER,
                null
        );
    }

    private HudLine createRedstoneSignalLine(
            CreateHudElement element
    ) {

        return new HudLine(
                Component.translatable(element.getDisplayName()),
                Component.literal(
                        Integer.toString(data.redstoneSignal())
                ),
                0,
                ChatFormatting.WHITE.getColor(),
                HudLineType.VALUE,
                HudGroup.CREATE_POWER,
                null
        );
    }
}