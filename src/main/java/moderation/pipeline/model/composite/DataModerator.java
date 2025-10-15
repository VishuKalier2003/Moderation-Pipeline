package moderation.pipeline.model.composite;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import moderation.pipeline.data.ModerationOutput;
import moderation.pipeline.model.base.BaseModerator;
import moderation.pipeline.model.domain.Moderation;
import moderation.pipeline.model.domain.Production;
import moderation.pipeline.model.storage.User;
import moderation.pipeline.repository.UserRepo;

@Component
@Scope("prototype")
@Getter
public class DataModerator implements Moderation, Production<DataModerator> {
    private final BaseModerator baseModerator;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectProvider<DataModerator> selfProvider;

    public DataModerator(BaseModerator baseModerator) {
        this.baseModerator = baseModerator;
    }

    @Override
    public ModerationOutput moderate(String data, ModerationOutput logs) {
        List<User> users = userRepo.findAll();
        for(String str : data.split(" ")) {
            for(User user : users) {
                if(str.equals(user.getName())) {
                    logs.getLogs().put("Data-Moderation", "username match "+true);
                    logs.getFlag().set(false);
                    System.out.println("Leaving DataModerator II");
                    return logs;
                }
            }
        }
        logs.getLogs().put("Data-Moderation", "done");
        logs.getFlag().set(true);
        return logs;
    }

    @Override
    public DataModerator produce() {
        return selfProvider.getObject();
    }
}
