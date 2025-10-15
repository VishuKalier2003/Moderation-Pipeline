package moderation.pipeline.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModerationOutput {

    private AtomicBoolean flag;
    private ConcurrentHashMap<String, String> logs;

    public ModerationOutput() {
        this.flag = new AtomicBoolean(true);
        this.logs = new ConcurrentHashMap<>();
    }
}
