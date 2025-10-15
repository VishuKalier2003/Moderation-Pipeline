package moderation.pipeline.model.composite;

import java.util.ArrayList;
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

@Getter
@Component
@Scope("prototype")
public class WordModerator implements Moderation, Production<WordModerator> {
    private final BaseModerator baseModerator;

    private final List<String> checkWords;

    @Autowired
    private ObjectProvider<WordModerator> selfProvider;

    public WordModerator(BaseModerator baseModerator) {
        this.baseModerator = baseModerator;
        this.checkWords = new ArrayList<>();
    }

    public void addWord(String word) {
        checkWords.add(word);
    }

    @Override
    public ModerationOutput moderate(String data, ModerationOutput logs) {
        StringBuilder builder = new StringBuilder();
        int c = 0;
        for(String str : data.split(" ")) {
            if(checkWords.contains(str))
                c++;
            else
                builder.append(str).append(" ");
            if(c >= 3) {
                logs.getFlag().set(false);
                logs.getLogs().put("Word-Moderation", builder.toString());
                return logs;
            }
        }
        logs.getFlag().set(true);
        return logs;
    }

    @Override
    public WordModerator produce() {
        return selfProvider.getObject();
    }
}
