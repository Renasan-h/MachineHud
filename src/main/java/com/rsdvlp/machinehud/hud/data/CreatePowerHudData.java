package com.rsdvlp.machinehud.hud.data;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
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
        State state
) {

    /**
     * 動力系ブロックの種類。
     * 今後、動力源や速度制御装置などを追加していく。
     */
    public enum Type {
        CLUTCH,
        GEARSHIFT
    }

    /**
     * 動力系ブロックの状態。
     * 現在はClutchの接続状態のみ。
     */
    public enum State {
        CONNECTED,
        DISCONNECTED,
        NORMAL,
        REVERSED
    }

    public enum PowerInputState {
        NO_INPUT,
        RECEIVING
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
                state
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
                state
        );
    }

    private static PowerInputState getPowerInputState(
            KineticBlockEntity kinetic
    ) {

        if (kinetic.isSource()) {
            return PowerInputState.RECEIVING;
        }

        return kinetic.hasSource()
                ? PowerInputState.RECEIVING
                : PowerInputState.NO_INPUT;
    }
}