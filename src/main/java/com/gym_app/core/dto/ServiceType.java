package com.gym_app.core.dto;

import java.time.LocalDate;

public abstract class ServiceType {
    private long serviceId;
    private long consumerId;
    private long supplierId;
    String serviceName;
    LocalDate serviceDate;
    int duration;

    public ServiceType(long consumerId, long supplierId, String serviceName, LocalDate serviceDate, int duration) {
        this.consumerId = consumerId;
        this.supplierId = supplierId;
        this.serviceName = serviceName;
        this.serviceDate = serviceDate;
        this.duration = duration;
    }


    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public long getConsumerId() {
        return consumerId;
    }

    public void setConsumer(long consumerId) {
        this.consumerId = consumerId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Service{" +
                "Id=" + serviceId +
                ", " + consumerId +
                ", " + supplierId +
                ", " + serviceName + '\'' +
                ", " + serviceDate +
                ", " + duration +
                '}';
    }
}
