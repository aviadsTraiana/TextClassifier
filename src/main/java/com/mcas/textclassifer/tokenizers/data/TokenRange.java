package com.mcas.textclassifer.tokenizers.data;

import lombok.Value;
/**
 * inclusive range for low<=x<=high
 */
@Value
public class TokenRange {
    int low;
    int high;
}
