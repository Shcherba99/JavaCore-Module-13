package com.goit.javacore.module13.pshcherba.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goit.javacore.module13.pshcherba.client.HttpService;
import com.goit.javacore.module13.pshcherba.client.JsonApiClient;
import com.goit.javacore.module13.pshcherba.entity.User;

import java.net.http.HttpClient;

public class Main {
    private static final String STR_USERS_URI = "https://jsonplaceholder.typicode.com/users";

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpService httpService= new HttpService(httpClient);
        JsonApiClient jsonApiClient = new JsonApiClient(objectMapper, httpService);


        //Task 1

        //Create new user
        User userCreate = User.createTestUser();
        jsonApiClient.createNewUser(STR_USERS_URI, userCreate);

        //Update user
        User userUpdate = User.createTestUser();
        userUpdate.setName("NewNameTest");
        jsonApiClient.updateUserById(STR_USERS_URI, 3, userUpdate);

        //Delete user
        jsonApiClient.deleteUserById(STR_USERS_URI, 4);

        //Get all users
        jsonApiClient.getAllUsers(STR_USERS_URI).forEach(System.out::println);

        //Get user by id
        System.out.println(jsonApiClient.getUserById(STR_USERS_URI, 5));

        //Get user by username
        System.out.println(jsonApiClient.getUserByUserName(STR_USERS_URI, "Elwyn.Skiles"));


        //Task 2
        jsonApiClient.printAndWriteCommentsFromLastPostByUserId(STR_USERS_URI, 7);


        //Task 3
        jsonApiClient.printOpenTodosById(STR_USERS_URI, 8);
    }
}