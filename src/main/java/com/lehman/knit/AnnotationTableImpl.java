package com.lehman.knit;

public class AnnotationTableImpl {
    /**
     * Annotation table if set.
     */
    protected AnnotationTable table = null;

    /**
     * Gets the AnnotationTable object.
     * @return An AnnotationTable object or null of none is set.
     */
    public AnnotationTable getTable() {
        return table;
    }

    /**
     * Sets the AnnotationTable object.
     * @param table is an AnnotationTable object or null.
     */
    public void setTable(AnnotationTable table) {
        this.table = table;
    }
}
