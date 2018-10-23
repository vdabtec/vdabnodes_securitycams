/*LICENSE*
 * Copyright (C) 2013 - 2018 MJA Technology LLC 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package vdab.extnodes.securitycam;

import java.io.InputStream;
import vdab.api.node.HTTPService_A;

import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.constants.IconCategory;
import com.lcrc.af.datatypes.AFFile;
import com.lcrc.af.file.FileUtility;
public class SecurityCamService extends HTTPService_A {
	private static Integer s_CamPort_Default = new Integer(80);
	private static String s_CamHost_Default = "localhost";
	private static String s_ImageFileName_Default = "IMAGE";
	private static final String EVENTNAME_IMAGEFILECREATED = "SecurityImage";
	private Integer c_FileNo = Integer.valueOf(0);
	private String c_Filename = s_ImageFileName_Default;
	// These are the remote version of these
	private String c_Host = s_CamHost_Default;
	private Integer c_Port = s_CamPort_Default;
	// CONSTRUCTORS 
	public SecurityCamService(){	
		super();
	}	
	// ATTRIBUTE Methods
	// This is the external public host name
	public Integer get_IconCode(){
		return  IconCategory.NODE_CAMERA;
	}
	public String get_Host(){
		return c_Host;
	}
	public void set_Host(String host){
		c_Host = host;
	}
	public Integer get_Port(){
		return c_Port;
	}
	public void set_Port( Integer port){
		c_Port = port;
	}
	public String get_Filename(){
		return c_Filename;
	}
	public void set_Filename(String name){
		c_Filename = name;
	}
	
	
	// SUPPORTING Methods --------------------------------------
	// http://<ip>:88/cgi-bin/CGIProxy.fcgi?cmd=snapPicture2&usr=<user>&pwd=<passwd>
	public String buildCompleteURL(AnalysisEvent ev){
		StringBuilder sb = new StringBuilder("http://");
		sb.append(c_Host).append(":").append(c_Port);
		sb.append("/cgi-bin/CGIProxy.fcgi?cmd=snapPicture2");
		// May not be required - Authentication info is also placed in the header.
		// Does not appear to check authentication
		sb.append("&usr=").append(get_User());
		sb.append("&pwd=").append(get_Password());
		return sb.toString();
		}
	
	public void processReturnStream(AnalysisEvent inEvent, int msgCode,InputStream is){
		String filename = FileUtility.buildFilenameFromTemplate(c_Filename, c_FileNo);
		AFFile outFile = new AFFile(filename,"jpg");
		c_FileNo++;
		processAsFileEvent(inEvent, "SecurityImage", outFile, is);
	}

 }
