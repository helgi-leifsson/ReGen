public class NatjamModelGenerator extends AbstractModelGenerator
{	
	public NatjamModelGenerator(DeadlineModelParameters params)
	{
		super(params);
	}
	
	private String generateStatevars(int appmasters, String policy)
	{
		int queue_size = this._params.get_queue_size();
		String statevars =
				"	statevars\n" +
				"	{\n" +
				"		int[" + queue_size + "] priority;\n" +
				"		int[" + queue_size + "] incoming_time_remaining;\n" +
				"		int[" + queue_size + "] incoming_deadline_remaining;\n" +
				"		int[" + queue_size + "] checkpoint_time_remaining;\n" +
				"		int[" + queue_size + "] checkpoint_deadline_remaining;\n" +
				"		int[" + appmasters + "] appmaster_time_remaining;\n" +
				"		int[" + appmasters + "] appmaster_deadline_remaining;\n" +
				"		int[" + appmasters + "] appmaster_job_type;\n" +
				"		int[" + appmasters + "] appmaster_mutex;\n" +
				"		int EMPTY;\n" +
				"		int QUEUE_SIZE;\n" +
				"		int APPMASTERS;\n" +
				"		int PRODUCTION_JOB;\n" +
				"		int RESEARCH_JOB;\n" +
				"		int PRIORITY_THRESHOLD;\n" +
				"		int INF;\n" +
				"		int LOCKED;\n" +
				"		int UNLOCKED;\n" +
				"		int m_production_queue_misses;\n" +
				"		int m_production_job_misses;\n" +
				"		int m_research_queue_misses;\n" +
				"		int m_research_job_misses;\n" +
				"		int m_production_job_completions;\n" +
				"		int m_research_job_completions;\n" +
				"		int m_checkpoint_queue_misses;\n" +
				"		int m_dropped_jobs;\n" +
				"		int m_margin;\n" +
				"		int m_checkpoint_overflow;\n" +
				"		int m_checkpoints;\n" +
				"	}\n\n";

		return statevars;
	}
	
	private String generateConstructor(int appmasters, String policy)
	{
		int queue_size = this._params.get_queue_size();
		String constructor =
				"	ResourceManager()\n" +
				"	{\n" +
				"		EMPTY = -999;\n" +
				"		PRODUCTION_JOB = -888;\n" +
				"		RESEARCH_JOB = -777;\n" +
				"		INF = 999;\n" +
				"		QUEUE_SIZE = " + queue_size + ";\n" +
				"		APPMASTERS = " + appmasters + ";\n" +
				"		PRIORITY_THRESHOLD = " + this._params.get_high_priority_job_probability() + ";\n" +
				"		LOCKED = 0;\n" +
				"		UNLOCKED = 1;\n" +
				"		m_production_queue_misses = 0;\n" +
				"		m_production_job_misses = 0;\n" +
				"		m_research_queue_misses = 0;\n" +
				"		m_research_job_misses = 0;\n" +
				"		m_production_job_completions = 0;\n" +
				"		m_research_job_completions = 0;\n" +
				"		m_checkpoint_queue_misses = 0;\n" +
				"		m_dropped_jobs = 0;\n" +
				"		m_margin = 0;\n" +
				"		m_checkpoint_overflow = 0;\n" +
				"		m_checkpoints = 0;\n";
		for(int I = 0; I < queue_size; I++)
		{
			constructor +=
				"		priority[" + I + "] = EMPTY;\n";
		}
		for(int I = 0; I < queue_size; I++)
		{
			constructor +=
				"		incoming_time_remaining[" + I + "] = EMPTY;\n";
		}
		for(int I = 0; I < queue_size; I++)
		{
			constructor +=
				"		incoming_deadline_remaining[" + I + "] = EMPTY;\n";
		}
		for(int I = 0; I < queue_size; I++)
		{
			constructor +=
				"		checkpoint_time_remaining[" + I + "] = EMPTY;\n";
		}
		for(int I = 0; I < queue_size; I++)
		{
			constructor +=
				"		checkpoint_deadline_remaining[" + I + "] = EMPTY;\n";
		}
		for(int I = 0; I < appmasters; I++)
		{
			constructor +=
				"		appmaster_time_remaining[" + I + "] = EMPTY;\n";
		}
		for(int I = 0; I < appmasters; I++)
		{
			constructor +=
				"		appmaster_deadline_remaining[" + I + "] = EMPTY;\n";
		}
		for(int I = 0; I < appmasters; I++)
		{
			constructor +=
				"		appmaster_job_type[" + I + "] = EMPTY;\n";
		}
		for(int I = 0; I < appmasters; I++)
		{
			constructor +=
				"		appmaster_mutex[" + I + "] = UNLOCKED;\n";
		}
		constructor +=
				"		self.processQueues();\n" +
				"	}\n\n";

		return constructor;
	}
	
	private String MLF()
	{
		String mlf =
				"						int laxity = appmaster_deadline_remaining[J] - appmaster_time_remaining[J];\n" +
				"						if(laxity > max)\n" +
				"						{\n" +
				"							max = laxity;\n" +
				"							index = J;\n" +
				"						}\n";
		return mlf;
	}
	
	private String MDF()
	{
		String mdf =
				"						if(appmaster_deadline_remaining[J] > max)\n" +
				"						{\n" +
				"							max = appmaster_deadline_remaining[J];\n" +
				"							index = J;\n" +
				"						}\n";
		return mdf;
	}

	private String LLF()
	{
		String llf =
				"				if(checkpoint_deadline_remaining[I] != EMPTY)\n" +
				"				{\n" +
				"					int laxity = checkpoint_deadline_remaining[I] - checkpoint_time_remaining[I];\n" +
				"					if(laxity < min)\n" +
				"					{\n" +
				"						min = laxity;\n" +
				"						index = I;\n" +
				"					}\n" +
				"				}\n";
		return llf;
	}
	
	private String EDF()
	{
		String edf =
				"				if(checkpoint_deadline_remaining[I] < min && checkpoint_deadline_remaining[I] != EMPTY)\n" +
				"				{\n" +
				"					min = checkpoint_deadline_remaining[I];\n" +
				"					index = I;\n" +
				"				}\n";
		return edf;
	}
	
	private String decrementTimers()
	{
		String decrementTimers =
				"		//Decrement timers.\n" +
				"		I = 0;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(incoming_deadline_remaining[I] != EMPTY)\n" +
				"			{\n" +
				"				incoming_deadline_remaining[I]--;\n" +
				"				if(incoming_deadline_remaining[I] <= 0 && incoming_deadline_remaining[I] != EMPTY)\n" +
				"				{\n" +
				"					if(priority[I] <= PRIORITY_THRESHOLD && priority[I] != EMPTY)\n" +
				"					{\n" +
				"						m_production_queue_misses++;\n" +
				"					}\n" +
				"					else\n" +
				"					{\n" +
				"						m_research_queue_misses++;\n" +
				"					}\n" +
				"					incoming_deadline_remaining[I] = EMPTY;\n" +
				"					incoming_time_remaining[I] = EMPTY;\n" +
				"					priority[I] = EMPTY;\n" +
				"				}\n" +
				"			}\n" +
				"			if(checkpoint_deadline_remaining[I] != EMPTY)\n" +
				"			{\n" +
				"				checkpoint_deadline_remaining[I]--;\n" +
				"				if(checkpoint_deadline_remaining[I] <= 0  && checkpoint_deadline_remaining[I] != EMPTY)\n" +
				"				{\n" +
				"					m_checkpoint_queue_misses++;\n" +
				"					checkpoint_deadline_remaining[I] = EMPTY;\n" +
				"					checkpoint_time_remaining[I] = EMPTY;\n" +
				"				}\n" +
				"			}\n" +
				"			I++;\n" +
				"		}\n" +
				"		I = 0;\n" +
				"		while(I < APPMASTERS)\n" +
				"		{\n" +
				"			if(appmaster_time_remaining[I] > 0 || appmaster_deadline_remaining[I] > 0)\n" +
				"			{\n" +
				"				appmaster_time_remaining[I]--;\n" +
				"				appmaster_deadline_remaining[I]--;\n" +
				"			}\n" +
				"			I++;\n" +
				"		}\n\n";
		return decrementTimers;
	}
	
	private String sortQueue()
	{
		/*String sortQueue =
				"		//Sort incoming job queue.\n" +
				"		I = 0;\n" +
				"		while(I < QUEUE_SIZE - 1)\n" +
				"		{\n" +
				"			if(incoming_deadline_remaining[I] > incoming_deadline_remaining[I + 1])\n" +
				"			{\n" +
				"				int swap = incoming_deadline_remaining[I];\n" +
				"				incoming_deadline_remaining[I] = incoming_deadline_remaining[I + 1];\n" +
				"				incoming_deadline_remaining[I + 1] = swap;\n" +
				"				swap = priority[I];\n" +
				"				priority[I] = priority[I + 1];\n" +
				"				priority[I + 1] = swap;\n" +
				"				swap = incoming_time_remaining[I];\n" +
				"				incoming_time_remaining[I] = incoming_time_remaining[I + 1];\n" +
				"				incoming_time_remaining[I + 1] = swap;\n" +
				"			}\n" +
				"			I++;\n" +
				"		}\n\n";
		return sortQueue;*/
		
		String sortQueue =
			"		//Sort incoming job queue.\n" +
			"		int swapped = 1;\n" +
			"		while(swapped == 1)\n" +
			"		{\n" +
			"			I = 0;\n" +
			"			swapped = 0;\n" +
			"			while(I < QUEUE_SIZE - 1)\n" +
			"			{\n" +
			"				if(incoming_deadline_remaining[I] > incoming_deadline_remaining[I + 1])\n" +
			"				{\n" +
			"					int swap = incoming_deadline_remaining[I];\n" +
			"					incoming_deadline_remaining[I] = incoming_deadline_remaining[I + 1];\n" +
			"					incoming_deadline_remaining[I + 1] = swap;\n" +
			"					swap = priority[I];\n" +
			"					priority[I] = priority[I + 1];\n" +
			"					priority[I + 1] = swap;\n" +
			"					swap = incoming_time_remaining[I];\n" +
			"					incoming_time_remaining[I] = incoming_time_remaining[I + 1];\n" +
			"					incoming_time_remaining[I + 1] = swap;\n" +
			"					swapped = 1;\n" +
			"				}\n" +
			"				I++;\n" +
			"			}\n" +
			"		}\n\n";
		return sortQueue;
	}

	private String dispatchHighPriorityToEmptyAppmasters(int appmasters)
	{
		String processQueues =
				"		//Dispatch high priority jobs to empty appmasters.\n" +
				"		I = 0;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(priority[I] <= PRIORITY_THRESHOLD && priority[I] != EMPTY)\n" +
				"			{\n";
		for(int I = 0; I < appmasters; I++)
		{
			if(I > 0)
			{
				processQueues +=
				"				else if(appmaster_job_type[" + I + "] == EMPTY && appmaster_mutex[" + I + "] == UNLOCKED)\n";
			}
			else
			{
				processQueues +=
				" 				if(appmaster_job_type[" + I + "] == EMPTY && appmaster_mutex[" + I + "] == UNLOCKED)\n";
			}
			processQueues +=
				"				{\n" +
				"					am" + (I + 1) + ".runJob(incoming_time_remaining[I], incoming_deadline_remaining[I], PRODUCTION_JOB);\n" +
				"					appmaster_time_remaining[" + I + "] = incoming_time_remaining[I];\n" +
				"					appmaster_deadline_remaining[" + I + "] = incoming_deadline_remaining[I];\n" +
				"					appmaster_job_type[" + I + "] = PRODUCTION_JOB;\n" +
				"					incoming_time_remaining[I] = EMPTY;\n" +
				"					incoming_deadline_remaining[I] = EMPTY;\n" +
				"					priority[I] = EMPTY;\n" +
				"					appmaster_mutex[" + I + "] = LOCKED;\n" +
				"				}\n";
		}
		processQueues +=
				"			}\n" +
				"			I++;\n" +
				"		}\n\n";
		return processQueues;
	}
	
	private String preemptForHighPriority(int appmasters, String policy)
	{
		String processQueues =
				"		//Dispatch high priority jobs and preempt low priority jobs if no free appmasters.\n" +
				"		I = 0;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(priority[I] <= PRIORITY_THRESHOLD && priority[I] != EMPTY)\n" +
				"			{\n" +
				"				int J = 0;\n" +
				"				int max = EMPTY;\n" +
				"				int index = EMPTY;\n" +
				"				while(J < APPMASTERS)\n" +
				"				{\n" +
				"					if(appmaster_job_type[J] == RESEARCH_JOB && appmaster_mutex[J] == UNLOCKED && appmaster_time_remaining[J] > 1)\n" +
				"					{\n";
		if(policy.contains("mdf"))
		{
			processQueues += this.MDF();
		}
		else if(policy.contains("mlf"))
		{
			processQueues += this.MLF();
		}
		processQueues +=
				"					}\n" +
				"					J++;\n" +
				"				}\n" +
				"				if(index != EMPTY)\n" +
				"				{\n";
		for(int I = 0; I < appmasters; I++)
		{
			if(I == 0)
			{
				processQueues +=
				"					if(index == " + I + ")\n";
			}
			else
			{
				processQueues +=
				"					else if(index == " + I + ")\n";				
			}
			processQueues +=
				"					{\n" +
				"						am" + (I + 1) + ".runJob(incoming_time_remaining[I], incoming_deadline_remaining[I], PRODUCTION_JOB);\n" +
				"					}\n";
		}
		processQueues +=
				"					appmaster_time_remaining[index] = incoming_time_remaining[I];\n" +
				"					appmaster_deadline_remaining[index] = incoming_deadline_remaining[I];\n" +
				"					appmaster_job_type[index] = PRODUCTION_JOB;\n" +
				"					appmaster_mutex[index] = LOCKED;\n" +
				"					incoming_time_remaining[I] = EMPTY;\n" +
				"					incoming_deadline_remaining[I] = EMPTY;\n" +
				"					priority[I] = EMPTY;\n" +
				"				}\n" +
				"			}\n" +
				"			I++;\n" +
				"		}\n\n";
		return processQueues;
	}
	
	private String checkpointsToFreeAppmasters(int appmasters, String policy)
	{
		String processQueues =
				"		//Dispatch checkpoints to free appmasters.\n";
		for(int I = 0; I < appmasters; I++)
		{
			processQueues +=
				"		if(appmaster_job_type[" + I + "] == EMPTY && appmaster_mutex[" + I + "] == UNLOCKED)\n" +
				"		{\n" +
				"			I = 0;\n" +
				"			int min = INF;\n" +
				"			int index = EMPTY;\n" +
				"			while(I < QUEUE_SIZE)\n" +
				"			{\n";
			if(policy.contains("mdf"))
			{
				processQueues += this.EDF();
			}
			else if(policy.contains("mlf"))
			{
				processQueues += this.LLF();
			}
			processQueues +=
				"				I++;\n" +
				"			}\n" +
				"			if(index != EMPTY)\n" +
				"			{\n" +
				"				am" + (I + 1) + ".runJob(checkpoint_time_remaining[index], checkpoint_deadline_remaining[index], RESEARCH_JOB);\n" +
				"				appmaster_time_remaining[" + I + "] = checkpoint_time_remaining[index];\n" +
				"				appmaster_deadline_remaining[" + I + "] = checkpoint_deadline_remaining[index];\n" +
				"				appmaster_job_type[" + I + "] = RESEARCH_JOB;\n" +
				"				appmaster_mutex[" + I + "] = LOCKED;\n" +
				"				checkpoint_time_remaining[index] = EMPTY;\n" +
				"				checkpoint_deadline_remaining[index] = EMPTY;\n" +
				"			}\n" +
				"		}\n\n";
		}
		return processQueues;
	}
	
	private String lowPriorityToFreeAppmasters(int appmasters)
	{
		String processQueues =
				"		//Dispatch new research jobs to free appmasters.\n" +
				"		I = 0;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(priority[I] > PRIORITY_THRESHOLD)\n" +
				"			{\n";
		for(int I = 0; I < appmasters; I++)
		{
			if(I == 0)
			{
				processQueues +=
				"				if(appmaster_job_type[" + I + "] == EMPTY && appmaster_mutex[" + I + "] == UNLOCKED)\n";
			}
			else
			{
				processQueues +=
				"				else if(appmaster_job_type[" + I + "] == EMPTY && appmaster_mutex[" + I + "] == UNLOCKED)\n";				
			}
			processQueues +=
				"				{\n" +
				"					am" + (I + 1) + ".runJob(incoming_time_remaining[I], incoming_deadline_remaining[I], RESEARCH_JOB);\n" +
				"					appmaster_time_remaining[" + I + "] = incoming_time_remaining[I];\n" +
				"					appmaster_deadline_remaining[" + I + "] = incoming_deadline_remaining[I];\n" +
				"					appmaster_job_type[" + I + "] = RESEARCH_JOB;\n" +
				"					appmaster_mutex[" + I + "] = LOCKED;\n" +
				"					incoming_time_remaining[I] = EMPTY;\n" +
				"					incoming_deadline_remaining[I] = EMPTY;\n" +
				"					priority[I] = EMPTY;\n" +
				"				}\n";
		}
		processQueues +=
				"			}\n" +
				"			I++;\n" +
				"		}\n\n";
		return processQueues;
	}
	
	private String insertNewJobs(String job_arrival_type, String job_length_type)
	{
		String processQueues =
				"		//Insert new jobs into the incoming queue.\n" +
				this.getQueueInsertion(job_arrival_type, job_length_type);
		processQueues +=
				"		I = 0;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(incoming_deadline_remaining[I] != EMPTY && priority[I] == EMPTY)\n" +
				"			{\n" +
				"				priority[I] = ?(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);\n";
		int high_priority_job_length = this._params.get_high_priority_job_length();
		if(high_priority_job_length > 0)
		{
			processQueues +=
				"				if(priority[I] <= " + this._params.get_high_priority_job_probability() + ")\n" +
				"				{\n" +
				"					float epsilon = " + this._params.get_epsilon() + "f;\n" +
				"					float dead_line = (float)" + high_priority_job_length + " * (1.0f + epsilon);\n" +
				"					incoming_deadline_remaining[I] = (int)dead_line + 1;\n" +
				"					incoming_time_remaining[I] = " + high_priority_job_length + ";\n" +
				"				}\n";
		}
		processQueues +=
				"			}\n" +
				"			I++;\n" +
				"		}\n";
		return processQueues;
	}
	
	private String unlockMutexes(int appmasters)
	{
		String processQueues =
				"		//Unlock appmaster mutexes\n" +
				"		I = 0;\n" +
				"		while(I < APPMASTERS)\n" +
				"		{\n" +
				"			appmaster_mutex[I] = UNLOCKED;\n" +
				"			I++;\n" +
				"		}\n\n";
		return processQueues;
	}

	//TODO: break into smaller functions
	private String preempt(int appmasters, String policy, String queue)
	{
		String preemption = "";
		if(queue.contentEquals("checkpoint"))
		{
			preemption +=
				"		//Preempt high laxity/deadline jobs for lower laxity/deadline jobs from the checkpoint queue.\n" +
				"		int diff = INF;\n";
		}
		else if(queue.contentEquals("incoming"))
		{
			preemption +=
				"		//Preempt high laxity/deadline jobs for lower laxity/deadline jobs from the incoming queue.\n" +
				"		diff = INF;\n";
		}
		preemption +=
				"		while(diff > 0)\n" +
				"		{\n" +
				"			int min = INF;\n" +
				"			int min_index = EMPTY;\n" +
				"			I = 0;\n" +
				"			while(I < QUEUE_SIZE)\n" +
				"			{\n" +
				"				if(" + queue + "_deadline_remaining[I] != EMPTY && " + queue + "_time_remaining[I] != EMPTY && priority[I] > PRIORITY_THRESHOLD)\n" +
				"				{\n";
		if(policy.contentEquals("mlf"))
		{
			preemption +=
				"					int laxity = " + queue + "_deadline_remaining[I] - " + queue + "_time_remaining[I];\n" +
				"					if(laxity < min)\n" +
				"					{\n" +
				"						min = laxity;\n" +
				"						min_index = I;\n" +
				"					}\n";
		}
		else if(policy.contentEquals("mdf"))
		{
			preemption +=
				"					if(" + queue + "_deadline_remaining[I] < min)\n" +
				"					{\n" +
				"						min = " + queue + "_deadline_remaining[I];\n" +
				"						min_index = I;\n" +
				"					}\n";
		}
		preemption +=
				"				}\n" +
				"				I++;\n" +
				"			}\n" +
				"			int max = EMPTY;\n" +
				"			int max_index = EMPTY;\n" +
				"			I = 0;\n" +
				"			while(I < APPMASTERS)\n" +
				"			{\n" +
				"				if(appmaster_deadline_remaining[I] != EMPTY && appmaster_time_remaining[I] != EMPTY && appmaster_job_type[I] == RESEARCH_JOB && appmaster_mutex[I] == UNLOCKED && appmaster_time_remaining[I] > 1 && appmaster_deadline_remaining[I] > 1)\n" +
				"				{\n";
		if(policy.contentEquals("mlf"))
		{
			preemption +=
				"					int laxity = appmaster_deadline_remaining[I] - appmaster_time_remaining[I];\n" +
				"					if(laxity > max)\n" +
				"					{\n" +
				"						max = laxity;\n" +
				"						max_index = I;\n" +
				"					}\n";
		}
		else if(policy.contentEquals("mdf"))
		{
			preemption +=
				"					if(appmaster_deadline_remaining[I] > max)\n" +// && appmaster_deadline_remaining[I] > appmaster_time_remaining[I])\n" +
				"					{\n" +
				"						max = appmaster_deadline_remaining[I];\n" +
				"						max_index = I;\n" +
				"					}\n";
		}
		preemption +=
				"				}\n" +
				"				I++;\n" +
				"			}\n" +
				"			if(min_index != EMPTY && max_index != EMPTY)\n" +
				"			{\n" +
				"				diff = max - min;\n" +
				"				if(diff > 0)\n" +
				"				{\n";
		for(int I = 0; I < appmasters; I++)
		{
			if(I > 0)
			{
				preemption +=
				"					else if(max_index == " + I + ")\n";
			}
			else
			{
				preemption +=
				"					if(max_index == " + I + ")\n";
			}
			preemption +=
				"					{\n" +
				"						am" + (I + 1) + ".runJob(" + queue + "_time_remaining[min_index], " + queue + "_deadline_remaining[min_index], RESEARCH_JOB);\n" +
				"						appmaster_time_remaining[" + I + "] = " + queue + "_time_remaining[min_index];\n" +
				"						appmaster_deadline_remaining[" + I + "] = " + queue + "_deadline_remaining[min_index];\n" +
				"						appmaster_job_type[" + I + "] = RESEARCH_JOB;\n" +
				"						appmaster_mutex[" + I + "] = LOCKED;\n" +
				"						" + queue + "_time_remaining[min_index] = EMPTY;\n" +
				"						" + queue + "_deadline_remaining[min_index] = EMPTY;\n" +
				"						priority[min_index] = EMPTY;\n" +
				"					}\n";
		}
		preemption +=
				"				}\n" +
				"			}\n" +
				"			else\n" +
				"			{\n" +
				"				diff = -1;\n" +
				"			}\n" +
				"		}\n\n";
		return preemption;
	}

	private String generateProcessQueues(int appmasters, String job_arrival_type, String job_length_type, String policy)
	{
		/*String processQueues =
				"	msgsrv processQueues()\n" +
				"	{\n" +
				"		int I = 0;\n\n" +
				this.decrementTimers() +
				this.sortQueue() +
				this.dispatchHighPriorityToEmptyAppmasters(appmasters) +
				this.preemptForHighPriority(appmasters, policy) +
				this.checkpointsToFreeAppmasters(appmasters, policy) +
				this.preempt(appmasters, policy, "checkpoint") +
				this.lowPriorityToFreeAppmasters(appmasters) +		
				this.preempt(appmasters, policy, "incoming") +
				this.unlockMutexes(appmasters) +
				this.insertNewJobs(job_arrival_type, job_length_type) +
				"		self.processQueues() after(1);\n" +
				"	}\n\n";
		return processQueues;*/
		
		String processQueues =
			"	msgsrv processQueues()\n" +
			"	{\n" +
			"		int I = 0;\n\n" +
			this.decrementTimers() +
			this.sortQueue() +
			this.dispatchHighPriorityToEmptyAppmasters(appmasters) +
			this.preemptForHighPriority(appmasters, policy) +
			this.checkpointsToFreeAppmasters(appmasters, policy);
			if(policy.contentEquals("mdf") || policy.contentEquals("mlf"))
			{
				processQueues += this.preempt(appmasters, policy, "checkpoint");
			}
			processQueues +=
			this.lowPriorityToFreeAppmasters(appmasters);
			if(policy.contentEquals("mdf") || policy.contentEquals("mlf"))
			{
				processQueues += this.preempt(appmasters, policy, "incoming");
			}
			processQueues +=
			this.unlockMutexes(appmasters) +
			this.insertNewJobs(job_arrival_type, job_length_type) +
			"		self.processQueues() after(1);\n" +
			"	}\n\n";
		return processQueues;
	}
	
	private String generateUpdate(int appmasters)
	{
		String update =
			"	msgsrv update(int time_remaining, int deadline_remaining, int job_type)\n" +
			"	{\n" +
			"		if(time_remaining <= 0 || deadline_remaining <= 0)\n" +
			"		{\n" +
			"			if(time_remaining <= 0)\n" +
			"			{\n" +
			"				if(job_type == PRODUCTION_JOB)\n" +
			"				{\n" +
			"					m_production_job_completions++;\n" +
			"				}\n" +
			"				else if(job_type == RESEARCH_JOB)\n" +
			"				{\n" +
			"					m_research_job_completions++;\n" +
			"				}\n" +
			"			}\n" +
			"			else if(deadline_remaining <= 0)\n" +
			"			{\n" +
			"				if(job_type == PRODUCTION_JOB)\n" +
			"				{\n" +
			"					m_production_job_misses++;\n" +
			"				}\n" +
			"				else if(job_type == RESEARCH_JOB)\n" +
			"				{\n" +
			"					m_research_job_misses++;\n" +
			"				}\n" +
			"			}\n" +
			"			m_margin = m_margin + (deadline_remaining - time_remaining);\n";
		for(int I = 0; I < appmasters; I++)
		{
			if(I == 0)
			{
				update +=
			"			if(sender == am" + (I + 1) + ")\n";
			}
			else
			{
				update +=
			"			else if(sender == am" + (I + 1) + ")\n";
			}
			update +=
			"			{\n" +
			"				appmaster_time_remaining[" + I + "] = EMPTY;\n" +
			"				appmaster_deadline_remaining[" + I + "] = EMPTY;\n" +
			"				appmaster_job_type[" + I + "] = EMPTY;\n" +
			"			}\n";
		}
		update +=
			"		}\n" +
			"		else\n" +
			"		{\n";
		for(int I = 0; I < appmasters; I++)
		{
			if(I == 0)
			{
				update +=
			"			if(sender == am" + (I + 1) + ")\n";
			}
			else
			{
				update +=
			"			else if(sender == am" + (I + 1) + ")\n";
			}
			update +=
			"			{\n" +
			"				appmaster_time_remaining[" + I + "] = time_remaining;\n" +
			"				appmaster_deadline_remaining[" + I + "] = deadline_remaining;\n" +
			"				appmaster_job_type[" + I + "] = job_type;\n" +
			"			}\n";
		}
			update +=
			"		}\n" +
			"	}\n\n";

		return update;
	}
	
	private String generateCheckPoint(int appmasters)
	{
		String checkPoint =
				"	msgsrv checkPoint(int prev_time_remaining, int prev_deadline_remaining, int cur_time_remaining, int cur_deadline_remaining, int cur_job_type)\n" +
				"	{\n" +
				"		int I = 0;\n" +
				"		int empty_checkpoint_slot = EMPTY;\n" +
				"		while(I < QUEUE_SIZE)\n" +
				"		{\n" +
				"			if(checkpoint_time_remaining[I] == EMPTY && checkpoint_deadline_remaining[I] == EMPTY)\n" +
				"			{\n" +
				"				empty_checkpoint_slot = I;\n" +
				"			}\n" +
				"			I++;\n" +
				"		}\n" +
				"		if(empty_checkpoint_slot != EMPTY)\n" +
				"		{\n" +
				"			checkpoint_time_remaining[empty_checkpoint_slot] = prev_time_remaining + " + this._params.get_checkpoint_overhead() + ";\n" +
				"			checkpoint_deadline_remaining[empty_checkpoint_slot] = prev_deadline_remaining;\n" +
				"			m_checkpoints++;\n" +
				"		}\n" +
				"		else\n" +
				"		{\n" +
				"			m_checkpoint_overflow++;\n" +
				"		}\n";
			for(int I = 0; I < appmasters; I++)
			{
				if(I == 0)
				{
					checkPoint +=
				"		if(sender == am" + (I + 1) + ")\n";
				}
				else
				{
					checkPoint +=
				"		else if(sender == am" + (I + 1) + ")\n";
				}
				checkPoint +=
				"		{\n" +
				"			appmaster_time_remaining[" + I + "] = cur_time_remaining;\n" +
				"			appmaster_deadline_remaining[" + I + "] = cur_deadline_remaining;\n" +
				"			appmaster_job_type[" + I + "] = cur_job_type;\n" +
				"		}\n";
			}
			checkPoint +=
				"	}\n" +
				"}\n\n";
		return checkPoint;
	}
	
	private String generateAppMasterClass(String policy)
	{
		String appMaster =
				"reactiveclass AppMaster(20)\n" +
				"{\n" +
				"	knownrebecs\n" +
				"	{\n" +
				"		ResourceManager rm;\n" +
				"	}\n" +
				"\n" +
				"	statevars\n" +
				"	{\n" +
				"		int EMPTY;\n" +
				"		int PRODUCTION_JOB;\n" +
				"		int RESEARCH_JOB;\n" +
				"		int m_time_remaining;\n" +
				"		int m_deadline_remaining;\n" +
				"		int m_job_type;\n" +
				"	}\n" +
				"\n" +
				"	AppMaster()\n" +
				"	{\n" +
				"		EMPTY = -999;\n" +
				"		PRODUCTION_JOB = -888;\n" +
				"		RESEARCH_JOB = -777;\n" +
				"		m_time_remaining = EMPTY;\n" +
				"		m_deadline_remaining = EMPTY;\n" +
				"		m_job_type = EMPTY;\n" +
				"	}\n" +
				"\n" +
				"	msgsrv runJob(int time_remaining, int deadline_remaining, int job_type)\n" +
				"	{\n" +
				"		if(m_job_type == RESEARCH_JOB)\n" +
				"		{\n" +
				"			rm.checkPoint(m_time_remaining, m_deadline_remaining, time_remaining, deadline_remaining, job_type);\n" +
				"		}\n" +
				/*"		else if(m_job_type == EMPTY)\n" +
				"		{\n" +
				"			rm.update(time_remaining, deadline_remaining, job_type);\n" +
				"		}\n" +*/
				"		m_time_remaining = time_remaining;\n" +
				"		m_deadline_remaining = deadline_remaining;\n" +
				"		if(m_job_type == EMPTY)\n" +
				"		{\n" +
				"			self.process() after(1);\n" +
				"		}\n" +
				"		m_job_type = job_type;\n" +
				"	}\n" +
				"\n" +
				"	msgsrv process()\n" +
				"	{\n" +
				"		m_time_remaining--;\n" +
				"		m_deadline_remaining--;\n" +
				"		if(m_time_remaining <= 0 || m_deadline_remaining <= 0)\n" +
				"		{\n" +
				"			rm.update(m_time_remaining, m_deadline_remaining, m_job_type);\n" +
				"			m_time_remaining = EMPTY;\n" +
				"			m_deadline_remaining = EMPTY;\n" +
				"			m_job_type = EMPTY;\n" +
				"		}\n" +
				"		else\n" +
				"		{\n" +
				"			self.process() after(1);\n" +
				"		}\n" +
				"	}\n" +
				"}\n\n";

		return appMaster;
	}

	@Override
	//TODO: pull up
	protected void generateModels(String job_arrival_type, String job_length_type, String policy, String path)
	{
		for(int appmasters = 1; appmasters <= this._params.get_appmasters(); appmasters++)
		{
			String start = this.generateModelStart(appmasters);
			String statevars = this.generateStatevars(appmasters, policy);
			String constructor = this.generateConstructor(appmasters, policy);
			String processQueues = this.generateProcessQueues(appmasters, job_arrival_type, job_length_type, policy);
			String update = this.generateUpdate(appmasters);
			String checkPoint = this.generateCheckPoint(appmasters);
			String amclass = this.generateAppMasterClass(policy);
			String main = this.generateMain(appmasters);
			String result = start + statevars + constructor + processQueues + update + checkPoint + amclass + main;
			String file = path + "\\yarn-deadline-" + policy + "-" + appmasters + "jobs-traces" + "-" + job_arrival_type + "-" + job_length_type + "-" + "epsilon" + ".rebeca";
			this.writeFile(file, result);
		}
	}

	//TODO: pull up
	protected String queueInsertion(String job_length) {
		String eps = this._params.get_epsilon() + "f";
		String result =
				"		I = 0;\n" +
				"		while(I < QUEUE_SIZE && jobs > 0)\n" +
				"		{\n" +
				"			if(incoming_deadline_remaining[I] == EMPTY)\n" +
				"			{\n" +
								job_length +
				"				float epsilon = " + eps + ";\n" +
				"				float dead_line = (float)job_length * (1.0f + epsilon);\n" +
				"				incoming_deadline_remaining[I] = (int)dead_line + 1;\n" +
				"				incoming_time_remaining[I] = job_length;\n" +
				"				jobs--;\n" +
				"			}\n" +
				"			I++;\n" +
				"		}\n" +
				"		m_dropped_jobs = m_dropped_jobs + jobs;\n";
				return result;
	}
}
