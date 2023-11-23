package org.platform.vehicle.mapper.jdbc;

import org.platform.vehicle.entity.TireNewestDataEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/14 11:17
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TireNewestDataJdbc {

    private final SqlSessionFactory sqlSessionFactory;

    public void batchInsert(Set<TireNewestDataEntity> paramList)
            throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into `jtt808_tire_newest_data`("
                + "client_id, "
                + "serial_no, "
                + "trailer_status, "
                + "gua_repeater_id, "
                + "longitude, "
                + "latitude, "
                + "tire_type, "
                + "tire_site_id, "
                + "tire_sensor_id,"
                + "voltage,"
                + "tire_pressure,"
                + "tire_temperature,"
                + "battery_voltage_status,"
                + "is_timeout, "
                + "scheme,"
                + "tire_pressure_status,"
                + "tire_temperature_status, "
                + "tire_status,"
                + "device_time"
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (TireNewestDataEntity tireNewestData : paramList) {
                statement.setString(1, tireNewestData.getClientId());
                if (StringUtils.isNotBlank(tireNewestData.getSerialNo())) {
                    statement.setString(2, tireNewestData.getSerialNo());
                } else {
                    statement.setNull(2, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getTrailerStatus() != null) {
                    statement.setInt(3, tireNewestData.getTrailerStatus());
                } else {
                    statement.setNull(3, java.sql.Types.INTEGER);
                }
                if (StringUtils.isNotBlank(tireNewestData.getGuaRepeaterId())) {
                    statement.setString(4, tireNewestData.getGuaRepeaterId());
                } else {
                    statement.setNull(4, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getLongitude())) {
                    statement.setString(5, tireNewestData.getLongitude());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getLatitude())) {
                    statement.setString(6, tireNewestData.getLatitude());
                } else {
                    statement.setNull(6, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getTireType() != null) {
                    statement.setInt(7, tireNewestData.getTireType());
                } else {
                    statement.setNull(7, java.sql.Types.INTEGER);
                }
                if (StringUtils.isNotBlank(tireNewestData.getTireSiteId())) {
                    statement.setString(8, tireNewestData.getTireSiteId());
                } else {
                    statement.setNull(8, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getTireSensorId())) {
                    statement.setString(9, tireNewestData.getTireSensorId());
                } else {
                    statement.setNull(9, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getVoltage())) {
                    statement.setString(10, tireNewestData.getVoltage());
                } else {
                    statement.setNull(10, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getTirePressure())) {
                    statement.setString(11, tireNewestData.getTirePressure());
                } else {
                    statement.setNull(11, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getTireTemperature())) {
                    statement.setString(12, tireNewestData.getTireTemperature());
                } else {
                    statement.setNull(12, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getBatteryVoltageStatus() != null) {
                    statement.setInt(13, tireNewestData.getBatteryVoltageStatus());
                } else {
                    statement.setNull(13, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getIsTimeout() != null) {
                    statement.setInt(14, tireNewestData.getIsTimeout());
                } else {
                    statement.setNull(14, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getScheme() != null) {
                    statement.setInt(15, tireNewestData.getScheme());
                } else {
                    statement.setNull(15, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getTirePressureStatus() != null) {
                    statement.setInt(16, tireNewestData.getTirePressureStatus());
                } else {
                    statement.setNull(16, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getTireTemperatureStatus() != null) {
                    statement.setInt(17, tireNewestData.getTireTemperatureStatus());
                } else {
                    statement.setNull(17, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getTireStatus() != null) {
                    statement.setInt(18, tireNewestData.getTireStatus());
                } else {
                    statement.setNull(18, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getDeviceTime() != null) {
                    statement.setTimestamp(19,
                            new java.sql.Timestamp(tireNewestData.getDeviceTime().getTime()));
                } else {
                    statement.setNull(19, java.sql.Types.DATE);
                }
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("tire newest data JDBC save batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }

    public void batchUpdate(Set<TireNewestDataEntity> paramList)
            throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "update `jtt808_tire_newest_data` set "
                + "serial_no = ?, "
                + "trailer_status = ?, "
                + "gua_repeater_id = ?, "
                + "longitude = ?, "
                + "latitude = ?, "
                + "tire_type = ?, "
                + "tire_site_id = ?, "
                + "tire_sensor_id = ?, "
                + "voltage = ?, "
                + "tire_pressure = ?, "
                + "tire_temperature = ?, "
                + "battery_voltage_status = ?, "
                + "is_timeout = ?, "
                + "scheme = ?, "
                + "tire_pressure_status = ?, "
                + "tire_temperature_status = ?, "
                + "tire_status = ?, "
                + "device_time = ? "
                + "where client_id = ? and tire_site_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (TireNewestDataEntity tireNewestData : paramList) {
                if (StringUtils.isNotBlank(tireNewestData.getSerialNo())) {
                    statement.setString(1, tireNewestData.getSerialNo());
                } else {
                    statement.setNull(1, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getTrailerStatus() != null) {
                    statement.setInt(2, tireNewestData.getTrailerStatus());
                } else {
                    statement.setNull(2, java.sql.Types.INTEGER);
                }
                if (StringUtils.isNotBlank(tireNewestData.getGuaRepeaterId())) {
                    statement.setString(3, tireNewestData.getGuaRepeaterId());
                } else {
                    statement.setNull(3, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getLongitude())) {
                    statement.setString(4, tireNewestData.getLongitude());
                } else {
                    statement.setNull(4, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getLatitude())) {
                    statement.setString(5, tireNewestData.getLatitude());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getTireType() != null) {
                    statement.setInt(6, tireNewestData.getTireType());
                } else {
                    statement.setNull(6, java.sql.Types.INTEGER);
                }
                if (StringUtils.isNotBlank(tireNewestData.getTireSiteId())) {
                    statement.setString(7, tireNewestData.getTireSiteId());
                } else {
                    statement.setNull(7, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getTireSensorId())) {
                    statement.setString(8, tireNewestData.getTireSensorId());
                } else {
                    statement.setNull(8, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getVoltage())) {
                    statement.setString(9, tireNewestData.getVoltage());
                } else {
                    statement.setNull(9, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getTirePressure())) {
                    statement.setString(10, tireNewestData.getTirePressure());
                } else {
                    statement.setNull(10, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getTireTemperature())) {
                    statement.setString(11, tireNewestData.getTireTemperature());
                } else {
                    statement.setNull(11, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getBatteryVoltageStatus() != null) {
                    statement.setInt(12, tireNewestData.getBatteryVoltageStatus());
                } else {
                    statement.setNull(12, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getIsTimeout() != null) {
                    statement.setInt(13, tireNewestData.getIsTimeout());
                } else {
                    statement.setNull(13, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getScheme() != null) {
                    statement.setInt(14, tireNewestData.getScheme());
                } else {
                    statement.setNull(14, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getTirePressureStatus() != null) {
                    statement.setInt(15, tireNewestData.getTirePressureStatus());
                } else {
                    statement.setNull(15, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getTireTemperatureStatus() != null) {
                    statement.setInt(16, tireNewestData.getTireTemperatureStatus());
                } else {
                    statement.setNull(16, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getTireStatus() != null) {
                    statement.setInt(17, tireNewestData.getTireStatus());
                } else {
                    statement.setNull(17, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getDeviceTime() != null) {
                    statement.setTimestamp(18,
                            new java.sql.Timestamp(tireNewestData.getDeviceTime().getTime()));
                } else {
                    statement.setNull(18, java.sql.Types.DATE);
                }
                statement.setString(19, tireNewestData.getClientId());
                statement.setString(20, tireNewestData.getTireSiteId());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("tire newest data JDBC update batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }

    public void insertOnDuplicateKeyUpdate(Set<TireNewestDataEntity> paramList)
            throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into `jtt808_tire_newest_data`("
                + "client_id, "
                + "serial_no, "
                + "trailer_status, "
                + "gua_repeater_id, "
                + "longitude, "
                + "latitude, "
                + "tire_type, "
                + "tire_site_id, "
                + "tire_sensor_id,"
                + "voltage,"
                + "tire_pressure,"
                + "tire_temperature,"
                + "battery_voltage_status,"
                + "is_timeout, "
                + "scheme,"
                + "tire_pressure_status,"
                + "tire_temperature_status, "
                + "tire_status,"
                + "device_time"
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                + "on duplicate key update "
                + "serial_no = ?, "
                + "trailer_status = ?, "
                + "gua_repeater_id = ?, "
                + "longitude = ?, "
                + "latitude = ?, "
                + "tire_type = ?, "
                + "tire_site_id = ?, "
                + "tire_sensor_id = ?, "
                + "voltage = ?, "
                + "tire_pressure = ?, "
                + "tire_temperature = ?, "
                + "battery_voltage_status = ?, "
                + "is_timeout = ?, "
                + "scheme = ?, "
                + "tire_pressure_status = ?, "
                + "tire_temperature_status = ?, "
                + "tire_status = ?, "
                + "device_time = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (TireNewestDataEntity tireNewestData : paramList) {
                statement.setString(1, tireNewestData.getClientId());
                if (StringUtils.isNotBlank(tireNewestData.getSerialNo())) {
                    statement.setString(2, tireNewestData.getSerialNo());
                    statement.setString(20, tireNewestData.getSerialNo());
                } else {
                    statement.setNull(2, java.sql.Types.VARCHAR);
                    statement.setNull(20, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getTrailerStatus() != null) {
                    statement.setInt(3, tireNewestData.getTrailerStatus());
                    statement.setInt(21, tireNewestData.getTrailerStatus());
                } else {
                    statement.setNull(3, java.sql.Types.INTEGER);
                    statement.setNull(21, java.sql.Types.INTEGER);
                }
                if (StringUtils.isNotBlank(tireNewestData.getGuaRepeaterId())) {
                    statement.setString(4, tireNewestData.getGuaRepeaterId());
                    statement.setString(22, tireNewestData.getGuaRepeaterId());
                } else {
                    statement.setNull(4, java.sql.Types.VARCHAR);
                    statement.setNull(22, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getLongitude())) {
                    statement.setString(5, tireNewestData.getLongitude());
                    statement.setString(23, tireNewestData.getLongitude());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                    statement.setNull(23, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(tireNewestData.getLatitude())) {
                    statement.setString(6, tireNewestData.getLatitude());
                    statement.setString(24, tireNewestData.getLatitude());
                } else {
                    statement.setNull(6, java.sql.Types.VARCHAR);
                    statement.setNull(24, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getTireType() != null) {
                    statement.setInt(7, tireNewestData.getTireType());
                    statement.setInt(25, tireNewestData.getTireType());
                } else {
                    statement.setNull(7, java.sql.Types.INTEGER);
                    statement.setNull(25, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getTireSiteId() != null) {
                    statement.setString(8, tireNewestData.getTireSiteId());
                    statement.setString(26, tireNewestData.getTireSiteId());
                } else {
                    statement.setNull(8, java.sql.Types.VARCHAR);
                    statement.setNull(26, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getTireSensorId() != null) {
                    statement.setString(9, tireNewestData.getTireSensorId());
                    statement.setString(27, tireNewestData.getTireSensorId());
                } else {
                    statement.setNull(9, java.sql.Types.VARCHAR);
                    statement.setNull(27, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getVoltage() != null) {
                    statement.setString(10, tireNewestData.getVoltage());
                    statement.setString(28, tireNewestData.getVoltage());
                } else {
                    statement.setNull(10, java.sql.Types.VARCHAR);
                    statement.setNull(28, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getTirePressure() != null) {
                    statement.setString(11, tireNewestData.getTirePressure());
                    statement.setString(29, tireNewestData.getTirePressure());
                } else {
                    statement.setNull(11, java.sql.Types.VARCHAR);
                    statement.setNull(29, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getTireTemperature() != null) {
                    statement.setString(12, tireNewestData.getTireTemperature());
                    statement.setString(30, tireNewestData.getTireTemperature());
                } else {
                    statement.setNull(12, java.sql.Types.VARCHAR);
                    statement.setNull(30, java.sql.Types.VARCHAR);
                }
                if (tireNewestData.getBatteryVoltageStatus() != null) {
                    statement.setInt(13, tireNewestData.getBatteryVoltageStatus());
                    statement.setInt(31, tireNewestData.getBatteryVoltageStatus());
                } else {
                    statement.setNull(13, java.sql.Types.INTEGER);
                    statement.setNull(31, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getIsTimeout() != null) {
                    statement.setInt(14, tireNewestData.getIsTimeout());
                    statement.setInt(32, tireNewestData.getIsTimeout());
                } else {
                    statement.setNull(14, java.sql.Types.INTEGER);
                    statement.setNull(32, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getScheme() != null) {
                    statement.setInt(15, tireNewestData.getScheme());
                    statement.setInt(33, tireNewestData.getScheme());
                } else {
                    statement.setNull(15, java.sql.Types.INTEGER);
                    statement.setNull(33, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getTirePressureStatus() != null) {
                    statement.setInt(16, tireNewestData.getTirePressureStatus());
                    statement.setInt(34, tireNewestData.getTirePressureStatus());
                } else {
                    statement.setNull(16, java.sql.Types.INTEGER);
                    statement.setNull(34, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getTireTemperatureStatus() != null) {
                    statement.setInt(17, tireNewestData.getTireTemperatureStatus());
                    statement.setInt(35, tireNewestData.getTireTemperatureStatus());
                } else {
                    statement.setNull(17, java.sql.Types.INTEGER);
                    statement.setNull(35, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getTireStatus() != null) {
                    statement.setInt(18, tireNewestData.getTireStatus());
                    statement.setInt(36, tireNewestData.getTireStatus());
                } else {
                    statement.setNull(18, java.sql.Types.INTEGER);
                    statement.setNull(36, java.sql.Types.INTEGER);
                }
                if (tireNewestData.getDeviceTime() != null) {
                    statement.setTimestamp(19,
                            new java.sql.Timestamp(tireNewestData.getDeviceTime().getTime()));
                    statement.setTimestamp(37,
                            new java.sql.Timestamp(tireNewestData.getDeviceTime().getTime()));
                } else {
                    statement.setNull(19, java.sql.Types.DATE);
                    statement.setNull(37, java.sql.Types.DATE);
                }
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("tire newest data JDBC insertOnDuplicateKeyUpdate batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }
}
