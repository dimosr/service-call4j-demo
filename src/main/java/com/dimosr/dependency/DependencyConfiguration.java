package com.dimosr.dependency;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class DependencyConfiguration {
    private boolean introduceFailures;
    private int latencyInMillis;

    public boolean getIntroduceFailures() {
        return introduceFailures;
    }

    public void setIntroduceFailures(final boolean introduceFailures) {
        this.introduceFailures = introduceFailures;
    }

    public int getLatencyInMillis() {
        return latencyInMillis;
    }

    public void setLatencyInMillis(final int latencyInMillis) {
        this.latencyInMillis = latencyInMillis;
    }
}
