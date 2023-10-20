package com.company.delete;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class deleteTest {

    @Test
    public void pass() {
        Assertions.assertEquals(6, 1);
    }

    @Test
    public void fail() {
        Assertions.assertEquals(5, 5);
    }
}
