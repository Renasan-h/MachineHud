package com.rsdvlp.machinehud.hud;

import com.rsdvlp.machinehud.MachineHUD;
import net.minecraft.resources.ResourceLocation;

/**
 * HUD項目を分類するグループ。
 * 各グループは表示名に加えて、HUD上で使用するアイコンのResourceLocationを保持する。
 */
public enum HudGroup {
    INFORMATION(
            "machinehud.header.information",
            ResourceLocation.fromNamespaceAndPath(
                    MachineHUD.MODID,
                    "textures/gui/icon/information.png"
            )
    ),

    CREATE_KINETIC(
            "machinehud.header.create_kinetic",
            ResourceLocation.fromNamespaceAndPath(
                    MachineHUD.MODID,
                    "textures/gui/icon/kinetic.png"
            )
    ),

    CREATE_PROCESSING(
            "machinehud.header.create_processing",
            ResourceLocation.fromNamespaceAndPath(
                    MachineHUD.MODID,
                    "textures/gui/icon/kinetic.png"
            )
    ),

    CREATE_BOILER(
            "machinehud.header.create_boiler",
            ResourceLocation.fromNamespaceAndPath(
                    MachineHUD.MODID,
                    "textures/gui/icon/kinetic.png"
            )
    ),

    CREATE_NETWORK(
            "machinehud.header.create_network",
            ResourceLocation.fromNamespaceAndPath(
                    MachineHUD.MODID,
                    "textures/gui/icon/network.png"
            )
    ),;

    // HUDへ表示するグループ名。
    private final String displayName;

    // グループヘッダー左側へ表示するアイコン。
    private final ResourceLocation icon;


    HudGroup(
            String displayName,
            ResourceLocation icon
    ) {
        this.displayName = displayName;
        this.icon = icon;
    }

    /**
     * HUDへ表示するグループ名を取得する。
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * グループヘッダー用アイコンを取得する。
     */
    public ResourceLocation getIcon() {
        return icon;
    }
}