package org.platform.vehicle.mapper.jdbc;

import org.platform.vehicle.entity.NewestGeoLocationEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/13 17:04
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NewestGeoLocationJdbc {

    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 批量保存轮胎数据
     *
     * @param paramList
     * @throws SQLException
     */
    public void batchInsert(List<NewestGeoLocationEntity> paramList)
            throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into `jtt808_newest_geo_location`("
                + "receiver_id, "
                + "msg_id, "
                + "lng, "
                + "lat, "
                + "speed, "
                + "altitude, "
                + "warn_bit, "
                + "status_bit, "
                + "device_time"
                + ") values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (NewestGeoLocationEntity newestGeoLocation : paramList) {
                statement.setString(1, newestGeoLocation.getReceiverId());
                if (newestGeoLocation.getMsgId() != null) {
                    statement.setString(2, newestGeoLocation.getMsgId());
                } else {
                    statement.setNull(2, java.sql.Types.VARCHAR);
                }
                if (newestGeoLocation.getSpeed() != null) {
                    statement.setString(3, newestGeoLocation.getLng());
                } else {
                    statement.setNull(3, java.sql.Types.VARCHAR);
                }
                if (newestGeoLocation.getSpeed() != null) {
                    statement.setString(4, newestGeoLocation.getLat());
                } else {
                    statement.setNull(4, java.sql.Types.VARCHAR);
                }
                if (newestGeoLocation.getSpeed() != null) {
                    statement.setString(5, newestGeoLocation.getSpeed());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                }
                if (newestGeoLocation.getAltitude() != null) {
                    statement.setString(6, newestGeoLocation.getAltitude());
                } else {
                    statement.setNull(6, java.sql.Types.VARCHAR);
                }
                if (newestGeoLocation.getWarnBit() != null) {
                    statement.setInt(7, newestGeoLocation.getWarnBit());
                } else {
                    statement.setNull(7, java.sql.Types.INTEGER);
                }
                if (newestGeoLocation.getStatusBit() != null) {
                    statement.setInt(8, newestGeoLocation.getStatusBit());
                } else {
                    statement.setNull(8, java.sql.Types.INTEGER);
                }
                if (newestGeoLocation.getDeviceTime() != null) {
                    statement.setTimestamp(9,
                            new Timestamp(newestGeoLocation.getDeviceTime().getTime()));
                } else {
                    statement.setNull(9, java.sql.Types.DATE);
                }
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("newest geoLocation JDBC save batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }

    public void batchUpdate(List<NewestGeoLocationEntity> paramList) throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "update `jtt808_newest_geo_location` set "
                + "msg_id = ?, "
                + "lng = ?, "
                + "lat = ?, "
                + "speed = ?, "
                + "altitude = ?, "
                + "warn_bit = ?, "
                + "status_bit = ?, "
                + "device_time = ? "
                + "where receiver_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (NewestGeoLocationEntity newestGeoLocation : paramList) {
                if (newestGeoLocation.getMsgId() != null) {
                    statement.setString(1, newestGeoLocation.getMsgId());
                } else {
                    statement.setNull(1, java.sql.Types.VARCHAR);
                }
                if (newestGeoLocation.getSpeed() != null) {
                    statement.setString(2, newestGeoLocation.getLng());
                } else {
                    statement.setNull(2, java.sql.Types.VARCHAR);
                }
                if (newestGeoLocation.getSpeed() != null) {
                    statement.setString(3, newestGeoLocation.getLat());
                } else {
                    statement.setNull(3, java.sql.Types.VARCHAR);
                }
                if (newestGeoLocation.getSpeed() != null) {
                    statement.setString(4, newestGeoLocation.getSpeed());
                } else {
                    statement.setNull(4, java.sql.Types.VARCHAR);
                }
                if (newestGeoLocation.getAltitude() != null) {
                    statement.setString(5, newestGeoLocation.getAltitude());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                }
                if (newestGeoLocation.getWarnBit() != null) {
                    statement.setInt(6, newestGeoLocation.getWarnBit());
                } else {
                    statement.setNull(6, java.sql.Types.INTEGER);
                }
                if (newestGeoLocation.getStatusBit() != null) {
                    statement.setInt(7, newestGeoLocation.getStatusBit());
                } else {
                    statement.setNull(7, java.sql.Types.INTEGER);
                }
                if (newestGeoLocation.getDeviceTime() != null) {
                    statement.setTimestamp(8,
                            new Timestamp(newestGeoLocation.getDeviceTime().getTime()));
                } else {
                    statement.setNull(8, java.sql.Types.DATE);
                }
                statement.setString(9, newestGeoLocation.getReceiverId());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("newest geoLocation JDBC save batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }

    public void insertOnDuplicateKeyUpdate(Set<NewestGeoLocationEntity> paramList)
            throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO `jtt808_newest_geo_location` ("
                + "receiver_id, "
                + "msg_id, "
                + "lng, "
                + "lat, "
                + "speed, "
                + "altitude, "
                + "warn_bit, "
                + "status_bit, "
                + "device_time"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "msg_id = VALUES(msg_id), "
                + "lng = VALUES(lng), "
                + "lat = VALUES(lat), "
                + "speed = VALUES(speed), "
                + "altitude = VALUES(altitude), "
                + "warn_bit = VALUES(warn_bit), "
                + "status_bit = VALUES(status_bit), "
                + "device_time = VALUES(device_time)";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (NewestGeoLocationEntity entity : paramList) {
                statement.setString(1, entity.getReceiverId());
                if (entity.getMsgId() != null) {
                    statement.setString(2, entity.getMsgId());
                } else {
                    statement.setNull(2, java.sql.Types.VARCHAR);
                }
                if (entity.getLng() != null) {
                    statement.setString(3, entity.getLng());
                } else {
                    statement.setNull(3, java.sql.Types.VARCHAR);
                }
                if (entity.getLat() != null) {
                    statement.setString(4, entity.getLat());
                } else {
                    statement.setNull(4, java.sql.Types.VARCHAR);
                }
                if (entity.getSpeed() != null) {
                    statement.setString(5, entity.getSpeed());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                }
                if (entity.getAltitude() != null) {
                    statement.setString(6, entity.getAltitude());
                } else {
                    statement.setNull(6, java.sql.Types.VARCHAR);
                }
                if (entity.getWarnBit() != null) {
                    statement.setInt(7, entity.getWarnBit());
                } else {
                    statement.setNull(7, java.sql.Types.INTEGER);
                }
                if (entity.getStatusBit() != null) {
                    statement.setInt(8, entity.getStatusBit());
                } else {
                    statement.setNull(8, java.sql.Types.INTEGER);
                }
                if (entity.getDeviceTime() != null) {
                    statement.setTimestamp(9,
                            new Timestamp(entity.getDeviceTime().getTime()));
                } else {
                    statement.setNull(9, java.sql.Types.DATE);
                }
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("Error in insertOnDuplicateKeyUpdate batch operation", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }

}
