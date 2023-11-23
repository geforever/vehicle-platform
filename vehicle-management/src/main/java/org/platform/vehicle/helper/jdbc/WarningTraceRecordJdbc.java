package org.platform.vehicle.helper.jdbc;

import org.platform.vehicle.entity.WarningTraceRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/1 12:21
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WarningTraceRecordJdbc {

    private final SqlSessionFactory sqlSessionFactory;

    public void saveBatch(List<WarningTraceRecord> insertParamList) throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into warning_trace_record("
                + "trace_no, "
                + "tire_code, "
                + "fleet_id, "
                + "fleet_name, "
                + "client_id, "
                + "client_name, "
                + "license_plate, "
                + "tire_site_id, "
                + "tire_site_name, "
                + "warning_type, "
                + "pressure, "
                + "temperature, "
                + "voltage, "
                + "location, "
                + "type, "
                + "is_follow"
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (WarningTraceRecord warningDetail : insertParamList) {
                if (StringUtils.isNotBlank(warningDetail.getTraceNo())) {
                    statement.setString(1, warningDetail.getTraceNo());
                } else {
                    statement.setString(1, "");
                }
                if (StringUtils.isNotBlank(warningDetail.getTireCode())) {
                    statement.setString(2, warningDetail.getTireCode());
                } else {
                    statement.setString(2, "");
                }
                statement.setInt(3, warningDetail.getFleetId());
                statement.setString(4, warningDetail.getFleetName());
                statement.setInt(5, warningDetail.getClientId());
                statement.setString(6, warningDetail.getClientName());
                statement.setString(7, warningDetail.getLicensePlate());
                if (warningDetail.getTireSiteId() != null) {
                    statement.setInt(8, warningDetail.getTireSiteId());
                } else {
                    statement.setNull(8, java.sql.Types.INTEGER);
                }
                if (StringUtils.isNotBlank(warningDetail.getTireSiteName())) {
                    statement.setString(9, warningDetail.getTireSiteName());
                } else {
                    statement.setNull(9, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(warningDetail.getWarningType())) {
                    statement.setString(10, warningDetail.getWarningType());
                } else {
                    statement.setNull(10, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(warningDetail.getPressure())) {
                    statement.setString(11, warningDetail.getPressure());
                } else {
                    statement.setNull(11, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(warningDetail.getTemperature())) {
                    statement.setString(12, warningDetail.getTemperature());
                } else {
                    statement.setNull(12, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(warningDetail.getVoltage())) {
                    statement.setString(13, warningDetail.getVoltage());
                } else {
                    statement.setNull(13, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(warningDetail.getLocation())) {
                    statement.setString(14, warningDetail.getLocation());
                } else {
                    statement.setNull(14, java.sql.Types.VARCHAR);
                }
                statement.setInt(15, warningDetail.getType());
                if (warningDetail.getIsFollow() != null) {
                    statement.setInt(16, warningDetail.getIsFollow());
                } else {
                    statement.setInt(16, 0);
                }
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("warning trace record JDBC save batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }
}
