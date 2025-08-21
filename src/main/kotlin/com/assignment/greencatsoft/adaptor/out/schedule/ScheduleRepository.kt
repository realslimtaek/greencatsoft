package com.assignment.greencatsoft.adaptor.out.schedule

import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleRepository: JpaRepository<ScheduleEntity, Long>
