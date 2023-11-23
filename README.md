# 车辆管理平台:
* 用户、车辆数据维护
* 车辆位置数据、温压数据上传
* 车辆数据展示
* 告警跟踪

# 项目说明:

* jt808:车辆通讯服务
* vehicle-consumer:车辆数据消费服务
* vehicle—gateway:服务网关
* vehicle-management:后台管理服务
* vehicle-oauth:权限认证服务


# 主要功能

### vehicle-gateway
* 服务API网关

### vehicle-management
* 用户数据维护
* 车辆数据维护
* 资产管理
* 告警监控
* 数据展示

### vehicle-oauth
* 用户登录

### vehicle-consumer
* 通过RabbitMQ消费车辆数据
* 数据保存至ES
* 异常数据校验,并保存mysql

### jt808
* 设备数据上传
* 数据下发
* 消息推送至RabbitMQ

### 联系方式
Discord:https://discord.gg/6npXER7A

