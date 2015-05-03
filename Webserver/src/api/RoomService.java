package api;

import javax.ws.rs.GET;  
import javax.ws.rs.Path;  
import javax.ws.rs.PathParam;  
import javax.ws.rs.Produces;   
import javax.ws.rs.core.MediaType;

import communication.RoomBean;

@Path("RoomService")
public class RoomService {
	 @GET  
     @Path("/RoomStatus/{id}")  
     @Produces("application/json")
	 public RoomBean sendStatus(@PathParam("id") int i){
		 RoomBean room = new RoomBean(true);
		 
		 return room;
	 }
}
