package org.app.material.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({"LOW", "MEDIUM", "HIGH", "MAX"})
public @interface Priority {}