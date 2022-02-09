package com.mcas.textclassifer.tokenizers;

import lombok.Value;
/**
 * inclusive range for low<=x<=high
 */
@Value
public class TokenRange {
    int low;
    int high;
}
