package com.printrevo.tech.userservice.entities.core.dto;

import com.printrevo.tech.userservice.entities.core.constants.ParameterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataEntry implements Serializable {

    @Enumerated(javax.persistence.EnumType.STRING)
    private ParameterType type = ParameterType.STRING;

    private String label;
    private String description;
    private String value;

    public DataEntry(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
