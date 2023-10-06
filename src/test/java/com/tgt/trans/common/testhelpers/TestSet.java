package com.tgt.trans.common.testhelpers;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestSet {
    public Set<BigDecimal> get() {
        return Stream.of(BigDecimal.ZERO, BigDecimal.ONE).collect(Collectors.toSet());
    }

    public Set<BigDecimal> get2() {
        return Stream.of(BigDecimal.ZERO, BigDecimal.TEN).collect(Collectors.toSet());
    }
}
