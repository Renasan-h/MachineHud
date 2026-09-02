package com.rsdvlp.machinehud;

import com.rsdvlp.machinehud.hud.HudState;
import com.rsdvlp.machinehud.hud.MachineHudRenderer;
import com.rsdvlp.machinehud.key.ModKeyMappings;
import com.rsdvlp.machinehud.model.MachineHudGogglesModel;
import com.rsdvlp.machinehud.screen.MachineHudConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = MachineHUD.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = MachineHUD.MODID, value = Dist.CLIENT)
public class MachineHUDClient {
    private static final ResourceLocation HUD_LAYER = ResourceLocation.fromNamespaceAndPath(
            MachineHUD.MODID,
            "machine_hud"
    );

    public MachineHUDClient(ModContainer container) {

        // Mods画面からMachineHUDのConfigボタンが押されたときに、
        // NeoForge標準設定画面ではなくMachineHUD専用設定画面を開く。
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (modContainer, parent) ->
                        new MachineHudConfigScreen(parent)
        );
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        MachineHUD.LOGGER.info("HELLO FROM CLIENT SETUP");
        MachineHUD.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void registerGuiLayers(RegisterGuiLayersEvent event) {

        // MachineHUD用のGUIレイヤーを登録する。
        // 実際の描画処理はMachineHudRendererへ委譲することで、
        // Client初期化処理とHUD描画処理を分離する。
        event.registerAboveAll(
                HUD_LAYER,
                (guiGraphics, deltaTracker) ->
                        MachineHudRenderer.render(guiGraphics)
        );
    }

    @SubscribeEvent
    static void registerKeyMappings(RegisterKeyMappingsEvent event) {

        // Minecraftのキー設定一覧へ
        // MachineHUDの表示切り替えキーを登録する。
        event.register(ModKeyMappings.TOGGLE_HUD);
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {

        // TOGGLE_HUDキーが押された回数だけ処理する。
        //
        // consumeClick()を使うことで、
        // キーを押しっぱなしにしただけで毎Tick切り替わるのを防ぐ。
        while (ModKeyMappings.TOGGLE_HUD.consumeClick()) {

            // HUD全体の表示状態を反転する。
            HudState.toggle();
        }
    }
}
