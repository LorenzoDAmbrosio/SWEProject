package com.project.sweprojectspring.controllers;

import com.project.sweprojectspring.StageInitializer;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;

public class MainMenuController {
    @Autowired
    private StageInitializer _stageInitializer;

}
