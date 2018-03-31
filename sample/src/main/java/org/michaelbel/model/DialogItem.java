package org.michaelbel.model;

public class DialogItem {

    private String dialogTitle;
    private String headerTitle;

    public DialogItem() {}

    public DialogItem setTitle(String title) {
        this.dialogTitle = title;
        return this;
    }

    public DialogItem(String header) {
        this.headerTitle = header;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public boolean isNotTitle() {
        return getDialogTitle() != null;
    }
}