import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;

public class NatjamModelCharter extends AbstractModelCharter
{
	public enum GraphType
	{
		MEAN_PRODUCTION_JOB_COMPLETIONS,
		MEAN_PRODUCTION_JOB_MISSES,
		MEAN_PRODUCTION_QUEUE_MISSES,
		MEAN_PRODUCTION_DEADLINE_MISSES,
		MEAN_RESEARCH_JOB_COMPLETIONS,
		MEAN_RESEARCH_JOB_MISSES,
		MEAN_RESEARCH_QUEUE_MISSES,
		MEAN_RESEARCH_DEADLINE_MISSES,
		MEAN_DEADLINE_MISSES,
		MEAN_JOB_COMPLETIONS,
		MEAN_DROPPED_JOBS,
		JOB_REQUEST_SUCCESS_RATE,
		JOB_SUCCESS_RATE,
		MEAN_MARGIN,
		MEAN_CHECKPOINT_OVERFLOW,
		MEAN_CHECKPOINTS,
		MEAN_CHECKPOINT_QUEUE_MISSES,
		MEAN_TOTAL_JOBS
	}

	public final class TraceResults extends AbstractTraceResults
	{
		private Float[] _mean_production_job_completions = null;
		private Float[] _mean_production_job_misses = null;
		private Float[] _mean_production_queue_misses = null;
		private Float[] _mean_production_deadline_misses = null;
		private Float[] _mean_research_job_completions = null;
		private Float[] _mean_research_job_misses = null;
		private Float[] _mean_research_queue_misses = null;
		private Float[] _mean_research_deadline_misses = null;
		private Float[] _mean_deadline_misses = null;
		private Float[] _mean_job_completions = null;
		private Float[] _mean_dropped_jobs = null;
		private Float[] _mean_margin = null;
		private Float[] _mean_checkpoint_overflow = null;
		private Float[] _mean_checkpoints = null;
		private Float[] _mean_checkpoint_queue_misses = null;
		private Float[] _mean_total_jobs = null;

		public TraceResults(
				Float[] mean_production_job_completions,
				Float[] mean_production_job_misses,
				Float[] mean_production_queue_misses,
				Float[] mean_production_deadline_misses,
				Float[] mean_research_job_completions,
				Float[] mean_research_job_misses,
				Float[] mean_research_queue_misses,
				Float[] mean_research_deadline_misses,
				Float[] mean_deadline_misses,
				Float[] mean_job_completions,
				Float[] mean_dropped_jobs,
				Float[] mean_margin,
				Float[] mean_checkpoint_overflow,
				Float[] mean_checkpoints,
				Float[] mean_checkpoint_queue_misses,
				Float[] mean_total_jobs)
		{
			this._mean_production_job_completions = mean_production_job_completions;
			this._mean_production_job_misses = mean_production_job_misses;
			this._mean_production_queue_misses = mean_production_queue_misses;
			this._mean_production_deadline_misses = mean_production_deadline_misses;
			this._mean_research_job_completions = mean_research_job_completions;
			this._mean_research_job_misses = mean_research_job_misses;
			this._mean_research_queue_misses = mean_research_queue_misses;
			this._mean_research_deadline_misses = mean_research_deadline_misses;
			this._mean_deadline_misses = mean_deadline_misses;
			this._mean_job_completions = mean_job_completions;
			this._mean_dropped_jobs = mean_dropped_jobs;
			this._mean_margin = mean_margin;
			this._mean_checkpoint_overflow = mean_checkpoint_overflow;
			this._mean_checkpoints = mean_checkpoints;
			this._mean_checkpoint_queue_misses = mean_checkpoint_queue_misses;
			this._mean_total_jobs = mean_total_jobs;
		}

		public Float[] get_mean_production_job_completions() {
			return _mean_production_job_completions;
		}

		public Float[] get_mean_production_job_misses() {
			return _mean_production_job_misses;
		}

		public Float[] get_mean_production_queue_misses() {
			return _mean_production_queue_misses;
		}

		public Float[] get_mean_production_deadline_misses() {
			return _mean_production_deadline_misses;
		}

		public Float[] get_mean_research_job_completions() {
			return _mean_research_job_completions;
		}

