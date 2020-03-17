package efrei.moutte.moutte_project.adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import efrei.moutte.moutte_project.R;
import efrei.moutte.moutte_project.activities.AnnonceActivity;
import efrei.moutte.moutte_project.models.Annonce;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Annonce> mData ;
    RequestOptions option;

    public RecyclerViewAdapter(Context mContext, List<Annonce> mData) {
        this.mContext = mContext;
        this.mData = mData;

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.annonce_row_item,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, AnnonceActivity.class);
                i.putExtra("annonce_ville", mData.get(viewHolder.getAdapterPosition()).getVille());
                i.putExtra("annonce_price",mData.get(viewHolder.getAdapterPosition()).getPrix() + "€        " + mData.get(viewHolder.getAdapterPosition()).getPrixm2() + "€/m²" );
                i.putExtra("annonce_description", mData.get(viewHolder.getAdapterPosition()).getDescription());
                i.putExtra("annonce_source", mData.get(viewHolder.getAdapterPosition()).getSource());
                i.putExtra("annonce_rendement", "Rendement: " + mData.get(viewHolder.getAdapterPosition()).getRendement() + "%");
                i.putExtra("annonce_nb_piece", mData.get(viewHolder.getAdapterPosition()).getNbpieces());
                i.putExtra("annonce_link", mData.get(viewHolder.getAdapterPosition()).getPermalien());
                i.putExtra("annonce_type", mData.get(viewHolder.getAdapterPosition()).getType());
                i.putExtra("annonce_loyet", "Loyer envisageable: " + mData.get(viewHolder.getAdapterPosition()).getLoyer() + "€");
                i.putExtra("annonce_img",mData.get(viewHolder.getAdapterPosition()).getImg_url());
                i.putExtra("annonce_id", mData.get(viewHolder.getAdapterPosition()).getId_annonce());
                i.putExtra("annonce_objectId", mData.get(viewHolder.getAdapterPosition()).getObjectID());

                mContext.startActivity(i);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_ville.setText(mData.get(position).getVille());
        holder.tv_price.setText(mData.get(position).getPrix() + "€     "+ mData.get(position).getPrixm2() + "€/m²");
        holder.tv_type.setText(mData.get(position).getType() + " - " + mData.get(position).getSurface() + "m²");
        holder.tv_description.setText(mData.get(position).getDescription());
        holder.tv_rendement.setText("Rendement: " + mData.get(position).getRendement() + "%");
        holder.tv_loyet.setText("Loyer envisageable: " + mData.get(position).getLoyer());
        Glide.with(mContext).load(mData.get(position).getImg_url()).apply(option).into(holder.img_annonce);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ville ;
        TextView tv_type ;
        TextView tv_description ;
        TextView tv_loyet ;
        TextView tv_price;
        TextView tv_rendement ;
        ImageView img_annonce;

        LinearLayout view_container;

        public MyViewHolder(View itemView) {
            super(itemView);
            view_container = itemView.findViewById(R.id.container);
            tv_ville = itemView.findViewById(R.id.annonce_ville);
            tv_type = itemView.findViewById(R.id.annonce_type);
            tv_loyet = itemView.findViewById(R.id.annonce_loyet);
            tv_price = itemView.findViewById(R.id.annonce_price);
            tv_rendement = itemView.findViewById(R.id.annonce_redement);
            tv_description = itemView.findViewById(R.id.annonce_description);
            img_annonce = itemView.findViewById(R.id.annonce_img);

        }
    }

}
