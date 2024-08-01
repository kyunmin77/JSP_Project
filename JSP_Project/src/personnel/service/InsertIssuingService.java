package personnel.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import personnel.dao.IssuingDao;
import personnel.model.Issuing;

public class InsertIssuingService {
	private IssuingDao issuingDao = new IssuingDao();
	
	public Issuing insert(IssuingRequest req) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			//트랜잭션 시작
			conn.setAutoCommit(false);
			
			Issuing issuing = toIssuing(req);
			//DB에 저장 하고 저장에 성공한 객체를 받아옴
			Issuing savedIssuing = issuingDao.insert(conn, issuing);
			if(savedIssuing == null) {	// 저장에 실패하면 RuntimeException
				throw new RuntimeException("fail to insert issuing");
			}
			
			conn.commit();
			//트랜잭션 끝
			return savedIssuing;
			
		}catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		}catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		}finally {
			JdbcUtil.close(conn);
		}
	}
	
	private Issuing toIssuing(IssuingRequest req) {
		return new Issuing(
				req.getIsu_num(),
				req.getIsu_led(),
				req.getIsu_pur(),
				req.getIsu_date());
	}
	
}