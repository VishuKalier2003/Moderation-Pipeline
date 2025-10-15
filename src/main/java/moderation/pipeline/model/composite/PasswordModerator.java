package moderation.pipeline.model.composite;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import moderation.pipeline.data.ModerationOutput;
import moderation.pipeline.model.domain.Moderation;
import moderation.pipeline.model.domain.Production;
import moderation.pipeline.model.storage.User;
import moderation.pipeline.repository.UserRepo;

@Component
@Scope("prototype")
public class PasswordModerator implements Moderation, Production<PasswordModerator> {

    private final DataModerator dataModeration;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectProvider<PasswordModerator> selfProvider;

    public PasswordModerator(DataModerator dataModeration) {
        this.dataModeration = dataModeration;
    }

    @Override
    public ModerationOutput moderate(String data, ModerationOutput logs) {
        List<User> users = userRepo.findAll();
        for(String str : data.split(" ")) {
            for(User user : users) {
                if(user.getPassword().equals(str)) {
                    logs.getLogs().put("Password-Moderation", "password match "+true);
                    logs.getFlag().set(false);
                    return logs;
                }
            }
        }
        logs.getLogs().put("Password-Moderation", "done");
        logs.getFlag().set(true);
        return logs;
    }

    public DataModerator getParent() {
        return dataModeration;
    }

    @Override
    public PasswordModerator produce() {
        return selfProvider.getObject();
    }
}
