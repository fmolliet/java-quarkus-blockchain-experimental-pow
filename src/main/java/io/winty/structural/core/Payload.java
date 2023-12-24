package io.winty.structural.core;

import java.math.BigInteger;
import java.time.LocalDateTime;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.Min;

public record Payload ( @Min(0) BigInteger sequence, LocalDateTime timestamp, @NotNull String data, @NotNull String lastHash ){

    public String stringify() {
        return sequence.toString() + timestamp.toString() + data + lastHash;
    }
    
    public String hash() {
        return Hash.sha256(stringify());
    }

}
