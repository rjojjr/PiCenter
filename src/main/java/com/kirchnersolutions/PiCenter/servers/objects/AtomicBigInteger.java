package com.kirchnersolutions.PiCenter.servers.objects;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class AtomicBigInteger {

    private final AtomicReference<BigInteger> valueHolder = new AtomicReference<>();

    public AtomicBigInteger(BigInteger bigInteger) {
        valueHolder.set(bigInteger);
    }

    public BigInteger incrementAndGet() {
        for (; ; ) {
            BigInteger current = valueHolder.get();
            BigInteger next = current.add(BigInteger.ONE);
            if (valueHolder.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    public BigInteger get() {
        for (; ; ) {
            BigInteger current = valueHolder.get();
            if (valueHolder.compareAndSet(current, current)) {
                return current;
            }
        }
    }
}