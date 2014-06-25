package edu.upc.eetac.dsa.dsaqp1314g1.spot.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.MediaType;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model.BotonMegusta;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model.Comentario;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model.Spot;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model.SpotCollection;
import edu.upc.eetac.dsa.dsaqp1314g1.spot.api.model.User;

@Path("/spots")
public class SpotResource {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	@Context
	private SecurityContext security;
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	@Context
	private Application app;
	
	@GET
	@Produces(MediaType.API_SPOT_COLLECTION)
	public SpotCollection getSpotsCollections(@QueryParam("length") int length,
			@QueryParam("after") int after) {

		System.out.println("Iniciando Get al recurso");

		SpotCollection spotcollection = new SpotCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {

			boolean updateFromLast = after > 0;
			System.out.println("Conexion a mysql hecha");
			stmt = conn.prepareStatement(buildGetSpotQuery(updateFromLast));

			if (updateFromLast) {
				if (length == 0) {
					stmt.setInt(1, after);
					stmt.setInt(2, 10);
				} else {
					stmt.setInt(1, after);
					stmt.setInt(2, length);
				}
			} else {

				if (length == 0)
					stmt.setInt(1, 100);
				else
					stmt.setInt(1, length);
			}

			System.out.println("Query preparada");
			ResultSet rs = stmt.executeQuery();
			System.out.println("Query ejecutada con exito");

			while (rs.next()) {

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
				spotcollection.addSpot(spot);
				System.out.println("Pasando a recivir el siguiente spot de mysql");
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
		System.out.println("devolviendo la coleccion de spots");
		return spotcollection;
	}

	private String buildGetSpotQuery(boolean updateFromLast) {

		if (updateFromLast)
			return "select * from spots where idspot>? limit ?";
		else
			return "select * from spots limit ?";
	}
	
	private String buildGetComentarioSpotByIdQuery() {
		return "select * from comentarios  where idspot=?";
	}
	
	
	@GET
	@Path("/search")
	@Produces(MediaType.API_SPOT_COLLECTION)
	public SpotCollection SearchLibros(@QueryParam("ciudad") String ciudad,
			@QueryParam("deporte") String deporte, @QueryParam("length") int length) {

		System.out.println("Comenzando busqueda");

		SpotCollection spotcollection = new SpotCollection();
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		System.out.println("ciudad = "+ciudad);
		System.out.println("deporte = "+deporte);
		System.out.println("Preparando consulta");
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildSearchStingsQuery(deporte));
			if (deporte == null) {
				stmt.setString(1, '%' + ciudad + '%');
				length = (length <= 0) ? 5 : length;
				stmt.setInt(2, length);
			} else {
				stmt.setString(1, '%' + ciudad + '%');
				stmt.setString(2, '%' + deporte + '%');
				length = (length <= 0) ? 5 : length;
				stmt.setInt(3, length);
			}
			System.out.println("Ejecutando la Query");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				Spot spot = new Spot();
				System.out.println("Reciviendo los datos de la Query");

				spot.setIdspot(rs.getInt("idspot"));
				System.out.println("Comprovando id: " + rs.getInt("idspot"));
				System.out.println("Comprovando ciudad: " + rs.getString("title"));
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
				
				System.out.println("Pasando a recivir los comentarios");

				PreparedStatement stmtr = null;
				stmtr = conn.prepareStatement(buildGetComentarioSpotByIdQuery());
				stmtr.setInt(1, spot.getIdspot());

				ResultSet rsr = stmtr.executeQuery();

				while (rsr.next()) {
					System.out.println("Comentarios");

					Comentario review = new Comentario();
					review.setIdcomentario(rsr.getInt("idcomentario"));
					System.out.println("Comentarios id");
					review.setIdspot(rsr.getInt("idspot"));
					System.out.println("Comentarios idspot");
					review.setUsuario(rsr.getString("usuario"));
					System.out.println("Comentarios usuario");
					//System.out.println("Comentarios fechaedicion: "+ rsr.getTimestamp("fechaedicion"));
					//review.setFechacreacion(rsr.getDate("fechaedicion"));
				
					review.setComentario(rsr.getString("comentario"));
					System.out.println("Comentarios p");
					spot.addComentario(review);
					System.out.println("Comentarios pu");

				}

				spotcollection.addSpot(spot);
				System.out.println("Pasando a recivir el siguiente spot de mysql");
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
		return spotcollection;
	}

	private String buildSearchStingsQuery(String deporte) {
		if (deporte == null)
			return "select * from spots where ciudad like ? limit ?";
		else
			return "select * from spots where ciudad like ? and deporte like ? limit ?";
	}
	
	
	private Spot getSpotFromDatabase(String idspot) {
		Spot spot = new Spot();
	 
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetSpotByIdQuery());

			stmt.setInt(1, Integer.valueOf(idspot));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {

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
				
				spot.setImageURL(app.getProperties().get("imgBaseURL")+Integer.toString(spot.getIdspot())+".png");
				System.out.println("Pasando a recivir los comentarios");

				PreparedStatement stmtr = null;
				stmtr = conn.prepareStatement(buildGetComentarioSpotByIdQuery());
				stmtr.setInt(1, spot.getIdspot());

				ResultSet rsr = stmtr.executeQuery();

				while (rsr.next()) {
					System.out.println("Comentarios");

					Comentario review = new Comentario();
					review.setIdcomentario(rsr.getInt("idcomentario"));
					System.out.println("Comentarios id: "+rsr.getInt("idcomentario") );
					review.setIdspot(rsr.getInt("idspot"));
					System.out.println("Comentarios idspot: " + rsr.getInt("idspot"));
					review.setUsuario(rsr.getString("usuario"));
					System.out.println("Comentarios usuario: "+rsr.getString("usuario"));
					System.out.println("Comentarios fechacreacion: "+ rsr.getTimestamp("fechacreacion"));
					review.setFechacreacion(rsr.getDate("fechacreacion"));
				
					review.setComentario(rsr.getString("comentario"));
					System.out.println("Comentarios comentario: " + rsr.getString("comentario"));
					spot.addComentario(review);
					System.out.println("Comentario añadido");

				}
				PreparedStatement stmtr2 = null;
				stmtr2 = conn.prepareStatement(buildGetMegustasSpotByIdQuery());
				stmtr2.setInt(1, spot.getIdspot());

				ResultSet rsr2 = stmtr2.executeQuery();
				while (rsr2.next()) {
					System.out.println("Botonmegustaa");
					BotonMegusta btnMG = new BotonMegusta();
					btnMG.setIdmegusta(rsr2.getInt("idmegustas"));
					btnMG.setIdspot(rsr2.getInt("idspot"));
					btnMG.setUsermegusta(rsr2.getString("usuario"));
					spot.addBotonMegusta(btnMG);
					System.out.println("Botonmegusta añadido");

				}
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
		System.out.println("Devolviendo la coleccion de spots");
		return spot;
	}
	
	private String buildGetSpotByIdQuery() {

		return "select * from spots where idspot=?";
}
	//COmentario sin mas
	private String buildGetMegustasSpotByIdQuery(){
		return "SELECT * FROM megustas WHERE idspot=?";
	}
	
	
    @GET
	@Path("/{idspot}")
	@Produces(MediaType.API_SPOT)
	public Spot getSpot(@PathParam("idspot") String idspot,@Context Request request) {
    	System.out.println("Comenzando a recivir un Spot de id: " +idspot);
		Spot spot = getSpotFromDatabase(idspot);
		return spot;
	}
    
    
    private UUID writeAndConvertImage(InputStream file, int idspot) {

    	System.out.println("Comienza funcion de insercion de imagen");
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
			System.out.println("Imagen leida");

		} catch (IOException e) {
			throw new InternalServerErrorException(
					"Something has been wrong when reading the file.");
		}
		UUID uuid = UUID.randomUUID();
		System.out.println("randomUUID ejecutado");
		String filename = Integer.toString(idspot) + ".png";
		System.out.println("Nombre de la imagen : " + filename);
		try {
			ImageIO.write(image,"png",new File(app.getProperties().get("imgBaseURL") + filename));
			System.out.println("Imagen guardada en la carpeta");
		} catch (IOException e) {
			throw new InternalServerErrorException(
					"Something has been wrong when converting the file.");
		}
		System.out.println("Fin de la funcion de guarado de imagen");
		return uuid;
	}
    
    
    @POST
	@Consumes(MediaType.API_SPOT)
	@Produces(MediaType.API_SPOT)
//	public Spot createSpot(Spot spot) {
    	public Spot createSpot(Spot spot,
    			@FormDataParam("image") InputStream image,
    			@FormDataParam("image") FormDataContentDisposition fileDisposition) {
		System.out.println("Subiendo un spot");

		System.out.println("Subiendo titulo: "+ spot.getTitle());
		validateSpot(spot);
		System.out.println("Campos del spot validados corectamente");
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		System.out.println("Conexion a sql hecha");
		try {
			
			System.out.println("Prepardo la Query");
			String sql = buildInsertSpot();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println("Insertando valores en la tabla");
			stmt.setString(1, spot.getTitle());
			stmt.setDouble(2, spot.getLatitud());
			stmt.setDouble(3, spot.getLongitud());
			stmt.setString(4, spot.getUsuario());
			stmt.setString(5, spot.getDeporte());
			stmt.setString(6, spot.getCiudad());

			stmt.executeUpdate();
			System.out.println("Query ejecutada");
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				
				int idspot= rs.getInt(1);
				System.out.println("Praparando para guardar la imagen");
				
				// DESCOMENTAR PER PUJAR LA IMATGE!!!
				UUID uuid = writeAndConvertImage(image, rs.getInt(1) );
				System.out.println("Pasando a recoger el spot creado");
				spot = getSpotFromDatabase(Integer.toString(idspot));
			} else {
				/*
			} */
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
	 
		System.out.println("Fin de la creacion de un spot, devolviendo dicho spot");
		return spot;
	}

	private void validateSpot(Spot spot) {
		if (spot.getTitle() == null)
			throw new BadRequestException("Title can't be null.");
		if (spot.getUsuario() == null)
			throw new BadRequestException("User can't be null.");
		if (spot.getDeporte() == null)
			throw new BadRequestException("Deporte can't be null.");
		if (spot.getCiudad() == null)
			throw new BadRequestException("City can't be null.");
	}
	private String buildInsertSpot() {
		return "insert into spots (title, latitud, longitud, megustas, usuario, deporte, ciudad) value (?,?,?, '0', ?, ?, ?)";
	}
    
	
	@POST
	@Path("/{idspot}/comentario")
	@Consumes(MediaType.API_COMENTARIO)
	@Produces(MediaType.API_SPOT)
	public Spot createComentario(@PathParam("idspot") String idspot,
			Comentario comentario) {

		System.out.println("Subiendo un comentario");
		Spot spot = new Spot();
		validateComentario(comentario);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		System.out.println("Conexion a mysql hecha");
		PreparedStatement stmt = null;
		try {
			String sql = buildInsertComentario();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			System.out.println("Creando la query");
			stmt.setString(1, idspot);
			System.out.println("Id Libro: " + idspot);
			stmt.setString(2, comentario.getUsuario());
			System.out.println("Username: " +comentario.getUsuario());
			stmt.setString(3, comentario.getComentario());
			System.out.println("Texto: " + comentario.getComentario());

			System.out.println("Ejecutando la Query");
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();

			System.out.println("Query ejecutara");
			if (rs.next()) {
				spot = getSpotFromDatabase(idspot);
				
				if (!comentario.getUsuario().equals(spot.getUsuario()))
						{
				             System.out.println("Informando al creador del spot " + spot.getUsuario() + " que el usuario " +comentario.getUsuario() + " ha hecho un comentario de su spot id " + spot.getIdspot() );
				             actualizacioncomentario(spot.getIdspot(),spot.getTitle(),spot.getUsuario(),comentario.getUsuario());
						}
			} else {
				// Something has failed...
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
		System.out.println("Devolviendo spot para actualizar la informacion del spot");
		return spot;
	}

	private void validateComentario(Comentario comentario) {

		if (comentario.getComentario() == null)
			throw new BadRequestException("El comentario no puede estar vacio");
		if (comentario.getComentario().length() > 140)
			throw new BadRequestException(
					"El comentario es demasiado largo");
	}
	private String buildInsertComentario() {
		return "insert into comentarios (idspot, usuario, comentario) value (?, ?, ?)";
	}

	
	@PUT
	@Path("/{idspot}/megustas/{username}")
	@Consumes(MediaType.API_SPOT)
	@Produces(MediaType.API_SPOT)
	public Spot updateMegustas(@PathParam("idspot") String idspot, @PathParam("username") String username,
			Spot spot)	{
		//String username = security.getUserPrincipal().getName();
		System.out.println("Comenzando el registro de Me Gusta");
		spot = getSpotFromDatabase(idspot);
		//user = getUserFromDatabase(username);
		System.out.println(username);

		System.out.println(spot.getCiudad());
		//validateUser(idspot);
		//validateUpdateMegustas(spot);
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs;
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		try {
			stmt = conn.prepareStatement(buildComprobarSiYaHanHechoMegusta());
			stmt.setInt(1, Integer.valueOf(idspot));
			stmt.setString(2, username);
			rs = stmt.executeQuery();
			if (rs.next() == true) {
				throw new NotAllowedException();
				// EL usuario ya tiene una reseña para el libro
			}

			else {
				try {
					String sql2 = buildInsertarMegusta();
					String sql = buildUpdateMegustas();
					
					stmt = conn.prepareStatement(sql);
					stmt.setInt(1, spot.getMegusta()+1);
					stmt.setInt(2, Integer.valueOf(idspot));
					
					stmt2 = conn.prepareStatement(sql2);
					stmt2.setInt(1, Integer.valueOf(idspot));
					stmt2.setString(2, username);
					stmt2.executeUpdate();
					System.out.println("Ejecutando la queri");
					int rows = stmt.executeUpdate();
					System.out.println("Query ejecutada");
					System.out.println("Reciviendo el id de el me gusta recien creado");
				if (rows == 1){
					
					if (!username.equals(spot.getUsuario()))
					{
			             System.out.println("Informando al creador del spot " + spot.getUsuario() + " que el usuario " +username + " le ha dado a me gusta en el spot de ID:  " + spot.getIdspot() );
			             actumegusta(spot, "megusta", username);
					}
					   spot = getSpotFromDatabase(idspot);
					}
					
					else {
						throw new NotFoundException("There's no spot with idspot="
								+ idspot);
					}
			 
				} 
				catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
					throw new ServerErrorException(e.getMessage(),
							Response.Status.INTERNAL_SERVER_ERROR);
				}
			}
		}
		
		}
		catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		return spot;
	}

 
	private String buildComprobarSiYaHanHechoMegusta(){
		return "SELECT * FROM megustas WHERE idspot=? and usuario=?";
	}
	private String buildInsertarMegusta(){
		
		return "INSERT INTO megustas (idspot,usuario) VALUES (?, ?)";
	}
	

	@PUT
	@Path("/{idspot}/NOmegustas/{username}")
	@Consumes(MediaType.API_SPOT)
	@Produces(MediaType.API_SPOT)
	public Spot updateNOMegustas(@PathParam("idspot") String idspot, @PathParam("username") String username,
			Spot spot)	{
		//String username = security.getUserPrincipal().getName();
		System.out.println("***************************");
		spot = getSpotFromDatabase(idspot);
		//user = getUserFromDatabase(username);
		System.out.println(username);

		System.out.println(spot.getCiudad());
		//validateUser(idspot);
		//validateUpdateMegustas(spot);
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs;
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		try {
			stmt = conn.prepareStatement(buildComprobarSiYaHanHechoMegusta());
			stmt.setInt(1, Integer.valueOf(idspot));
			stmt.setString(2, username);
			rs = stmt.executeQuery();
			if (rs.next() == false) {
				throw new NotAllowedException();
				// EL no ha hecho megusta por lo que no puede hacer nomegusta
			}

			else {
				try {
					String sql = buildUpdateMegustas();
					stmt = conn.prepareStatement(sql);
					stmt.setInt(1, spot.getMegusta()-1);
					stmt.setInt(2, Integer.valueOf(idspot));
			 
					String sql2 = buildDeleteMegusta();
					stmt2 = conn.prepareStatement(sql2);
					stmt2 = conn.prepareStatement(sql2);
					stmt2.setInt(1, Integer.valueOf(idspot));
					stmt2.setString(2, username);
					stmt2.executeUpdate();
					int rows = stmt.executeUpdate();
				if (rows == 1){
					
						spot = getSpotFromDatabase(idspot);
					}
					
					else {
						throw new NotFoundException("There's no spot with idspot="
								+ idspot);
					}
			 
				} 
				catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
					throw new ServerErrorException(e.getMessage(),
							Response.Status.INTERNAL_SERVER_ERROR);
				}
			}
		}
		
		}
		catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		System.out.println("Pasando a eliminar la actualizacion de la tabla actumegusta");
		deleteActuMegustaElNO(Integer.valueOf(idspot), username);
		System.out.println();
		return spot;
	}
	
	
	private String buildUpdateMegustas() {
		return "update spots set megustas=? where idspot=?";
	}
	
	private String buildDeleteMegusta() {
		return "delete from megustas where idspot=? and usuario=?";
	}
	
	private void deleteActuMegustaElNO(int idspot, String usermegusta){

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		System.out.println("Conexion a mysql hecha");
		PreparedStatement stmt = null;
		try {
			String sql = buildDeleteActuMegusta();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			System.out.println("Creando la query");
			stmt.setInt(1, idspot);
			stmt.setString(2, usermegusta);
			System.out.println("Ejecutando la Query");
			stmt.executeUpdate();
			System.out.println("Query ejecutada");
			System.out.println("...............");
		} 
		catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} 
		finally 
		{
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} 
			catch (SQLException e) {
			}
		}
		System.out.println("Fin de eliminacion actumegusta");

    }
	
	@DELETE
	@Path("/{idspot}/comentario/{idcomentario}")
	@Produces(MediaType.API_SPOT)
	public Spot deleteComentario(@PathParam("idspot") String idspot,
			@PathParam("idcomentario") String idcomentario) {

//		if (!security.isUserInRole("registered"))
//			throw new ForbiddenException(
//					"You are not allowed to create reviews for a book");

		System.out.println("Comenzando eliminacion de un comentario");

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		System.out.println("Conexion a mysql hecha");
		PreparedStatement stmt = null;
		try {

			System.out.println("Preparando la Query");
			System.out.println("Id del spot : " + idspot
					+ " Id del comentario a eliminar: " + idcomentario);
			String sql = buildDeleteReview();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.valueOf(idcomentario));
			stmt.setInt(2, Integer.valueOf(idspot));

			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no sting with stingid="
						+ idspot);
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
		
		System.out.println("Comentario eliminado");
		System.out.println("Pasando a eliminar la actualizacion : " +idspot + " / "+ idcomentario);
		deleteActualizacionComentario(Integer.valueOf(idspot), Integer.valueOf(idcomentario));
		System.out.println("Actualizacion de comentario eliminada");
		return getSpotFromDatabase(idspot);
	}

	private String buildDeleteReview() {
		return "delete from comentarios where idcomentario=? and idspot=?";
	}
	
	private void deleteActualizacionComentario(int idspot, int idcoment){

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		System.out.println("Conexion a mysql hecha");
		PreparedStatement stmt = null;
		try {
			String sql = buildDeleteActualizacion();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			System.out.println("Creando la query");
			stmt.setInt(1, idcoment);
			stmt.setInt(2, idspot);
			System.out.println("Ejecutando la Query");
			stmt.executeUpdate();
			System.out.println("Query ejecutada");
			System.out.println("...............");
		} 
		catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} 
		finally 
		{
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} 
			catch (SQLException e) {
			}
		}

    }
	
	private void actumegusta(Spot spot, String estado, String usermegusta){

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		System.out.println("Conexion a mysql hecha");
		PreparedStatement stmt = null;
		try {
			String sql = buildInsertActumegusta();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			System.out.println("Creando la query");
			stmt.setInt(1, spot.getIdspot());
			stmt.setString(2, spot.getTitle() );
			stmt.setString(3, estado );
			stmt.setString(4, spot.getUsuario());
			stmt.setString(5, usermegusta);
			System.out.println("Ejecutando la Query");
			stmt.executeUpdate();
			System.out.println("Query ejecutada");
			System.out.println("...............");
		} 
		catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} 
		finally 
		{
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} 
			catch (SQLException e) {
			}
		}

    }
	
	private String buildInsertActumegusta() {
		return "insert into actumegusta (idspot, nombrespot, estado, userspot, usermegusta) value (? ,?, ?, ?, ?)";
	}
	
	private void actualizacioncomentario(int idspot, String spottitle, String creadorspot, String creadorcomentario){

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		System.out.println("Conexion a mysql hecha");
		PreparedStatement stmt = null;
		try {
			String sql = buildInsertActualizacion();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			System.out.println("Creando la query");
			stmt.setInt(1, idspot);
			stmt.setString(2, spottitle );
			System.out.println("Titulo spot : " +spottitle);
			stmt.setString(3, creadorspot );
			System.out.println("Username: " +creadorspot );
			stmt.setString(4, creadorcomentario);
			System.out.println("Usuario que comento: " + creadorcomentario);
			System.out.println("Ejecutando la Query");
			stmt.executeUpdate();
			System.out.println("Query ejecutada");
			System.out.println("...............");
		} 
		catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} 
		finally 
		{
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} 
			catch (SQLException e) {
			}
		}

    }
	
	private String buildInsertActualizacion() {
		return "insert into actualizaciones (idspot, nombrespot, userspot, usercomentario) value (? ,?, ?, ?)";
	}
	
	@DELETE
	@Path("/{idspot}/actualizacion/{idactualizacion}")
	public void deleteactualizacion(@PathParam("idspot") String idspot,
			@PathParam("idactualizacion") String idactualizacion) {

		System.out.println("Comenzando eliminacion de una actualizacion");

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		System.out.println("Conexion a mysql hecha");
		PreparedStatement stmt = null;
		try {

			System.out.println("Preparando la Query");
			System.out.println("Id del spot : " + idspot
					+ " Id de la acualizacion a eliminar: " + idactualizacion);
			String sql = buildDeleteActualizacion();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.valueOf(idactualizacion));
			stmt.setInt(2, Integer.valueOf(idspot));

			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no sting with stingid="
						+ idspot);
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
		System.out.println("Comentario eliminado");
	}

	private String buildDeleteActualizacion() {
		return "delete from actualizaciones where idcomentario=? and idspot=?";
	}
	
	@DELETE
	@Path("/{idspot}/actumegusta/{usermegusta}")
	public void deleteActuMegusta(@PathParam("idspot") String idspot,
			@PathParam("usermegusta") String usermegusta) {

		System.out.println("Comenzando eliminacion de una actualizacion de megusta");

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		System.out.println("Conexion a mysql hecha");
		PreparedStatement stmt = null;
		try {

			System.out.println("Preparando la Query");
			System.out.println("Id del spot : " + idspot
					+ " Id de la acualizacion a eliminar: " + usermegusta);
			String sql = buildDeleteActuMegusta();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.valueOf(idspot));
			stmt.setString(2, usermegusta);

			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no sting with stingid="
						+ idspot);
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
		System.out.println("Comentario eliminado");
	}
	
	private String buildDeleteActuMegusta() {
		return "delete from actumegusta where idspot=? and usermegusta=?";
	}
}
