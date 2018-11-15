package de.htw.ai.kbe.songsServlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SongsServlet extends HttpServlet {
	
	private String uriToDB = null;
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
	    // Beispiel: Laden eines Konfigurationsparameters aus der web.xml
		// this.uriToDB = servletConfig.getInitParameter("uriToDBComponent");
		
		// Reads a list of songs from a JSON-file into List<Song> 
		/*
		@SuppressWarnings("unchecked")
		static List<OurSong> readJSONToSongs(String filename) throws FileNotFoundException, IOException {
			ObjectMapper objectMapper = new ObjectMapper();
			try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
				return (List<OurSong>) objectMapper.readValue(is, new TypeReference<List<OurSong>>(){});
			}
		}  */
	}

	@Override
	public void doGet(HttpServletRequest request, 
	        HttpServletResponse response) throws IOException {
		
		String acceptHeader = request.getHeader("accept").trim();
		if (acceptHeader == null || acceptHeader.compareTo("*") == 0 || acceptHeader.compareTo("application/json") == 0 ) {
			Enumeration<String> paramNames = request.getParameterNames();
			
		}
		else {
			// 406 not acceptable
		}
		
		/*
		// alle Parameter (keys)
		Enumeration<String> paramNames = request.getParameterNames();

		String responseStr = "";
		String param = "";
		while (paramNames.hasMoreElements()) {
			param = paramNames.nextElement();
			responseStr = responseStr + param + "=" 
			+ request.getParameter(param) + "\n";
		}
		response.setContentType("text/plain");
		try (PrintWriter out = response.getWriter()) {
			responseStr += "\n Your request will be sent to " + uriToDB;
			out.println(responseStr);
		} */
	}
	
	private void doGetAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}
	
	private void doGetOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/* response.setContentType("text/plain");
		ServletInputStream inputStream = request.getInputStream();
		byte[] inBytes = IOUtils.toByteArray(inputStream);
		try (PrintWriter out = response.getWriter()) {
			out.println(new String(inBytes));
		} */
	}
	
	@Override
	public void destroy() {
		/*
		 	// Write a List<Song> to a JSON-file
	static void writeSongsToJSON(List<OurSong> songs, String filename) throws FileNotFoundException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		try (OutputStream os = new BufferedOutputStream(new FileOutputStream(filename))) {
			objectMapper.writeValue(os, songs);
		}
	}
		 */
		
	}
}
