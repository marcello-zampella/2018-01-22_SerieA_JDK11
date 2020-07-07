package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		ArrayList<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Match> getPuntiTotali(Team t) {
		String sql = "SELECT m.match_id AS partita, m.Season, m.FTR AS risultato, m.HomeTeam AS casa, m.AwayTeam AS fuori " + 
				"FROM matches m " + 
				"WHERE m.HomeTeam=? OR m.AwayTeam=?";
		ArrayList<Match> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getTeam());
			st.setString(2, t.getTeam());
			ResultSet res = st.executeQuery();
			

			while (res.next()) {
				result.add(new Match(res.getInt("partita"), new Season(res.getInt("Season"),null), res.getString("risultato"),
						new Team(res.getString("casa")), new Team(res.getString("fuori")) ));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public LinkedList<Integer> getAnni(Team t) {
		String sql = "SELECT DISTINCT m.Season " + 
				"FROM matches m " + 
				"WHERE m.HomeTeam=? OR m.AwayTeam=? ";
		LinkedList<Integer> result = new LinkedList<Integer>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getTeam());
			st.setString(2, t.getTeam());
			ResultSet res = st.executeQuery();
			

			while (res.next()) {
				result.add(res.getInt("Season"));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}

