
package com.myroundviewgrapheample;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ghanshyam.graphlibs.Graph;
import com.ghanshyam.graphlibs.GraphData;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Graph graph = (Graph)findViewById(R.id.graph);
        graph.setMinValue(0f);
        graph.setMaxValue(100f);
        graph.setDevideSize(0.5f);
        graph.setBackgroundShapeWidthInDp(10);
        graph.setShapeForegroundColor(getResources().getColor(R.color.orange));
        graph.setShapeBackgroundColor(getResources().getColor(R.color.light_gray));
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources resources = getResources();
                Collection<GraphData> data = new ArrayList<>();
                data.add(new GraphData(20f, resources.getColor(R.color.green)));
                data.add(new GraphData(15f, resources.getColor(R.color.orange)));
                data.add(new GraphData(55f, resources.getColor(R.color.pink)));
                data.add(new GraphData(10f, resources.getColor(R.color.chart_value_1)));
                graph.setData(data);
            }
        });
    }
}
