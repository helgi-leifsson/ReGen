import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class DispatchModelGenerator extends AbstractModelGenerator
{
	public DispatchModelGenerator(DeadlineModelParameters params)
	{
		super(params);		
	}
	
	private String generateStatevars(int ams, boolean priority_queue, int queue_size)
	{
		String statevars = null;
		statevars =
				"	statevars\n" +
				"	{\n" +
				"		int FREE;\n" +
				"		int BUSY;\n" +
				"		int EMPTY;\n";
		
		for(int I = 1; I <= ams; I++)
		{
			statevars +=
				"		int appMaster" + I + ";\n";
		}
		
		statevars +=
				"		int m_queue_misses;\n" +
				"		int m_update_misses;\n" +
				"		int m_jobs_complete;\n" +
				"		int m_dropped_jobs;\n" +
				"		int[" + queue_size + "] deadline_queue;\n" +
				"		int[" + queue_size + "] job_length_queue;\n" +
				"		int QUEUE_SIZE;\n" +
				"		int INF;\n";
		
		if(priority_queue)
		{
			statevars +=
				"		int[" + queue_size + "] priority;\n";
		}
		statevars +=
				"	}\n\n";
		return statevars;
	}
	
	private String generateConstructor(int ams, boolean priority_queue, int queue_size)
	{
		String constructor = null;
		constructor =
				"	ResourceManager()\n" +
				"	{\n" +
				"		FREE = 1;\n" +
				"		BUSY = 0;\n" +
				"		EMPTY = -9999;\n";
		
		for(int I = 1; I <= ams; I++)
		{
			constructor +=
				"		appMaster" + I + " = FREE;\n";
		}

		constructor +=
				"		m_queue_misses = 0;\n" +
				"		m_update_misses = 0;\n" +
				"		m_jobs_complete = 0;\n" +
				"		m_dropped_jobs = 0;\n" +
				"		QUEUE_SIZE = " + queue_size + ";\n" +
				"		INF = 9999;\n";
		for(int I = 0; I < queue_size; I++)
		{
			constructor +=
				"		deadline_queue[" + I + "] = EMPTY;\n" +
				"		job_length_queue[" + I + "] = EMPTY;\n";
			
			if(priority_queue)
			{
				constructor +=
				"		priority[" + I + "] = EMPTY;\n";
			}
		}			
		constructor +=
				"		self.checkQueue();\n" +
				"	}\n\n";
		return constructor;
	}

	private String edfPolicy(int ams, String job_arrival_type, String job_length_type)
	{
		String checkQueue =
				"	msgsrv checkQueue()\n" +
				"	{\n" +
				"		int I = 0;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(deadline_queue[I] != EMPTY)\n" +
				"			{\n" +
				"				deadline_queue[I]--;\n" +
				"				if(deadline_queue[I] <= 0 && deadline_queue[I] != EMPTY)\n" +
				"				{\n" +
				"					m_queue_misses++;\n" +
				"					deadline_queue[I] = EMPTY;\n" +
				"					job_length_queue[I] = EMPTY;\n" +
				"				}\n" +
				"			}\n" +
				"			I++;\n" +
				"		}\n";
		for(int I = 1; I <= ams; I++)
		{
			checkQueue += 
				"		if(appMaster" + I + " == FREE)\n" +
				"		{\n" +
				"			I = 0;\n" +
				"			int earliest_deadline = INF;\n" +
				"			int edf = EMPTY;\n" +
				"			while(I < QUEUE_SIZE)\n" +
				"			{\n" +
				"				if(deadline_queue[I] < earliest_deadline && deadline_queue[I] != EMPTY)\n" +
				"				{\n" +
				"					earliest_deadline = deadline_queue[I];\n" +
				"					edf = I;\n" +
				"				}\n" +
				"				I++;\n" +
				"			}\n" +
				"			if(edf != EMPTY && earliest_deadline != INF)\n" +
				"			{\n" +
				"				appMaster" + I + " = BUSY;\n" +
				"				am" + I + ".runJob(deadline_queue[edf], job_length_queue[edf]);\n" +
				"				deadline_queue[edf] = EMPTY;\n" +
				"				job_length_queue[edf] = EMPTY;\n" +
				"			}\n" +
				"		}\n";
		}
		checkQueue += this.getQueueInsertion(job_arrival_type, job_length_type);
		return checkQueue;
	}
	
	private String fifoPolicy(int ams, String job_arrival_type, String job_length_type)
	{
		String checkQueue =
				"	msgsrv checkQueue()\n" +
				"	{\n" +
				"		int I = 0;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(deadline_queue[I] != EMPTY)\n" +
				"			{\n" +
				"				deadline_queue[I]--;\n" +
				"				if(deadline_queue[I] <= 0 && deadline_queue[I] != EMPTY)\n" +
				"				{\n" +
				"					m_queue_misses++;\n" +
				"					deadline_queue[I] = EMPTY;\n" +
				"					int J = I;\n" +
				"					int swap = 0;\n" +
				"					while(J < QUEUE_SIZE - 1)\n" +
				"					{\n" +
				"						swap = deadline_queue[J];\n" +
				"						deadline_queue[J] = deadline_queue[J + 1];\n" +
				"						deadline_queue[J + 1] = swap;\n" +
				"						J++;\n" +
				"					}\n" +
				"					job_length_queue[I] = EMPTY;\n" +
				"					J = I;\n" +
				"					swap = 0;\n" +
				"					while(J < QUEUE_SIZE - 1)\n" +
				"					{\n" +
				"						swap = job_length_queue[J];\n" +
				"						job_length_queue[J] = job_length_queue[J + 1];\n" +
				"						job_length_queue[J + 1] = swap;\n" +
				"						J++;\n" +
				"					}\n" +
				"				}\n" +
				"				else\n" +
				"				{\n" +
				"					I++;\n" +
				"				}\n" +
				"			}\n" +
				"			else\n" +
				"			{\n" +
				"				I++;\n" +
				"			}\n" +
				"		}\n";
		for(int I = 1; I <= ams; I++)
		{
			checkQueue +=
				"		if(appMaster" + I + " == FREE && deadline_queue[0] != EMPTY)\n" +					
				"		{\n" +
				"			appMaster" + I + " = BUSY;\n" +
				"			am" + I + ".runJob(deadline_queue[0], job_length_queue[0]);\n" +
				"			deadline_queue[0] = EMPTY;\n" +
				"			I = 0;\n" +
				"			int swap = 0;\n" +
				"			while(I < QUEUE_SIZE - 1)\n" +
				"			{\n" +
				"				swap = deadline_queue[I];\n" +
				"				deadline_queue[I] = deadline_queue[I + 1];\n" +
				"				deadline_queue[I + 1] = swap;\n" +
				"				I++;\n" +
				"			}\n" +
				"			job_length_queue[0] = EMPTY;\n" +
				"			I = 0;\n" +
				"			swap = 0;\n" +
				"			while(I < QUEUE_SIZE - 1)\n" +
				"			{\n" +
				"				swap = job_length_queue[I];\n" +
				"				job_length_queue[I] = job_length_queue[I + 1];\n" +
				"				job_length_queue[I + 1] = swap;\n" +
				"				I++;\n" +
				"			}\n" +
				"		}\n";
		}
		checkQueue += this.getQueueInsertion(job_arrival_type, job_length_type);
		return checkQueue;
	}
	
	private String priorityPolicy(int ams, String job_arrival_type, String job_length_type, int high_priority_probability, int high_priority_job_length)
	{
		String checkQueue =
				"	msgsrv checkQueue()\n" +
				"	{\n" +
				"		int I = 0;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(deadline_queue[I] != EMPTY)\n" +
				"			{\n" +
				"				deadline_queue[I]--;\n" +
				"				if(deadline_queue[I] <= 0 && deadline_queue[I] != EMPTY)\n" +
				"				{\n" +
				"					m_queue_misses++;\n" +
				"					deadline_queue[I] = EMPTY;\n" +
				"					priority[I] = EMPTY;\n" +
				"					job_length_queue[I] = EMPTY;\n" +
				"				}\n" +
				"			}\n" +
				"			I++;\n" +
				"		}\n";

		for(int I = 1; I <= ams; I++)
		{
			checkQueue +=
				"		if(appMaster" + I + " == FREE)\n" +
				"		{\n" +
				"			I = 0;\n" +
				"			int earliest_deadline = INF;\n" +
				"			int edf = EMPTY;\n" +
				"			while(I < QUEUE_SIZE)\n" +
				"			{\n" +
				"				if(priority[I] <= " + high_priority_probability + " && priority[I] != EMPTY)\n" +
				"				{\n" +
				"					if(deadline_queue[I] < earliest_deadline && deadline_queue[I] != EMPTY)\n" +
				"					{\n" +
				"						earliest_deadline = deadline_queue[I];\n" +
				"						edf = I;\n" +
				"					}\n" +
				"				}\n" +
				"				I++;\n" +
				"			}\n" +
				"			if(earliest_deadline != INF && edf != EMPTY)\n" +
				"			{\n" +
				"				appMaster" + I + " = BUSY;\n" +
				"				am" + I + ".runJob(deadline_queue[edf], job_length_queue[edf]);\n" +
				"				deadline_queue[edf] = EMPTY;\n" +
				"				job_length_queue[edf] = EMPTY;\n" +
				"				priority[edf] = EMPTY;\n" +
				"			}\n" +
				"			else\n" +
				"			{\n" +
				"				I = 0;\n" +
				"				earliest_deadline = INF;\n" +
				"				edf = EMPTY;\n" +
				"				while(I < QUEUE_SIZE)\n" +
				"				{\n" +
				"					if(priority[I] > " + high_priority_probability + " && priority[I] != EMPTY)\n" +
				"					{\n" +
				"						if(deadline_queue[I] < earliest_deadline && deadline_queue[I] != EMPTY)\n" +
				"						{\n" +
				"							earliest_deadline = deadline_queue[I];\n" +
				"							edf = I;\n" +
				"						}\n" +
				"					}\n" +
				"					I++;\n" +
				"				}\n" +
				"				if(earliest_deadline != INF && edf != EMPTY)\n" +
				"				{\n" +
				"					appMaster" + I + " = BUSY;\n" +
				"					am" + I + ".runJob(deadline_queue[edf], job_length_queue[edf]);\n" +
				"					deadline_queue[edf] = EMPTY;\n" +
				"					job_length_queue[edf] = EMPTY;\n" +
				"					priority[edf] = EMPTY;\n" +
				"				}\n" +
				"			}\n" +
				"		}\n";
		}
		checkQueue += this.getQueueInsertion(job_arrival_type, job_length_type);
		checkQueue +=
				"		I = 0;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(deadline_queue[I] != EMPTY && priority[I] == EMPTY)\n" +
				"			{\n" +
				"				priority[I] = ?(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);\n";
		if(high_priority_job_length > 0)
		{
			checkQueue +=
				"				if(priority[I] <= " + high_priority_probability + ")\n" +
				"				{\n" +
				"					float epsilon = " + this._params.get_epsilon() + "f;\n" +
				"					float dead_line = (float)" + high_priority_job_length + " * (1.0f + epsilon);\n" +
				"					deadline_queue[I] = (int)dead_line + 1;\n" +
				"					job_length_queue[I] = " + high_priority_job_length + ";\n" +
				"				}\n";
		}
		checkQueue +=
				"			}\n" +
				"			I++;\n" +
				"		}\n";
		return checkQueue;
	}
	
	private String mdfPolicy(int ams, String job_arrival_type, String job_length_type)
	{
		String checkQueue =
				"	msgsrv checkQueue()\n" +
				"	{\n" +
				"		int I = 0;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(deadline_queue[I] != EMPTY)\n" +
				"			{\n" +
				"				deadline_queue[I]--;\n" +
				"				if(deadline_queue[I] <= 0 && deadline_queue[I] != EMPTY)\n" +
				"				{\n" +
				"					m_queue_misses++;\n" +
				"					deadline_queue[I] = EMPTY;\n" +
				"					job_length_queue[I] = EMPTY;\n" +
				"				}\n" +
				"			}\n" +
				"			I++;\n" +
				"		}\n";
		for(int I = 1; I <= ams; I++)
		{
			checkQueue += 
				"		if(appMaster" + I + " == FREE)\n" +
				"		{\n" +
				"			I = 0;\n" +
				"			int maximum_deadline = EMPTY;\n" +
				"			int mdf = EMPTY;\n" +
				"			while(I < QUEUE_SIZE)\n" +
				"			{\n" +
				"				if(deadline_queue[I] > maximum_deadline && deadline_queue[I] != EMPTY)\n" +
				"				{\n" +
				"					maximum_deadline = deadline_queue[I];\n" +
				"					mdf = I;\n" +
				"				}\n" +
				"				I++;\n" +
				"			}\n" +
				"			if(mdf != EMPTY && maximum_deadline != EMPTY)\n" +
				"			{\n" +
				"				appMaster" + I + " = BUSY;\n" +
				"				am" + I + ".runJob(deadline_queue[mdf], job_length_queue[mdf]);\n" +
				"				deadline_queue[mdf] = EMPTY;\n" +
				"				job_length_queue[mdf] = EMPTY;\n" +
				"			}\n" +
				"		}\n";
		}
		checkQueue += this.getQueueInsertion(job_arrival_type, job_length_type);
		return checkQueue;
	}
	
	private String generateCheckQueue(int ams, String policy, String job_arrival_type, String job_length_type)
	{
		String checkQueue = null;
		if(policy.contentEquals("edf"))
		{
			checkQueue = this.edfPolicy(ams, job_arrival_type, job_length_type);
		}
		else if(policy.contentEquals("fifo"))
		{
			checkQueue = this.fifoPolicy(ams, job_arrival_type, job_length_type);
		}
		else if(policy.contentEquals("mdf"))
		{
			checkQueue = this.mdfPolicy(ams, job_arrival_type, job_length_type);
		}
		else if(policy.contentEquals("priority"))
		{
			checkQueue = this.priorityPolicy(ams, job_arrival_type, job_length_type, this._params.get_high_priority_job_probability(), this._params.get_high_priority_job_length());
		}
		checkQueue +=
				"		self.checkQueue() after(1);\n" +
				"	}\n\n";

		return checkQueue;
	}
	
	private String generateUpdate(int ams)
	{
		String update =
				"	msgsrv update(boolean deadline_miss)\n" +
				"	{\n" +
				"		if(deadline_miss == true)\n" +
				"		{\n" +
				"			m_update_misses++;\n" +
				"		}\n" +
				"		else\n" +
				"		{\n" +
				"			m_jobs_complete++;\n" +
				"		}\n";
		
		for(int I = 1; I <= ams; I++)
		{
			update +=
				"		if(sender == am" + I + ")\n" +
				"		{\n" +
				"			appMaster" + I + " = FREE;\n" +
				"		}\n";
		}
		update +=
				"	}\n}\n\n";
		return update;
	}

	private String generateAppMasterClass()
	{
		String appmasterclass =
				"reactiveclass AppMaster(5)\n" +
				"{\n" +
				"	knownrebecs\n" +
				"	{\n" +
				"		ResourceManager rm;\n" +
				"	}\n" +
				"	statevars\n" +
				"	{\n" +
				"	}\n" +
				"	AppMaster()\n" +
				"	{\n" +
				"	}\n" +
				"	msgsrv runJob(int dead_line, int job_length)\n" +
				"	{\n" +
				"		boolean deadline_miss;\n" +
				"		if(job_length > dead_line)\n" +
				"		{\n" +
				"			deadline_miss = true;\n" +
				"			delay(dead_line);\n" +
				"		}\n" +
				"		else\n" +
				"		{\n" +
				"			deadline_miss = false;\n" +
				"			delay(job_length);\n" +
				"		}\n" +
				"		rm.update(deadline_miss);\n" +
				"	}\n" +
				"}\n\n";

		return appmasterclass;
	}
	
	protected void generateModels(String job_arrival_type, String job_length_type, String policy, String path)
	{
		boolean priority_queue = policy.contentEquals("priority");
		for(int ams = 1; ams <= this._params.get_appmasters(); ams++)
		{
			String start = this.generateModelStart(ams);
			String statevars = this.generateStatevars(ams, priority_queue, this._params.get_queue_size());
			String constructor = this.generateConstructor(ams, priority_queue, this._params.get_queue_size());
			String checkQueue = this.generateCheckQueue(ams, policy, job_arrival_type, job_length_type);
			String update = this.generateUpdate(ams);
			String amclass = this.generateAppMasterClass();
			String main = this.generateMain(ams);
			String result = start + statevars + constructor + checkQueue + update + amclass + main;
			Writer writer;
			String file = null;
			try {
				file = path + "\\yarn-deadline-" + policy + "-" + ams + "jobs-traces" + "-" + job_arrival_type + "-" + job_length_type + "-" + "epsilon" + ".rebeca";
				writer = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(file), "utf-8"));
				writer.write(result);
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

	protected String queueInsertion(String job_length) {
		String eps = this._params.get_epsilon() + "f";
		String result =
				"		I = 0;\n" +
				"		while(I < QUEUE_SIZE && jobs > 0)\n" +
				"		{\n" +
				"			if(deadline_queue[I] == EMPTY)\n" +
				"			{\n" +
								job_length +
				"				float epsilon = " + eps + ";\n" +
				"				float dead_line = (float)job_length * (1.0f + epsilon);\n" +
				"				deadline_queue[I] = (int)dead_line + 1;\n" +
				"				job_length_queue[I] = job_length;\n" +
				"				jobs--;\n" +
				"			}\n" +
				"			I++;\n" +
				"		}\n" +
				"		m_dropped_jobs = m_dropped_jobs + jobs;\n";
				return result;
	}
}
