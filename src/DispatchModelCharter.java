import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;

public final class DispatchModelCharter extends AbstractModelCharter
{
	public final class TraceResults extends AbstractTraceResults
	{
		private Float[] _mean_job_completions = null;
		private Float[] _mean_job_misses = null;
		private Float[] _mean_deadline_misses = null;
		private Float[] _mean_queue_misses = null;
		private Float[] _mean_dropped_jobs = null;

		public TraceResults(Float[] mean_job_completions, Float[] mean_job_misses,
				Float[] mean_deadline_misses, Float[] mean_queue_misses, Float[] mean_dropped_jobs)
		{
			this._mean_job_completions = mean_job_completions;
			this._mean_job_misses = mean_job_misses;
			this._mean_deadline_misses = mean_deadline_misses;
			this._mean_queue_misses = mean_queue_misses;
			this._mean_dropped_jobs = mean_dropped_jobs;
		}

		public Float[] get_mean_job_completions() {
			return _mean_job_completions;
		}

		public Float[] get_mean_job_misses() {
			return _mean_job_misses;
		}

		public Float[] get_mean_deadline_misses() {
			return _mean_deadline_misses;
		}

		public Float[] get_mean_queue_misses() {
			return _mean_queue_misses;
		}

		public Float[] get_mean_dropped_jobs() {
			return _mean_dropped_jobs;
		}
	}

	public DispatchModelCharter(DeadlineModelParameters params) {
		super(params);
		// TODO Auto-generated constructor stub
	}

	public void chartCombinedCharts()
	{
		this.initAggregateResults();
		for(String job_arrival_type : _params.get_job_arrival_types())
		{
			for(String job_length_type : _params.get_job_length_types())
			{
				Map<String, AbstractTraceResults> policy_results = new HashMap<String, AbstractTraceResults>();
				int appmasters = _params.get_appmasters();		
				for(String policy : _params.get_policies())
				{					
					Float[] mean_job_completions = new Float[appmasters];
					Float[] mean_job_misses = new Float[appmasters];
					Float[] mean_deadline_misses = new Float[appmasters];
					Float[] mean_queue_misses = new Float[appmasters];
					Float[] mean_dropped_jobs = new Float[appmasters];
					for(int I = 0; I < appmasters; I++)
					{
						String traces_path = _params.get_traces_path() + "\\" + policy + (I + 1) + "-" + job_arrival_type + "-" + job_length_type + "\\traces.xml";
						DeadlineTracesParser parser = new DeadlineTracesParser();
						parser.parseDocument(traces_path);
						
						float traces = (float)parser.getTraces();
						mean_job_completions[I] = (float)parser.getJobCompletions()/traces;
						mean_job_misses[I] = (float)parser.getJobMisses()/traces;
						mean_deadline_misses[I] = (float)parser.getDeadlineMisses()/traces;
						mean_queue_misses[I] = (float)parser.getQueueMisses()/traces;
						mean_dropped_jobs[I] = (float)parser.getDroppedJobs()/traces;
						
						this.addToSum(_sum_job_completions, policy, parser.getJobCompletions());
						this.addToSum(_sum_job_misses, policy, parser.getJobMisses());
						this.addToSum(_sum_deadline_misses, policy, parser.getDeadlineMisses());
					}
					AbstractTraceResults results = new TraceResults(mean_job_completions, mean_job_misses,
							mean_deadline_misses, mean_queue_misses, mean_dropped_jobs);
					policy_results.put(policy, results);
				}
				this.writeCharts(job_arrival_type, job_length_type, policy_results);
			}
		}
	}
	
