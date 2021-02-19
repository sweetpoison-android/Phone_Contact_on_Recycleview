package com.example.phonecontactonrecycleview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyrecyclerviewAdapter extends RecyclerView.Adapter<MyrecyclerviewAdapter.Myinner> {

    Context con;

    LayoutInflater inflater;
    List<Mybean> modellist;
    ArrayList<Mybean> arrayList;


    public MyrecyclerviewAdapter(Context con, List<Mybean> lt) {
        this.con = con;
        this.modellist = lt;
        inflater = LayoutInflater.from(con);
        this.arrayList = new ArrayList<Mybean>();
        this.arrayList.addAll(modellist);
    }

    @NonNull
    @Override
    public Myinner onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater lnf=LayoutInflater.from(con);
        View v=lnf.inflate(R.layout.myrecyclerview_item, null, true);

        return new MyrecyclerviewAdapter.Myinner(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myinner holder, int position) {

        holder.tv.setText(modellist.get(position).getName());

        //for expanded recycleview .........

        boolean expended = modellist.get(position).isExpanded();
        holder.rl2.setVisibility(expended ? View.VISIBLE : View.GONE);

        holder.number.setText(modellist.get(position).getNumber());


   //  for first letter image  ..................

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(50) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(get_first_letter(modellist.get(position).getName()), Color.parseColor(get_color_for_letter(modellist.get(position).getName())));
        holder.first_letter_img.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }

    public class Myinner extends RecyclerView.ViewHolder {

        View itemView;
        ImageView first_letter_img;
        TextView tv;

        RelativeLayout rl1,rl2;
        TextView number;

        public Myinner(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;

            tv=itemView.findViewById(R.id.recyclerviewitem_textview);
            first_letter_img=itemView.findViewById(R.id.recyclerviewitem_imageview);

            rl1=itemView.findViewById(R.id.myrecyclerviewitem_relative1);
            rl2=itemView.findViewById(R.id.myrecyclerviewitem_relative2);
            number=itemView.findViewById(R.id.recyclerviewitem_number);


            rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Mybean ma = modellist.get(getAdapterPosition());
                    ma.setExpanded(!ma.isExpanded());
                    notifyItemChanged(getAdapterPosition());

                }
            });

        }
    }

    public String get_first_letter(String name)
    {
        char a = name.charAt(0);
        String ans = Character.toString(a);
        return  ans;
    }
    public String get_color_for_letter(String name)
    {
        String color= "#2e3c50";

        char a = name.charAt(0);
        String ans = Character.toString(a);

        if(ans.equalsIgnoreCase("a") || ans.equalsIgnoreCase("b") || ans.equalsIgnoreCase("c") || ans.equalsIgnoreCase("d"))
        {
            color="#990000";
        }
        else if(ans.equalsIgnoreCase("e") || ans.equalsIgnoreCase("f") || ans.equalsIgnoreCase("g") || ans.equalsIgnoreCase("h"))
        {
            color="#0000ff";
        }
        else if(ans.equalsIgnoreCase("i") || ans.equalsIgnoreCase("j") || ans.equalsIgnoreCase("k") || ans.equalsIgnoreCase("l"))

        {
            color ="#bf00bf";
        }
        else if(ans.equalsIgnoreCase("m") || ans.equalsIgnoreCase("n") || ans.equalsIgnoreCase("o") || ans.equalsIgnoreCase("p"))

        {
            color = "#00bfff";
        }
        else if(ans.equalsIgnoreCase("q") || ans.equalsIgnoreCase("r") || ans.equalsIgnoreCase("s") || ans.equalsIgnoreCase("t"))

        {
            color = "#ff4000";
        }

        return color;
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if(charText.length()==0)
        {
            modellist.addAll(arrayList);
        }
        else
        {
            for(Mybean model : arrayList)
            {
                if(model.getName().toLowerCase(Locale.getDefault())
                        .contains(charText))
                {
                    modellist.add(model);

                }

            }
        }
        notifyDataSetChanged();
    }

}
