package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.model.HistoricalQuote;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_SYMBOL;


public class DetailActivity extends AppCompatActivity {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.detail_symbol)
    TextView detailSymbol;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.chart)
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String symbol = intent.getStringExtra("symbol");

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        detailSymbol.setText(symbol);

        String history = extractHistory(symbol);

        final List<HistoricalQuote> historicalQuotes = parseHistory(history);
        Log.i("STOCKS", historicalQuotes.toString());

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < historicalQuotes.size(); i++) {
            HistoricalQuote historicalQuote = historicalQuotes.get(historicalQuotes.size() - i - 1);
            entries.add(
                new Entry(i, historicalQuote.getPrice().floatValue())
            );
        }

        LineDataSet dataSet = new LineDataSet(entries, "Time series");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);


        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return formatTimestamp(historicalQuotes.get((int) (historicalQuotes.size() - value - 1)));
            }
        });

        chart.invalidate(); // refresh

    }

    private String formatTimestamp(HistoricalQuote historicalQuote) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        return dateFormat.format(new Date(historicalQuote.getTimestamp()));
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
