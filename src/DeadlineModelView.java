import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")

public class DeadlineModelView extends JFrame implements ActionListener
{
	public static void main(String[] args)
	{
		DeadlineModelView view = new DeadlineModelView();
		view.createAndShowGUI();
	}

	public class LabelAndSpinner
	{
		private JLabel _label = null;
		private JSpinner _spinner = null;
		
		public LabelAndSpinner(JLabel label, JSpinner spinner)
		{
			this._label = label;
			this._spinner = spinner;
		}

		public Object getValue()
		{
			return this._spinner.getValue();
		}
		
		public void setEnabled(boolean enabled)
		{
			this._label.setEnabled(enabled);
			this._spinner.setEnabled(enabled);
		}
		
		public boolean isEnabled()
		{
			return this._spinner.isEnabled();
		}
		
		public void setEditable(boolean editable)
		{
			((JSpinner.DefaultEditor)this._spinner.getEditor()).getTextField().setEditable(editable);
		}		
	}
	
	public class PolicyListSelectionHandler implements ListSelectionListener
	{
		private DeadlineModelView _view = null;
		
		public PolicyListSelectionHandler(DeadlineModelView view)
		{
			this._view = view;
		}
		
		@Override
		public void valueChanged(ListSelectionEvent arg0)
		{
			this._view.updateRunButton();
			if(!this._view.isNatjamMode())
			{
				this._view.setPrioritySpinnersStatus(false);
				if(this._view.getPolicySelection().contains("priority"))
				{
					this._view.setPrioritySpinnersStatus(true);
				}
			}
		}
	}
	
	public class JobArrivalTypesListSelectionHandler implements ListSelectionListener
	{
		private DeadlineModelView _view = null;
		
		public JobArrivalTypesListSelectionHandler(DeadlineModelView view)
		{
			this._view = view;
		}
		
		@Override
		public void valueChanged(ListSelectionEvent arg0)
		{
			this._view.disableJobArrivalSpinners();
			boolean enabled = true;
			List<String> job_arrival_types = this._view.getJobArrivalTypesSelection();
			if(job_arrival_types.contains("bursty"))
			{
				this._view.setJobArrivalBurstSpinnersStatus(enabled);
			}
			if(job_arrival_types.contains("nondet"))
			{
				this._view.setJobArrivalNondetSpinnersStatus(enabled);
			}
			if(job_arrival_types.contains("uniform"))
			{
				this._view.setJobArrivalUniformSpinnersStatus(enabled);
			}
			if(job_arrival_types.contains("wave"))
			{
				this._view.setJobArrivalWaveSpinnersStatus(enabled);
			}
			if(job_arrival_types.contains("ascending"))
			{
				this._view.setJobArrivalAscendingSpinnersStatus(enabled);
			}
			if(job_arrival_types.contains("descending"))
			{
				this._view.setJobArrivalDescendingSpinnersStatus(enabled);
			}
			this._view.updateRunButton();
		}
	}

	public class JobLengthTypesListSelectionHandler implements ListSelectionListener
	{
		private DeadlineModelView _view = null;
		
		public JobLengthTypesListSelectionHandler(DeadlineModelView view)
		{
			this._view = view;
		}
		
		@Override
		public void valueChanged(ListSelectionEvent arg0)
		{
			this._view.disableJobLengthSpinners();
			boolean enabled = true;
			List<String> job_length_types = this._view.getJobLengthTypesSelection();
			if(job_length_types.contains("exponential"))
			{
				this._view.setJobLengthExponentialSpinnersStatus(enabled);
			}
			if(job_length_types.contains("nondet"))
			{
				this._view.setJobLengthNondetSpinnersStatus(enabled);
			}
			if(job_length_types.contains("uniform"))
			{
				this._view.setJobLengthUniformSpinnersStatus(enabled);
			}
			if(job_length_types.contains("wave"))
			{
				this._view.setJobLengthWaveSpinnersStatus(enabled);
			}
			if(job_length_types.contains("ascending"))
			{
				this._view.setJobLengthAscendingSpinnersStatus(enabled);
			}
			if(job_length_types.contains("descending"))
			{
				this._view.setJobLengthDescendingSpinnersStatus(enabled);
			}
			this._view.updateRunButton();
		}
	}
	
