package com.github.hfantin.models;

import java.time.LocalDateTime;

public record ProcessDTO(ProcessStatus status, LocalDateTime lastUpdate) {
    @Override
    public String toString() {
        return status + " " + lastUpdate;
    }
}
