package moderation.pipeline.factory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import moderation.pipeline.model.base.BaseModerator;
import moderation.pipeline.model.composite.ContextModerator;
import moderation.pipeline.model.composite.DataModerator;
import moderation.pipeline.model.composite.Limiter;
import moderation.pipeline.model.composite.MessagingModerator;
import moderation.pipeline.model.composite.PasswordModerator;
import moderation.pipeline.model.composite.StyleModerator;
import moderation.pipeline.model.composite.WordModerator;
import moderation.pipeline.model.domain.Moderation;

@Component
public class ModerationFactory {

    private final Map<String, ObjectProvider<? extends Moderation>> providers = new HashMap<>();

    public ModerationFactory(
            ObjectProvider<BaseModerator> baseProvider,
            ObjectProvider<WordModerator> wordProvider,
            ObjectProvider<DataModerator> dataProvider,
            ObjectProvider<PasswordModerator> passwordProvider,
            ObjectProvider<MessagingModerator> messagingProvider,
            ObjectProvider<ContextModerator> contextProvider,
            ObjectProvider<StyleModerator> styleProvider,
            ObjectProvider<Limiter> limiterProvider
    ) {
        // Register providers instead of instances
        providers.put("BASE", baseProvider);
        providers.put("WORD", wordProvider);
        providers.put("DATA", dataProvider);
        providers.put("PASSWORD", passwordProvider);
        providers.put("MESSAGING", messagingProvider);
        providers.put("CONTEXT", contextProvider);
        providers.put("STYLE", styleProvider);
        providers.put("LIMITER", limiterProvider);
    }

    /**
     * Creates a fresh Spring-managed instance of the requested moderator type.
     */
    public <T extends Moderation> T create(String type) {
        ObjectProvider<? extends Moderation> provider = providers.get(type.toUpperCase());
        if (provider != null) {
            return (T) provider.getObject(); // Spring creates a new prototype instance
        }
        throw new IllegalArgumentException("Unknown moderation type: " + type);
    }

    /**
     * Optional: dynamic registration of new types
     */
    public void register(String key, ObjectProvider<? extends Moderation> provider) {
        providers.put(key.toUpperCase(), provider);
    }
}
