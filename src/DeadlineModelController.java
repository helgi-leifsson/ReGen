import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.SwingWorker;

public class DeadlineModelController extends SwingWorker<Void, Void>
{
	private DeadlineModelParameters _params = null;
	private String _name = null;
	
	public DeadlineModelController(DeadlineModelParameters params, String name)
	{
		this._params = params;
		this._name = name;
	}
	
	protected Void doInBackground()
	{
		this.runAllCombinations(_params);
		return null;
	}
	
	private String currentTime()
	{
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return sdf.format(cal.getTime());
	}
	
	public void runAllCombinations(DeadlineModelParameters params)
	{
		AbstractModelGenerator generator = null;
		AbstractModelCharter charter = null;
		if(params.get_view().isNatjamMode())
		{
			generator = new NatjamModelGenerator(params);
			charter = new NatjamModelCharter(params);
		}
		else
		{
			generator = new DispatchModelGenerator(params);
			charter = new DispatchModelCharter(params);
		}
		generator.createDirectoriesModelsAndBatches();
		params.get_view().appendResults(this.currentTime() + " " + this._name + " generating traces...\n");
		for(String job_arrival_type : params.get_job_arrival_types())
		{
			for(String job_length_type : params.get_job_length_types())
			{
				String path = params.get_base_path() + "\\" + params.get_base_name() + "-" + job_arrival_type + "-" + job_length_type;
				
				List<Process> procs = new ArrayList<Process>();
				for(String policy : params.get_policies())
				{
					try {
						Process p = Runtime.getRuntime().exec("cmd /c start /wait " + path + "\\" + policy + ".bat");
						procs.add(p);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for(Process p : procs)
				{
					try {
						p.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		params.get_view().appendResults(this.currentTime() + " " + this._name + " charting...\n");
		charter.chartCombinedCharts();
		charter.writeTableResults();
		params.get_view().appendResults(this.currentTime() + " " + this._name + "\\" + params.get_base_name() + ".txt and charts written!\n");
	}	
}
