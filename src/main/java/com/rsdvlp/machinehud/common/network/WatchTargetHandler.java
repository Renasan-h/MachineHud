package com.rsdvlp.machinehud.common.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * MachineHUDがServer側で監視するBlockEntityを処理するための共通契約。
 * CreateやMekanismなど、MOD固有のBlockEntity判定・処理を
 * MachineWatchManagerから分離するために使用する。
 */
public interface WatchTargetHandler {

    /**
     * このHandlerが指定されたBlockEntityを処理できるか判定する。
     * @param blockEntity 監視対象候補のBlockEntity
     * @return このHandlerが処理できる場合true
     */
    boolean supports(BlockEntity blockEntity);

    /**
     * 監視中のBlockEntityをServer側で処理する。
     * 将来的には、ここからMOD固有データを取得して
     * Clientへ同期する処理につなげる。
     *
     * @param player 監視しているPlayer
     * @param blockEntity 監視対象のBlockEntity
     */
    void process(
            ServerPlayer player,
            BlockEntity blockEntity
    );
}