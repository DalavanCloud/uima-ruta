PACKAGE org.apache.uima;

DECLARE T1, T2, T3, T4, T5, T6, T7, T8;

Document{ -> RETAINTYPE(MARKUP)};
W+{-PARTOF(T1) -> MARK(T1)};

T1{-> SHIFT(T1, 1, 2)} MARKUP;

