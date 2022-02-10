package com.mcas.textclassifier.types;

import lombok.Value;
/**
 * inclusive range for low<=x<=high
 */
@Value
public class TokenRange {
    int low;
    int high;
}