		public Float[] get_mean_research_job_misses() {
			return _mean_research_job_misses;
		}

		public Float[] get_mean_research_queue_misses() {
			return _mean_research_queue_misses;
		}

		public Float[] get_mean_research_deadline_misses() {
			return _mean_research_deadline_misses;
		}

		public Float[] get_mean_deadline_misses() {
			return _mean_deadline_misses;
		}

		public Float[] get_mean_job_completions() {
			return _mean_job_completions;
		}

		public Float[] get_mean_dropped_jobs() {
			return _mean_dropped_jobs;
		}

		public Float[] get_mean_margin() {
			return _mean_margin;
		}

		public Float[] get_mean_checkpoint_overflow() {
			return _mean_checkpoint_overflow;
		}

		public Float[] get_mean_checkpoints() {
			return _mean_checkpoints;
		}

		public Float[] get_mean_checkpoint_queue_misses() {
			return _mean_checkpoint_queue_misses;
		}

		public Float[] get_mean_total_jobs() {
			return _mean_total_jobs;
		}

	}

	public NatjamModelCharter(DeadlineModelParameters params) {
		super(params);
		// TODO Auto-generated constructor stub
	}

	//TODO: thread for each job arrival type/length combo
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
					Float[] mean_production_job_completions = new Float[appmasters];
					Float[] mean_production_job_misses = new Float[appmasters];
					Float[] mean_production_queue_misses = new Float[appmasters];
					Float[] mean_production_deadline_misses = new Float[appmasters];
					Float[] mean_research_job_completions = new Float[appmasters];
					Float[] mean_research_job_misses = new Float[appmasters];
					Float[] mean_research_queue_misses = new Float[appmasters];
					Float[] mean_research_deadline_misses = new Float[appmasters];
					Float[] mean_deadline_misses = new Float[appmasters];
					Float[] mean_job_completions = new Float[appmasters];
					Float[] mean_dropped_jobs = new Float[appmasters];
					Float[] mean_margin = new Float[appmasters];
					Float[] mean_checkpoint_overflow = new Float[appmasters];
					Float[] mean_checkpoints = new Float[appmasters];
					Float[] mean_checkpoint_queue_misses = new Float[appmasters];
					Float[] mean_total_jobs = new Float[appmasters];
					for(int I = 0; I < appmasters; I++)
					{
						String traces_path = _params.get_traces_path() + "\\" + policy + (I + 1) + "-" + job_arrival_type + "-" + job_length_type + "\\traces.xml";
						NatjamTracesParser parser = new NatjamTracesParser();
						parser.parseDocument(traces_path);
						
						float traces = (float)parser.get_traces();
						mean_production_job_completions[I] = (float)parser.get_production_job_completions()/traces;
						mean_production_job_misses[I] = (float)parser.get_production_job_misses()/traces;
						mean_production_queue_misses[I] = (float)parser.get_production_queue_misses()/traces;
						mean_production_deadline_misses[I] = (float)parser.get_production_deadline_misses()/traces;
						mean_research_job_completions[I] = (float)parser.get_research_job_completions()/traces;
						mean_research_job_misses[I] = (float)parser.get_research_job_misses()/traces;
						mean_research_queue_misses[I] = (float)parser.get_research_queue_misses()/traces;
						mean_research_deadline_misses[I] = (float)parser.get_research_deadline_misses()/traces;
						mean_deadline_misses[I] = (float)parser.get_deadline_misses()/traces;
						mean_job_completions[I] = (float)parser.get_job_completions()/traces;
						mean_dropped_jobs[I] = (float)parser.get_dropped_jobs()/traces;
						mean_margin[I] = (float)parser.get_margin()/traces;
						mean_checkpoint_overflow[I] = (float)parser.get_checkpoint_overflow()/traces;
						mean_checkpoints[I] = (float)parser.get_checkpoints()/traces;
						mean_checkpoint_queue_misses[I] = (float)parser.get_checkpoint_queue_misses()/traces;
						mean_total_jobs[I] = (float)parser.get_total_jobs()/traces;

						this.addToSum(_sum_job_completions, policy, parser.get_job_completions());
						this.addToSum(_sum_job_misses, policy, parser.get_job_misses());
						this.addToSum(_sum_deadline_misses, policy, parser.get_deadline_misses());
					}
					AbstractTraceResults results = new TraceResults(
							mean_production_job_completions,
							mean_production_job_misses,
							mean_production_queue_misses,
							mean_production_deadline_misses,
							mean_research_job_completions,
							mean_research_job_misses,
							mean_research_queue_misses,
							mean_research_deadline_misses,
							mean_deadline_misses,
							mean_job_completions,
							mean_dropped_jobs,
							mean_margin,
							mean_checkpoint_overflow,
							mean_checkpoints,
							mean_checkpoint_queue_misses,
							mean_total_jobs);
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

