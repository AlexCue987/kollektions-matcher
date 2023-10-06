package com.tgt.trans.common.testhelpers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TestMap {
    public Map<BigDecimal, BigDecimal> get1() {
        Map<BigDecimal, BigDecimal> ret = new HashMap<>(2);
        ret.put(BigDecimal.ZERO, BigDecimal.ONE);
        ret.put(BigDecimal.ONE, BigDecimal.ZERO);
        return ret;
    }

    public Map<BigDecimal, BigDecimal> get2() {
        Map<BigDecimal, BigDecimal> ret = new HashMap<>(2);
        ret.put(BigDecimal.ONE, BigDecimal.ONE);
        ret.put(BigDecimal.TEN, BigDecimal.ONE);
        return ret;
    }
}
