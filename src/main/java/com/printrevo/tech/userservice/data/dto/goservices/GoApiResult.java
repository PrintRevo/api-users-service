package com.printrevo.tech.userservice.data.dto.goservices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoApiResult<T> {
    private String status;
    private String message;
    private List<String> errors;
    private T data;
}
