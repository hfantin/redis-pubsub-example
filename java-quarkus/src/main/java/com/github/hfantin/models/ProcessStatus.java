package com.github.hfantin.models;

public enum ProcessStatus {
    STARTED, STOPED, INVALID;

    public static ProcessStatus getByName(final String value) {
        for(ProcessStatus p: ProcessStatus.values()){
            if(p.name().equalsIgnoreCase(value)){
                return p;
            }
        }
        return INVALID;
    }
}