	JPanel _panel_input_output_options = null;
	JPanel _panel_variables = null;
	JPanel _panel_lists = null;
	JPanel _panel_buttons = null;
	JPanel _panel_results = null;
	
	JTextField _tfield_base_path = null;
	JTextField _tfield_base_name = null;
	JTextField _tfield_compiler_path = null;
	JTextField _tfield_traces_path = null;
	
	LabelAndSpinner _lblspin_appmasters = null;
	LabelAndSpinner _lblspin_queue_size = null;

	LabelAndSpinner _lblspin_traces = null;
	LabelAndSpinner _lblspin_timeunits = null;

	LabelAndSpinner _lblspin_epsilon = null;
	
	LabelAndSpinner _lblspin_job_arrival_burst_interval = null;
	LabelAndSpinner _lblspin_job_arrival_burst_size = null;
	LabelAndSpinner _lblspin_job_arrival_nondet_min = null;
	LabelAndSpinner _lblspin_job_arrival_nondet_max = null;
	LabelAndSpinner _lblspin_job_arrival_uniform = null;
	LabelAndSpinner _lblspin_job_arrival_wave_additional_jobs_per_timeunit = null;
	LabelAndSpinner _lblspin_job_arrival_wave_minimum = null;
	LabelAndSpinner _lblspin_job_arrival_wave_points = null; //whole numbers only
	LabelAndSpinner _lblspin_job_arrival_ascending_increment = null;
	LabelAndSpinner _lblspin_job_arrival_ascending_minimum = null;
	LabelAndSpinner _lblspin_job_arrival_ascending_points = null;
	LabelAndSpinner _lblspin_job_arrival_descending_decrement = null;
	LabelAndSpinner _lblspin_job_arrival_descending_maximum = null;
	LabelAndSpinner _lblspin_job_arrival_descending_points = null;
	
	LabelAndSpinner _lblspin_high_priority_job_probability = null;
	LabelAndSpinner _lblspin_high_priority_job_length = null;
	
	LabelAndSpinner _lblspin_checkpoint_overhead = null;
	
	LabelAndSpinner _lblspin_job_length_exponential_multiplier = null;
	LabelAndSpinner _lblspin_job_length_nondet_min = null;
	LabelAndSpinner _lblspin_job_length_nondet_max = null;
	LabelAndSpinner _lblspin_job_length_uniform = null;
	LabelAndSpinner _lblspin_job_length_wave_additional_length_per_timeunit = null;
	LabelAndSpinner _lblspin_job_length_wave_minimum = null;
	LabelAndSpinner _lblspin_job_length_wave_points = null; //whole numbers only
	LabelAndSpinner _lblspin_job_length_ascending_increment = null;
	LabelAndSpinner _lblspin_job_length_ascending_minimum = null;
	LabelAndSpinner _lblspin_job_length_ascending_points = null;
	LabelAndSpinner _lblspin_job_length_descending_decrement = null;
	LabelAndSpinner _lblspin_job_length_descending_maximum = null;
	LabelAndSpinner _lblspin_job_length_descending_points = null;

	JRadioButton _rbtn_nonpreemptive = null;
	JRadioButton _rbtn_natjam = null;
	ButtonGroup _grp_policy_types = null;
	
	JList<String> _list_policies = null;
	JList<String> _list_job_arrival_types = null;
	JList<String> _list_job_length_types = null;
	
	JButton _btn_run = null;
	
	JTextArea _tarea_results = null;

	public DeadlineModelView()
	{

	}
	
