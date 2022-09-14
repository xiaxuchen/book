package org.originit.analyze.consts;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface BookConst {

    String SUFFIX_HTML = "html";

    String SUFFIX_PDF = "pdf";

    List<String> SUFFIX_ALL = Collections.unmodifiableList(Arrays.asList(SUFFIX_PDF,SUFFIX_HTML));
}
