package ru.webapp.exchange.models;

public class Currency {
    private String code;
    private double value;

    public Currency(String code, double value) {
        this.code = code;
        this.value = value;
    }

    public Currency() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String toString() {
        return "Currency{" +
                "code='" + code + '\'' +
                ", value=" + value +
                '}';
    }
}
