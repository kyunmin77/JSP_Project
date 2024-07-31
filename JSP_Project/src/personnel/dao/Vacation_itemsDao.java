package personnel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import jdbc.JdbcUtil;
import personnel.model.Appointment;
import personnel.model.Attend_items;
import personnel.model.Reward;
import personnel.model.Vacation_items;

public class Vacation_itemsDao {
	
	public Vacation_items insert(Connection conn, Vacation_items vac) throws SQLException{
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("insert into vacation_items values(?,?,?,?)");
			pstmt.setString(1, vac.getVac_name());
			pstmt.setDate(2, vac.getVac_start());
			pstmt.setDate(3, vac.getVac_end());
			pstmt.setString(4, vac.getVac_used());
						
			int insertedCount = pstmt.executeUpdate();
			
			if(insertedCount>0) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT*FROM(SELECT vac_name FROM vacation_items ORDER BY ROWNUM DESC) WHERE ROWNUM = 1");
				if(rs.next()) {
					
					String newNum = rs.getString(1);
					
					return new Vacation_items(newNum, 
							vac.getVac_start(),
							vac.getVac_end(),
							vac.getVac_used()
							);	
				}
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
		}
	}
	
	 public Vacation_items selectByName(Connection conn, String name) throws SQLException {
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      try {
	         pstmt=conn.prepareStatement("select*from vacation_items where vac_name=?");
	         pstmt.setString(1, name);
	         rs = pstmt.executeQuery();
	         Vacation_items vacation_items = null;
	         if(rs.next()) {
	        	 vacation_items = convertVacation_items(rs);
	         }
	         return vacation_items;
	      } finally {
	         JdbcUtil.close(rs);
	         JdbcUtil.close(pstmt);
	      }
	   }
	 
	 public List<Vacation_items> selectAll(Connection conn) throws SQLException {
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try {
	            pstmt = conn.prepareStatement("SELECT * FROM vacation_items");
	            rs = pstmt.executeQuery();
	            List<Vacation_items> result = new ArrayList<>();
	            while (rs.next()) {
	                result.add(convertVacation_items(rs));
	            }
	            return result;
	        } finally {
	            JdbcUtil.close(rs);
	            JdbcUtil.close(pstmt);
	        }
	    }
	
	private Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
	
	private Vacation_items convertVacation_items(ResultSet rs) throws SQLException {
		return new Vacation_items(
			rs.getString("vac_name"),
            rs.getDate("vac_start"),
            rs.getDate("vac_end"),
            rs.getString("vac_used")
				);
	}
}