package com.dimosr.dependency.servicecall;

import com.dimosr.dependency.RemoteService;
import com.dimosr.service.core.ServiceCall;

/**
 * Adapter of RemoteService to the ServiceCall interface
 * provided by Service-Call-4j library
 */
public class RemoteServiceCall implements ServiceCall<Integer, Integer> {
    private final RemoteService remoteService;

    public RemoteServiceCall(final RemoteService remoteService) {
        this.remoteService = remoteService;
    }

    @Override
    public Integer call(Integer input) {
        return remoteService.calculateResult(input);
    }
}
