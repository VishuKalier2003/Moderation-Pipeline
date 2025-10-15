package moderation.pipeline.model.composite;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import moderation.pipeline.data.ModerationOutput;
import moderation.pipeline.model.domain.Moderation;
import moderation.pipeline.model.domain.Production;

@Getter
@Component
@Scope("prototype")
public class Limiter implements Moderation, Production<Limiter> {

    private final ContextModerator contextModerator;
    private final StyleModerator styleModerator;

    @Autowired
    private ObjectProvider<Limiter> selfProvider;

    public Limiter(ContextModerator moderator1, StyleModerator moderator2) {
        this.contextModerator = moderator1;
        this.styleModerator = moderator2;
    }

    @Override
    public ModerationOutput moderate(String data, ModerationOutput logs) {
        String rate = logs.getLogs().get("rate");
        if(Integer.parseInt(rate) > 10) {
            logs.getLogs().put("Limiter", "failed");
            logs.getFlag().set(false);
            return logs;
        }
        logs.getLogs().put("Limiter", "passed");
        logs.getFlag().set(true);
        return logs;
    }

    @Override
    public Limiter produce() {
        return selfProvider.getObject();
    }
}
