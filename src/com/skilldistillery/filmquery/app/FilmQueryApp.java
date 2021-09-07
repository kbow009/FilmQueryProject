package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();

		app.launch();
	}



	private void launch() {
		Scanner input = new Scanner(System.in);

		
			startUserInterface(input);
	
		input.close();
		
	}

	private void startUserInterface(Scanner input){
		boolean looking;
		int choice = 0;
		while (looking = true) {

			System.out.println("------ WELCOME TO THE FILM QUERY APP ------");

			System.out.println("Select a choice from the menu: ");
			System.out.println("1.) Look up a film by its id");
			System.out.println("2.) Look up a film by a search keyword.");
			System.out.println("3.) Exit the app.");
			choice = input.nextInt();

			switch (choice) {
			case 1:

				System.out.println("Please enter the film id:");
			
					int filmId = input.nextInt();
					
					Film film;
					film = db.findFilmById(filmId);
					System.out.println("Film: " + film.getTitle() + " " + film.getReleaseYear() + " " + film.getRating()
					+ " " + film.getDescription()+" "+"Language: "+ db.findFilmLanguage(film.getLanguageId())+" "+film.getActors());
				
					
				break;

			case 2:

				System.out.println("Please enter the film keyword:");
				String filmKeyword = input.next();

				List<Film> filmList = new ArrayList<>();
				
					filmList = db.findFilmByKeyword(filmKeyword);
				
					
				
				for (Film film2 : filmList) {
					System.out.println("Film: " + film2.getTitle() + " " + film2.getReleaseYear() + " "
							+ film2.getRating() + " " + film2.getDescription()+" "+ "Language: "+ db.findFilmLanguage(film2.getLanguageId()
									)+" "+ film2.getActors());
				}

				break;

			case 3:
				System.out.println("You have chosen to exit:");
				looking = false;
				System.exit(0);
				break;

			default:
				System.out.println("Please use number 1-3 to search from the menu");
				break;
			}
		}
	}
}
