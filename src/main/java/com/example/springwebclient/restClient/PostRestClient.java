package com.example.springwebclient.restClient;

import com.example.springwebclient.UriConstant;
import com.example.springwebclient.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
public class PostRestClient {
    private WebClient webClient;

    public PostRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Post> getAllPosts() {
        return webClient.get().uri(UriConstant.POST_URI)
                .retrieve()
                .bodyToFlux(Post.class)
                .collectList()
                .block();
    }

    public Post getPostById(int postId) {
        /*String uri = UriComponentsBuilder.fromUriString(UriConstant.POST_BY_ID_URI)
                .se("postId", postId)
                .toUriString();*/
        try {
            return webClient.get()
                    .uri(UriConstant.POST_BY_ID_URI, postId)
                    .retrieve()
                    .bodyToMono(Post.class)
                    .block();
        } catch (WebClientResponseException webClientResponseException) {
            log.info("Error Response code is {} and Response body is {}", webClientResponseException.getRawStatusCode(), webClientResponseException.getResponseBodyAsString());
            log.info("webClientResponseException when get post by id:"+ webClientResponseException);
            throw webClientResponseException;
        } catch (Exception e) {
            log.info("Exception when getting post by its id:"+ e);
            throw e;
        }
    }

    public Post addPost(Post post) {

        try {
            return webClient.post()
                    .uri(UriConstant.POST_URI)
                    .bodyValue(post)
                    .retrieve()
                    .bodyToMono(Post.class)
                    .block();
        } catch (WebClientResponseException webClientResponseException) {
            log.info("Error Response code is {} and Response body is {}", webClientResponseException.getRawStatusCode(), webClientResponseException.getResponseBodyAsString());
            log.info("webClientResponseException when addPost:"+ webClientResponseException);
            throw webClientResponseException;
        } catch (Exception e) {
            log.info("Exception when getting addPost:"+ e);
            throw e;
        }


    }

    public String deleteById(int postId) {
        try {
            return webClient.delete()
                    .uri(UriConstant.POST_BY_ID_URI, postId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException webClientResponseException) {
            log.info("Error Response code is {} and Response body is {}", webClientResponseException.getRawStatusCode(), webClientResponseException.getResponseBodyAsString());
            log.info("webClientResponseException when deleteById:"+ webClientResponseException);
            throw webClientResponseException;
        } catch (Exception e) {
            log.info("Exception when getting addPost:"+ e);
            throw e;
        }

    }

    public String updatePost(Post post) {
        try {
            return webClient.put()
                    .uri(UriConstant.POST_BY_ID_URI, post.getId())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException webClientResponseException) {
            log.info("Error Response code is {} and Response body is {}", webClientResponseException.getRawStatusCode(), webClientResponseException.getResponseBodyAsString());
            log.info("webClientResponseException when updatePost:"+ webClientResponseException);
            throw webClientResponseException;
        } catch (Exception e) {
            log.info("Exception when getting addPost:"+ e);
            throw e;
        }

    }




}
