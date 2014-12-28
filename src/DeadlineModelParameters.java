import java.util.List;

//TODO: Make serializable for a save/load feature
public final class DeadlineModelParameters
{
	private String _base_path = null;
	private String _base_name = null;
	private String _compiler_path = null;
	private String _traces_path = null;
	
	private int _appmasters = 0;
	private int _queue_size = 0;
	private int _traces = 0;
	private int _timeunits = 0;

	private float _epsilon = 0.0f;
	private int _job_arrival_burst_interval = 0;
	private int _job_arrival_burst_size = 0;
	private int _job_arrival_nondet_min = 0;
	private int _job_arrival_nondet_max = 0;
	private int _job_arrival_uniform = 0;
	private int _job_arrival_wave_points = 0;
	private int _job_arrival_wave_minimum = 0;
	private int _job_arrival_wave_additional_jobs_per_timeunit = 0;
	private int _job_arrival_ascending_increment = 0;
	private int _job_arrival_ascending_minimum = 0;
	private int _job_arrival_ascending_points = 0;
	private int _job_arrival_descending_decrement = 0;
	private int _job_arrival_descending_maximum = 0;
	private int _job_arrival_descending_points = 0;
	
	private int _high_priority_job_length = 0;
	private int _high_priority_job_probability = 0;
	
	private int _checkpoint_overhead = 0;
	
	private int _job_length_exponential_multiplier = 0;
	private int _job_length_nondet_min = 0;
	private int _job_length_nondet_max = 0;
	private int _job_length_wave_points = 0;
	private int _job_length_wave_minimum = 0;
	private int _job_length_wave_additional_length_per_timeunit = 0;
	private int _job_length_uniform = 0;
	private int _job_length_ascending_increment = 0;
	private int _job_length_ascending_minimum = 0;
	private int _job_length_ascending_points = 0;
	private int _job_length_descending_decrement = 0;
	private int _job_length_descending_maximum = 0;
	private int _job_length_descending_points = 0;
	
	private List<String> _job_arrival_types = null;
	private List<String> _job_length_types = null;
	private List<String> _policies = null;
	
	private DeadlineModelView _view = null;

	public DeadlineModelParameters(String base_path,
		String base_name,
		String compiler_path,
		String traces_path,
		int appmasters,
		int queue_size,
		int traces,
		int timeunits,
		float epsilon,
		int job_arrival_burst_interval,
		int job_arrival_burst_size,
		int job_arrival_nondet_min,
		int job_arrival_nondet_max,
		int job_arrival_uniform,
		int job_arrival_wave_points,
		int job_arrival_wave_minimum,
		int job_arrival_wave_additional_jobs_per_timeunit,
		int job_arrival_ascending_increment,
		int job_arrival_ascending_minimum,
		int job_arrival_ascending_points,
		int job_arrival_descending_decrement,
		int job_arrival_descending_maximum,
		int job_arrival_descending_points,
		int high_priority_job_length,
		int high_priority_job_probability,
		int checkpoint_overhead,
		int job_length_exponential_multiplier,
		int job_length_nondet_min,
		int job_length_nondet_max,
		int job_length_wave_points,
		int job_length_wave_minimum,
		int job_length_wave_additional_length_per_timeunit,
		int job_length_uniform,
		int job_length_ascending_increment,
		int job_length_ascending_minimum,
		int job_length_ascending_points,
		int job_length_descending_decrement,
		int job_length_descending_maximum,
		int job_length_descending_points,
		List<String> job_arrival_types,
		List<String> job_length_types,
		List<String> policies,
		DeadlineModelView view)
	{
		this._base_path = base_path;
		this._base_name = base_name;
		this._compiler_path = compiler_path;
		this._traces_path = traces_path;
		
		this._appmasters = appmasters;
		this._queue_size = queue_size;
		this._traces = traces;
		this._timeunits = timeunits;
	
		this._epsilon = epsilon;

		this._job_arrival_burst_interval = job_arrival_burst_interval;
		this._job_arrival_burst_size = job_arrival_burst_size;
		this._job_arrival_nondet_min = job_arrival_nondet_min;
		this._job_arrival_nondet_max = job_arrival_nondet_max;
		this._job_arrival_uniform = job_arrival_uniform;
		this._job_arrival_wave_points = job_arrival_wave_points;
		this._job_arrival_wave_minimum = job_arrival_wave_minimum;
		this._job_arrival_wave_additional_jobs_per_timeunit = job_arrival_wave_additional_jobs_per_timeunit;
		this._job_arrival_ascending_increment = job_arrival_ascending_increment;
		this._job_arrival_ascending_minimum = job_arrival_ascending_minimum; 
		this._job_arrival_ascending_points = job_arrival_ascending_points;
		this._job_arrival_descending_decrement = job_arrival_descending_decrement;
		this._job_arrival_descending_maximum = job_arrival_descending_maximum;
		this._job_arrival_descending_points = job_arrival_descending_points;
		
		this._high_priority_job_length = high_priority_job_length;
		this._high_priority_job_probability = high_priority_job_probability;
		
		this._checkpoint_overhead = checkpoint_overhead;
		
		this._job_length_exponential_multiplier = job_length_exponential_multiplier;
		this._job_length_nondet_min = job_length_nondet_min;
		this._job_length_nondet_max = job_length_nondet_max;
		this._job_length_wave_points = job_length_wave_points;
		this._job_length_wave_minimum = job_length_wave_minimum;
		this._job_length_wave_additional_length_per_timeunit = job_length_wave_additional_length_per_timeunit;
		this._job_length_uniform = job_length_uniform;
		this._job_length_ascending_increment = job_length_ascending_increment;
		this._job_length_ascending_minimum = job_length_ascending_minimum;
		this._job_length_ascending_points = job_length_ascending_points;
		this._job_length_descending_decrement = job_length_descending_decrement;
		this._job_length_descending_maximum = job_length_descending_maximum;
		this._job_length_descending_points = job_length_descending_points;
		
		this._job_arrival_types = job_arrival_types;
		this._job_length_types = job_length_types;
		this._policies = policies;
		
		this._view = view;
	}

