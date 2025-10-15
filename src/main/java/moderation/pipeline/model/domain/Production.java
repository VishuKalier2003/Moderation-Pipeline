package moderation.pipeline.model.domain;

/**
 * A generic interface for production entities.
 *
 * @param <T> the type of the production entity
 */
public interface Production<T extends Production<T>> {
    public T produce();
}
