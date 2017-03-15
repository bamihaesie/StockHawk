package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.model.HistoricalQuote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_SYMBOL;


public class DetailActivity extends AppCompatActivity {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.detail_symbol)
    TextView detailSymbol;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.quoteHistory)
    RecyclerView quoteHistoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String symbol = intent.getStringExtra("symbol");

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        detailSymbol.setText(symbol);

        String history = extractHistory(symbol);

        List<HistoricalQuote> historicalQuotes = parseHistory(history);
        Log.i("STOCKS", historicalQuotes.toString());

        HistoricalQuoteAdapter historicalQuoteAdapter = new HistoricalQuoteAdapter(historicalQuotes);
        quoteHistoryRecyclerView.setAdapter(historicalQuoteAdapter);
        quoteHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private String extractHistory(String symbol) {
        Cursor mCursor = getContentResolver().query(
                Contract.Quote.URI,
                new String[] {Contract.Quote.COLUMN_HISTORY},
                COLUMN_SYMBOL + " = ?",
                new String[]{symbol},
                null
        );

        if (null == mCursor) {
            Log.e("STOCKS", "null cursor");
            return null;
        } else if (mCursor.getCount() < 1) {
            Log.e("STOCKS", "unsuccessful search");
            return null;
        } else {
            Log.i("STOCKS", "got back data " + mCursor.getCount());

            mCursor.moveToFirst();
            String history = mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
            Log.i("STOCKS", "History: " + history);

            return history;
        }
    }

    private List<HistoricalQuote> parseHistory(String history) {
        List<HistoricalQuote> historicalQuotes = new ArrayList<>();

        String[] historyLines = history.split("\n");
        for (String historyLine : historyLines) {
            String[] split = historyLine.split(", ");
            historicalQuotes.add(new HistoricalQuote(
                    Long.parseLong(split[0]),
                    new BigDecimal(split[1])
            ));
        }

        return historicalQuotes;
    }
}
