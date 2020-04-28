package com.pradeesh.knowcovid.model;

import com.pradeesh.knowcovid.utils.Constant;

public class VehicleEventMessage {
    int speedValue;
    String ignitionStatus;

    public VehicleEventMessage(){
        speedValue = 0;
        ignitionStatus = Constant.VEH_IGNITION_STATUS_OFF;
    }

    public void setSpeedValue(int speedValue) {
        this.speedValue = speedValue;
    }

    public void setIgnitionStatus(String ignitionStatus) {
        this.ignitionStatus = ignitionStatus;
    }

    public int getSpeedValue() {
        return this.speedValue;
    }

    public String getIgnitionStatus() {
        return this.ignitionStatus;
    }
}
