package com.example.examentp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ElementViewHolder> {
    private final List<Element> listFull;
    private List<Element> ElementList;
    private OnItemClickListener mOnItemClickListener;

    public ElementAdapter(List<Element> ElementList) {
        this.ElementList = ElementList;
           listFull=new ArrayList<>(ElementList);
    }

    @NonNull
    @Override
    public ElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new ElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElementViewHolder holder, int position) {
        Element Element = ElementList.get(position);
        holder.currency.setText(Element.getCurrency());
        holder.somme.setText(Element.getSomme());


        //if(Element.getUrlToImg()!=null)
       // Log.d("ffefrnnn",Element.getUrlToImg());
       // Glide.with(holder.element_image.getContext()).load(Element.getUrlToImg()).into(holder.element_image);


        // Set the OnClickListener on the CardView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(Element);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ElementList.size();
    }


    public static class ElementViewHolder extends RecyclerView.ViewHolder {
        public TextView currency;
        public TextView somme;


        ImageView element_image;

        public ElementViewHolder(View itemView) {
            super(itemView);
            currency = itemView.findViewById(R.id.currency);
            somme = itemView.findViewById(R.id.somme);


        }
    }
    public List<Element> getElementList() {
        return ElementList;
    }


    public void setElementList(List<Element> ElementList) {
        this.ElementList = ElementList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    /*public void sortData(String sortBy,ArrayList<Element> fullElemntAdapter) {
        switch (sortBy.toLowerCase()) {
            case "name":
                Collections.sort(ElementList, new Comparator<Element>() {
                    @Override
                    public int compare(Element o1, Element o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                break;
            case "rating":
                Collections.sort(ElementList, new Comparator<Element>() {
                    @Override
                    public int compare(Element o1, Element o2) {
                        return o2.getRating().compareTo(o1.getRating());
                    }
                });
                break;
            case "none":
                ElementList.clear();
                ElementList.addAll(fullElemntAdapter);
                break;
            default:
                throw new IllegalArgumentException("Invalid sortBy argument: " + sortBy);
        }
        notifyDataSetChanged();
    }*/

}
