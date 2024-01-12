package com.bbangle.bbangle.dto;

import lombok.*;

import java.util.HashMap;

@Builder
public record TagDto(
        HashMap<String, Boolean> glutenFreeTag,
        HashMap<String, Boolean> highProteinTag,
        HashMap<String, Boolean> sugarFreeTag,
        HashMap<String, Boolean> veganTag,
        HashMap<String, Boolean> ketogenicTag
) { }
