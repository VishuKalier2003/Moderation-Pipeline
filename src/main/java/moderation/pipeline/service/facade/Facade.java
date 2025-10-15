package moderation.pipeline.service.facade;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import moderation.pipeline.data.ModerationOutput;
import moderation.pipeline.factory.ModerationFactory;
import moderation.pipeline.model.storage.User;
import moderation.pipeline.repository.UserRepo;
import moderation.pipeline.service.business.NodeManager;
import moderation.pipeline.service.business.NodePipeline;
import moderation.pipeline.service.business.UserRegister;
import moderation.pipeline.utils.Node;

@Service
public class Facade {

    @Autowired
    private ModerationFactory factory;

    @Autowired
    private NodeManager manager;

    @Autowired
    private NodePipeline pipeline;

    @Autowired
    private UserRegister userRegister;

    @Autowired
    private UserRepo userRepo;

    public String appendBlock(String name) {
        String Id = "Block-"+((int)(Math.random()*1000))+name;
        Node node = new Node(Id, name, factory);
        manager.appendAtEnd(node);
        return "passed";
    }

    public String appendBlockFront(String name) {
        String Id = "Block-"+((int)(Math.random()*1000))+name;
        Node node = new Node(Id, name, factory);
        manager.appendAtHead(node);
        return "passed";
    }

    public String buildPipeline(List<String> types) {
        pipeline.buildPipeline(types);
        return "passed";
    }

    public String registerUser(String username, String password) {
        userRegister.registerUser(username, password);
        return "User registered successfully";
    }

    public ConcurrentMap<String, String> testPipelineI(String data, String username, String password) {
        ModerationOutput dto = new ModerationOutput();
        dto.getLogs().put("name", username);
        dto.getLogs().put("password", password);
        return manager.executeSync(data, dto).getLogs();
    }

    public ConcurrentMap<String, String> testPipelineII(String data, String username, String password) {
        try {
            System.out.println("started");
            ModerationOutput dto = new ModerationOutput();
            dto.getLogs().put("name", username);
            dto.getLogs().put("password", password);
            System.out.println(dto.getLogs());
            return pipeline.executeSync(data, dto).getLogs();
        } catch (Exception e) {
            e.getLocalizedMessage();
            return new ConcurrentHashMap<>();
        }
    }

    public ConcurrentMap<String, String> testPipelineIII(String data, String username, String password) {
        try {
            ModerationOutput dto = new ModerationOutput();
            dto.getLogs().put("name", username);
            dto.getLogs().put("password", password);
            return pipeline.executeAsync(data, dto).get().getLogs();
        } catch (InterruptedException | ExecutionException e) {
            e.getLocalizedMessage();
            return new ConcurrentHashMap<>();
        }
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void printPipeline() {
        manager.printPipeline();
    }
}