	public String get_base_path() {
		return _base_path;
	}

	public String get_base_name() {
		return _base_name;
	}

	public String get_compiler_path() {
		return _compiler_path;
	}

	public String get_traces_path() {
		return _traces_path;
	}

	public int get_appmasters() {
		return _appmasters;
	}

	public int get_queue_size() {
		return _queue_size;
	}

	public int get_traces() {
		return _traces;
	}

	public int get_timeunits() {
		return _timeunits;
	}

	public float get_epsilon() {
		return _epsilon;
	}

	public int get_job_arrival_burst_interval() {
		return _job_arrival_burst_interval;
	}

	public int get_job_arrival_burst_size() {
		return _job_arrival_burst_size;
	}

	public int get_job_arrival_nondet_min() {
		return _job_arrival_nondet_min;
	}

	public int get_job_arrival_nondet_max() {
		return _job_arrival_nondet_max;
	}

	public int get_job_arrival_uniform() {
		return _job_arrival_uniform;
	}

	public int get_job_arrival_wave_points() {
		return _job_arrival_wave_points;
	}

	public int get_job_arrival_wave_minimum() {
		return _job_arrival_wave_minimum;
	}

	public int get_job_arrival_wave_additional_jobs_per_timeunit() {
		return _job_arrival_wave_additional_jobs_per_timeunit;
	}

	public int get_job_length_exponential_multiplier() {
		return _job_length_exponential_multiplier;
	}

	public int get_job_length_nondet_min() {
		return _job_length_nondet_min;
	}

	public int get_job_length_nondet_max() {
		return _job_length_nondet_max;
	}

	public int get_job_length_wave_points() {
		return _job_length_wave_points;
	}

	public int get_job_length_wave_minimum() {
		return _job_length_wave_minimum;
	}

	public int get_job_length_wave_additional_length_per_timeunit() {
		return _job_length_wave_additional_length_per_timeunit;
	}

	public int get_job_length_uniform() {
		return _job_length_uniform;
	}

	public List<String> get_job_arrival_types() {
		return _job_arrival_types;
	}

	public List<String> get_job_length_types() {
		return _job_length_types;
	}

	public List<String> get_policies() {
		return _policies;
	}

	public DeadlineModelView get_view() {
		return _view;
	}

	public int get_job_arrival_ascending_increment() {
		return _job_arrival_ascending_increment;
	}

	public int get_job_arrival_ascending_minimum() {
		return _job_arrival_ascending_minimum;
	}

	public int get_job_arrival_ascending_points() {
		return _job_arrival_ascending_points;
	}

	public int get_job_length_ascending_increment() {
		return _job_length_ascending_increment;
	}

	public int get_job_length_ascending_minimum() {
		return _job_length_ascending_minimum;
	}

	public int get_job_length_ascending_points() {
		return _job_length_ascending_points;
	}

	public int get_job_arrival_descending_decrement() {
		return _job_arrival_descending_decrement;
	}

	public int get_job_arrival_descending_maximum() {
		return _job_arrival_descending_maximum;
	}

	public int get_job_arrival_descending_points() {
		return _job_arrival_descending_points;
	}

	public int get_job_length_descending_decrement() {
		return _job_length_descending_decrement;
	}

	public int get_job_length_descending_maximum() {
		return _job_length_descending_maximum;
	}

	public int get_job_length_descending_points() {
		return _job_length_descending_points;
	}

	public int get_high_priority_job_probability() {
		return _high_priority_job_probability;
	}

	public int get_high_priority_job_length() {
		return _high_priority_job_length;
	}

	public int get_checkpoint_overhead() {
		return _checkpoint_overhead;
	}	
}
