import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DeadlineTracesParser extends DefaultHandler
{
	private boolean mVariable = false;
	private String mCurVariableName = null;
	private int mCurTraceQueueMisses = 0;
	private int mCurTraceJobMisses = 0;
	private int mCurTraceJobCompletions = 0;
	private int mCurTraceDroppedJobs = 0;
	private int mSumTraceQueueMisses = 0;
	private int mSumTraceJobMisses = 0;
	private int mSumTraceJobCompletions = 0;
	private int mSumTraceDroppedJobs = 0;
	private int mTraces = 0;
	
	public DeadlineTracesParser()
	{
		
	}
	
	public int getTraces()
	{
		return this.mTraces;
	}
	
	public int getDeadlineMisses()
	{
		return this.mSumTraceJobMisses + this.mSumTraceQueueMisses;
	}

	public int getQueueMisses()
	{
		return this.mSumTraceQueueMisses;
	}

	public int getJobMisses()
	{
		return this.mSumTraceJobMisses;
	}

	public int getJobCompletions()
	{
		return this.mSumTraceJobCompletions;
	}
	
	public int getDroppedJobs()
	{
		return this.mSumTraceDroppedJobs;
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
			this.mVariable = true;
			this.mCurVariableName = attributes.getValue("name");
		}
	}
	
	public void endElement(String uri, String localName, String qName)
	{
		if(qName.equals("result"))
		{
			this.mSumTraceJobCompletions += this.mCurTraceJobCompletions;
			this.mSumTraceJobMisses += this.mCurTraceJobMisses;
			this.mSumTraceQueueMisses += this.mCurTraceQueueMisses;
			this.mSumTraceDroppedJobs += this.mCurTraceDroppedJobs;
			this.mTraces++;
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if(mVariable)
		{
			if(this.mCurVariableName.contentEquals("m_queue_misses"))
			{
				Integer value = buildInteger(ch, start, length);
				this.mCurTraceQueueMisses = value;
			}
			else if(this.mCurVariableName.contentEquals("m_update_misses"))
			{
				Integer value = buildInteger(ch, start, length);
				this.mCurTraceJobMisses = value;
			}
			else if(this.mCurVariableName.contentEquals("m_jobs_complete"))
			{
				Integer value = buildInteger(ch, start, length);
				this.mCurTraceJobCompletions = value;
			}
			else if(this.mCurVariableName.contentEquals("m_dropped_jobs"))
			{
				Integer value = buildInteger(ch, start, length);
				this.mCurTraceDroppedJobs = value;
			}
			mVariable =  false;
		}
	}
	
	private Integer buildInteger(char[] ch, int start, int length)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(ch, start, length);
		Integer value = Integer.parseInt(sb.toString());
		return value;
	}
}
