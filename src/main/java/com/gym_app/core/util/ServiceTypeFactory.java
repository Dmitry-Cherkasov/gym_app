package com.gym_app.core.util;

import com.gym_app.core.dto.ServiceType;

import java.time.LocalDate;

public interface ServiceTypeFactory <T extends ServiceType> {

    T createService(long consumerId, long supplierId, String serviceName, LocalDate serviceDate, int duration, Object extraArg);
}
