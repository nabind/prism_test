package com.prism.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;


public class SFTPUtil {
	
	private static final Logger logger = Logger.getLogger(SFTPUtil.class);
	
	public static boolean send (String fileName,String SFTPWORKINGDIR) {
		
		Properties tascProperties = PropertyFile.loadProperties(Constants.TASC_PROPERTIES_FILE);
		boolean isTransffered = false;
        String SFTPHOST = tascProperties.getProperty("sftp.host");
        int SFTPPORT = Integer.parseInt(tascProperties.getProperty("sftp.port"));
        String SFTPUSER = tascProperties.getProperty("sftp.username");
        String SFTPPASS =  tascProperties.getProperty("sftp.password");
        String baseLocation =  tascProperties.getProperty("sftp.base.loc");

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        logger.info("preparing the host information for sftp.");
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            logger.info("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            logger.info("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
            try{
            	SftpATTRS attrs = channelSftp.lstat(baseLocation+SFTPWORKINGDIR);
            	attrs.toString();
            } catch(Exception e) {
            	channelSftp.mkdir(baseLocation+SFTPWORKINGDIR);
            }
            channelSftp.cd(baseLocation+SFTPWORKINGDIR);
            File f = new File(fileName);
            channelSftp.put(new FileInputStream(f), f.getName());
            isTransffered = true;
            logger.info("File transfered successfully to host.");
        } catch (Exception ex) {
        	logger.error("Exception found while tranfer the response.");
        }
        finally{

            channelSftp.exit();
            logger.info("sftp Channel exited.");
            channel.disconnect();
            logger.info("Channel disconnected.");
            session.disconnect();
            logger.info("Host Session disconnected.");
        }
        
        return isTransffered; 
    }   
	
}
