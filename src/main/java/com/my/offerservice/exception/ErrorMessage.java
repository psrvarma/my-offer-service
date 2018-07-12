package com.my.offerservice.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    private String message;

    private String code;

    public static ErrorMessage forException(OfferException exception) {
        return new ErrorMessage(exception.getCode(), exception.getMessage());
    }

    public static ErrorMessage forException(Exception exception) {
        return new ErrorMessage("", exception.getMessage());
    }

}
