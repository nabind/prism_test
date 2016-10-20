This utility will be used to decrypt the encrypted values from XML. UDB will send the encrypted value of 
some attributes to Prism and Prism will decrypt the values before sending the XML file back to the FTP server.

For now the Texas is the only customer for this encryption/decryption. And for now only SSN and Government ID
will be decrypted.

All the attributes which are need to be decrypted has to be set in aesDecryption.properties file (DEMO_ENCRYPTED_ATTR)
As per the current logic the decryption will happen for the below XML structure. Any change in the following structure
will cause a change in utility. 

The utility will return 1(error) or 0(success) based on the statu of the process and it will log the error/info in the
custom log file(can be set from aesDecryption properties file)

<Report>
	<Candidate>
		<Demographic	... ... ssn="" ... ... governmentID="" ... ...>
		</Demographic>
		...
		...
	</Candidate>
	...
	...
</Report> 

The aesDecryption properties file needs to be kept outside of the jar and parallel to the jar.

Create an Runnable jar file by Export as Runnable jar file.

How to call for command prompt: %JAVA_HOME%/bin/java -jar AESDecryptionUtility.jar <XML_FILE_NAME.xml>  <XSD_FILE_NAME.xsd> 