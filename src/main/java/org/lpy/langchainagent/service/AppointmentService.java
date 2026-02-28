package org.lpy.langchainagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lpy.langchainagent.entity.Appointment;

public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);
}
