package com.waverchat.api.v1.resources.usercreationconfirmation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UCCJobScheduler {

    @Autowired
    protected UCCService uccService;

    private static final int RM_EXPIRED_UCC_FIXED_DELAY = 60 * 60 * 1000; // 1 hour

    @Scheduled(fixedDelay = RM_EXPIRED_UCC_FIXED_DELAY)
    public void removeExpiredUserCreationConfirmations() {
        this.uccService.batchDeleteExpired();
    }

}
