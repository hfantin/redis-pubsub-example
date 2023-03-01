package com.github.hfantin.models;

import org.junit.jupiter.api.Test;

import static com.github.hfantin.models.ProcessStatus.INVALID;
import static com.github.hfantin.models.ProcessStatus.STOPED;
import static org.junit.jupiter.api.Assertions.*;

class ProcessDTOStatusTest {

    @Test
    void whenGetByStarted_thenReturnValidProcessStattus() {
        assertEquals(ProcessStatus.STARTED, ProcessStatus.getByName("iniciado"));
    }

    @Test
    void whenGetByStoped_thenReturnValidProcessStattus() {
        assertEquals(STOPED, ProcessStatus.getByName("parado"));
    }

    @Test
    void whenGetByInvalid_thenReturnDefault() {
        assertEquals(INVALID, ProcessStatus.getByName(""));
    }
}