package moderation.pipeline.controller.input;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataDto {
    private final String data;
    private final String username;
    private final String password;
}
