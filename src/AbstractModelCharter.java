import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public abstract class AbstractModelCharter
{
	public abstract class AbstractTraceResults{}

	protected DeadlineModelParameters _params = null;
	protected Map<String, Integer> _sum_job_completions = null;
	protected Map<String, Integer> _sum_job_misses = null;
	protected Map<String, Integer> _sum_deadline_misses = null;

	public AbstractModelCharter(DeadlineModelParameters params)
	{
		super();
		this._params = params;
	}

	protected void initAggregateResults() {
		this._sum_job_completions = new HashMap<String, Integer>();
		this._sum_job_misses = new HashMap<String, Integer>();
		this._sum_deadline_misses = new HashMap<String, Integer>();
	
		for(String policy : this._params.get_policies())
		{
			this._sum_job_completions.put(policy, 0);
			this._sum_job_misses.put(policy, 0);
			this._sum_deadline_misses.put(policy, 0);
		}		
	}

	protected void addToSum(Map<String, Integer> map, String policy, int value) {
		Integer sum = map.get(policy);
		sum += value;
		map.put(policy, sum);		
	}

	protected void addResultsToSeries(int appmasters, XYSeries series, Float[] results) {
		for(int I = 0; I < appmasters; I++)
		{
			series.add(I + 1, results[I]);
		}
	}

	protected void saveChart(String str_yaxis, XYPlot plot, String header,
			String out_path, String filename) {
				ValueAxis xaxis = new NumberAxis("AppMasters");
				xaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
				ValueAxis yaxis = new NumberAxis(str_yaxis);
				yaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
				plot.setDomainAxis(0, xaxis);
				plot.setRangeAxis(0, yaxis);
				JFreeChart chart = new JFreeChart(header, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
				try {
					//ChartUtilities.saveChartAsJPEG(new File(out_path + "\\" + filename + ".jpg"), chart, 800, 600);
					ChartUtilities.saveChartAsPNG(new File(out_path + "\\" + filename + ".png"), chart, 800, 600);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	protected void addDatasetToPlot(XYPlot plot, XYSeries series, int plot_index) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		plot.setDataset(plot_index, dataset);
	
		XYItemRenderer line = new XYLineAndShapeRenderer();
		if(plot_index == 3)
		{
			line.setSeriesPaint(0, Color.ORANGE);
		}
		plot.setRenderer(plot_index, line);		
	}

	public abstract void chartCombinedCharts();

	public void writeTableResults() {
		float traces = (float)this._params.get_traces();
		float appmasters = (float)this._params.get_appmasters();
		float combinations = this._params.get_job_arrival_types().size() * this._params.get_job_length_types().size();
		String results = 
				"		Mean deadline misses	Job request success rate	Job success rate\n";
		for(String policy : _params.get_policies())
		{
			float mean_deadline_misses_per_appmaster = ((this._sum_deadline_misses.get(policy)/traces)/appmasters)/combinations;
			String rmdm = String.format("%.2f", mean_deadline_misses_per_appmaster);
			
			float mean_job_completions = (float)this._sum_job_completions.get(policy)/traces;
			float mean_deadline_misses = (float)this._sum_deadline_misses.get(policy)/traces;
			float job_request_success_rate = (mean_job_completions/(mean_job_completions + mean_deadline_misses)) * 100.0f;
			String rjrsr = String.format("%.2f", job_request_success_rate);

			float mean_job_misses = (float)this._sum_job_misses.get(policy)/traces;
			float job_success_rate = (mean_job_completions/(mean_job_completions + mean_job_misses)) * 100.0f;
			String rjsr = String.format("%.2f", job_success_rate);

			String padding = "";
			int longest_policy_name = "priority".length(); 
			for(int I = 0; I < longest_policy_name - policy.length(); I++)
			{
				padding += " ";
			}
			String line = 
					policy + ":      " + padding + rmdm;
			for(int I = line.length(); I < 41; I++)
			{
				line += " ";
			}
			line += rjrsr + "%";
			for(int I = line.length(); I < 65; I++)
			{
				line += " ";
			}
			line += rjsr + "%\n";
			results += line;
			//rmdm begins at 15, rjrsr col 41, rjsr 65
		}
		results += this.getParametersString();
		this.writeStringToFile(results, _params.get_base_path() + "\\" + _params.get_base_name() + ".txt");
	}

	private String getParametersString() {
		String results = "";
		
		results += "\nParameters:\n";
		results += "\nAppMasters: " + _params.get_appmasters();
		results += "\nQueue size: " + _params.get_queue_size();
		results += "\nTraces: " + _params.get_traces();
		results += "\nTimeunits: " + _params.get_timeunits();
		results += "\n";
		results += "\nEpsilon: " + _params.get_epsilon();
		results += "\n";
		
		if(_params.get_policies().contains("priority"))
		{
			results += "\nHigh priority job probability %: " + _params.get_high_priority_job_probability();
			results += "\nHigh priority job length: " + _params.get_high_priority_job_length();
			results += "\n";
		}
		
		List<String> job_arrival_types = _params.get_job_arrival_types();
		if(job_arrival_types.contains("bursty"))
		{
			results += "\nJob arrival burst interval: " + _params.get_job_arrival_burst_interval();
			results += "\nJob arrival burst size: " + _params.get_job_arrival_burst_size();			
		}
		if(job_arrival_types.contains("nondet"))
		{
			results += "\nJob arrival nondet minimum: " + _params.get_job_arrival_nondet_min();
			results += "\nJob arrival nondet maximum: " + _params.get_job_arrival_nondet_max();
		}
		if(job_arrival_types.contains("uniform"))
		{
			results += "\nJob arrival uniform value: " + _params.get_job_arrival_uniform();
		}
		if(job_arrival_types.contains("wave"))
		{
			results += "\nJob arrival wave additional jobs per timeunit: " + _params.get_job_arrival_wave_additional_jobs_per_timeunit();
			results += "\nJob arrival wave minimum: " + _params.get_job_arrival_wave_minimum();
			results += "\nJob arrival wave points: " + _params.get_job_arrival_wave_points();
		}
		if(job_arrival_types.contains("ascending"))
		{
			results += "\nJob arrival ascending increment: " + _params.get_job_arrival_ascending_increment();
			results += "\nJob arrival ascending minimum: " + _params.get_job_arrival_ascending_minimum();
			results += "\nJob arrival ascending points: " + _params.get_job_arrival_ascending_points();
		}
		if(job_arrival_types.contains("descending"))
		{
			results += "\nJob arrival descending decrement: " + _params.get_job_arrival_descending_decrement();
			results += "\nJob arrival descending maximum: " + _params.get_job_arrival_descending_maximum();
			results += "\nJob arrival descending points: " + _params.get_job_arrival_descending_points();
		}
		results += "\n";
		
		List<String> job_length_types = _params.get_job_length_types();
		if(job_length_types.contains("exponential"))
		{
			results += "\nJob length exponential multiplier: " + _params.get_job_length_exponential_multiplier();
		}
		if(job_length_types.contains("nondet"))
		{
			results += "\nJob length nondet minimum: " + _params.get_job_length_nondet_min();
			results += "\nJob length nondet maximum: " + _params.get_job_length_nondet_max();
		}
		if(job_length_types.contains("uniform"))
		{
			results += "\nJob length uniform value: " + _params.get_job_length_uniform();
		}
		if(job_length_types.contains("wave"))
		{
			results += "\nJob length wave additional length per timeunit: " + _params.get_job_length_wave_additional_length_per_timeunit();
			results += "\nJob length wave minimum: " + _params.get_job_length_wave_minimum();
			results += "\nJob length wave points: " + _params.get_job_length_wave_points();
		}
		if(job_length_types.contains("ascending"))
		{
			results += "\nJob length ascending increment: " + _params.get_job_length_ascending_increment();
			results += "\nJob length ascending minimum: " + _params.get_job_length_ascending_minimum();
			results += "\nJob length ascending points: " + _params.get_job_length_ascending_points();
		}
		if(job_length_types.contains("descending"))
		{
			results += "\nJob length descending decrement: " + _params.get_job_length_descending_decrement();
			results += "\nJob length descending maximum: " + _params.get_job_length_descending_maximum();
			results += "\nJob length descending points: " + _params.get_job_length_descending_points();
		}
	
		results += "\n\nJob arrival types: ";
		for(String job_arrival_type : _params.get_job_arrival_types())
		{
			results += "\n" + job_arrival_type;
		}
		
		results += "\n\nJob length types: ";
		for(String job_length_type : _params.get_job_length_types())
		{
			results += "\n" + job_length_type;
		}
	
		return results;
	}

	private void writeStringToFile(String string, String full_path) {
		Writer writer = null;
	    try {
			writer = new BufferedWriter(new OutputStreamWriter(
			      new FileOutputStream(full_path), "utf-8"));
		    writer.write(string);
		    writer.close();
	    } catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
}