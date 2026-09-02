package com.rsdvlp.machinehud.item;

import com.rsdvlp.machinehud.MachineHUD;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/**
 * MachineHUDのアイテムをMinecraftの
 * Creative Mode Tabへ追加するためのイベント処理。
 */
@EventBusSubscriber(
        modid = MachineHUD.MODID
)
public final class ModCreativeTabs {

    private ModCreativeTabs() {
    }

    @SubscribeEvent
    public static void onBuildCreativeTab(
            BuildCreativeModeTabContentsEvent event
    ) {

        // Tools & Utilitiesタブが構築されている場合だけ、
        // Machine HUD Gogglesを追加する。
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {

            event.accept(
                    ModItems.MACHINE_HUD_GOGGLES.get()
            );
        }
    }
}