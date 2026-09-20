package com.rsdvlp.machinehud.create.network;

import com.rsdvlp.machinehud.MachineHUD;
import com.rsdvlp.machinehud.common.network.WatchTargetHandler;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Createの機械をServer側で監視するためのHandler。
 * Create固有のBlockEntity型やデータ取得処理を
 * common側のMachineWatchManagerから分離する。
 */
public final class CreateWatchTargetHandler implements WatchTargetHandler {

    /**
     * 指定されたBlockEntityが、
     * Create側でServer監視を必要とする機械か判定する。
     * 現在はMechanical Pumpのみが対象。
     */
    @Override
    public boolean supports(BlockEntity blockEntity) {
        return CreateWatchTargets.supports(blockEntity);
    }

    /**
     * 監視中のCreate機械を処理する。
     * 現在はServer → Client同期がまだ未実装なので、
     * Mechanical Pumpの取得確認として速度をログへ出力する。
     */
    @Override
    public void process(
            ServerPlayer player,
            BlockEntity blockEntity
    ) {
        /*
         * supports()を通過した後に呼ばれる想定だが、
         * Handler単体で呼ばれても安全になるよう
         * ここでも型を確認する。
         */
        if (!(blockEntity instanceof PumpBlockEntity pump)) {
            return;
        }

        /*
         * 現段階では同期処理の代わりに、Server側でPumpを取得できていることを確認する。
         * 後で実流量などのServer専用データを取得する処理へ
         * この部分を置き換える。
         */
        MachineHUD.LOGGER.debug(
                "Processing watched Pump: player={}, pos={}, speed={}",
                player.getName().getString(),
                pump.getBlockPos(),
                pump.getSpeed()
        );
    }
}