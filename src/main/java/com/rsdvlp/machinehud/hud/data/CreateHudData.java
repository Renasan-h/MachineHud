package com.rsdvlp.machinehud.hud.data;

import com.rsdvlp.machinehud.mixin.KineticBlockEntityAccessor;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.world.level.block.state.BlockState;


/**
 * CreateのKineticBlockEntityから、
 * MachineHUDで使用する情報を取得するクラス。
 *
 * Renderer側でCreate内部のAPIやMixinを直接扱わず、
 * Createに関するデータ取得処理をこのクラスへ集約する。
 */
public final class CreateHudData {

    // 情報取得対象となるCreateの回転BlockEntity。
    private final KineticBlockEntity kinetic;

    // 対象ブロックのBlockState。
    // Stress Impactなど、ブロック自体から取得する情報に使用する。
    private final BlockState blockState;

    // Create内部のNetwork情報へアクセスするためのMixin Accessor。
    private final KineticBlockEntityAccessor accessor;


    /**
     * CreateのHUD情報取得オブジェクトを作成する。
     *
     * @param kinetic 対象となるCreateの回転BlockEntity
     * @param blockState 対象ブロックの状態
     */
    public CreateHudData(
            KineticBlockEntity kinetic,
            BlockState blockState
    ) {

        this.kinetic = kinetic;
        this.blockState = blockState;

        // MixinによってKineticBlockEntityへ追加された
        // Accessorを取得する。
        //
        // Network StressやCapacityなど、
        // Createが公開getterを用意していない値の取得に使用する。
        this.accessor =
                (KineticBlockEntityAccessor) kinetic;
    }


    /**
     * 現在実際に回転している速度を取得する。
     */
    public float getSpeed() {

        return kinetic.getSpeed();
    }


    /**
     * Stress超過を無視した理論上の回転速度を取得する。
     */
    public float getTheoreticalSpeed() {

        return kinetic.getTheoreticalSpeed();
    }


    /**
     * 対象機械のStress Impact係数を取得する。
     *
     * この値は1 RPMあたりのStress消費量を表す。
     */
    public double getImpact() {

        return BlockStressValues.getImpact(
                blockState.getBlock()
        );
    }


    /**
     * 対象機械単体の現在のStress消費量を計算する。
     *
     * Stress = Impact × |RPM|
     */
    public double getStress() {

        // 回転方向はStress量に影響しないため、
        // RPMを絶対値へ変換して計算する。
        double rpm =
                Math.abs(getTheoreticalSpeed());

        return getImpact() * rpm;
    }


    /**
     * 回転ネットワーク全体のStressを取得する。
     */
    public float getNetworkStress() {

        return accessor.machinehud$getNetworkStress();
    }


    /**
     * 回転ネットワーク全体のStress Capacityを取得する。
     */
    public float getNetworkCapacity() {

        return accessor.machinehud$getNetworkCapacity();
    }


    /**
     * Createが保持しているネットワークサイズを取得する。
     */
    public int getNetworkSize() {

        return accessor.machinehud$getNetworkSize();
    }


    /**
     * ネットワークのStress使用率を百分率で取得する。
     *
     * 例:
     * Stress   = 384 SU
     * Capacity = 512 SU
     *
     * 384 / 512 * 100 = 75%
     */
    public double getNetworkUsage() {

        float capacity = getNetworkCapacity();

        // Capacityが0の場合は0除算を防ぐため、
        // 使用率を0%として返す。
        if (capacity <= 0) {
            return 0.0;
        }

        return getNetworkStress()
                / capacity
                * 100.0;
    }


    /**
     * Createの回転ネットワークがStress超過状態か確認する。
     */
    public boolean isOverstressed() {

        return kinetic.isOverStressed();
    }


    /**
     * 対象へ実際に回転が伝わっているか確認する。
     */
    public boolean isRunning() {

        return getSpeed() != 0;
    }

    /**
     * 対象機械の回転状態を取得する。
     * ここでいうRunningは「機械が加工中」という意味ではなく、
     * このBlockEntityへ実際に回転が伝わっていることを表す。
     */
    public KineticStatus getKineticStatus() {

        // Stress Capacityを超過している場合は、
        // Speedが0でもStoppedではなくOverstressedとして扱う。
        if (isOverstressed()) {
            return KineticStatus.OVERSTRESSED;
        }

        // 実際の回転速度が0以外なら、
        // この機械へ回転が伝わっている。
        if (isRunning()) {
            return KineticStatus.RUNNING;
        }

        // Stress超過でもなく回転速度も0なら停止状態。
        return KineticStatus.STOPPED;
    }

    /**
     * Createの回転ネットワーク全体の状態を取得する。
     */
    public NetworkStatus getNetworkStatus() {

        // Create自身がOverstressedと判定している場合は、
        // Capacity不足によってネットワークが停止している。
        if (isOverstressed()) {
            return NetworkStatus.OVERSTRESSED;
        }

        // Stress超過ではないが実際の回転速度が0の場合は、
        // 現在ネットワークから回転が供給されていない。
        if (getSpeed() == 0) {
            return NetworkStatus.STOPPED;
        }

        // Stress超過しておらず回転している場合は正常。
        return NetworkStatus.STABLE;
    }

    /**
     * 対象のKineticBlockEntityが
     * Createの回転ネットワークIDを持っているか確認する。
     */
    public boolean hasNetwork() {

        return kinetic.hasNetwork();
    }
}