package moderation.pipeline.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Getter;
import moderation.pipeline.data.ModerationOutput;
import moderation.pipeline.factory.ModerationFactory;
import moderation.pipeline.model.domain.Moderation;

@Getter
public class Node {
    private final String nodeID;
    private final Moderation moderator;
    private final AtomicReference<Node> next;

    public Node(String nodeID, String moderatorName, ModerationFactory factory) {
        this.nodeID = nodeID;
        this.moderator = factory.create(moderatorName);
        this.next = new AtomicReference<>(null);
    }

    public void setNext(Node next) {
        this.next.set(next);
    }

    public ModerationOutput handleSync(String data, ModerationOutput dto) {
        return moderator.moderate(data, dto);
    }

    public CompletableFuture<ModerationOutput> handleAsync(String data, ModerationOutput dto, Executor executor) {
        return CompletableFuture.supplyAsync(() -> moderator.moderate(data, dto), executor);
    }
}
