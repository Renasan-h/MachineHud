package com.rsdvlp.machinehud;

import com.rsdvlp.machinehud.model.MachineHudGogglesModel;
import com.rsdvlp.machinehud.model.ModModelLayers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;

/**
 * Machine HUD Gogglesのクライアント専用処理。
 *
 * 専用ArmorModelを遅延生成して保持する。
 */
public final class MachineHudGogglesClient {

    // 一度生成したモデルを再利用する。
    private static MachineHudGogglesModel MODEL;

    private MachineHudGogglesClient() {
    }

    /**
     * Machine HUD Gogglesの専用モデルを取得する。
     *
     * 初回呼び出し時だけbakeLayer()してモデルを生成する。
     */
    public static HumanoidModel<?> getModel() {

        if (MODEL == null) {

            // 登録済みのLayerDefinitionから、
            // 実際に描画可能なModelPartを生成する。
            MODEL =
                    new MachineHudGogglesModel(
                            Minecraft.getInstance()
                                    .getEntityModels()
                                    .bakeLayer(
                                            ModModelLayers.MACHINE_HUD_GOGGLES
                                    )
                    );
        }

        return MODEL;
    }
}