package org.lpy.langchainagent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lpy.langchainagent.entity.Appointment;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}
