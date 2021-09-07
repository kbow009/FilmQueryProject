package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";

	public DatabaseAccessorObject() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Error loading database driver:");
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;

		Connection conn;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT film.id, title, description, release_year, language_id, rental_duration, "
					+ "rental_rate, length, replacement_cost, rating, special_features FROM film JOIN language ON film.language_id = language.id  WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				film = new Film();
				film.setId(rs.getInt(1));
				film.setTitle(rs.getString(2));
				film.setDescription(rs.getString("description"));
				film.setReleaseYear(rs.getInt("release_year"));
				film.setLanguageId(rs.getInt("language_id"));
				film.setRentalDuration(rs.getInt("rental_duration"));
				film.setRentalRate(rs.getDouble("rental_rate"));
				film.setLength(rs.getInt("length"));
				film.setReplacementCost(rs.getDouble("replacement_cost"));
				film.setRating(rs.getString("rating"));
				film.setSpecialFeatures(rs.getString("special_features"));
				film.setActors(findActorsByFilmId(filmId));
				
			}
			else if(!rs.next()) {
				System.out.println("No data found");
			}
			rs.close();
		    stmt.close();
		    conn.close();

		} catch (SQLException e) {
			System.err.println("Database error:");
			e.printStackTrace();
		}

		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT first_name, last_name, id FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			actor = new Actor();
			if (rs.next()) {
				actor.setFirstName(rs.getString("first_name"));
				actor.setLastName(rs.getString("last_name"));
				actor.setId(rs.getInt("id"));
				System.out.println(rs.getString("id"));
			}
			else if(!rs.next()) {
				System.out.println("No data found");
			}
			rs.close();
		    stmt.close();
		    conn.close();
					} catch (SQLException e) {
			System.err.println("Error finding actor by id:");
			e.printStackTrace();
		}

		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		
		List<Actor> actors = new ArrayList<>();
		

		Connection conn;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id, first_name, last_name " +
					" " + " FROM actor JOIN film_actor ON actor.id = film_actor.actor_id "
							+ " WHERE film_id = ?";
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setInt(1, filmId);
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						int id = rs.getInt("id");
						String firstName = rs.getString("first_name");
						String lastName = rs.getString("last_name");
						Actor actor = new Actor(id, firstName, lastName);
						
						actors.add(actor);
					}
					rs.close();
				    stmt.close();
				    conn.close();
		} catch (SQLException e) {
			System.err.println("Film id not found");
			e.printStackTrace();
		}
		return actors;
		
		}
	public List<Film> findFilmByKeyword(String filmKeyword){
		List<Film> filmList = new ArrayList<>();
		
		Connection conn;
		
			try {
				conn = DriverManager.getConnection(URL, user, pass);
				String sql = "SELECT film.id, title, description, release_year, language_id, "
						+ "rating FROM film JOIN language ON film.language_id = language.id  WHERE description LIKE ? OR title LIKE ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, "%"+filmKeyword+"%");
				stmt.setString(2,  filmKeyword+"%");
				ResultSet rs = stmt.executeQuery();
				if(rs.next()) {
					Film film = new Film();
					film.setId(rs.getInt("id")); 
					film.setTitle(rs.getString("title"));
					film.setDescription(rs.getString("description"));
					film.setReleaseYear(rs.getInt("release_year"));
					film.setLanguageId(rs.getInt("language_id"));
					film.setRating(rs.getString("rating"));
					film.setActors(findActorsByFilmId(rs.getInt("id")));
					
					filmList.add(film);
				}
				else if(!rs.next()) {
					System.out.println("No data found");
				}
				rs.close();
			    stmt.close();
			    conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
	
		
		
		return filmList;
		
	}

	@Override
	public String findFilmLanguage(int languageId) {
		String language = null;
		
		Connection conn;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT name FROM language WHERE id = ?";
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setInt(1, languageId);
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						language =rs.getString("name");
					}
					rs.close();
				    stmt.close();
				    conn.close();
		} catch (SQLException e) {
			System.err.println("Film id not found");
			e.printStackTrace();
		}
		return language;
		
		}
		
		
	}

	


