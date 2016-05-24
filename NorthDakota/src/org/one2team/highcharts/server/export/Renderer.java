package org.one2team.highcharts.server.export;

import java.io.OutputStream;
import java.io.Reader;

public interface Renderer<T> {

	Renderer<T> setChartOptions (T options);

	Renderer<T> setOutputStream (OutputStream outputStream);

	Renderer<T> setGlobalOptions (T options);
	
	public abstract Reader getReader();

	void render ();
	
	public static abstract class PojoRenderer<T> implements Renderer<T> {

		
		public Renderer<T> setChartOptions (T options) {
			this.options = options;
			return this;
		}

		
		public Renderer<T> setOutputStream (OutputStream output) {
			this.output = output;
			return this;
		}

		
		public Renderer<T> setGlobalOptions (T options) {
			this.globalOptions = options;
			return this;
		}

		protected T getChartOptions () {
			return options;
		}

		protected OutputStream getOutputStream () {
			return output;
		}

		protected T getGlobalOptions () {
			return globalOptions;
		}

		private T options, globalOptions;

		private OutputStream output;
		
		private String fileName;
	    private Reader reader;
		
		public void setFileName(String FileName)
	    {
	      this.fileName = FileName;
	    }

	    public String getFileName()
	    {
	      return this.fileName;
	    }

	    public void setReader(Reader reader)
	    {
	      this.reader = reader;
	    }

	    public Reader getReader()
	    {
	      return this.reader;
	    }

	}

}