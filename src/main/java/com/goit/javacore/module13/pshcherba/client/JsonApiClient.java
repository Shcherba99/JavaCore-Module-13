package com.goit.javacore.module13.pshcherba.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.goit.javacore.module13.pshcherba.entity.Post;
import com.goit.javacore.module13.pshcherba.entity.Todo;
import com.goit.javacore.module13.pshcherba.entity.User;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JsonApiClient {
    private final ObjectMapper objectMapper;
    private final HttpService httpService;

    public void createNewUser(String strUri, User user) {
        String json = readObjectToStringJson(user);
        HttpRequest request = httpService.createPostRequest(json, strUri);
        HttpResponse<String> response = httpService.createResponse(request);
        System.out.println("Create new user. Status code: "
                + response.statusCode()
                + ".");
        if (isSuccessStatusCode(response)) {
            System.out.println(response.body());
        }
    }


    public void updateUserById(String strUri, int id, User user) {
        String currentStrUri = strUri + "/" + id;
        String json = readObjectToStringJson(user);
        HttpRequest request = httpService.createPutRequest(json, currentStrUri);
        HttpResponse<String> response = httpService.createResponse(request);
        System.out.println("Update user by id: "
                + id
                + ". Status code: "
                + response.statusCode()
                + ".");
        if (isSuccessStatusCode(response)) {
            System.out.println(response.body());
        }
    }

    public void deleteUserById(String strUri, int id) {
        String currentStrUri = strUri + "/" + id;
        HttpRequest request = httpService.createDeleteRequest(currentStrUri);
        HttpResponse<String> response = httpService.createResponse(request);
        System.out.println("Delete user by id: "
                + id
                + ". Status code: "
                + response.statusCode()
                + ".");
    }


    public List<User> getAllUsers(String strUri) {
        HttpRequest request = httpService.createGetRequest(strUri);
        HttpResponse<String> response = httpService.createResponse(request);
        System.out.println("Get all users. Status code: "
                + response.statusCode()
                + ".");
        if (!isJsonContentType(response)) {
            System.out.println("Get all users. JSON not found.");
            return new ArrayList<>();
        }
        if (isSuccessStatusCode(response)) {
            return readObjectsFromJson(response.body(), new TypeReference<>() {
            });
        }

        return new ArrayList<>();
    }


    public User getUserById(String strUri, int id) {
        String currentStrUri = strUri + "/" + id;
        HttpRequest request = httpService.createGetRequest(currentStrUri);
        HttpResponse<String> response = httpService.createResponse(request);
        System.out.println("Get user by id: "
                + id
                + ". Status code: "
                + response.statusCode()
                + ".");
        if (!isJsonContentType(response)) {
            System.out.println("Get user by id: "
                    + id
                    + ". JSON not found.");
            return null;
        }
        if (isSuccessStatusCode(response)) {
            return readObjectFromJson(response.body(), User.class);
        }

        return null;
    }


    public User getUserByUserName(String strUri, String userName) {
        String currentStrUri = strUri + "?username=" + userName;
        try {
            HttpRequest request = httpService.createGetRequest(currentStrUri);
            HttpResponse<String> response = httpService.createResponse(request);
            System.out.println("Get user by username: "
                    + userName
                    + ". Status code: "
                    + response.statusCode()
                    + ".");
            if (!isJsonContentType(response)) {
                System.out.println("Get user by username: "
                        + userName
                        + ". JSON not found.");
                return null;
            }

            if (isSuccessStatusCode(response)) {
                List<User> users = readObjectsFromJson(response.body(), new TypeReference<>() {
                });
                return !users.isEmpty() ? users.get(0) : null;
            }

            return null;


        } catch (IllegalArgumentException e) {
            System.out.println("Invalid username: " + userName);
            return null;
        }
    }


    public void printAndWriteCommentsFromLastPostByUserId(String strUri, int id) {
        String postStrUri = strUri + "/" + id + "/" + "posts";
        HttpRequest requestPost = httpService.createGetRequest(postStrUri);
        HttpResponse<String> responsePost = httpService.createResponse(requestPost);
        if (!isJsonContentType(responsePost)) {
            System.out.println("Print and write comments from last post by userId: "
                    + id
                    + ". JSON not found.");
            return;
        }
        List<Post> posts = readObjectsFromJson(responsePost.body(), new TypeReference<>() {
        });

        if (posts.isEmpty()) {
            System.out.println("No posts were found for this id: " + id);
            return;
        }

        int lastId = posts.get(posts.size() - 1).getId();

        String commentStrUri = "https://jsonplaceholder.typicode.com/posts/" + lastId + "/comments";

        HttpRequest requestComment = httpService.createGetRequest(commentStrUri);
        HttpResponse<String> responseComment = httpService.createResponse(requestComment);

        List<Object> comments = readObjectsFromJson(responseComment.body(), new TypeReference<>() {
        });

        comments.forEach(System.out::println);

        String filePath = "src/main/resources/";
        String fileName = "user-" + id + "-post-" + lastId + "-comments.json";
        writeObjectsToJsonFile(filePath, fileName, comments);
    }

    public void printOpenTodosById(String strUri, int id) {
        String currentStrUri = strUri + "/" + id + "/" + "todos";
        HttpRequest request = httpService.createGetRequest(currentStrUri);
        HttpResponse<String> response = httpService.createResponse(request);
        if (!isSuccessStatusCode(response)) {
            System.out.println("Print open todos by id: "
                    + id
                    + ". Status code: " + response.statusCode());
        }

        if (!isJsonContentType(response)) {
            System.out.println("Print open todos by id: "
                    + id
                    + ". JSON not found.");
        }

        List<Todo> todos = readObjectsFromJson(response.body(), new TypeReference<>() {
        });

        System.out.println("Print open todos by id: " + id + ":");

        for (Todo todo: todos) {
            if (!todo.isCompleted()) {
                System.out.println(todo);
            }
        }
    }


    private String readObjectToStringJson(Object object) {
        String json = "";
        try {
            json = objectMapper.writer().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private <T> T readObjectFromJson(String json, Class<T> clazz) {
        T object = null;

        try {
            object = objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return object;
    }


    private <T> List<T> readObjectsFromJson(String json, TypeReference<List<T>> typeReference) {
        List<T> objects = new ArrayList<>();

        try {
            objects = objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return objects;
    }

    private void writeObjectsToJsonFile(String filePath, String fileName, List<Object> objects) {
        File directory = new File(filePath);
        File file = new File(directory, fileName);
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(file, objects);
            System.out.println("Objects successfully written to file. ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isSuccessStatusCode(HttpResponse<String> response) {
        return response.statusCode() >= 200 && response.statusCode() < 300;
    }

    private boolean isJsonContentType(HttpResponse<String> response) {
        String contentType = response.headers().firstValue("Content-Type").orElse("");
        return contentType.contains("application/json");
    }
}
