package dev.joaov.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Anime {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;

}
