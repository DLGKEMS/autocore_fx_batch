package com.hakjin.autocore.rate.store;

import com.hakjin.autocore.rate.model.DailyFxRates;

public interface FxRateStore {
    void save(DailyFxRates daily);
}
