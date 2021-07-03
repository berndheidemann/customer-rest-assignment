package de.szut.customer.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReadCustomerDto {

    private final String id;
    private final String name;
    private final String company;

    @JsonCreator
    public ReadCustomerDto(@JsonProperty("id") final String id,
                           @JsonProperty("name") final String name,
                           @JsonProperty("company") final String company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }
}
