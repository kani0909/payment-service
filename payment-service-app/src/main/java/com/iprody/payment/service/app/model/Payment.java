package com.iprody.payment.service.app.model;

public class Payment {

    private Long id;
    private Double value;

    public Payment() {
    }

    public Payment(Long id, Double value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


}

