PACKAGE org.apache.uima;

DECLARE T1, T2, T3, T4, T5, T6, T7, T8;

Document{ -> RETAINTYPE(SPACE, MARKUP)};

SPACE ANY{-> MARK(T1,1,2)};
ANY MARKUP{-> MARK(T2,1,2)};

