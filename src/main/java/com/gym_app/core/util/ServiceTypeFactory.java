package com.gym_app.core.util;

import com.gym_app.core.dto.ServiceType;
import com.gym_app.core.dto.User;

import java.time.LocalDate;

public interface ServiceTypeFactory {

    ServiceType createService(User consumer, User supplier, String serviceName, LocalDate serviceDate, int duration);
}