	public void createAndShowGUI()
	{
		this.setTitle("ReGen v1.0");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		this.createInputOutputOptionsPanel();
		this.createVariablesPanel();
		this.createListsPanel();
		this.createButtonsPanel();
		this.createResultsPanel();
			
		this.setLayout(new BorderLayout());
		this.add(this._panel_input_output_options, BorderLayout.EAST);
		this.add(this._panel_variables, BorderLayout.CENTER);
		this.add(this._panel_lists, BorderLayout.WEST);
		this.add(this._panel_buttons, BorderLayout.NORTH);
		this.add(this._panel_results, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

	public void appendResults(String results)
	{
		this._tarea_results.append(results);
		this._tarea_results.update(this._tarea_results.getGraphics());
		this._tarea_results.setCaretPosition(this._tarea_results.getDocument().getLength());
		this.revalidate();
		this.repaint();
	}
	
	private void createListsPanel()
	{
		int x = 0;
		int y = 0;
		
		this._panel_lists = new JPanel(new GridBagLayout());
		
		this._rbtn_nonpreemptive = new JRadioButton("Dispatch");
		this._rbtn_natjam = new JRadioButton("Natjam-R eviction");
		this._grp_policy_types = new ButtonGroup();
		this._grp_policy_types.add(_rbtn_nonpreemptive);
		this._grp_policy_types.add(_rbtn_natjam);
		this._panel_lists.add(new JLabel("<HTML><U>Policy types<HTML><U>"), this.setGridLocation(x, y));
		this._panel_lists.add(this._rbtn_nonpreemptive, this.setGridLocation(x, ++y));
		this._panel_lists.add(this._rbtn_natjam, this.setGridLocation(x, ++y));
		this._rbtn_nonpreemptive.setSelected(true);
		this._rbtn_natjam.addActionListener(this);
		this._rbtn_nonpreemptive.addActionListener(this);
		
		List<String> policies = this.createPolicyList();
		this._list_policies = this.createAndPlaceLabelAndList("Policies: ", policies, x, ++y);
		this._list_policies.addListSelectionListener(new PolicyListSelectionHandler(this));

		List<String> job_arrival_types = new ArrayList<String>();
		job_arrival_types.add("bursty");
		job_arrival_types.add("nondet");
		job_arrival_types.add("uniform");
		job_arrival_types.add("wave");
		job_arrival_types.add("ascending");
		job_arrival_types.add("descending");
		this._list_job_arrival_types = this.createAndPlaceLabelAndList("Job arrival patterns: ", job_arrival_types, x, ++y);
		this._list_job_arrival_types.addListSelectionListener(new JobArrivalTypesListSelectionHandler(this));
		
		List<String> job_length_types = new ArrayList<String>();
		job_length_types.add("exponential");
		job_length_types.add("nondet");
		job_length_types.add("uniform");
		job_length_types.add("wave");
		job_length_types.add("ascending");
		job_length_types.add("descending");
		this._list_job_length_types = this.createAndPlaceLabelAndList("Job length patterns: ", job_length_types, x, ++y);
		this._list_job_length_types.addListSelectionListener(new JobLengthTypesListSelectionHandler(this));
	}
	
	private List<String> createPolicyList()
	{
		List<String> policies = new ArrayList<String>();
		policies.add("edf");
		policies.add("fifo");
		policies.add("mdf");
		policies.add("priority");
		return policies;
	}
	
	private List<String> createNatjamPolicyList()
	{
		List<String> policies = new ArrayList<String>();
		/*policies.add("mdf-HP");
		policies.add("mlf-HP");
		policies.add("mdf-HLP");
		policies.add("mlf-HLP");*/
		policies.add("mdf");
		policies.add("mlf");
		return policies;
	}
	
	private void createInputOutputOptionsPanel()
	{
		int x = 0;
		int y = 0;

		this._panel_input_output_options = new JPanel(new GridBagLayout());
		
		this._tfield_base_path = new JTextField(16);
		this._tfield_base_name = new JTextField(16);
		this._tfield_compiler_path = new JTextField(16);
		this._tfield_traces_path = new JTextField(16);

		this._panel_input_output_options.add(new JLabel("<HTML><U>Input/Output options<U><HTML>"), this.setGridLocation(x, y));
		this.createAndPlaceLabelAndTextField(this._panel_input_output_options, x, ++y, "Output path: ", this._tfield_base_path, "C:\\results");
		this.createAndPlaceLabelAndTextField(this._panel_input_output_options, x, ++y, "Prefix: ", this._tfield_base_name, "deadlinemodels");
		this.createAndPlaceLabelAndTextField(this._panel_input_output_options, x, ++y, "Compiler path: ", this._tfield_compiler_path, "C:\\ReGen\\compiler");
		this.createAndPlaceLabelAndTextField(this._panel_input_output_options, x, ++y, "Traces path: ", this._tfield_traces_path, "C:\\results\\traces");	
	}

	private void createVariablesPanel()
	{
		int x = 0;
		int y = 0;
		
		//Integer value = new Integer(0);
		Integer min = new Integer(0);
		//Integer max = new Integer(10);
		Integer step = new Integer(1);
		
		this._panel_variables = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		this._panel_variables.add(new JLabel("<HTML><U>Common parameters<U><HTML>"), this.setGridLocation(x, y));
		this._lblspin_appmasters = this.createAndPlaceLabelAndSpinner("Max AppMasters: ", x, ++y, 4, 1, Integer.MAX_VALUE, step);
		this._lblspin_queue_size = this.createAndPlaceLabelAndSpinner("Queue size: ", x, ++y, 8, min, Integer.MAX_VALUE, step);
		this._lblspin_traces = this.createAndPlaceLabelAndSpinner("Simulation traces: ", x, ++y, 20, min, Integer.MAX_VALUE, 10);
		this._lblspin_timeunits = this.createAndPlaceLabelAndSpinner("Simulation timeunits: ", x, ++y, 100, min, Integer.MAX_VALUE, 100);
		this.addEmptyLine(x, ++y);
		
		//Float fvalue = new Float(0.0f);
		Float fmin = new Float(0.0f);
		Float fmax = new Float(10.0f);
		Float fstep = new Float(0.1f);
		JLabel lbl_epsilon = new JLabel("Epsilon: ");
		this._panel_variables.add(lbl_epsilon, this.setGridLocation(x, ++y));
		JSpinner spin_epsilon = new JSpinner(new SpinnerNumberModel(new Float(0.5f), fmin, fmax, fstep));
		this._panel_variables.add(spin_epsilon, this.setGridLocation(x + 1, y));
		this._lblspin_epsilon = new LabelAndSpinner(lbl_epsilon, spin_epsilon);
		this.addEmptyLine(x, ++y);
		
		this._panel_variables.add(new JLabel("<HTML><U>Job arrival parameters<U><HTML>"), this.setGridLocation(x, ++y));
		
		this._lblspin_job_arrival_burst_interval = this.createAndPlaceLabelAndSpinner("Burst interval: ", x, ++y, 3, min, Integer.MAX_VALUE, step);
		this._lblspin_job_arrival_burst_size = this.createAndPlaceLabelAndSpinner("Burst size: ", x, ++y, 2, min, Integer.MAX_VALUE, step);

		this._lblspin_job_arrival_nondet_min = this.createAndPlaceLabelAndSpinner("Nondet minimum: ", x, ++y, 1, min, Integer.MAX_VALUE, step);
		this._lblspin_job_arrival_nondet_max = this.createAndPlaceLabelAndSpinner("Nondet maximum: ", x, ++y, 2, min, Integer.MAX_VALUE, step);

		this._lblspin_job_arrival_uniform = this.createAndPlaceLabelAndSpinner("Uniform value: ", x, ++y, 1, min, Integer.MAX_VALUE, step);

		this._lblspin_job_arrival_wave_additional_jobs_per_timeunit = this.createAndPlaceLabelAndSpinner("Wave jobs per timeunit: ", x, ++y, 1, min, Integer.MAX_VALUE, step);
		this._lblspin_job_arrival_wave_minimum = this.createAndPlaceLabelAndSpinner("Wave minimum: ", x, ++y, 0, min, Integer.MAX_VALUE, step);
		this._lblspin_job_arrival_wave_points = this.createAndPlaceLabelAndSpinner("Wave points: ", x, ++y, 4, min, Integer.MAX_VALUE, 2);
		this._lblspin_job_arrival_wave_points.setEditable(false);
		
		this._lblspin_job_arrival_ascending_increment = this.createAndPlaceLabelAndSpinner("Ascending increment: ", x, ++y, 1, 1, Integer.MAX_VALUE, step);
		this._lblspin_job_arrival_ascending_minimum = this.createAndPlaceLabelAndSpinner("Ascending minimum: ", x, ++y, 0, 0, Integer.MAX_VALUE, step);
		this._lblspin_job_arrival_ascending_points = this.createAndPlaceLabelAndSpinner("Ascending points: ", x, ++y, 2, 2, Integer.MAX_VALUE, step);

		this._lblspin_job_arrival_descending_decrement = this.createAndPlaceLabelAndSpinner("Descending decrement: ", x, ++y, 1, 1, Integer.MAX_VALUE, step);
		this._lblspin_job_arrival_descending_maximum = this.createAndPlaceLabelAndSpinner("Descending maximum: ", x, ++y, 2, 1, Integer.MAX_VALUE, step);
		this._lblspin_job_arrival_descending_points = this.createAndPlaceLabelAndSpinner("Descending points: ", x, ++y, 2, 2, Integer.MAX_VALUE, step);

		x += 2;
		y = 0;
		
		this._panel_variables.add(new JLabel("<HTML><U>High priority job<U><HTML>"), this.setGridLocation(x, y));
		
		this._lblspin_high_priority_job_probability = this.createAndPlaceLabelAndSpinner("Probability %: ", x, ++y, 10, 0, 100, 10);
		this._lblspin_high_priority_job_probability.setEditable(false);
		this._lblspin_high_priority_job_length = this.createAndPlaceLabelAndSpinner("Length: ", x, ++y, 0, 0, Integer.MAX_VALUE, step);

		y += 2;
		this._panel_variables.add(new JLabel("<HTML><U>Natjam-R options<U><HTML>"), this.setGridLocation(x, y));
		this._lblspin_checkpoint_overhead = this.createAndPlaceLabelAndSpinner("Checkpoint overhead: ", x, ++y, 0, 0, Integer.MAX_VALUE, step);
		this._lblspin_checkpoint_overhead.setEnabled(false);
		
		y = 7;		
		this._panel_variables.add(new JLabel("<HTML><U>Job length parameters<U><HTML>"), this.setGridLocation(x, ++y));
		
		this._lblspin_job_length_exponential_multiplier = this.createAndPlaceLabelAndSpinner("Exponential multiplier: ", x, ++y, 2, min, Integer.MAX_VALUE, step);

		this._lblspin_job_length_nondet_min = this.createAndPlaceLabelAndSpinner("Nondet minimum: ", x, ++y, 1, min, Integer.MAX_VALUE, step);
		this._lblspin_job_length_nondet_max = this.createAndPlaceLabelAndSpinner("Nondet maximum: ", x, ++y, 2, min, Integer.MAX_VALUE, step);

		this._lblspin_job_length_uniform = this.createAndPlaceLabelAndSpinner("Uniform value: ", x, ++y, 1, min, Integer.MAX_VALUE, step);

		this._lblspin_job_length_wave_additional_length_per_timeunit = this.createAndPlaceLabelAndSpinner("Wave length per timeunit: ", x, ++y, 1, min, Integer.MAX_VALUE, step);
		this._lblspin_job_length_wave_minimum = this.createAndPlaceLabelAndSpinner("Wave minimum: ", x, ++y, 1, min, Integer.MAX_VALUE, step);
		this._lblspin_job_length_wave_points = this.createAndPlaceLabelAndSpinner("Wave points: ", x, ++y, 4, min, Integer.MAX_VALUE, 2);
		this._lblspin_job_length_wave_points.setEditable(false);
		
		this._lblspin_job_length_ascending_increment = this.createAndPlaceLabelAndSpinner("Ascending increment: ", x, ++y, 1, 1, Integer.MAX_VALUE, step);
		this._lblspin_job_length_ascending_minimum = this.createAndPlaceLabelAndSpinner("Ascending minimum: ", x, ++y, 1, 1, Integer.MAX_VALUE, step);
		this._lblspin_job_length_ascending_points = this.createAndPlaceLabelAndSpinner("Ascending points: ", x, ++y, 2, 2, Integer.MAX_VALUE, step);

		this._lblspin_job_length_descending_decrement = this.createAndPlaceLabelAndSpinner("Descending decrement: ", x, ++y, 1, 1, Integer.MAX_VALUE, step);
		this._lblspin_job_length_descending_maximum = this.createAndPlaceLabelAndSpinner("Descending maximum: ", x, ++y, 2, 1, Integer.MAX_VALUE, step);
		this._lblspin_job_length_descending_points = this.createAndPlaceLabelAndSpinner("Descending points: ", x, ++y, 2, 2, Integer.MAX_VALUE, step);
		this.addEmptyLine(x, ++y);
		
		this.disableJobArrivalSpinners();
		this.disableJobLengthSpinners();
		this.setPrioritySpinnersStatus(false);
	}
	
	private void createButtonsPanel()
	{
		this._panel_buttons = new JPanel(new GridBagLayout());
		this._btn_run = new JButton("Run");
		this._btn_run.addActionListener(this);
		this._panel_buttons.add(this._btn_run, this.setGridLocation(0, 0));
		this._btn_run.setEnabled(false);
	}
	
	private void createResultsPanel()
	{
		this._tarea_results = new JTextArea(7, 0);
		this._tarea_results.setEditable(false);
		JScrollPane pane = new JScrollPane(this._tarea_results);
		this._panel_results = new JPanel(new BorderLayout());
		this._panel_results.add(pane, BorderLayout.CENTER);
	}
	
	private void addEmptyLine(int x, int y)
	{
		this._panel_variables.add(new JLabel(" "), this.setGridLocation(x, y));
	}
	
	private LabelAndSpinner createAndPlaceLabelAndSpinner(String label, int x, int y, int value, int min, int max, int step)
	{
		JLabel lbl = new JLabel(label);
		this._panel_variables.add(lbl, this.setGridLocation(x, y));
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, step));
		this._panel_variables.add(spinner, this.setGridLocation(x + 1, y));
		LabelAndSpinner lblspinner = new LabelAndSpinner(lbl, spinner);
		return lblspinner;
	}
	
