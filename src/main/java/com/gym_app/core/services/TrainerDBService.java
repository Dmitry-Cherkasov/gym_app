package com.gym_app.core.services;

import com.gym_app.core.dao.JpaDao;
import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.util.TrainerUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TrainerDBService extends AbstractDbService<Trainer>{

    private final TrainerJpaDaoImpl trainerJpaDao;

    @Autowired
    public TrainerDBService(TrainerJpaDaoImpl trainerJpaDaoDao) {
        this.trainerJpaDao = trainerJpaDaoDao;
    }


    @Override
    protected JpaDao<Trainer, Long> getDao() {
        return trainerJpaDao;
    }

    @Override
    protected String getTypeName() {
        return "trainer";
    }

    @Override
    protected Trainer updateUser(Trainer trainer, String[] updates) {
        return TrainerUpdater.updateTrainer(trainer, updates);
    }

//    protected Dao<Trainer, Long> getDao() {
//        return trainerJpaDao;
//    }
//
//    public Trainer create(Trainer trainer) {
//        trainer.setUserName(generate(trainer.getFirstName(), trainer.getLastName()));
//        trainer.setPassword(PasswordGenerator.createPassword(10));
//        try {
//            trainer = trainerJpaDao.save(trainer);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Failed create new trainer: " + trainer);
//        }
//        return trainer;
//    }
//
//    public void delete(Long id, String username, String password) {
//        if (!authenticate(username, password)) {
//            throw new SecurityException("Authentication failed for trainer with username: " + username);
//        }
//        Optional<Trainer> trainer = trainerJpaDao.getById(id);
//        if (trainer.isPresent()) {
//            trainerJpaDao.delete(trainer.get());
//        } else {
//            throw new RuntimeException("Trainer with ID " + id + " not found.");
//        }
//
//    }
//
//    public Optional<Trainer> selectByUsername(String username, String password) {
//        if (!authenticate(username, password)) {
//            throw new SecurityException("Authentication failed for trainer with username: " + username);
//        }
//        Optional<Trainer> selectedTrainer;
//        try {
//            selectedTrainer = trainerJpaDao.getByUserName(username);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Failed to find trainer with username " + username);
//        }
//        return selectedTrainer;
//    }
//
//    public void changePassword(String newPassword, String username, String password) {
//        if (!authenticate(username, password)) {
//            throw new SecurityException("Authentication failed for trainer with username: " + username);
//        }
//        Optional<Trainer> trainer;
//        try {
//            trainer = trainerJpaDao.getByUserName(username);
//            trainerJpaDao.updatePassword(trainer.get(), newPassword);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Failed to change password for trainer: " + username);
//        }
//    }
//
//    public void changeStatus(Trainer trainer, String username, String password) {
//        if (!authenticate(username, password)) {
//            throw new SecurityException("Authentication failed for trainer with username: " + username);
//        }
//        try {
//            trainerJpaDao.changeStatus(trainer);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Failed to change active/inactive status of trainer with id: " + trainer.getId());
//        }
//    }
//
//    public void update(Trainer oldentity, String username, String password, String[] updates) {
//        if (!authenticate(username, password)) {
//            throw new SecurityException("Authentication failed for trainer with username: " + username);
//        }
//        try {
//            trainerJpaDao.update(oldentity, TrainerUpdater.updateTrainer(oldentity, updates));
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Failed to update trainer with id" + oldentity.getId());
//        }
//    }
//
//    private String generate(String firstName, String lastName) {
//        String baseUserName = firstName + "." + lastName;
//        String userName = baseUserName;
//        int serialNumber = 1;
//
//        while (trainerJpaDao.getByUserName(userName).isPresent()) {
//            userName = baseUserName + serialNumber;
//            serialNumber++;
//        }
//        return userName;
//    }
//
//    private boolean authenticate(String username, String password) {
//        Optional<Trainer> trainerOpt = trainerJpaDao.getByUserName(username);
//        if (trainerOpt.isPresent()) {
//            Trainer trainer = trainerOpt.get();
//            return trainer.getPassword().equals(password);
//        }
//        return false;
//    }

}