		String job_type = "production";
		
		filename = prefix + "-mean_" + job_type + "_job_completions" + filename_end;
		str_yaxis = "Mean " + job_type + " job completions";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_PRODUCTION_JOB_COMPLETIONS, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_" + job_type + "_job_misses" + filename_end;
		str_yaxis = "Mean " + job_type + " job misses";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_PRODUCTION_JOB_MISSES, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_" + job_type + "_queue_misses" + filename_end;
		str_yaxis = "Mean " + job_type + " queue misses";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_PRODUCTION_QUEUE_MISSES, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_" + job_type + "_deadline_misses" + filename_end;
		str_yaxis = "Mean " + job_type + " deadline misses";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_PRODUCTION_DEADLINE_MISSES, str_yaxis, header, out_path, filename);

		job_type = "research";

		filename = prefix + "-mean_" + job_type + "_job_completions" + filename_end;
		str_yaxis = "Mean " + job_type + " job completions";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_RESEARCH_JOB_COMPLETIONS, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_" + job_type + "_job_misses" + filename_end;
		str_yaxis = "Mean " + job_type + " job misses";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_RESEARCH_JOB_MISSES, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_" + job_type + "_queue_misses" + filename_end;
		str_yaxis = "Mean " + job_type + " queue misses";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_RESEARCH_QUEUE_MISSES, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_" + job_type + "_deadline_misses" + filename_end;
		str_yaxis = "Mean " + job_type + " deadline misses";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_RESEARCH_DEADLINE_MISSES, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_deadline_misses" + filename_end;
		str_yaxis = "Mean deadline misses";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_DEADLINE_MISSES, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_job_completions" + filename_end;
		str_yaxis = "Mean job completions";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_JOB_COMPLETIONS, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_dropped_jobs" + filename_end;
		str_yaxis = "Mean dropped jobs";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_DROPPED_JOBS, str_yaxis, header, out_path, filename);

		filename = prefix + "-job_request_success_rate" + filename_end;
		str_yaxis = "Job request success rate %";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.JOB_REQUEST_SUCCESS_RATE, str_yaxis, header, out_path, filename);

		filename = prefix + "-job_success_rate" + filename_end;
		str_yaxis = "Job success rate %";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.JOB_SUCCESS_RATE, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_margin" + filename_end;
		str_yaxis = "Mean margin";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_MARGIN, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_checkpoint_overflow" + filename_end;
		str_yaxis = "Mean checkpoint overflow";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_CHECKPOINT_OVERFLOW, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_checkpoints" + filename_end;
		str_yaxis = "Mean checkpoints";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_CHECKPOINTS, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_checkpoint_queue_misses" + filename_end;
		str_yaxis = "Mean checkpoint queue misses";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_CHECKPOINT_QUEUE_MISSES, str_yaxis, header, out_path, filename);

		filename = prefix + "-mean_total_jobs" + filename_end;
		str_yaxis = "Mean total jobs";
		header = str_yaxis + " - " + traces + " traces, " + timeunits + " timeunits" + header_end;
		this.writeChart(policy_results, GraphType.MEAN_TOTAL_JOBS, str_yaxis, header, out_path, filename);

	}

	private void writeChart(Map<String, AbstractTraceResults> policy_results, GraphType type,
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
			Float[] mean_job_completions = results.get_mean_job_completions();
			Float[] mean_deadline_misses = results.get_mean_deadline_misses();
			Float[] mean_production_job_misses = results.get_mean_production_job_misses();
			Float[] mean_research_job_misses = results.get_mean_research_job_misses();
			Float[] mean_total_jobs = results.get_mean_total_jobs();
			Float[] mean_checkpoint_overflow = results.get_mean_checkpoint_overflow();
			Float[] mean_checkpoint_queue_misses = results.get_mean_checkpoint_queue_misses();

			switch(type)
			{
			case MEAN_DEADLINE_MISSES:
				this.addResultsToSeries(appmasters, series, mean_deadline_misses);
				break;
				
			case MEAN_DROPPED_JOBS:
				Float[] mean_dropped_jobs = results.get_mean_dropped_jobs();
				this.addResultsToSeries(appmasters, series, mean_dropped_jobs);
				break;
				
			case MEAN_JOB_COMPLETIONS:
				this.addResultsToSeries(appmasters, series, mean_job_completions);
				break;

			case MEAN_PRODUCTION_DEADLINE_MISSES:
				Float[] mean_production_deadline_misses = results.get_mean_production_deadline_misses();
				this.addResultsToSeries(appmasters, series, mean_production_deadline_misses);
				break;
				
			case MEAN_PRODUCTION_JOB_COMPLETIONS:
				Float[] mean_production_job_completions = results.get_mean_production_job_completions();
				this.addResultsToSeries(appmasters, series, mean_production_job_completions);
				break;

			case MEAN_PRODUCTION_JOB_MISSES:
				this.addResultsToSeries(appmasters, series, mean_production_job_misses);
				break;

			case MEAN_PRODUCTION_QUEUE_MISSES:
				Float[] mean_production_queue_misses = results.get_mean_production_queue_misses();
				this.addResultsToSeries(appmasters, series, mean_production_queue_misses);
				break;

			case MEAN_RESEARCH_DEADLINE_MISSES:
				Float[] mean_research_deadline_misses = results.get_mean_research_deadline_misses();
				this.addResultsToSeries(appmasters, series, mean_research_deadline_misses);
				break;

			case MEAN_RESEARCH_JOB_COMPLETIONS:
				Float[] mean_research_job_completions = results.get_mean_research_job_completions();
				this.addResultsToSeries(appmasters, series, mean_research_job_completions);
				break;

			case MEAN_RESEARCH_JOB_MISSES:
				this.addResultsToSeries(appmasters, series, mean_research_job_misses);
				break;

			case MEAN_RESEARCH_QUEUE_MISSES:
				Float[] mean_research_queue_misses = results.get_mean_research_queue_misses();
				this.addResultsToSeries(appmasters, series, mean_research_queue_misses);
				break;

			case JOB_REQUEST_SUCCESS_RATE:
				for(int I = 0; I < appmasters; I++)
				{
					float job_requests = mean_total_jobs[I];//mean_job_completions[I] + mean_deadline_misses[I];
					series.add(I + 1, (mean_job_completions[I]/job_requests) * 100.0f);
				}
				break;

			case JOB_SUCCESS_RATE:
				for(int I = 0; I < appmasters; I++)
				{
					float job_attempts = mean_job_completions[I] + mean_production_job_misses[I] + mean_research_job_misses[I] +
							mean_checkpoint_overflow[I] + mean_checkpoint_queue_misses[I];
					series.add(I + 1, (mean_job_completions[I]/job_attempts) * 100.0f);
				}
				break;
				
			case MEAN_MARGIN:
				Float[] mean_margin = results.get_mean_margin();
				this.addResultsToSeries(appmasters, series, mean_margin);
				break;
				
			case MEAN_CHECKPOINT_OVERFLOW:
				this.addResultsToSeries(appmasters, series, mean_checkpoint_overflow);
				break;

			case MEAN_CHECKPOINTS:
				Float[] mean_checkpoints = results.get_mean_checkpoints();
				this.addResultsToSeries(appmasters, series, mean_checkpoints);
				break;

			case MEAN_CHECKPOINT_QUEUE_MISSES:
				this.addResultsToSeries(appmasters, series, mean_checkpoint_queue_misses);
				break;

			case MEAN_TOTAL_JOBS:
				this.addResultsToSeries(appmasters, series, mean_total_jobs);
				break;

			}
			this.addDatasetToPlot(plot, series, plot_index);
			plot_index++;
		}
		this.saveChart(str_yaxis, plot, header, out_path, filename);
	}
}
