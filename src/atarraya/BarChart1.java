package atarraya;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.event.WindowEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * A bar chart with only two bars - here the 'maxBarWidth' attribute in the renderer prevents
 * the bars from getting too wide.
 *
 */
public class BarChart1 extends ApplicationFrame {

       String[] options;
       double[] values;
       String category = "";

       CategoryDataset dataset=null;
       JFreeChart chart=null;
       ChartPanel chartPanel=null;
       static DefaultCategoryDataset dataset1=null;

    public BarChart1(final String title,String[] options,String category,double[] values) {

        super(title);
        this.options=options;
        this.values=values;
        this.category=category;
        this.options=options;
        this.values=values;
        dataset = createDataset();
        chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        setSize(600,350);
        //setDefaultCloseOperation(HIDE_ON_CLOSE);

    }

    public void windowClosing(WindowEvent we)
    {
        setVisible(false);
    }


    private CategoryDataset createDataset() {

       dataset1 = new DefaultCategoryDataset();

       for(int i=0;i<options.length;i++)
       {
            dataset1.addValue(values[i], ""+options[i], category);
       }

        return dataset1;

    }


    private JFreeChart createChart(final CategoryDataset dataset) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            "Simulation Result",         // chart title
            "Dead Node",               // domain axis label
            "Count in Units",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(new Color(0xBBBBDD));

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.10);

        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.PINK,
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.BLUE,
            0.0f, 0.0f, Color.lightGray
        );

          final GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.GREEN,
            0.0f, 0.0f, Color.lightGray
        );

           final GradientPaint gp3 = new GradientPaint(
            0.0f, 0.0f, Color.RED,
            0.0f, 0.0f, Color.lightGray
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
         renderer.setSeriesPaint(2, gp2);
          renderer.setSeriesPaint(3, gp3);

        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }




}