package com.hakjin.autocore.processing.fx;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface FxRateLookup {
    Optional<BigDecimal> findKrwPerUnit(String currency, LocalDate postingDate);
}
