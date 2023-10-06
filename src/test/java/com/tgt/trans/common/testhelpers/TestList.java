package com.tgt.trans.common.testhelpers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TestList {
    public List<BigDecimal> get1() {
        return Arrays.asList(BigDecimal.ZERO, BigDecimal.ONE);
    }

    public List<BigDecimal> get2() {
        return Arrays.asList(BigDecimal.ZERO, BigDecimal.TEN);
    }
}