	private JList<String> createAndPlaceLabelAndList(String label, List<String> elements, int x, int y)
	{
		this._panel_lists.add(new JLabel(label), this.setGridLocation(x, y));
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(String element : elements)
		{
			model.addElement(element);
		}
		JList<String> list = new JList<String>(model);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setVisibleRowCount(7);
		
		JScrollPane listScrollPane = new JScrollPane(list);
		this._panel_lists.add(listScrollPane, this.setGridLocation(x + 1, y));
		return list;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getSource() == this._btn_run)
		{
			DeadlineModelParameters params = new DeadlineModelParameters(
					this._tfield_base_path.getText(),
					this._tfield_base_name.getText(),
					this._tfield_compiler_path.getText(),
					this._tfield_traces_path.getText(),
					(int)this._lblspin_appmasters.getValue(),
					(int)this._lblspin_queue_size.getValue(),
					(int)this._lblspin_traces.getValue(),
					(int)this._lblspin_timeunits.getValue(),
					(float)this._lblspin_epsilon.getValue(),
					(int)this._lblspin_job_arrival_burst_interval.getValue(),
					(int)this._lblspin_job_arrival_burst_size.getValue(),
					(int)this._lblspin_job_arrival_nondet_min.getValue(),
					(int)this._lblspin_job_arrival_nondet_max.getValue(),
					(int)this._lblspin_job_arrival_uniform.getValue(),
					(int)this._lblspin_job_arrival_wave_points.getValue(),
					(int)this._lblspin_job_arrival_wave_minimum.getValue(),
					(int)this._lblspin_job_arrival_wave_additional_jobs_per_timeunit.getValue(),
					(int)this._lblspin_job_arrival_ascending_increment.getValue(),
					(int)this._lblspin_job_arrival_ascending_minimum.getValue(),
					(int)this._lblspin_job_arrival_ascending_points.getValue(),
					(int)this._lblspin_job_arrival_descending_decrement.getValue(),
					(int)this._lblspin_job_arrival_descending_maximum.getValue(),
					(int)this._lblspin_job_arrival_descending_points.getValue(),
					(int)this._lblspin_high_priority_job_length.getValue(),
					(int)this._lblspin_high_priority_job_probability.getValue(),
					(int)this._lblspin_checkpoint_overhead.getValue(),
					(int)this._lblspin_job_length_exponential_multiplier.getValue(),
					(int)this._lblspin_job_length_nondet_min.getValue(),
					(int)this._lblspin_job_length_nondet_max.getValue(),
					(int)this._lblspin_job_length_wave_points.getValue(),
					(int)this._lblspin_job_length_wave_minimum.getValue(),
					(int)this._lblspin_job_length_wave_additional_length_per_timeunit.getValue(),
					(int)this._lblspin_job_length_uniform.getValue(),
					(int)this._lblspin_job_length_ascending_increment.getValue(),
					(int)this._lblspin_job_length_ascending_minimum.getValue(),
					(int)this._lblspin_job_length_ascending_points.getValue(),
					(int)this._lblspin_job_length_descending_decrement.getValue(),
					(int)this._lblspin_job_length_descending_maximum.getValue(),
					(int)this._lblspin_job_length_descending_points.getValue(),
					this._list_job_arrival_types.getSelectedValuesList(),
					this._list_job_length_types.getSelectedValuesList(),
					this._list_policies.getSelectedValuesList(),
					this);

				DeadlineModelController controller = new DeadlineModelController(params, params.get_base_path());
				controller.execute();
		}
		else if(arg0.getSource() == this._rbtn_natjam)
		{
			this.setPrioritySpinnersStatus(true);
			this._lblspin_checkpoint_overhead.setEnabled(true);
			List<String> policies = this.createNatjamPolicyList();
			this.populateList(this._list_policies, policies);
		}
		else if(arg0.getSource() == this._rbtn_nonpreemptive)
		{
			this.setPrioritySpinnersStatus(false);
			this._lblspin_checkpoint_overhead.setEnabled(false);
			List<String> policies = this.createPolicyList();
			this.populateList(this._list_policies, policies);
		}
	}
	
	private void populateList(JList<String> list, List<String> elements)
	{
		DefaultListModel<String> model = (DefaultListModel<String>)list.getModel();
		model.removeAllElements();
		for(String element : elements)
		{
			model.addElement(element);
		}		
	}
	
	private void setGridLocation(int x, int y, GridBagConstraints c)
	{
		c.gridx = x;
		c.gridy = y;
	}
	
	private GridBagConstraints setGridLocation(int x, int y)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = x;
		c.gridy = y;
		return c;
	}
	
	private void createAndPlaceLabelAndTextField(JPanel panel, int x, int y, String label, JTextField tfield, String text)
	{
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		this.setGridLocation(x, y, c);
		panel.add(new JLabel(label), c);
		
		this.setGridLocation(x + 1, y, c);
		tfield.setText(text);
		panel.add(tfield, c);	
	}
	
	public void setJobArrivalBurstSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_arrival_burst_interval.setEnabled(enabled);
		this._lblspin_job_arrival_burst_size.setEnabled(enabled);
	}
	
	public void setJobArrivalNondetSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_arrival_nondet_min.setEnabled(enabled);
		this._lblspin_job_arrival_nondet_max.setEnabled(enabled);
	}
	
	public void setJobArrivalUniformSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_arrival_uniform.setEnabled(enabled);
	}
	
	public void setJobArrivalWaveSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_arrival_wave_additional_jobs_per_timeunit.setEnabled(enabled);
		this._lblspin_job_arrival_wave_minimum.setEnabled(enabled);
		this._lblspin_job_arrival_wave_points.setEnabled(enabled);
	}
	
	public void setJobArrivalAscendingSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_arrival_ascending_increment.setEnabled(enabled);
		this._lblspin_job_arrival_ascending_minimum.setEnabled(enabled);
		this._lblspin_job_arrival_ascending_points.setEnabled(enabled);
	}
	
	public void setJobArrivalDescendingSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_arrival_descending_decrement.setEnabled(enabled);
		this._lblspin_job_arrival_descending_maximum.setEnabled(enabled);
		this._lblspin_job_arrival_descending_points.setEnabled(enabled);
	}
	
	public void disableJobArrivalSpinners()
	{
		boolean enabled = false;
		this.setJobArrivalAscendingSpinnersStatus(enabled);
		this.setJobArrivalBurstSpinnersStatus(enabled);
		this.setJobArrivalDescendingSpinnersStatus(enabled);
		this.setJobArrivalNondetSpinnersStatus(enabled);
		this.setJobArrivalUniformSpinnersStatus(enabled);
		this.setJobArrivalWaveSpinnersStatus(enabled);
	}

	public List<String> getPolicySelection()
	{
		return this._list_policies.getSelectedValuesList();
	}
	
	public List<String> getJobArrivalTypesSelection()
	{
		return this._list_job_arrival_types.getSelectedValuesList();
	}

	public List<String> getJobLengthTypesSelection()
	{
		return this._list_job_length_types.getSelectedValuesList();
	}

	public void setJobLengthExponentialSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_length_exponential_multiplier.setEnabled(enabled);
	}
	
	public void setJobLengthNondetSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_length_nondet_min.setEnabled(enabled);
		this._lblspin_job_length_nondet_max.setEnabled(enabled);
	}
	
	public void setJobLengthUniformSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_length_uniform.setEnabled(enabled);
	}
	
	public void setJobLengthWaveSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_length_wave_additional_length_per_timeunit.setEnabled(enabled);
		this._lblspin_job_length_wave_minimum.setEnabled(enabled);
		this._lblspin_job_length_wave_points.setEnabled(enabled);
	}
	
	public void setJobLengthAscendingSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_length_ascending_increment.setEnabled(enabled);
		this._lblspin_job_length_ascending_minimum.setEnabled(enabled);
		this._lblspin_job_length_ascending_points.setEnabled(enabled);
	}

	public void setJobLengthDescendingSpinnersStatus(boolean enabled)
	{
		this._lblspin_job_length_descending_decrement.setEnabled(enabled);
		this._lblspin_job_length_descending_maximum.setEnabled(enabled);
		this._lblspin_job_length_descending_points.setEnabled(enabled);
	}

	public void disableJobLengthSpinners()
	{
		boolean enabled = false;
		this.setJobLengthAscendingSpinnersStatus(enabled);
		this.setJobLengthDescendingSpinnersStatus(enabled);
		this.setJobLengthExponentialSpinnersStatus(enabled);
		this.setJobLengthNondetSpinnersStatus(enabled);
		this.setJobLengthUniformSpinnersStatus(enabled);
		this.setJobLengthWaveSpinnersStatus(enabled);
	}
	
	public void updateRunButton()
	{
		boolean policy_selected = this._list_policies.getSelectedValuesList().size() > 0;
		boolean job_arrival_type_selected = this._list_job_arrival_types.getSelectedValuesList().size() > 0;
		boolean job_length_type_selected = this._list_job_length_types.getSelectedValuesList().size() > 0;
		
		this._btn_run.setEnabled(policy_selected && job_arrival_type_selected && job_length_type_selected);
	}
	
	public void setPrioritySpinnersStatus(boolean enabled)
	{
		this._lblspin_high_priority_job_length.setEnabled(enabled);
		this._lblspin_high_priority_job_probability.setEnabled(enabled);
	}
	
	public boolean isNatjamMode()
	{
		return this._rbtn_natjam.isSelected();
	}
}
