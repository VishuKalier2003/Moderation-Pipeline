package moderation.pipeline.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ThreadPool {
    private final Executor executor = Executors.newFixedThreadPool(5);
}
