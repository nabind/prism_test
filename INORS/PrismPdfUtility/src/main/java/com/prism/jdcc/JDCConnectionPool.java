package com.prism.jdcc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;

class ConnectionReaper extends Thread {
	private static final Logger logger = Logger.getLogger(ConnectionReaper.class);

    private JDCConnectionPool pool;
    private final long delay=10000; //300000;
    
	ConnectionReaper(JDCConnectionPool pool) {
        this.pool=pool;
    }

    public void run() {
    	logger.info("Starting JDCConnectionPool thread. Please wait...");
    	int count = 0;
        //while(true) {
    	while(count < 3) {
           try {
        	   count++;
              sleep(delay);
           } catch( InterruptedException e) { }
           pool.reapConnections();
        }
    	logger.info("Shutting down JDCConnectionPool thread. Please wait...");
    }
}

public class JDCConnectionPool {

   private Vector connections;
   private String url, user, password;
   final private long timeout=60000;
   private ConnectionReaper reaper;
   final private int poolsize=10;

   public JDCConnectionPool(String url, String user, String password) {
      this.url = url;
      this.user = user;
      this.password = password;
      connections = new Vector(poolsize);
      reaper = new ConnectionReaper(this);
      reaper.start();
   }

   public synchronized void reapConnections() {

      long stale = System.currentTimeMillis() - timeout;
      Enumeration connlist = connections.elements();
    
      while((connlist != null) && (connlist.hasMoreElements())) {
          JDCConnection conn = (JDCConnection)connlist.nextElement();

          if((conn.inUse()) && (stale >conn.getLastUse()) && 
                                            (!conn.validate())) {
 	      removeConnection(conn);
         }
      }
   }

   public synchronized void closeConnections() {
        
      Enumeration connlist = connections.elements();

      while((connlist != null) && (connlist.hasMoreElements())) {
          JDCConnection conn = (JDCConnection)connlist.nextElement();
          removeConnection(conn);
      }
   }

   private synchronized void removeConnection(JDCConnection conn) {
       connections.removeElement(conn);
   }


   public synchronized Connection getConnection() throws SQLException {

       JDCConnection c;
       for(int i = 0; i < connections.size(); i++) {
           c = (JDCConnection)connections.elementAt(i);
           if (c.lease()) {
              return c;
           }
       }

       Connection conn = DriverManager.getConnection(url, user, password);
       c = new JDCConnection(conn, this);
       c.lease();
       connections.addElement(c);
       return c;
  } 

   public synchronized void returnConnection(JDCConnection conn) {
      conn.expireLease();
   }
}