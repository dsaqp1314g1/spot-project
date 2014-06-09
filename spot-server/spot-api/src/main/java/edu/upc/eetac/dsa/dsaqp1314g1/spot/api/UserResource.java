package edu.upc.eetac.dsa.dsaqp1314g1.spot.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model.Spot;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model.SpotCollection;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model.User;

@Path("/user")
public class UserResource {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Context
	private SecurityContext security;
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	@POST
	@Consumes(MediaType.API_USER)
	@Produces(MediaType.API_USER)
	public User UserLogin (User user) {
		
		System.out.println("Comienza el loguin de: " + user.getUsername());
		 User userlog = new User();
		System.out.println("Preparando la conexion a la base de datos");
		Connection conn = null;
		System.out.println(".............");
		
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		System.out.println("Preparando Statement");
		PreparedStatement stmt = null;
        try {
			
			System.out.println("Creando la Query");
			stmt = conn.prepareStatement(bulidLoginUserQuery());
			
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getUserpass());
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
			   
				userlog.setUsername(rs.getString("username"));
				System.out.println("NombreUsuario: " + userlog.getUsername());
				userlog.setName(rs.getString("name"));
				System.out.println("Nombre: " + userlog.getName());
				userlog.setEmail(rs.getString("email"));
				System.out.println("Email: " + userlog.getEmail());
				}
		} 
		catch (SQLException e)
		{
			throw new ServerErrorException(e.getMessage(),Response.Status.INTERNAL_SERVER_ERROR);
		} 
		finally 
		{
			try
			{
				if (stmt != null)
					stmt.close();
				conn.close();
			} 
			catch (SQLException e)
			{
				
			}
		}
		System.out.println("Fin del loging, devolviendo datos del usuario");
		return userlog;
	}
	
	private String bulidLoginUserQuery()
	{
		System.out.println("Ejecutando bulidGetUserQuery");
		return "select * from users where username=? and userpass= MD5(?)";
	}
	
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.API_USER)
	public User getUser(@PathParam("username") String username) {
		
		System.out.println("Comienza el @GET del usuario " + username);
		User usuario = new User();
		System.out.println("Preparando la conexion a la base de datos");
		Connection conn = null;
		System.out.println(".............");
		
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		System.out.println("Preparando Statement");
		PreparedStatement stmt = null;
		
		try {
			
			System.out.println("Creando la Query");
			stmt = conn.prepareStatement(bulidGetUserQuery());
			
			System.out.println("Usuario es: " + username);
			stmt.setString(1, username);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				
				usuario.setUsername(rs.getString("username"));
				System.out.println("NombreUsuario: " + usuario.getUsername());
				usuario.setName(rs.getString("name"));
				System.out.println("Nombre: " + usuario.getName());
				usuario.setEmail(rs.getString("email"));
				System.out.println("Email: " + usuario.getEmail());
				}
		} 
		catch (SQLException e)
		{
			
			throw new ServerErrorException(e.getMessage(),Response.Status.INTERNAL_SERVER_ERROR);
		} 
		finally 
		{
			try
			{
				if (stmt != null)
					stmt.close();
				conn.close();
			} 
			catch (SQLException e)
			{
				
			}
		}
		System.out.println("Fin del Get, devolviendo datos del usuario");
		return usuario;
	}
	
	private String bulidGetUserQuery()
	{
		System.out.println("Ejecutando bulidGetUserQuery");
		return "select * from users where username=?";
	}
	
	@GET
	@Path("/{username}/spots")
	@Produces(MediaType.API_SPOT_COLLECTION)
	public SpotCollection getSpots(@PathParam("username") String usuario,@QueryParam("length") int length,
			@QueryParam("before") long before) {
		
		System.out.println("Usuario es: " + usuario);
		SpotCollection spotCollection = new SpotCollection();
	 
		Connection conn = null;
		try {
			conn = ds.getConnection();
			System.out.println("conexion realizada con exito");
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		
		try {
			System.out.println("mandando la query");
			stmt = conn.prepareStatement(buildGetStings());
			System.out.println("insertand en la query");
			stmt.setString(1,  usuario);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				System.out.println("reciviendo parametors");
				Spot spot = new Spot();
				System.out.println("Reciviendo los datos de la Query");

				spot.setIdspot(rs.getInt("idspot"));
				System.out.println("Comprovando id: " + rs.getInt("idspot"));
				System.out.println("Comprovando titulo: " + rs.getString("title"));
				spot.setTitle(rs.getString("title"));
				System.out.println("Comprovando ciudad: " + rs.getString("ciudad"));
				spot.setCiudad(rs.getString("ciudad"));
				System.out.println("Comprovando deporte: " + rs.getString("deporte"));
				spot.setDeporte(rs.getString("deporte"));
				System.out.println("Comprovando usuario: " + rs.getString("usuario"));
				spot.setUsuario(rs.getString("usuario"));
				spot.setLongitud(rs.getDouble("longitud"));
				System.out.println("Comprovando longitud: " + rs.getDouble("longitud"));
				spot.setLatitud(rs.getDouble("latitud"));
				System.out.println("Comprovando latitud: " + rs.getDouble("latitud"));
				spot.setFechasubida(sdf.format(new java.util.Date(rs.getDate("fechasubida").getTime())));
				spot.setMegusta(rs.getInt("megustas"));
				System.out.println("Comprovando longitud: "+Integer.toString(spot.getIdspot()) +".png");
				
				//spot.setImageURL(app.getProperties().get("imgBaseURL")+Integer.toString(spot.getIdspot())+".png");
				
				spotCollection.addSpot(spot);
				System.out.println("Pasando a recivir el siguiente spot de mysql");
				
		}
		}catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	 
		return spotCollection;
	}
	
	private String buildGetStings()
	{
		return "select * from spots where usuario=?";
	}
	
	@GET
	@Path("/{username}/search")
	@Produces(MediaType.API_USER)
	public User SearchUser(@QueryParam("usersearch") String name) {

		System.out.println("Comenzando busqueda del usuario: " + name);
		
		User usuario = new User();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		System.out.println("Preparando consulta");
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildSearchUserQuery(name));
			if (name == null) {
				stmt.setString(1, '%' + name + '%');
			} else {
				stmt.setString(1, '%' + name + '%');
			}
			System.out.println("Ejecutando la Query");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				
				System.out.println("Reciviendo los datos de la Query");

				usuario.setUsername(rs.getString("username"));
				System.out.println("NombreUsuario: " + usuario.getUsername());
				usuario.setName(rs.getString("name"));
				System.out.println("Nombre: " + usuario.getName());
				usuario.setEmail(rs.getString("email"));
				System.out.println("Email: " + usuario.getEmail());
				
				System.out.println("Pasando a recivir los Spots de : " + usuario.getUsername());

				PreparedStatement stmtr = null;
				stmtr = conn.prepareStatement(buildGetStings());
				System.out.println("insertand en la query");
				stmtr.setString(1, usuario.getUsername());
				ResultSet rsr = stmtr.executeQuery();
				SpotCollection spotCollection = new SpotCollection();
				
				while (rsr.next()) {
					
					System.out.println("reciviendo parametors");
					Spot spot = new Spot();
					System.out.println("Reciviendo los datos de la Query");
					
					System.out.println("Comprovando id: " + rsr.getInt("idspot"));
					spot.setIdspot(rsr.getInt("idspot"));
					System.out.println("Comprovando titulo: " + rsr.getString("title"));
					spot.setTitle(rsr.getString("title"));
					System.out.println("Comprovando ciudad: " + rsr.getString("ciudad"));
					spot.setCiudad(rsr.getString("ciudad"));
					System.out.println("Comprovando deporte: " + rsr.getString("deporte"));
					spot.setDeporte(rsr.getString("deporte"));
					System.out.println("Comprovando usuario: " + rsr.getString("usuario"));
					spot.setUsuario(rsr.getString("usuario"));
					spot.setLongitud(rsr.getDouble("longitud"));
					System.out.println("Comprovando longitud: " + rsr.getDouble("longitud"));
					spot.setLatitud(rsr.getDouble("latitud"));
					System.out.println("Comprovando latitud: " + rsr.getDouble("latitud"));
					spot.setFechasubida(sdf.format(new java.util.Date(rsr.getDate("fechaSubida").getTime())));
					spot.setMegusta(rsr.getInt("megustas"));
					System.out.println("Comprovando longitud: "+Integer.toString(spot.getIdspot()) +".png");
					
					//spot.setImageURL(app.getProperties().get("imgBaseURL")+Integer.toString(spot.getIdspot())+".png");
					
					spotCollection.addSpot(spot);
					System.out.println("Pasando a recivir el siguiente spot de mysql");
					
			}
				usuario.setSpotcollection(spotCollection);
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		System.out.println("Busqueda finalizada");
		return  usuario;
	}

	private String buildSearchUserQuery(String usersearch) {
		if (usersearch == null)
			return "select * from users where name like ?";
		else
			return "select * from users where name like ?";
	}

}
