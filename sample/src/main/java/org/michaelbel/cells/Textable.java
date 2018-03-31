package org.michaelbel.cells;

import android.support.annotation.StringRes;

public interface Textable {

    void setText(String text);

    void setText(@StringRes int textId);
}