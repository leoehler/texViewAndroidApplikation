package de.lingen.hsosna.texview.database;

import android.provider.BaseColumns;

/**
 * Deklaration der Spaltennamen für die Tabelle "Artikelkombinationen"
 */
public class TableArtikelkombination {
    private TableArtikelkombination () {
    }

    public static final class ArtikelkombinationenEntry implements BaseColumns {
        public static final String TABLE_NAME = "artikelkombinationen";
        public static final String COLUMN_ARTIKEL_ID = "artikel_id_artikelkombinationen";
        public static final String COLUMN_ARTIKEL_BEZEICHNUNG =
                "artikel_bezeichnung_artikelkombinationen";
        public static final String COLUMN_GROESSE_ID = "groesse_id_artikelkombinationen";
        public static final String COLUMN_FARBE_ID = "farbe_id_artikelkombinationen";
        public static final String COLUMN_FARBE_BEZEICHNUNGEN =
                "farbe_bezeichnung_artikelkombinationen";
    }
}