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
public class StyleModerator implements Moderation, Production<StyleModerator> {
    private final MessagingModerator messagingModerator;

    public StyleModerator(MessagingModerator messagingModerator) {
        this.messagingModerator = messagingModerator;
    }

    @Autowired
    private ObjectProvider<StyleModerator> selfProvider;

    @Override
    public ModerationOutput moderate(String data, ModerationOutput logs) {
        for(String str : data.split("!")) {
            String s[] = str.split(" ");
            String s0 = s[0];
            int n = s0.length();
            if(!Character.isUpperCase(s0.charAt(0))) {
                logs.getLogs().put("Style-Moderation", "failed");
                logs.getFlag().set(false);
                return logs;
            }
            for(int i = 1; i < n; i++) {
                if(!Character.isLowerCase(s0.charAt(i))) {
                    logs.getLogs().put("Style-Moderation", "failed");
                logs.getFlag().set(false);
                return logs;
                }
            }
        }
        logs.getLogs().put("Style-Moderation", "passed");
        logs.getFlag().set(true);
        return logs;
    }

    public MessagingModerator getParent() {return messagingModerator;}

    @Override
    public StyleModerator produce() {
        return selfProvider.getObject();
    }
}
