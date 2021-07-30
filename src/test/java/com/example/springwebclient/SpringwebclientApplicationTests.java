package com.example.springwebclient;

import com.example.springwebclient.model.Post;
import com.example.springwebclient.restClient.PostRestClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
class SpringwebclientApplicationTests {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private WebClient webClient = WebClient.create(BASE_URL);

    @Test
    void getAllPosts() {
        PostRestClient postRestClient = new PostRestClient(webClient);

        List<Post> allPosts = postRestClient.getAllPosts();
        System.out.println(allPosts);
        System.out.println("size:"+allPosts.size());

    }

    @Test
    void getPostById()  {
        PostRestClient postRestClient = new PostRestClient(webClient);
        /*try {
            Post pos = postRestClient.getPostById(10000000);
            System.out.println(pos.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Assertions.assertThrows(WebClientResponseException.class, () -> postRestClient.getPostById(1000000));
    }

    @Test
    void addNewPost() {
        PostRestClient postRestClient = new PostRestClient(webClient);
        Post post = new Post();
        post.setTitle("Post Title");
        post.setBody("Post body title");
        Post post1 = postRestClient.addPost(post);
        assertEquals( "Post Title", post1.getTitle());
    }

    @Test
    void deletePostById() {
        PostRestClient postRestClient = new PostRestClient(webClient);
        postRestClient.deleteById(101);
    }

}
