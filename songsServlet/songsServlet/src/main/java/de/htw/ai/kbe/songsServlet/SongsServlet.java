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
	
    private Songs songs = null;
    private ObjectMapper objectMapper;
	
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
        this.songs = getInstance(getServletContext().getResourceAsStream("/WEB-INF/songs"));
        this.objectMapper = new ObjectMapper();
	}

	@Override
	public void doGet(HttpServletRequest request, 
	        HttpServletResponse response) throws IOException {
		
		String responseStr = "\nThis is a SongDB";
		
		String acceptHeader = request.getHeader("accept").trim();
		if (acceptHeader == null || acceptHeader.compareTo("*") == 0 || acceptHeader.compareTo("application/json") == 0 ) {
			Enumeration<String> paramNames = request.getParameterNames();
	        
			while (parameterNames.hasMoreElements()) {
	            String param = parameterNames.nextElement();
	            try {
	                if (param.equals("all")) {
	                    responseStr = objectMapper.writeValueAsString(songs.getAllSongs());
	                    break;
	                } else if (param.equals("songId")) {
	                    String parameter = request.getParameter(param);
	                    int songId = Integer.parseInt(parameter);
	                    responseStr = objectMapper.writeValueAsString(songs.getSongById(songId));
	                    break;
	                }
	            } catch (JsonProcessingException e) {
	                responseStr = e.toString();
	            }
	        }

	        response.setContentType("application/json");
	        response.setStatus(200);
	        try (PrintWriter out = response.getWriter()) {
	            out.print(responseStr);
	        }
		}
		else {
			// 406 not acceptable
			response.setStatus(406);
	        try (PrintWriter out = response.getWriter()) {
	            out.print("Wrong accept header!");
	        }
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
		
		
		String acceptHeader = request.getHeader("accept").trim();
		if (acceptHeader == null || acceptHeader.compareTo("*") == 0 || acceptHeader.compareTo("application/json") == 0 ) {
			String redirectText = "http://localhost:8080/songsServlet?songId=";
	        int songId = 0;
	        Song songFromRequest = null;

	        try {
	            songFromRequest = parseSong(request);

	            if (songFromRequest.getId() == 0) {
	                songId = saveNewSong(songFromRequest);
	            } else {
	                songId = updateSong(songFromRequest);
	            }
	            redirectText += songId;
	            returnSuccess(response, redirectText);
	        } catch (Exception e) {
	            returnError(response);
	        }
	        try (PrintWriter out = response.getWriter()) {
	            out.print(responseStr);
	        }
		}
		else {
			// 406 not acceptable
			response.setStatus(406);
	        try (PrintWriter out = response.getWriter()) {
	            out.print("Wrong accept header!");
	        }
		}
	}
	
	private void returnError(HttpServletResponse response) throws IOException {
        response.setStatus(400);
        try (PrintWriter out = response.getWriter()) {
            out.print("Wrong Song format!");
        }
    }

    private void returnSuccess(HttpServletResponse response, String redirectText) throws IOException {
        response.setStatus(200);
        try (PrintWriter out = response.getWriter()) {
            out.print(redirectText);
        }
//        response.sendRedirect(redirectText);
    }

    private Song parseSong(HttpServletRequest request) throws Exception {
        ServletInputStream inputStream = request.getInputStream();
        String jsonFromRequest = new String(IOUtils.toByteArray(inputStream));
        return objectMapper.readValue(jsonFromRequest, Song.class);
    }

    private int saveNewSong(Song newSong) {
        int songId = generateNewSongId();
        newSong.setId(songId);
        songs.getAllSongs().add(newSong);
        return songId;
    }

    private int updateSong(Song updatedSong) {
        for (Song tmp : songs.getAllSongs()) {
            if (tmp.getId() == updatedSong.getId()) {
                songs.getAllSongs().remove(tmp);
                break;
            }
        }
        return saveNewSong(updatedSong);
    }

    private int generateNewSongId() {
        return Math.abs(new Random().nextInt());
    }

    protected Songs getSongs() {
        return songs;
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected void setSongs(Songs songs) {
        this.songs = songs;
    }

    protected void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected synchronized void writeToFile() throws IOException {
        String contextPath = getServletContext().getRealPath("/");

        String xmlFilePath = contextPath + "WEB-INF\\songs";

        System.out.println(xmlFilePath);

        File myfile = new File(xmlFilePath);

        myfile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(myfile));
        writer.write(objectMapper.writeValueAsString(songs.getAllSongs()));

        writer.close();
    }
	
	@Override
	public void destroy() {
		try {
			writeToFile()
		}
		catch (IOException e) {
			e.printStackTrace();
		}
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
