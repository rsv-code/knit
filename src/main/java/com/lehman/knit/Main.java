package com.lehman.knit;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        knitParser parser = new knitParser();
        dwFile f = parser.parseFile("dw/test.dw");
        System.out.println(f.toString());
    }
}
