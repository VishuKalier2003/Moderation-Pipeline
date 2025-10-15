package moderation.pipeline.model.domain;

import moderation.pipeline.data.ModerationOutput;

/**
 * The interface for all moderation types. It is to be overridden for the base moderator and the decorators
 */
public interface Moderation {
    public ModerationOutput moderate(String data, ModerationOutput log);
}
