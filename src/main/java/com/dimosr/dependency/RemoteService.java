package com.dimosr.dependency;

public class RemoteService {
    public DependencyConfiguration configuration;

    public RemoteService() {
        this.configuration = new DependencyConfiguration();
        this.configuration.setIntroduceFailures(false);
        this.configuration.setLatencyInMillis(0);
    }

    public int calculateResult(int input) {
        if(configuration.getIntroduceFailures()) {
            throw new RuntimeException("Underlying dependency failed");
        }

        try {
            Thread.sleep(configuration.getLatencyInMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return input + 42;
    }

    public void updateConfiguration(final DependencyConfiguration configuration) {
        this.configuration = configuration;
    }

}
