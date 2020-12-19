package com.lehman.knit;

import java.util.ArrayList;

public class dwFunction {
    private String commentString = "";
    private dwComment comment = new dwComment();
    private String name = "";
    private ArrayList<dwArgument> arguments = new ArrayList<dwArgument>();

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

    public ArrayList<dwArgument> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<dwArgument> arguments) {
        this.arguments = arguments;
    }
}
