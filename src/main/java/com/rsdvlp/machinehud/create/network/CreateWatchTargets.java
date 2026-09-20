package com.rsdvlp.machinehud.create.network;

import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * CreateのBlockEntityがMachineHUDの
 * Server監視対象になるかを判定する。
 * Create固有クラスへの依存をこのクラスへ集約し、
 * 共通の監視管理処理からCreateへの直接依存を分離する。
 */
public final class CreateWatchTargets {

    private CreateWatchTargets() {
    }

    /**
     * 指定されたBlockEntityが、
     * Server側で継続監視する必要のあるCreate機械か判定する。
     * 現在Server同期が必要なのはMechanical Pumpだけなので、
     * PumpBlockEntityのみを対象とする。
     */
    public static boolean supports(BlockEntity blockEntity) {
        return blockEntity instanceof PumpBlockEntity;
    }
}