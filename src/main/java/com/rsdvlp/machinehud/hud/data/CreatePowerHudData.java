package com.rsdvlp.machinehud.hud.data;

import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity;
import com.simibubi.create.content.kinetics.transmission.ClutchBlockEntity;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Createの動力系ブロックから取得したHUD表示用データ。
 * 動力源・動力伝達・動力制御など、
 * Createの動力ネットワーク構築を支援する情報を扱う。
 */
public record CreatePowerHudData(
        Type type,
        State state,
        float targetSpeed
) {

    /**
     * 動力系ブロックの種類。
     */
    public enum Type {
        CLUTCH,
        GEARSHIFT,
        SPEED_CONTROLLER
    }

    /**
     * 動力系ブロックの状態。
     */
    public enum State {
        CONNECTED,
        DISCONNECTED,
        NORMAL,
        REVERSED,
        NONE
    }

    /**
     * Clutchから動力制御情報を取得する。
     * CreateのClutchはRedstone信号を受けると
     * 動力伝達を遮断する。
     */
    public static CreatePowerHudData create(
            ClutchBlockEntity clutch
    ) {

        boolean powered =
                clutch.getBlockState()
                        .getValue(
                                BlockStateProperties.POWERED
                        );

        State state =
                powered
                        ? State.DISCONNECTED
                        : State.CONNECTED;

        return new CreatePowerHudData(
                Type.CLUTCH,
                state,
                0
        );
    }

    public static CreatePowerHudData create(
            GearshiftBlockEntity gearshift
    ) {

        boolean powered =
                gearshift.getBlockState()
                        .getValue(
                                BlockStateProperties.POWERED
                        );

        // RS信号がONの時入力を反転させる
        State state = powered ? State.REVERSED : State.NORMAL;

        return new CreatePowerHudData(
                Type.GEARSHIFT,
                state,
                0
        );
    }

    public static CreatePowerHudData create(
            SpeedControllerBlockEntity controller
    ) {

        return new CreatePowerHudData(
                Type.SPEED_CONTROLLER,
                State.NONE,
                controller.targetSpeed.getValue()
        );
    }
}