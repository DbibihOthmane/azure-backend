package com.othmane.service;

import com.othmane.model.PlanType;
import com.othmane.model.Subscription;
import com.othmane.model.User;

public interface SubscriptionService {

    Subscription createSubscription(User user);

    Subscription getUserSubscription(Long userId) throws Exception;

    Subscription upgradeSubscription(Long userId, PlanType planType);

    boolean isValid(Subscription subscription);
}
