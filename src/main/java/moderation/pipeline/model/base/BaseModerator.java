package moderation.pipeline.model.base;

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
public class BaseModerator implements Moderation, Production<BaseModerator> {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectProvider<BaseModerator> selfProvider;

    @Override
    public ModerationOutput moderate(String data, ModerationOutput log) {
        String name = log.getLogs().get("name"), password = log.getLogs().get("password");
        User user = null;
        for(User u : userRepo.findAll()) {
            if(u.getName().equals(name)) {
                user = u;
                break;
            }
        }
        System.out.println(user);
        log.getLogs().put("Base-Moderation", user != null && !user.getPassword().equals(password) ? "fail" : "pass");
        log.getFlag().set(true);
        return log;
    }

    @Override
    public BaseModerator produce() {
        return selfProvider.getObject();
    }
}
