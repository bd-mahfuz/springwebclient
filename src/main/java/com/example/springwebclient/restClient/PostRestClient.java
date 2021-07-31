package com.example.springwebclient.restClient;

import com.example.springwebclient.UriConstant;
import com.example.springwebclient.exception.ClientDataException;
import com.example.springwebclient.model.ErrorResponseModel;
import com.example.springwebclient.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
public class PostRestClient {
    private WebClient webClient;

    public PostRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Retry<?> getRetry() {
        return Retry.anyOf(ClientDataException.class)
                .fixedBackoff(Duration.ofSeconds(2))
                .retryMax(3)
                .doOnRetry(objectRetryContext -> {
                    log.error("exception is:"+ objectRetryContext);
                });
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

    public Post getPostByIdWithCustomExceptionHandling(int postId) {
        /*String uri = UriComponentsBuilder.fromUriString(UriConstant.POST_BY_ID_URI)
                .se("postId", postId)
                .toUriString();*/
        return webClient.get()
                .uri(UriConstant.POST_BY_ID_URI, postId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> handle4xxError(clientResponse))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> handle5xxError(clientResponse))
                .bodyToMono(Post.class)
                //.retryWhen(getRetry())
                .block();
    }

    private Mono<? extends Throwable> handle5xxError(ClientResponse clientResponse) {
        Mono<ErrorResponseModel> errorResponseModelMono = clientResponse.bodyToMono(ErrorResponseModel.class);
        return errorResponseModelMono.flatMap(errorResponseModel -> {
            log.error("error response code is: {} and message: {}", errorResponseModel.getStatus(), errorResponseModel.getMessage());
            return Mono.error(new ClientDataException("Error:"+ errorResponseModel.getMessage()));
        });

    }

    private Mono<? extends Throwable> handle4xxError(ClientResponse clientResponse) {
        Mono<ErrorResponseModel> errorResponseModelMono = clientResponse.bodyToMono(ErrorResponseModel.class);
        return errorResponseModelMono.flatMap(errorResponseModel -> {
            log.error("error response code is: {} and message: {}", errorResponseModel.getStatus(), errorResponseModel.getMessage());
            return Mono.error(new ClientDataException("Error:"+ errorResponseModel.getMessage()));
        });
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
