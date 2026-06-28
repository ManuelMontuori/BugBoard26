package org.frontend.models;

public class MeseOpzione {
    private final int anno;
    private final int mese;
    private final String etichetta;

    public MeseOpzione(int anno, int mese, String etichetta) {
        this.anno = anno;
        this.mese = mese;
        this.etichetta = etichetta;
    }

    public int getAnno() { return anno; }
    public int getMese() { return mese; }

    @Override
    public String toString() {
        // JavaFX usa il toString() per decidere cosa mostrare nella ComboBox
        return etichetta;
    }
}