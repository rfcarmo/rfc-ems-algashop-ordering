package com.algaworks.algashop.ordering.domain.utility;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;

import java.util.UUID;

public class IdGenerator {

    private static final TimeBasedEpochRandomGenerator timeBasedGenerator = Generators.timeBasedEpochRandomGenerator();

    private IdGenerator() {
    }

    public static UUID generateTimeBasedUUID() {
        return timeBasedGenerator.generate();
    }
}
