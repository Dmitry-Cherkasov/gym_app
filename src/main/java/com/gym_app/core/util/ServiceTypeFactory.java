package com.gym_app.core.util;

import java.time.LocalDate;

public interface ServiceTypeFactory <T, Id> {

    T createService(Id consumerId, Id supplierId, String serviceName, LocalDate serviceDate, int duration, Object extraArg);
}
