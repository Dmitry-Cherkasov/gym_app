package com.gym_app.core.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

public abstract class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long serviceId;
    private User consumer;
    private User supplier;
    String serviceName;
    LocalDate serviceDate;
    int duration;

    public ServiceType(User consumer, User supplier, String serviceName, LocalDate serviceDate, int duration) {
        this.consumer = consumer;
        this.supplier = supplier;
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

    public User getConsumer() {
        return consumer;
    }

    public void setConsumer(User consumer) {
        this.consumer = consumer;
    }

    public User getSupplier() {
        return supplier;
    }

    public void setSupplier(User supplier) {
        this.supplier = supplier;
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
                ", " + consumer +
                ", " + supplier +
                ", " + serviceName + '\'' +
                ", " + serviceDate +
                ", " + duration +
                '}';
    }
}