	protected void writeCharts(String job_arrival_type, String job_length_type, Map<String, AbstractTraceResults> policy_results)
	{
		String filename_end = "-" + job_arrival_type + "-" + job_length_type;
		String header_end = " - " + job_arrival_type + " arrival, " + job_length_type + " length";
		String out_path = _params.get_base_path() + "\\" + _params.get_base_name() + "-" + job_arrival_type + "-" + job_length_type;

		int traces = _params.get_traces();
		int timeunits = _params.get_timeunits();
		
		String prefix = _params.get_base_name();
		String filename = null;
		String header = null;
		String str_yaxis = null;
		int type = 0;
	
		filename = prefix + "-mean_deadline_misses" + filename_end;
		header = "Mean deadline misses - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		str_yaxis = "Mean deadline misses";
		type = 0;
		this.writeChart(policy_results, type, str_yaxis, header, out_path, filename);
		
		filename = prefix + "-job_request_success_rate" + filename_end;
		header = "Job request success rate - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		str_yaxis = "Job request success rate %";
		type = 1;
		this.writeChart(policy_results, type, str_yaxis, header, out_path, filename);

		filename = prefix + "-job_success_rate" + filename_end;
		header = "Job success rate - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		str_yaxis = "Job success rate %";
		type = 2;
		this.writeChart(policy_results, type, str_yaxis, header, out_path, filename);
		
		filename = prefix + "-mean_job_completions" + filename_end;
		header = "Mean job completions - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		str_yaxis = "Mean job completions";
		type = 3;
		this.writeChart(policy_results, type, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_job_misses" + filename_end;
		header = "Mean job misses - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		str_yaxis = "Mean job misses";
		type = 4;
		this.writeChart(policy_results, type, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_queue_misses" + filename_end;
		header = "Mean queue misses - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		str_yaxis = "Mean queue misses";
		type = 5;
		this.writeChart(policy_results, type, str_yaxis, header, out_path, filename);

		filename = prefix + "-total_jobs" + filename_end;
		header = "Total jobs - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		str_yaxis = "Total jobs";
		type = 6;
		this.writeChart(policy_results, type, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_dropped_jobs" + filename_end;
		header = "Mean dropped jobs - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		str_yaxis = "Mean dropped jobs";
		type = 7;
		this.writeChart(policy_results, type, str_yaxis, header, out_path, filename);		
	}

	protected void writeChart(Map<String, AbstractTraceResults> policy_results, int type,
			String str_yaxis, String header, String out_path, String filename)
	{
		int appmasters = _params.get_appmasters();
		XYPlot plot = new XYPlot();
		int plot_index = 0;
		for(String policy : _params.get_policies())
		{
			XYSeries series = new XYSeries(policy.toUpperCase());
			series.add(0, 0.0f);
			TraceResults results = (TraceResults)policy_results.get(policy);
			Float[] mean_deadline_misses = results.get_mean_deadline_misses();
			Float[] mean_job_completions = results.get_mean_job_completions();
			Float[] mean_job_misses = results.get_mean_job_misses();
			Float[] mean_queue_misses = results.get_mean_queue_misses();
			
			if(type == 0)
			{
				this.addResultsToSeries(appmasters, series, mean_deadline_misses);
			}
			else if(type == 1)
			{
				for(int I = 0; I < appmasters; I++)
				{
					float job_requests = mean_job_completions[I] + mean_deadline_misses[I];
					series.add(I + 1, (mean_job_completions[I]/job_requests) * 100.0f);
				}
			}
			else if(type == 2)
			{
				for(int I = 0; I < appmasters; I++)
				{
					float job_attempts = mean_job_completions[I] + mean_job_misses[I];
					series.add(I + 1, (mean_job_completions[I]/job_attempts) * 100.0f);
				}
			}
			else if(type == 3)
			{
				this.addResultsToSeries(appmasters, series, mean_job_completions);
			}
			else if(type == 4)
			{
				this.addResultsToSeries(appmasters, series, mean_job_misses);
			}
			else if(type == 5)
			{
				this.addResultsToSeries(appmasters, series, mean_queue_misses);
			}
			else if(type == 6)
			{
				for(int I = 0; I < appmasters; I++)
				{
					series.add(I + 1, mean_job_completions[I] + mean_deadline_misses[I]);
				}
			}
			else if(type == 7)
			{
				Float[] mean_dropped_jobs = results.get_mean_dropped_jobs();
				this.addResultsToSeries(appmasters, series, mean_dropped_jobs);
			}
			this.addDatasetToPlot(plot, series, plot_index);
			plot_index++;
		}
		this.saveChart(str_yaxis, plot, header, out_path, filename);
	}
}