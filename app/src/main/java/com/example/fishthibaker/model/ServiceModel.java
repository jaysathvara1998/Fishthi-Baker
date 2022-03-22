package com.example.fishthibaker.model;

import java.io.Serializable;

public class ServiceModel implements Serializable {

    String id;
    String providerId;
    String charge;
    String service;
    String serviceDescription;

    public ServiceModel(String id, String providerId, String service, String charge, String serviceDescription) {
        this.id = id;
        this.providerId = providerId;
        this.service = service;
        this.charge = charge;
        this.serviceDescription = serviceDescription;
    }

    public ServiceModel() {

    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

}
