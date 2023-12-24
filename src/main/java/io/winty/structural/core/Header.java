package io.winty.structural.core;

import java.math.BigInteger;

public record Header (
    BigInteger nonce,
    String blockHash
){}
