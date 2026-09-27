package com.rsdvlp.machinehud.mekanism;

import com.rsdvlp.machinehud.common.hud.element.HudElements;
import com.rsdvlp.machinehud.common.hud.provider.HudProviders;
import com.rsdvlp.machinehud.common.network.MachineHudPayloads;
import com.rsdvlp.machinehud.common.network.WatchTargetHandlers;
import com.rsdvlp.machinehud.mekanism.config.MekanismHudElementConfig;
import com.rsdvlp.machinehud.mekanism.data.*;
import com.rsdvlp.machinehud.mekanism.element.MekanismHudElement;
import com.rsdvlp.machinehud.mekanism.network.MekanismPayloads;
import com.rsdvlp.machinehud.mekanism.network.MekanismWatchTargetHandler;
import com.rsdvlp.machinehud.mekanism.provider.MekanismHudProviders;

/**
 * Mekanism連携の初期化を担当する。
 * Mekanismが導入されている場合にのみ呼び出す。
 */
public final class MekanismInitialize {

    private static boolean initialized = false;

    private MekanismInitialize() {
    }

    public static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;

        // Mekanism固有のHUD要素を登録する。
        HudElements.register(MekanismHudElement.values());

        // 既存の電動加工機械
        MekanismMachineHudDataReader.register(
                new ElectricMachineReader()
        );

        // 冶金注入機と高度電動加工機械
        MekanismMachineHudDataReader.register(
                new ProgressMachineReader()
        );

        // 加圧反応室
        MekanismMachineHudDataReader.register(
                new PressurizedReactionMachineReader()
        );

        // 電解分離機
        MekanismMachineHudDataReader.register(
                new ElectrolyticSeparatorMachineReader()
        );

        // MekanismのProvider生成処理を登録する。
        HudProviders.register(MekanismHudProviders::create);

        // サーバー側でMekanismの機械を監視できるようにする。
        WatchTargetHandlers.register(
                new MekanismWatchTargetHandler()
        );

        // Mekanism専用Payloadの登録処理を共通レジストリへ追加する。
        MachineHudPayloads.addRegistration(
                MekanismPayloads::register
        );
    }

    /**
     * MekanismのHUD要素とClientConfigを対応付ける。
     */
    public static void registerConfig() {
        MekanismHudElementConfig.register();
    }
}