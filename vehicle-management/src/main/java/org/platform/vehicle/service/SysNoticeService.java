package org.platform.vehicle.service;

import org.platform.vehicle.param.NoticeBatchReadParam;
import org.platform.vehicle.param.SysNoticeConditionQueryParam;
import org.platform.vehicle.param.SysNoticeWarningParam;
import org.platform.vehicle.vo.NoticeSimpleVo;
import org.platform.vehicle.vo.SysNoticeDetailVo;
import org.platform.vehicle.vo.SysNoticeVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;

/**
 * @Author gejiawei
 * @Date 2023/9/6 11:10
 */
public interface SysNoticeService {

    /**
     * 消息列表-获取当前用户未读消息数量
     *
     * @return
     */
    BaseResponse getNotReadCount();

    /**
     * 消息列表-条件查询
     *
     * @param param
     * @return
     */
    BasePageResponse<List<SysNoticeVo>> conditionQuery(SysNoticeConditionQueryParam param);

    /**
     * 消息列表-查看
     *
     * @param id
     * @return
     */
    BaseResponse<SysNoticeDetailVo> getDetail(Integer id);

    /**
     * 首页-我的待办
     *
     * @return
     */
    BaseResponse<List<SysNoticeVo>> getMyTodoList();

    /**
     * 发送消息
     *
     * @param param
     */
    void sendTireWarningNotice(SysNoticeWarningParam param);

    /**
     * 温压管理-报警记录与跟进-报警提醒轮询
     *
     * @param
     * @return
     */
    BaseResponse<List<NoticeSimpleVo>> poll();

    /**
     * 温压管理-报警记录与跟进-批量已读(我知道了)
     *
     * @param
     * @param param
     * @return
     */
    BaseResponse batchRead(NoticeBatchReadParam param);

    /**
     * 根据目标主键和类型修改为已读
     *
     * @param targetId
     * @param type
     * @return
     */
    BaseResponse updateReadByTargetId(Integer targetId, Integer type);
}
