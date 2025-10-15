package moderation.pipeline.service.business;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import moderation.pipeline.data.ModerationOutput;
import moderation.pipeline.utils.Node;
import moderation.pipeline.utils.ThreadPool;

@Service
public class NodeManager {
    private final AtomicReference<Node> head = new AtomicReference<>();
    private final Executor executor;

    public NodeManager(ThreadPool threadPool) {
        this.executor = threadPool.getExecutor();
    }

    public AtomicReference<Node> getHead() {
        return head;
    }

    public void appendAtHead(Node node) {
        Node old;
        do {
            old = head.get();
            node.setNext(old);
        } while (!head.compareAndSet(old, node));
    }

    public void appendAtEnd(Node node) {
        Node temp = head.get();
        if(temp == null) {
            appendAtHead(node);
            return;
        }
        Node curr = temp;
        while(curr.getNext().get() != null)
            curr = curr.getNext().get();
        curr.setNext(node);
    }

    public ModerationOutput executeSync(String data, ModerationOutput dto) {
        Node temp = head.get();
        while(temp != null) {
            dto = temp.handleSync(data, dto);
            if(!dto.getFlag().get())
                return dto;
            temp = temp.getNext().get();
        }
        return dto;
    }

    public void printPipeline() {
        Node temp = head.get();
        while(temp != null) {
            System.out.println(temp.getNodeID() + " -> " + temp.getModerator().getClass().getSimpleName());
            temp = temp.getNext().get();
        }
    }

    public CompletableFuture<ModerationOutput> executeAsync(String data, ModerationOutput dto) {
        Node start = head.get();
        if(start == null)
            return CompletableFuture.completedFuture(dto);
        return runNodeAsync(start, data, dto);
    }

    private CompletableFuture<ModerationOutput> runNodeAsync(Node node, String data, ModerationOutput dto) {
        // If node is null, return completed future with dto
        if(node == null)
            return CompletableFuture.completedFuture(dto);
        return node.handleAsync(data, dto, executor).thenCompose(result -> {
            if(!result.getFlag().get())
                return CompletableFuture.completedFuture(result);
            return runNodeAsync(node.getNext().get(), data, result);
        });
    }
}
