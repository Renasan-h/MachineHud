package com.rsdvlp.machinehud.model;

import com.rsdvlp.machinehud.MachineHUD;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * MachineHUDで使用するEntity Model Layerを管理するクラス。
 *
 * ModelLayerLocationは、
 * Minecraftへ「どのMODの、どのモデルレイヤーなのか」を
 * 識別させるために使用する。
 */
public final class ModModelLayers {

    /**
     * Machine HUD Gogglesのモデルレイヤー。
     */
    public static final ModelLayerLocation MACHINE_HUD_GOGGLES =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(
                            MachineHUD.MODID,
                            "machine_hud_goggles"
                    ),
                    "main"
            );

    // 定数だけを持つクラスなので
    // インスタンス化できないようにする。
    private ModModelLayers() {
    }
}