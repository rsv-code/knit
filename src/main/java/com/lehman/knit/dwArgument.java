package com.lehman.knit;

import javax.xml.crypto.Data;

public class dwArgument {
    private String name = "";
    private String datatype = "";

    public dwArgument() { }
    public dwArgument(String Name) { this.name = Name; }
    public dwArgument(String Name, String Datatype) {
        this.name = Name;
        this.datatype = Datatype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
}
