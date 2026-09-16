package com.rsdvlp.machinehud.hud.data;

import com.simibubi.create.content.kinetics.chainDrive.ChainGearshiftBlockEntity;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity;
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
        float targetSpeed,
        float speedModifier,
        int redstoneSignal
) {

    /**
     * 動力系ブロックの種類。
     */
    public enum Type {
        CLUTCH,
        GEARSHIFT,
        SPEED_CONTROLLER,
        CREATIVE_MOTOR,
        ADJUSTABLE_CHAIN_GEARSHIFT
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
                0,
                1,
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
                0,
                1,
                0
        );
    }

    public static CreatePowerHudData create(
            SpeedControllerBlockEntity controller
    ) {

        return new CreatePowerHudData(
                Type.SPEED_CONTROLLER,
                State.NONE,
                controller.targetSpeed.getValue(),
                1,
                0
        );
    }

    public static CreatePowerHudData create(
            CreativeMotorBlockEntity motor
    ) {

        return new CreatePowerHudData(
                Type.CREATIVE_MOTOR,
                State.NONE,
                motor.generatedSpeed.getValue(),
                1,
                0
        );
    }

    /**
     * Adjustable Chain Gearshiftから
     * 現在の回転速度倍率を取得する。
     * Redstone信号強度に応じて
     * 1.0 ～ 2.0倍の倍率を持つ。
     */
    public static CreatePowerHudData create(
            ChainGearshiftBlockEntity gearshift
    ) {

        int signal = 0;

        if (gearshift.getLevel() != null) {
            signal = gearshift.getLevel()
                    .getBestNeighborSignal(
                            gearshift.getBlockPos()
                    );
        }

        return new CreatePowerHudData(
                Type.ADJUSTABLE_CHAIN_GEARSHIFT,
                State.NONE,
                0,
                gearshift.getModifier(),
                signal
        );
    }
}