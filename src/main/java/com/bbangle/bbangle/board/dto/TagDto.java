package com.bbangle.bbangle.board.dto;

import com.bbangle.bbangle.board.domain.TagEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Builder;

@Builder
public record TagDto(
    Boolean gluten_free_tag,
    Boolean high_protein_tag,
    Boolean sugar_free_tag,
    Boolean vegan_tag,
    Boolean ketogenic_tag
) {
    private static List<String> tags = new ArrayList<>(5);

    public List<String> toStringList() {
        if (Objects.nonNull(gluten_free_tag) && gluten_free_tag){
            tags.add(TagEnum.GLUTEN_FREE.label());
        }

        if (Objects.nonNull(high_protein_tag) && high_protein_tag){
            tags.add(TagEnum.HIGH_PROTEIN.label());
        }

        if (Objects.nonNull(sugar_free_tag) && sugar_free_tag){
            tags.add(TagEnum.SUGAR_FREE.label());
        }

        if (Objects.nonNull(vegan_tag) && vegan_tag){
            tags.add(TagEnum.VEGAN.label());
        }

        if (Objects.nonNull(ketogenic_tag) && ketogenic_tag){
            tags.add(TagEnum.KETOGENIC.label());
        }

        return tags;
    }
}
