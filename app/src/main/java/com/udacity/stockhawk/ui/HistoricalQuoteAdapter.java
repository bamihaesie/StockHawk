package com.udacity.stockhawk.ui;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.model.HistoricalQuote;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

class HistoricalQuoteAdapter extends RecyclerView.Adapter<HistoricalQuoteAdapter.HistoricalQuoteViewHolder> {

    private List<HistoricalQuote> historicalQuotes;

    class HistoricalQuoteViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.timestamp)
        TextView timestamp;

        @BindView(R.id.quotePrice)
        TextView quotePrice;

        HistoricalQuoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public HistoricalQuoteAdapter(List<HistoricalQuote> historicalQuotes) {
        this.historicalQuotes = historicalQuotes;
    }

    @Override
    public HistoricalQuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_quote_history, parent, false);
        return new HistoricalQuoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoricalQuoteViewHolder holder, int position) {
        HistoricalQuote historicalQuote = historicalQuotes.get(position);
        holder.timestamp.setText(formatTimestamp(historicalQuote));
        holder.quotePrice.setText(formatPrice(historicalQuote));
    }

    private String formatTimestamp(HistoricalQuote historicalQuote) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
        return dateFormat.format(new Date(historicalQuote.getTimestamp()));
    }

    private String formatPrice(HistoricalQuote historicalQuote) {
        return historicalQuote.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    @Override
    public int getItemCount() {
        return historicalQuotes.size();
    }
}
