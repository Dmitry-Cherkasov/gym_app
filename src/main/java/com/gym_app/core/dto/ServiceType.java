package com.gym_app.core.dto;

import java.time.LocalDate;

public abstract class ServiceType<T,Id> {
    private long serviceId;
    private Id consumerId;
    private Id supplierId;
    String serviceName;
    LocalDate serviceDate;
    int duration;

    public ServiceType(Id consumerId, Id supplierId, String serviceName, LocalDate serviceDate, int duration) {
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

    public Id getConsumerId() {
        return consumerId;
    }

    public void setConsumer(Id consumerId) {
        this.consumerId = consumerId;
    }

    public Id getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Id supplierId) {
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
