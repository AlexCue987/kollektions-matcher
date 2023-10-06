package com.tgt.trans.common.testhelpers;

import java.math.BigDecimal;

public class TestJavaObject {
    private final String name;
    private final BigDecimal length;
    private final int quantity;

    public TestJavaObject(String name, BigDecimal length, int quantity) {
        this.name = name;
        this.length = length;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getLength() {
        return length;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "TestJavaObject{" +
                "name='" + name + '\'' +
                ", length=" + length +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestJavaObject that = (TestJavaObject) o;

        if (quantity != that.quantity) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return length != null ? length.equals(that.length) : that.length == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + quantity;
        return result;
    }
}
