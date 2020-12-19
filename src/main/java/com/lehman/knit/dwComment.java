package com.lehman.knit;

import java.util.ArrayList;

public class dwComment {
    private String text = "";
    private ArrayList<dwCommentAnnotation> annotations = new ArrayList<dwCommentAnnotation>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<dwCommentAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(ArrayList<dwCommentAnnotation> annotations) {
        this.annotations = annotations;
    }
}
