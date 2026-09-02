package com.rsdvlp.machinehud.hud.data;

/**
 * Createの回転機構の状態を表す。
 *
 * 表示文字列そのものではなく「状態」を保持することで、
 * Renderer側で表示方法を自由に変更できるようにする。
 */
public enum KineticStatus {

    RUNNING("Running",0x36c136),
    STOPPED("Stopped", 0xc5c0c3),
    OVERSTRESSED("OVERSTRESSED", 0xd03c41);

    private final String status;
    private final int color;
    KineticStatus(String status, int color) {
        this.status = status;
        this.color = color;
    }

    public String getStatus(){return this.status;}
    public int getColor(){return this.color;}
}