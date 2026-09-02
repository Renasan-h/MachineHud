package com.rsdvlp.machinehud.hud;

import net.minecraft.resources.ResourceLocation;

/**
 * HUD項目を分類するグループ。
 *
 * 各グループは表示名に加えて、
 * HUD上で使用するアイコンのResourceLocationを保持する。
 */
public enum HudGroup {

    COMMON(
            "Information",
            ResourceLocation.fromNamespaceAndPath(
                    "machinehud",
                    "textures/gui/icon/information.png"
            )
    ),

    MACHINE(
            "Kinetic Stats",
            ResourceLocation.fromNamespaceAndPath(
                    "machinehud",
                    "textures/gui/icon/kinetic.png"
            )
    ),

    NETWORK(
            "Network",
            ResourceLocation.fromNamespaceAndPath(
                    "machinehud",
                    "textures/gui/icon/network.png"
            )
    );


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