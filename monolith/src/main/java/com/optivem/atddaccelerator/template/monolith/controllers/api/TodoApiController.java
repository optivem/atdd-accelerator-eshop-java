package com.optivem.atddaccelerator.template.monolith.controllers.api;

import com.optivem.atddaccelerator.template.monolith.models.Todo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class TodoApiController {

    @Value("${todos.api.host}")
    private String todosApiHost;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/todos/{id}")
    public Todo getTodo(@PathVariable("id") int id) {
        String url = todosApiHost + "/todos/" + id;
        return restTemplate.getForObject(url, Todo.class);
    }
}