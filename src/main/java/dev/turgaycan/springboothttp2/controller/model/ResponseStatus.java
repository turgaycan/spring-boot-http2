package dev.turgaycan.springboothttp2.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class ResponseStatus implements Serializable {
    private String code;
    private String description;

    public void ok(){
        this.code = "OK";
        this.description = "SUCCESSFUL";
    }
}
