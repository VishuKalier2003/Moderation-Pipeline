package moderation.pipeline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import moderation.pipeline.model.storage.User;

public interface UserRepo extends JpaRepository<User, String> {

}
