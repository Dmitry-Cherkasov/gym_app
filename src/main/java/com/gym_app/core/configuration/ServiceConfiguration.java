package com.gym_app.core.configuration;

import com.gym_app.core.dao.TraineeJpaDaoImpl;
import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.services.TraineeService;
import com.gym_app.core.services.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {
//    @Autowired
//    TrainerJpaDaoImpl trainerJpaDao;
//    @Autowired
//    TraineeJpaDaoImpl traineeJpaDao;
//
//    @Bean
//    public TraineeService getTraineeService(){
//        return new TraineeService(traineeJpaDao);
//    }
//
//    @Bean
//    public TrainerService getTrainerService(){
//        return new TrainerService(trainerJpaDao);
//    }
}
