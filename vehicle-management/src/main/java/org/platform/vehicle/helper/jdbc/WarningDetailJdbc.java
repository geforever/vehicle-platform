package org.platform.vehicle.helper.jdbc;

import org.platform.vehicle.entity.WarningDetail;
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
 * @Date 2023/9/25 08:47
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WarningDetailJdbc {

    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 批量保存轮胎告警数据
     *
     * @param insertParamList
     * @throws SQLException
     */
    public void saveBatch(List<WarningDetail> insertParamList) throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into warning_detail("
                + "receiver_id, "
                + "trace_no, "
                + "client_id, "
                + "fleet_id, "
                + "license_plate, "
                + "tire_code, "
                + "tire_site, "
                + "tire_site_name, "
                + "warning_type, "
                + "pressure, "
                + "temperature, "
                + "pressure_threshold, "
                + "temperature_threshold, "
                + "serial_no"
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (WarningDetail warningDetail : insertParamList) {
                statement.setString(1, warningDetail.getReceiverId());
                statement.setString(14, warningDetail.getSerialNo());
                statement.setString(2, warningDetail.getTraceNo());
                statement.setInt(3, warningDetail.getClientId());
                statement.setInt(4, warningDetail.getFleetId());
                if (StringUtils.isNotBlank(warningDetail.getLicensePlate())) {
                    statement.setString(5, warningDetail.getLicensePlate());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(warningDetail.getTireCode())) {
                    statement.setString(6, warningDetail.getTireCode());
                } else {
                    statement.setNull(6, java.sql.Types.VARCHAR);
                }
                statement.setInt(7, warningDetail.getTireSite());
                if (StringUtils.isNotBlank(warningDetail.getTireSiteName())) {
                    statement.setString(8, warningDetail.getTireSiteName());
                } else {
                    statement.setNull(8, java.sql.Types.VARCHAR);
                }
                statement.setInt(9, warningDetail.getWarningType());
                if (StringUtils.isNotBlank(warningDetail.getPressure())) {
                    statement.setString(10, warningDetail.getPressure());
                } else {
                    statement.setNull(10, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(warningDetail.getTemperature())) {
                    statement.setString(11, warningDetail.getTemperature());
                } else {
                    statement.setNull(11, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(warningDetail.getPressureThreshold())) {
                    statement.setString(12, warningDetail.getPressureThreshold());
                } else {
                    statement.setNull(12, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(warningDetail.getTemperatureThreshold())) {
                    statement.setString(13, warningDetail.getTemperatureThreshold());
                } else {
                    statement.setNull(13, java.sql.Types.VARCHAR);
                }
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("warning detail JDBC save batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }

    }
}
