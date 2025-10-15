package moderation.pipeline.model.composite;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import moderation.pipeline.data.ModerationOutput;
import moderation.pipeline.model.base.BaseModerator;
import moderation.pipeline.model.domain.Moderation;
import moderation.pipeline.model.domain.Production;

@Component
@Scope("prototype")
public class MessagingModerator implements Moderation, Production<MessagingModerator> {

    private final BaseModerator baseModerator;

    @Autowired
    private ObjectProvider<MessagingModerator> baseModeratorProvider;

    public MessagingModerator(BaseModerator baseModerator) {
        this.baseModerator = baseModerator;
    }

    @Override
    public ModerationOutput moderate(String data, ModerationOutput logs) {
        if(data.length() >= 50) {
            logs.getLogs().put("Message-Moderation", "failed, length : "+data.length());
            logs.getFlag().set(false);
            return logs;
        }
        logs.getLogs().put("Message-Moderation", "done");
        logs.getFlag().set(true);
        return logs;
    }

    public BaseModerator getParent() {return baseModerator;}

    @Override
    public MessagingModerator produce() {
        return baseModeratorProvider.getObject();
    }
}
