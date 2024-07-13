package com.goit.javacore.module13.pshcherba.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Todo {
    private int userId;
    private int id;
    private String title;
    private boolean completed;
}
