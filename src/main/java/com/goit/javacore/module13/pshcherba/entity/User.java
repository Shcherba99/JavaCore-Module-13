package com.goit.javacore.module13.pshcherba.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {

    private int id;

    private String name;

    @JsonProperty("username")
    private String userName;

    private String email;

    private Address address;

    private String phone;

    private String website;

    private Company company;

    private static User user;

    public static User createTestUser() {
        user = new User();
        user.setUserName("TestUserName");
        user.setName("TestName");
        user.setEmail("test@email.com");
        user.setWebsite("testsite.com");
        user.setPhone("000-00-00");
        return user;
    }
}
