package com.github.hfantin.models;

import java.time.LocalDateTime;

public class ProcessDTO {

    private ProcessStatus status;

    private LocalDateTime lastUpdate;

    public ProcessDTO(ProcessStatus status, LocalDateTime lastUpdate) {
        this.status = status;
        this.lastUpdate = lastUpdate;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return status + " " + lastUpdate;
    }
}
