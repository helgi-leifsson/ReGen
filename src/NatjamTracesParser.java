import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NatjamTracesParser extends DefaultHandler
{
	private int _production_queue_misses = 0;
	private int _production_job_misses = 0;
	private int _research_queue_misses = 0;
	private int _research_job_misses = 0;
	private int _production_job_completions = 0;
	private int _research_job_completions = 0;
	private int _checkpoint_queue_misses = 0;
	private int _dropped_jobs = 0;
	private int _margin = 0;
	private int _checkpoint_overflow = 0;
	private int _checkpoints = 0;
	private int _sum_production_queue_misses = 0;
	private int _sum_production_job_misses = 0;
	private int _sum_research_queue_misses = 0;
	private int _sum_research_job_misses = 0;
	private int _sum_production_job_completions = 0;
	private int _sum_research_job_completions = 0;
	private int _sum_checkpoint_queue_misses = 0;
	private int _sum_dropped_jobs = 0;
	private int _sum_margin = 0;
	private int _sum_checkpoint_overflow = 0;
	private int _sum_checkpoints = 0;
	private int _traces = 0;
	private boolean _variable = false;
	private String _cur_variable_name = null;
	
	public NatjamTracesParser()
	{
		
	}

	public void parseDocument(String fileName)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(fileName, this);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes)
	{
		if(qName.equalsIgnoreCase("variable"))
		{
			this._variable = true;
			this._cur_variable_name = attributes.getValue("name");
		}
	}
	
	private Integer buildInteger(char[] ch, int start, int length)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(ch, start, length);
		Integer value = Integer.parseInt(sb.toString());
		return value;
	}

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if(this._variable)
		{
			if(this._cur_variable_name.contentEquals("m_checkpoint_queue_misses"))
			{
				Integer value = buildInteger(ch, start, length);
				this._checkpoint_queue_misses = value;
			}
			else if(this._cur_variable_name.contentEquals("m_production_job_completions"))
			{
				Integer value = buildInteger(ch, start, length);
				this._production_job_completions = value;
			}
			else if(this._cur_variable_name.contentEquals("m_production_job_misses"))
			{
				Integer value = buildInteger(ch, start, length);
				this._production_job_misses = value;
			}
			else if(this._cur_variable_name.contentEquals("m_production_queue_misses"))
			{
				Integer value = buildInteger(ch, start, length);
				this._production_queue_misses = value;
			}
			else if(this._cur_variable_name.contentEquals("m_research_job_completions"))
			{
				Integer value = buildInteger(ch, start, length);
				this._research_job_completions = value;
			}
			else if(this._cur_variable_name.contentEquals("m_research_job_misses"))
			{
				Integer value = buildInteger(ch, start, length);
				this._research_job_misses = value;
			}
			else if(this._cur_variable_name.contentEquals("m_research_queue_misses"))
			{
				Integer value = buildInteger(ch, start, length);
				this._research_queue_misses = value;
			}			
			else if(this._cur_variable_name.contentEquals("m_dropped_jobs"))
			{
				Integer value = buildInteger(ch, start, length);
				this._dropped_jobs = value;
			}			
			else if(this._cur_variable_name.contentEquals("m_margin"))
			{
				Integer value = buildInteger(ch, start, length);
				this._margin = value;
			}			
			else if(this._cur_variable_name.contentEquals("m_checkpoint_overflow"))
			{
				Integer value = buildInteger(ch, start, length);
				this._checkpoint_overflow = value;
			}			
			else if(this._cur_variable_name.contentEquals("m_checkpoints"))
			{
				Integer value = buildInteger(ch, start, length);
				this._checkpoints = value;
			}			
			this._variable =  false;
		}
	}

	public void endElement(String uri, String localName, String qName)
	{
		if(qName.equals("result"))
		{
			this._sum_production_job_completions += this._production_job_completions;
			this._sum_production_job_misses += this._production_job_misses;
			this._sum_production_queue_misses += this._production_queue_misses;
			this._sum_research_job_completions += this._research_job_completions;
			this._sum_research_job_misses += this._research_job_misses;
			this._sum_research_queue_misses += this._research_queue_misses;
			this._sum_dropped_jobs += this._dropped_jobs;
			this._sum_margin += this._margin;
			this._sum_checkpoint_overflow += this._checkpoint_overflow;
			this._sum_checkpoints += this._checkpoints;
			this._sum_checkpoint_queue_misses += this._checkpoint_queue_misses;
			this._traces++;
		}
	}
	
	public int get_production_deadline_misses()
	{
		return this._sum_production_job_misses + this._sum_production_queue_misses;
	}
	
	public int get_research_deadline_misses()
	{
		return this._sum_checkpoint_queue_misses + this._sum_research_job_misses + this._sum_research_queue_misses;
	}

	public int get_deadline_misses()
	{
		return this._sum_checkpoint_queue_misses + this._sum_production_job_misses + this._sum_production_queue_misses +
				this._sum_research_job_misses + this._sum_research_queue_misses + this._sum_checkpoint_overflow;
	}

	public int get_queue_misses()
	{
		return this._sum_checkpoint_queue_misses + this._sum_production_queue_misses + this._sum_research_queue_misses;
	}
	
	public int get_job_misses()
	{
		return this._sum_production_job_misses + this._sum_research_job_misses + this._sum_checkpoint_overflow +
				this._sum_checkpoint_queue_misses;
	}
	
	public int get_job_completions()
	{
		return this._sum_production_job_completions + this._sum_research_job_completions;
	}
	
	public int get_production_queue_misses() {
		return _sum_production_queue_misses;
	}

	public int get_production_job_misses() {
		return _sum_production_job_misses;
	}

	public int get_research_queue_misses() {
		return _sum_research_queue_misses;
	}

	public int get_research_job_misses() {
		return _sum_research_job_misses;
	}

	public int get_production_job_completions() {
		return _sum_production_job_completions;
	}

	public int get_research_job_completions() {
		return _sum_research_job_completions;
	}

	public int get_checkpoint_queue_misses() {
		return _sum_checkpoint_queue_misses;
	}

	public int get_dropped_jobs() {
		return _sum_dropped_jobs;
	}

	public int get_traces() {
		return _traces;
	}

	public int get_margin() {
		return _sum_margin;
	}

	public int get_checkpoint_overflow() {
		return _sum_checkpoint_overflow;
	}

	public int get_checkpoints() {
		return _sum_checkpoints;
	}
	
	public int get_total_jobs()
	{
		return this._sum_checkpoint_overflow + this._sum_checkpoint_queue_misses + this._sum_dropped_jobs +
				this._sum_production_job_completions + this._sum_production_job_misses + this._sum_production_queue_misses +
				this._sum_research_job_completions + this._sum_research_job_misses + this._sum_research_queue_misses;
	}
}