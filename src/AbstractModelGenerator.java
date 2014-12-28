import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public abstract class AbstractModelGenerator {

	protected DeadlineModelParameters _params = null;

	public AbstractModelGenerator(DeadlineModelParameters params)
	{
		super();
		this._params = params;
	}

	protected String generateModelStart(int appmasters) {
		String start =
				"reactiveclass ResourceManager(20)\n" +
				"{\n" +
				"	knownrebecs\n" +
				"	{\n" +
				"		AppMaster am1;\n";
	
		for(int I = 2; I <= appmasters; I++)
		{
			start +=
				"		AppMaster am" + I + ";\n";
		}
		
		start +=
				"	}\n\n";
		return start;
	}

	protected String generateMain(int ams) {
		String main =
				"main\n" +
				"{\n" +
				"	ResourceManager rm(am1";
		
		for(int I = 2; I <= ams; I++)
		{
			main +=
				", am" + I;
		}
		main += "):();\n";
		
		for(int I = 1; I <= ams; I++)
		{
			main +=
				"	AppMaster am" + I + "(rm):();\n";
		}
		main +=	"}\n";
		
		return main;
	}

	public void createDirectoriesModelsAndBatches() {
		File base_dir = new File(this._params.get_base_path());
		base_dir.mkdirs();
		File traces_dir = new File(this._params.get_traces_path());
		traces_dir.mkdirs();
		for(String job_arrival_type : this._params.get_job_arrival_types())
		{
			for(String job_length_type : this._params.get_job_length_types())
			{
				String path = this._params.get_base_path() + "\\" + this._params.get_base_name() + "-" + job_arrival_type + "-" + job_length_type;
				for(String policy : this._params.get_policies())
				{
					File out_dir = new File(path);
					out_dir.mkdirs();
					generateModels(job_arrival_type, job_length_type, policy, path);
					writeModelBatch(job_arrival_type, job_length_type, policy, path);
					
					File trace_dir = null;
					for(int I = 1; I <= this._params.get_appmasters(); I++)
					{
						trace_dir = new File(this._params.get_traces_path() + "\\" + policy + I + "-" + job_arrival_type + "-" + job_length_type);
						trace_dir.mkdirs();
					}
				}
			}
		}
	}

	protected abstract void generateModels(String job_arrival_type, String job_length_type, String policy, String path);
	
	//TODO: add option for how many appmasters per batch file
	protected void writeModelBatch(String job_arrival_type, String job_length_type, String policy, String path)
	{
		Writer writer = null;
		String drive_letter = path.substring(0, 2);
		String model_run = drive_letter + "\n\n";
		String compiler_path = this._params.get_compiler_path();
		for(int I = 1; I <= this._params.get_appmasters(); I++)
		{
			String traces_path = this._params.get_traces_path() + "\\" + policy + I + "-" + job_arrival_type + "-" + job_length_type;
			model_run +=
				"del " + traces_path + "\\*.* /Q\n" +
				"cd " + path + "\n" +
				"java -jar " + compiler_path + "\\" + "rmc-2.5.0-SNAPSHOT.jar -s yarn-deadline-" + policy + "-" + I + "jobs-traces-" + job_arrival_type + "-" + job_length_type + "-" + "epsilon" + ".rebeca -v 2.1 -e TimedRebeca -o " + traces_path + " --tracegenerator\n" +
				"copy " + compiler_path +  "\\" + "\"g++.exe\" " + traces_path + "\n" +
				"copy " + compiler_path + "\\" + "cygiconv-2.dll " + traces_path + "\n" +
				"copy " + compiler_path + "\\" + "cygintl-3.dll " + traces_path + "\n" +
				"copy " + compiler_path + "\\" + "cygwin1.dll " + traces_path + "\n" +
				"cd " + traces_path + "\n" +
				"g++ *.cpp -w -o execute\n" +
				"execute -o traces.xml -g " + this._params.get_traces() + " -l " + (this._params.get_timeunits() - 2) + "\n\n"; //The Rebeca Model Checker is off by two timeunits
		}
		model_run += "exit";
		try {
			writer = new BufferedWriter(new OutputStreamWriter( 
					new FileOutputStream(path + "\\" + policy + ".bat"), "utf-8"));
			writer.write(model_run);
			writer.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void writeFile(String file, String contents)
	{
		Writer writer;
		try {
			writer = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(file), "utf-8"));
			writer.write(contents);
			writer.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String jobArrivalBursty() {
		String result =
				"		int jobs = 0;\n" +
				"		if(now % " + this._params.get_job_arrival_burst_interval() + " == 0)\n" +
				"		{\n" +
				"			jobs = " + this._params.get_job_arrival_burst_size() + ";\n" +
				"		}\n";
		return result;
	}

	private String jobSizeExponential(String padding) {
		String result =
				padding + "int job_length = now * " + this._params.get_job_length_exponential_multiplier() + ";\n";
		return result;
	}

	private String jobNondet(int min, int max, String variable_name, String padding) {
		String result =
				padding + "int " + variable_name + " = ?(" + min;
		for(int I = min + 1; I <= max; I++)
		{
			result +=
					", " + I;
		}
		result +=
				");\n";
		return result;
	}

	private String jobUniform(int uniform, String variable_name, String padding) {
		String result =
				padding + "int " + variable_name + " = " + uniform + ";\n";
		return result;
	}

	private String jobWave(int points, int min, int additional_jobs_per_timeunit, String variable_name,
			String padding) {
				/*String result = 
						padding + "int[" + points + "] " + variable_name + "_wave;\n";
				int jobs = min;
				int peak = points/2;
				for(int I = 0; I < points; I++)
				{
					result +=
							padding + variable_name + "_wave[" + I + "] = " + jobs + ";\n";
					if(I < peak)
					{
						jobs += additional_jobs_per_timeunit;
					}
					else
					{
						jobs -= additional_jobs_per_timeunit;
					}
				}*/
				//result +=
				String result =
						padding + "int " + variable_name + "_cur_wave_point = now % " + points + ";\n" +
						padding + "int " + variable_name + " = " + variable_name + "_wave[" + variable_name + "_cur_wave_point];\n";
				return result;
			}

	private String jobAscending(int points, int min, int increment,
			String variable_name, String padding) {
				/*String result =
						padding + "int[" + points + "] " + variable_name + "_asc;\n";
				int jobs = min;
				for(int I = 0; I < points; I++)
				{
					result +=
							padding + variable_name + "_asc[" + I + "] = " + jobs + ";\n";
					jobs += increment;
				}*/
				//move everything above this so that it's called right after job arrival
				//result +=
				String result =
						padding + "int " + variable_name + "_cur_asc_point = now % " + points + ";\n" +
						padding + "int " + variable_name + " = " + variable_name + "_asc[" + variable_name + "_cur_asc_point];\n";
				return result;
			}

	private String jobDescending(int points, int max, int decrement,
			String variable_name, String padding) {
				/*String result =
						padding + "int[" + points + "] " + variable_name + "_desc;\n";
				int jobs = max;
				for(int I = 0; I < points; I++)
				{
					result +=
							padding + variable_name + "_desc[" + I + "] = " + jobs + ";\n";
					jobs -= decrement;
					if(jobs < 0)
					{
						jobs = 0;
					}
				}*/
				//result +=
				String result =
						padding + "int " + variable_name + "_cur_desc_point = now % " + points + ";\n" +
						padding + "int " + variable_name + " = " + variable_name + "_desc[" + variable_name + "_cur_desc_point];\n";
				return result;
			}

	protected abstract String queueInsertion(String job_length);
	
	//TODO: Change the if-elses to switches
	//TODO: Recreating the arrays with wave, ascending and descending job length patterns
	//within the queue loop is less than ideal. Refactor so they are generated at most only once per
	//time unit. Done. Test and refactor. Try and move this even further out, into the constructor
	//or even as a global.
	protected String getQueueInsertion(String job_arrival_type, String job_length_type) {
		String jobs_variable_name = "jobs";
		String job_length_variable_name = "job_length";
		String job_arrival = null;
		String padding = 
				"		";
		if(job_arrival_type.contains("bursty"))
		{
			job_arrival = this.jobArrivalBursty();
		}
		else if(job_arrival_type.contains("nondet"))
		{
			job_arrival = this.jobNondet(this._params.get_job_arrival_nondet_min(),
					this._params.get_job_arrival_nondet_max(), jobs_variable_name, padding);
		}
		else if(job_arrival_type.contains("uniform"))
		{
			job_arrival = this.jobUniform(this._params.get_job_arrival_uniform(), jobs_variable_name, padding);
		}
		else if(job_arrival_type.contains("wave"))
		{
			job_arrival = this.jobWave(this._params.get_job_arrival_wave_points(),
					this._params.get_job_arrival_wave_minimum(),
					this._params.get_job_arrival_wave_additional_jobs_per_timeunit(), jobs_variable_name, padding);
		}
		else if(job_arrival_type.contains("ascending"))
		{
			job_arrival = this.jobAscending(this._params.get_job_arrival_ascending_points(),
					this._params.get_job_arrival_ascending_minimum(),
					this._params.get_job_arrival_ascending_increment(), jobs_variable_name, padding);
		}
		else if(job_arrival_type.contains("descending"))
		{
			job_arrival = this.jobDescending(this._params.get_job_arrival_descending_points(),
					this._params.get_job_arrival_descending_maximum(),
					this._params.get_job_arrival_descending_decrement(), jobs_variable_name, padding);
		}
		
		String job_length = null;
		padding =
				"				";
		if(job_length_type.contains("exponential"))
		{
			job_length = this.jobSizeExponential(padding);
		}
		else if(job_length_type.contains("nondet"))
		{
			job_length = this.jobNondet(this._params.get_job_length_nondet_min(),
					this._params.get_job_length_nondet_max(), job_length_variable_name, padding);
		}
		else if(job_length_type.contains("uniform"))
		{
			job_length = this.jobUniform(this._params.get_job_length_uniform(), job_length_variable_name, padding);
		}
		else if(job_length_type.contains("wave"))
		{
			job_length = this.jobWave(this._params.get_job_length_wave_points(),
					this._params.get_job_length_wave_minimum(),
					this._params.get_job_length_wave_additional_length_per_timeunit(), job_length_variable_name, padding);
		}
		else if(job_length_type.contains("ascending"))
		{
			job_length = this.jobAscending(this._params.get_job_length_ascending_points(),
					this._params.get_job_length_ascending_minimum(),
					this._params.get_job_length_ascending_increment(), job_length_variable_name, padding);
		}
		else if(job_length_type.contains("descending"))
		{
			job_length = this.jobDescending(this._params.get_job_length_descending_points(),
					this._params.get_job_length_descending_maximum(),
					this._params.get_job_length_descending_decrement(), job_length_variable_name, padding);
		}
		
		String queue_insertion = this.queueInsertion(job_length);
		if(job_length_type.contains("ascending"))
		{
			String result =
					"		" + "int[" + this._params.get_job_length_ascending_points() + "] " + job_length_variable_name + "_asc;\n";
			int jobs = this._params.get_job_length_ascending_minimum();
			for(int I = 0; I < this._params.get_job_length_ascending_points(); I++)
			{
				result +=
						"		" + job_length_variable_name + "_asc[" + I + "] = " + jobs + ";\n";
				jobs += this._params.get_job_length_ascending_increment();
			}
			job_arrival += result;
		}
		else if(job_length_type.contains("descending"))
		{
			String result =
					"		" + "int[" + this._params.get_job_length_descending_points() + "] " + job_length_variable_name + "_desc;\n";
			int jobs = this._params.get_job_length_descending_maximum();
			for(int I = 0; I < this._params.get_job_length_descending_points(); I++)
			{
				result +=
						"		" + job_length_variable_name + "_desc[" + I + "] = " + jobs + ";\n";
				jobs -= this._params.get_job_length_descending_decrement();
				if(jobs < 0)
				{
					jobs = 0;
				}
			}
			job_arrival += result;
		}
		else if(job_length_type.contains("wave"))
		{
			String result = 
					"		" + "int[" + this._params.get_job_length_wave_points() + "] " + job_length_variable_name + "_wave;\n";
			int jobs = this._params.get_job_length_wave_minimum();
			int peak = this._params.get_job_length_wave_points()/2;
			for(int I = 0; I < this._params.get_job_length_wave_points(); I++)
			{
				result +=
						"		" + job_length_variable_name + "_wave[" + I + "] = " + jobs + ";\n";
				if(I < peak)
				{
					jobs += this._params.get_job_length_wave_additional_length_per_timeunit();
				}
				else
				{
					jobs -= this._params.get_job_length_wave_additional_length_per_timeunit();
				}
			}
			job_arrival += result;
		}
		return job_arrival + queue_insertion;
	}
}