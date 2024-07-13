package com.goit.javacore.module13.pshcherba.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Company {
    private String name;

    @JsonProperty("catchPhrase")
    private String catchPhrase;
    private String bs;
}
