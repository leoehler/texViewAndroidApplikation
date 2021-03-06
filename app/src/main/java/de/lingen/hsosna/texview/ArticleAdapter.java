package de.lingen.hsosna.texview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Um die RecyclerView zu füllen, wird der Adapter mit den TextView's des Layouts gefüllt und diesen
 * den zugehörigen Daten zugeordnet.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ExampleViewHolder> {

    private ArrayList<Article> mExampleList;
    private OnItemClickListener mListener;

    /**
     *
     */
    public interface OnItemClickListener {
        void onItemClick (int position);
        void onLocationMarkerClick (int position);
    }


    /**
     *
     * @param listener
     */
    public void setOnItemClickListener (OnItemClickListener listener) {
        mListener = listener;
    }



    /**
     * In der ViewHolder Klasse werden die TextView's als Attribute gesetzt und mittels des Konstruktors
     * werden die TextView's aus dem Layout den Attributen zugeordnet.
     */
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextArtikelNr;
        public TextView mTextArtikelBez;
        public TextView mTextStuecknummer;
        public TextView mTextFarbeID;
        public TextView mTextFarbeBez;
        public TextView mTextGroessenID;
        public TextView mTextFertZust;
        public TextView mTextMenge;
        public TextView mTextMengenEinheit;

        public ImageView mLocationMarkerImage;

        /**
         * Die Klassenparameter werden mit den TextView's des Layouts initialisiert.
         *
         * @param itemView Layout eines einzelnen Artikels, das mittels der RecyclerView
         *                 wiederverwendet wird.
         */
        public ExampleViewHolder (@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextArtikelNr       = itemView.findViewById(R.id.itemArticle_placeholder_articleId);
            mTextArtikelBez      = itemView.findViewById(R.id.itemArticle_placeholder_articleDescription);
            mTextStuecknummer    = itemView.findViewById(R.id.itemArticle_placeholder_pieceId);
            mTextFarbeID         = itemView.findViewById(R.id.itemArticle_placeholder_colorId);
            mTextFarbeBez        = itemView.findViewById(R.id.itemArticle_placeholder_colorDescription);
            mTextGroessenID      = itemView.findViewById(R.id.itemArticle_placeholder_size);
            mTextFertZust        = itemView.findViewById(R.id.itemArticle_placeholder_manufacturingState);
            mTextMenge           = itemView.findViewById(R.id.itemArticle_placeholder_amount);
            mTextMengenEinheit   = itemView.findViewById(R.id.itemArticle_placeholder_amountUnit);
            mLocationMarkerImage = itemView.findViewById(R.id.itemArticle_icon_location);

            // item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // location marker
            mLocationMarkerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        //int test = mExampleList.get(position).getArtikelID();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onLocationMarkerClick(position);
                        }
                    }
                }
            });
        }
    }



    public ArticleAdapter (ArrayList<Article> exampleList) {
        mExampleList = exampleList;
    }


    public ArticleAdapter(){
        mExampleList = new ArrayList<>();
    }



    /**
     * Ein ViewHolder wird durch das item_article erstellt.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ExampleViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent,
                false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);

        return evh;
    }


    /**
     * Das aktuelle Item wird herangezogen und den TextView's zugeordnet.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder (@NonNull ExampleViewHolder holder, int position) {
        Article currentItem = mExampleList.get(position);

        holder.mTextArtikelNr.setText(String.valueOf(currentItem.getArtikelID()));
        holder.mTextArtikelBez.setText(currentItem.getArtikel_Bezeichnung());
        // STUECKNUMMER
        if (currentItem.getStueckteilung() == 0) {
            holder.mTextStuecknummer.setText(String.valueOf(currentItem.getStuecknummer()));
        } else {
            String stuecknummer = currentItem.getStuecknummer() + "/" + currentItem.getStueckteilung();
            holder.mTextStuecknummer.setText(stuecknummer);
        }
        holder.mTextFarbeID.setText(String.valueOf(currentItem.getFarbe_ID()));
        holder.mTextFarbeBez.setText(currentItem.getFarbe_Bezeichnung());
        holder.mTextGroessenID.setText(String.valueOf(currentItem.getGroessen_ID()));
        holder.mTextFertZust.setText(currentItem.getFertigungszustand());
        holder.mTextMenge.setText(currentItem.getMenge());
        holder.mTextMengenEinheit.setText(currentItem.getMengenEinheit());
    }


    /**
     *
     * @return
     */
    @Override
    public int getItemCount () {
        return mExampleList.size();
    }

}
