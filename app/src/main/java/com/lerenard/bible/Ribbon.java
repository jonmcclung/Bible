package com.lerenard.bible;

/**
 * Created by mc on 18-Dec-16.
 */
public class Ribbon {
    private Translation translation;
    private Reference reference;
    private String name;

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ribbon() {
        this.translation = null;
        this.reference = null;
        this.name = null;
    }

    public Ribbon(Translation translation, Reference reference, String name) {

        this.translation = translation;
        this.reference = reference;
        this.name = name;
    }
}
