package com.rsdvlp.machinehud.mixin;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * CreateのKineticBlockEntity内部に保持されている
 * 回転ネットワーク情報をMachineHUDから読み取るためのAccessor。
 *
 * KineticBlockEntityにはNetwork StressやCapacityが
 * クライアント側にも同期されているが、
 * 外部MOD向けのgetterが用意されていない。
 *
 * そのためMixin Accessorを使用して読み取り専用で参照する。
 */
@Mixin(KineticBlockEntity.class)
public interface KineticBlockEntityAccessor {
    // KineticBlockEntity#stressを取得する。
    //
    // この値は、この機械単体のStressではなく、
    // 接続されている回転ネットワーク全体のStress。
    @Accessor("stress")
    float machinehud$getNetworkStress();


    // KineticBlockEntity#capacityを取得する。
    //
    // 接続されている回転ネットワーク全体が
    // 供給できるStress Capacity。
    @Accessor("capacity")
    float machinehud$getNetworkCapacity();


    // KineticBlockEntity#networkSizeを取得する。
    //
    // Createが保持している回転ネットワークのサイズ。
    @Accessor("networkSize")
    int machinehud$getNetworkSize();
}
