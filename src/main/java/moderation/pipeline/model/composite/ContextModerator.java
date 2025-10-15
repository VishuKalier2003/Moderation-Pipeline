package moderation.pipeline.model.composite;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import moderation.pipeline.data.ModerationOutput;
import moderation.pipeline.model.domain.Moderation;
import moderation.pipeline.model.domain.Production;

@Component
@Scope("prototype")
public class ContextModerator implements Moderation, Production<ContextModerator> {
    private final MessagingModerator messagingModerator;

    @Autowired
    private ObjectProvider<ContextModerator> messagingModeratorProvider;

    public ContextModerator(MessagingModerator messagingModerator) {
        this.messagingModerator = messagingModerator;
    }

    @Override
    public ModerationOutput moderate(String data, ModerationOutput logs) {
        for(String str : data.split("!")) {
            String s[] = str.split(" ");
            if(!s[0].equalsIgnoreCase("the") && !s[0].equalsIgnoreCase("an") && !s[0].equalsIgnoreCase("a")) {
                logs.getLogs().put("Context-Moderation", "failed");
                logs.getFlag().set(false);
                return logs;
            }
        }
        logs.getLogs().put("Context-Moderation", "passed");
        logs.getFlag().set(true);
        return logs;
    }

    public MessagingModerator getParent() {return messagingModerator;}

    @Override
    public ContextModerator produce() {
        return messagingModeratorProvider.getObject();
    }
}
