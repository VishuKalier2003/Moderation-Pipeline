package moderation.pipeline.service.business;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import moderation.pipeline.data.ModerationOutput;
import moderation.pipeline.factory.ModerationFactory;
import moderation.pipeline.utils.Node;

@Service
public class NodePipeline {

    @Autowired
    private ModerationFactory factory;

    @Autowired
    private NodeManager manager;

    public void buildPipeline(List<String> types) {
        manager.getHead().set(null);
        Node prev = null;
        for(int i = 0; i < types.size(); i++) {
            String modName = types.get(i).toUpperCase();
            Node node = new Node("Node-" + (i + 1), modName, factory);
            if(prev == null) {
                manager.appendAtHead(node);
            } else {
                manager.appendAtEnd(node);
            }
            prev = node;
        }
    }

    public ModerationOutput executeSync(String data, ModerationOutput dto) {
        return manager.executeSync(data, dto);
    }

    public CompletableFuture<ModerationOutput> executeAsync(String data, ModerationOutput dto) {
        return manager.executeAsync(data, dto);
    }
}
