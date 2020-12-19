package com.lehman.knit;

public class dwVariable {
    private String commentString = "";
    private dwComment comment = new dwComment();
    private String name = "";

    public String getCommentString() {
        return commentString;
    }

    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }

    public dwComment getComment() {
        return comment;
    }

    public void setComment(dwComment comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
