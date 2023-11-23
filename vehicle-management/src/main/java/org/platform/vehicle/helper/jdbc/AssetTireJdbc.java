package org.platform.vehicle.helper.jdbc;

import org.platform.vehicle.constant.AssetTireConstant;
import org.platform.vehicle.entity.AssetTire;
import org.platform.vehicle.entity.AssetTireDeviceBindRecord;
import org.platform.vehicle.entity.AssetTireFitRecord;
import org.platform.vehicle.entity.AssetTireStockRecord;
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
public class AssetTireJdbc {

    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 批量保存轮胎数据
     *
     * @param insertTireParamList
     * @throws SQLException
     */
    public void saveBatchTire(List<AssetTire> insertTireParamList) throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into asset_tire("
                + "code, "
                + "client_id, "
                + "fleet_id, "
                + "warehouse_id, "
                + "tire_brand_id, "
                + "tire_spec_id, "
                + "degree, "
                + "mileage, "
                + "tire_status, "
                + "has_sensor, "
                + "is_delete, "
                + "create_person"
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (AssetTire assetTire : insertTireParamList) {
                statement.setString(1, assetTire.getCode());
                statement.setInt(2, assetTire.getClientId());
                statement.setInt(3, assetTire.getFleetId());
                if (assetTire.getWarehouseId() != null) {
                    statement.setInt(4, assetTire.getWarehouseId());
                } else {
                    statement.setNull(4, java.sql.Types.INTEGER);
                }
                statement.setInt(5, assetTire.getTireBrandId());
                statement.setInt(6, assetTire.getTireSpecId());
                if (StringUtils.isNotBlank(assetTire.getDegree())) {
                    statement.setString(7, assetTire.getDegree());
                } else {
                    statement.setNull(7, java.sql.Types.VARCHAR);
                }
                if (assetTire.getMileage() != null) {
                    statement.setInt(8, assetTire.getMileage());
                } else {
                    statement.setNull(8, java.sql.Types.INTEGER);
                }
                statement.setInt(9, assetTire.getTireStatus());
                statement.setInt(10, assetTire.getHasSensor());
                if (assetTire.getIsDelete() != null) {
                    statement.setInt(11, assetTire.getIsDelete());
                } else {
                    statement.setInt(11, AssetTireConstant.NOT_DELETE);
                }
                statement.setString(12, assetTire.getCreatePerson());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("asset tire JDBC save batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }

    /**
     * 批量保存轮胎库存记录
     *
     * @param paramList
     * @throws SQLException
     */
    public void saveBatchTireStockRecord(List<AssetTireStockRecord> paramList) throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into asset_tire_stock_record("
                + "tire_code, "
                + "fleet_id, "
                + "client_name, "
                + "fleet_name, "
                + "warehouse_id, "
                + "warehouse_name, "
                + "type, "
                + "stock_type, "
                + "target, "
                + "tire_brand, "
                + "tire_spec, "
                + "degree, "
                + "mileage, "
                + "create_person"
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (AssetTireStockRecord param : paramList) {
                statement.setString(1, param.getTireCode());
                statement.setInt(2, param.getFleetId());
                statement.setString(3, param.getClientName());
                statement.setString(4, param.getFleetName());
                if (param.getWarehouseId() != null) {
                    statement.setInt(5, param.getWarehouseId());
                } else {
                    statement.setNull(5, java.sql.Types.INTEGER);
                }
                if (StringUtils.isNotBlank(param.getWarehouseName())) {
                    statement.setString(6, param.getWarehouseName());
                } else {
                    statement.setNull(6, java.sql.Types.VARCHAR);
                }
                statement.setInt(7, param.getType());
                statement.setInt(8, param.getStockType());
                if (StringUtils.isNotBlank(param.getTarget())) {
                    statement.setString(9, param.getTarget());
                } else {
                    statement.setNull(9, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getTireBrand())) {
                    statement.setString(10, param.getTireBrand());
                } else {
                    statement.setNull(10, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getTireSpec())) {
                    statement.setString(11, param.getTireSpec());
                } else {
                    statement.setNull(11, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getDegree())) {
                    statement.setString(12, param.getDegree());
                } else {
                    statement.setNull(12, java.sql.Types.VARCHAR);
                }
                if (param.getMileage() != null) {
                    statement.setInt(13, param.getMileage());
                } else {
                    statement.setNull(13, java.sql.Types.INTEGER);
                }
                statement.setString(14, param.getCreatePerson());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("asset tire stock record JDBC save batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }

    /**
     * 批量保存轮胎安装记录
     *
     * @param paramList
     * @throws SQLException
     */
    public void saveBatchFitRecord(List<AssetTireFitRecord> paramList)
            throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into asset_tire_fit_record("
                + "client_name, "
                + "fleet_name, "
                + "license_plate, "
                + "tire_code, "
                + "tire_site_name, "
                + "brand_name, "
                + "type, "
                + "create_person"
                + ") values(?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (AssetTireFitRecord param : paramList) {
                if (StringUtils.isNotBlank(param.getClientName())) {
                    statement.setString(1, param.getClientName());
                } else {
                    statement.setNull(1, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getFleetName())) {
                    statement.setString(2, param.getFleetName());
                } else {
                    statement.setNull(2, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getLicensePlate())) {
                    statement.setString(3, param.getLicensePlate());
                } else {
                    statement.setNull(3, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getTireCode())) {
                    statement.setString(4, param.getTireCode());
                } else {
                    statement.setNull(4, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getTireSiteName())) {
                    statement.setString(5, param.getTireSiteName());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getBrandName())) {
                    statement.setString(6, param.getBrandName());
                } else {
                    statement.setNull(6, java.sql.Types.VARCHAR);
                }
                if (param.getType() != null) {
                    statement.setInt(7, param.getType());
                } else {
                    statement.setNull(7, java.sql.Types.INTEGER);
                }
                statement.setString(8, param.getCreatePerson());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("asset tire fit record JDBC save batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }

    public void saveBatchDeviceRecord(List<AssetTireDeviceBindRecord> paramList)
            throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into asset_tire_device_bind_record("
                + "code, "
                + "device_type, "
                + "license_plate, "
                + "tire_site_name, "
                + "tire_code, "
                + "create_person"
                + ") values(?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (AssetTireDeviceBindRecord param : paramList) {
                if (StringUtils.isNotBlank(param.getCode())) {
                    statement.setString(1, param.getCode());
                } else {
                    statement.setNull(1, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getDeviceType())) {
                    statement.setString(2, param.getDeviceType());
                } else {
                    statement.setNull(2, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getLicensePlate())) {
                    statement.setString(3, param.getLicensePlate());
                } else {
                    statement.setNull(3, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getTireSiteName())) {
                    statement.setString(4, param.getTireSiteName());
                } else {
                    statement.setNull(4, java.sql.Types.VARCHAR);
                }
                if (StringUtils.isNotBlank(param.getTireCode())) {
                    statement.setString(5, param.getTireCode());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                }
                statement.setString(6, param.getCreatePerson());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            log.error("asset tire fit record JDBC save batch error", e);
        } finally {
            statement.close();
            sqlSession.close();
        }
    }
}
